/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.ql.exec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.CommandNeedRetryException;
import org.apache.hadoop.hive.ql.Context;
import org.apache.hadoop.hive.ql.DriverContext;
import org.apache.hadoop.hive.ql.QueryPlan;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.metadata.Hive;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.plan.api.StageType;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.apache.hadoop.hive.ql.session.SessionState.LogHelper;
import org.apache.hadoop.util.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Task implementation.
 **/

public abstract class Task<T extends Serializable> implements Serializable, Node {

  private static final long serialVersionUID = 1L;
  protected transient boolean started;
  protected transient boolean initialized;
  protected transient boolean isdone;
  protected transient boolean queued;
  protected transient HiveConf conf;
  protected transient Hive db;
  protected transient LogHelper console;
  protected transient QueryPlan queryPlan;
  protected transient TaskHandle taskHandle;
  protected transient HashMap<String, Long> taskCounters;
  protected transient DriverContext driverContext;
  protected transient boolean clonedConf = false;
  protected transient String jobID;
  protected Task<? extends Serializable> backupTask;
  protected List<Task<? extends Serializable>> backupChildrenTasks = new ArrayList<Task<? extends Serializable>>();
  protected static transient Log LOG = LogFactory.getLog(Task.class);
  protected int taskTag;
  private boolean isLocalMode =false;
  private boolean retryCmdWhenFail = false;

  public static final int NO_TAG = 0;
  public static final int COMMON_JOIN = 1;
  public static final int CONVERTED_MAPJOIN = 2;
  public static final int CONVERTED_LOCAL_MAPJOIN = 3;
  public static final int BACKUP_COMMON_JOIN = 4;
  public static final int LOCAL_MAPJOIN=5;


  // Descendants tasks who subscribe feeds from this task
  protected transient List<Task<? extends Serializable>> feedSubscribers;

  public static enum FeedType {
    DYNAMIC_PARTITIONS, // list of dynamic partitions
  };

  // Bean methods

  protected List<Task<? extends Serializable>> childTasks;
  protected List<Task<? extends Serializable>> parentTasks;

  public Task() {
    isdone = false;
    started = false;
    initialized = false;
    queued = false;
    this.taskCounters = new HashMap<String, Long>();
    taskTag = Task.NO_TAG;
  }

  public void initialize(HiveConf conf, QueryPlan queryPlan, DriverContext driverContext) {
    this.queryPlan = queryPlan;
    isdone = false;
    started = false;
    setInitialized();
    this.conf = conf;

    try {
      db = Hive.get(conf);
    } catch (HiveException e) {
      // Bail out ungracefully - we should never hit
      // this here - but would have hit it in SemanticAnalyzer
      LOG.error(StringUtils.stringifyException(e));
      throw new RuntimeException(e);
    }
    this.driverContext = driverContext;

    console = new LogHelper(LOG);
  }

  /**
   * This method is called in the Driver on every task. It updates counters and calls execute(),
   * which is overridden in each task
   *
   * @return return value of execute()
   */
  public int executeTask() {
    try {
      SessionState ss = SessionState.get();
      this.setStarted();
      if (ss != null) {
        ss.getHiveHistory().logPlanProgress(queryPlan);
      }
      int retval = execute(driverContext);
      this.setDone();
      if (ss != null) {
        ss.getHiveHistory().logPlanProgress(queryPlan);
      }
      return retval;
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * This method is overridden in each Task. TODO execute should return a TaskHandle.
   *
   * @return status of executing the task
   */
  protected abstract int execute(DriverContext driverContext);

  // dummy method - FetchTask overwrites this
  public boolean fetch(ArrayList<String> res) throws IOException, CommandNeedRetryException {
    assert false;
    return false;
  }

  public void setChildTasks(List<Task<? extends Serializable>> childTasks) {
    this.childTasks = childTasks;
  }

  public List<? extends Node> getChildren() {
    return getChildTasks();
  }

  public List<Task<? extends Serializable>> getChildTasks() {
    return childTasks;
  }

  public void setParentTasks(List<Task<? extends Serializable>> parentTasks) {
    this.parentTasks = parentTasks;
  }

  public List<Task<? extends Serializable>> getParentTasks() {
    return parentTasks;
  }

  public Task<? extends Serializable> getBackupTask() {
    return backupTask;
  }


  public void setBackupTask(Task<? extends Serializable> backupTask) {
    this.backupTask = backupTask;
  }

  public List<Task<? extends Serializable>> getBackupChildrenTasks() {
    return backupChildrenTasks;
  }

  public void setBackupChildrenTasks(List<Task<? extends Serializable>> backupChildrenTasks) {
    this.backupChildrenTasks = backupChildrenTasks;
  }

  public Task<? extends Serializable> getAndInitBackupTask() {
    if (backupTask != null) {
      // first set back the backup task with its children task.
      if( backupChildrenTasks!= null) {
        for (Task<? extends Serializable> backupChild : backupChildrenTasks) {
          backupChild.getParentTasks().add(backupTask);
        }
      }

      // recursively remove task from its children tasks if this task doesn't have any parent task
      this.removeFromChildrenTasks();
    }
    return backupTask;
  }

  public void removeFromChildrenTasks() {

    List<Task<? extends Serializable>> childrenTasks = this.getChildTasks();
    if (childrenTasks == null) {
      return;
    }

    for (Task<? extends Serializable> childTsk : childrenTasks) {
      // remove this task from its children tasks
      childTsk.getParentTasks().remove(this);

      // recursively remove non-parent task from its children
      List<Task<? extends Serializable>> siblingTasks = childTsk.getParentTasks();
      if (siblingTasks == null || siblingTasks.size() == 0) {
        childTsk.removeFromChildrenTasks();
      }
    }

    return;
  }


  /**
   * The default dependent tasks are just child tasks, but different types could implement their own
   * (e.g. ConditionalTask will use the listTasks as dependents).
   *
   * @return a list of tasks that are dependent on this task.
   */
  public List<Task<? extends Serializable>> getDependentTasks() {
    return getChildTasks();
  }

  /**
   * Add a dependent task on the current task. Return if the dependency already existed or is this a
   * new one
   *
   * @return true if the task got added false if it already existed
   */
  public boolean addDependentTask(Task<? extends Serializable> dependent) {
    boolean ret = false;
    if (getChildTasks() == null) {
      setChildTasks(new ArrayList<Task<? extends Serializable>>());
    }
    if (!getChildTasks().contains(dependent)) {
      ret = true;
      getChildTasks().add(dependent);
      if (dependent.getParentTasks() == null) {
        dependent.setParentTasks(new ArrayList<Task<? extends Serializable>>());
      }
      if (!dependent.getParentTasks().contains(this)) {
        dependent.getParentTasks().add(this);
      }
    }
    return ret;
  }

  /**
   * Remove the dependent task.
   *
   * @param dependent
   *          the task to remove
   */
  public void removeDependentTask(Task<? extends Serializable> dependent) {
    if ((getChildTasks() != null) && (getChildTasks().contains(dependent))) {
      getChildTasks().remove(dependent);
      if ((dependent.getParentTasks() != null) && (dependent.getParentTasks().contains(this))) {
        dependent.getParentTasks().remove(this);
      }
    }
  }

  public void setStarted() {
    this.started = true;
  }

  public boolean started() {
    return started;
  }

  public boolean done() {
    return isdone;
  }

  public void setDone() {
    isdone = true;
  }

  public void setQueued() {
    queued = true;
  }

  public boolean getQueued() {
    return queued;
  }

  public void setInitialized() {
    initialized = true;
  }

  public boolean getInitialized() {
    return initialized;
  }

  public boolean isRunnable() {
    boolean isrunnable = true;
    if (parentTasks != null) {
      for (Task<? extends Serializable> parent : parentTasks) {
        if (!parent.done()) {
          isrunnable = false;
          break;
        }
      }
    }
    return isrunnable;
  }

  protected String id;
  protected T work;

  public void setWork(T work) {
    this.work = work;
  }

  public T getWork() {
    return work;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public boolean isMapRedTask() {
    return false;
  }

  public boolean isMapRedLocalTask() {
    return false;
  }

  public Collection<Operator<? extends Serializable>> getTopOperators() {
    return new LinkedList<Operator<? extends Serializable>>();
  }
  
  public boolean hasReduce() {
    return false;
  }

  public Operator<? extends Serializable> getReducer() {
    return null;
  }
  
  public HashMap<String, Long> getCounters() {
    return taskCounters;
  }

  /**
   * Should be overridden to return the type of the specific task among the types in StageType.
   *
   * @return StageType.* or null if not overridden
   */
  public abstract StageType getType();

  /**
   * If this task uses any map-reduce intermediate data (either for reading or for writing),
   * localize them (using the supplied Context). Map-Reduce intermediate directories are allocated
   * using Context.getMRTmpFileURI() and can be localized using localizeMRTmpFileURI().
   *
   * This method is declared abstract to force any task code to explicitly deal with this aspect of
   * execution.
   *
   * @param ctx
   *          context object with which to localize
   */
  abstract protected void localizeMRTmpFilesImpl(Context ctx);

  /**
   * Localize a task tree
   *
   * @param ctx
   *          context object with which to localize
   */
  public final void localizeMRTmpFiles(Context ctx) {
    localizeMRTmpFilesImpl(ctx);

    if (childTasks == null) {
      return;
    }

    for (Task<? extends Serializable> t : childTasks) {
      t.localizeMRTmpFiles(ctx);
    }
  }

  /**
   * Subscribe the feed of publisher. To prevent cycles, a task can only subscribe to its ancestor.
   * Feed is a generic form of execution-time feedback (type, value) pair from one task to another
   * task. Examples include dynamic partitions (which are only available at execution time). The
   * MoveTask may pass the list of dynamic partitions to the StatsTask since after the MoveTask the
   * list of dynamic partitions are lost (MoveTask moves them to the table's destination directory
   * which is mixed with old partitions).
   *
   * @param publisher
   *          this feed provider.
   */
  public void subscribeFeed(Task<? extends Serializable> publisher) {
    if (publisher != this && publisher.ancestorOrSelf(this)) {
      if (publisher.getFeedSubscribers() == null) {
        publisher.setFeedSubscribers(new LinkedList<Task<? extends Serializable>>());
      }
      publisher.getFeedSubscribers().add(this);
    }
  }

  // return true if this task is an ancestor of itself of parameter desc
  private boolean ancestorOrSelf(Task<? extends Serializable> desc) {
    if (this == desc) {
      return true;
    }
    List<Task<? extends Serializable>> deps = getDependentTasks();
    if (deps != null) {
      for (Task<? extends Serializable> d : deps) {
        if (d.ancestorOrSelf(desc)) {
          return true;
        }
      }
    }
    return false;
  }

  public List<Task<? extends Serializable>> getFeedSubscribers() {
    return feedSubscribers;
  }

  public void setFeedSubscribers(List<Task<? extends Serializable>> s) {
    feedSubscribers = s;
  }

  // push the feed to its subscribers
  protected void pushFeed(FeedType feedType, Object feedValue) {
    if (feedSubscribers != null) {
      for (Task<? extends Serializable> s : feedSubscribers) {
        s.receiveFeed(feedType, feedValue);
      }
    }
  }

  // a subscriber accept the feed and do something depending on the Task type
  protected void receiveFeed(FeedType feedType, Object feedValue) {
  }

  protected void cloneConf() {
    if (!clonedConf) {
      clonedConf = true;
      conf = new HiveConf(conf);
    }
  }


  public int getTaskTag() {
    return taskTag;
  }

  public void setTaskTag(int taskTag) {
    this.taskTag = taskTag;
  }

  public boolean isLocalMode() {
    return isLocalMode;
  }

  public void setLocalMode(boolean isLocalMode) {
    this.isLocalMode = isLocalMode;
  }

  public boolean requireLock() {
    return false;
  }

  public boolean ifRetryCmdWhenFail() {
    return retryCmdWhenFail;
  }

  public void setRetryCmdWhenFail(boolean retryCmdWhenFail) {
    this.retryCmdWhenFail = retryCmdWhenFail;
  }

  public QueryPlan getQueryPlan() {
    return queryPlan;
  }

  public void setQueryPlan(QueryPlan queryPlan) {
    this.queryPlan = queryPlan;
  }

  public String getJobID() {
    return jobID;
  }
}
