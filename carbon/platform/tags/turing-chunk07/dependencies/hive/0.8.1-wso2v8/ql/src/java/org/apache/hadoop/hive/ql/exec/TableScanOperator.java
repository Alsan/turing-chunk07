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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.common.FileUtils;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.metadata.VirtualColumn;
import org.apache.hadoop.hive.ql.plan.TableDesc;
import org.apache.hadoop.hive.ql.plan.TableScanDesc;
import org.apache.hadoop.hive.ql.plan.api.OperatorType;
import org.apache.hadoop.hive.ql.stats.StatsPublisher;
import org.apache.hadoop.hive.ql.stats.StatsSetupConst;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils.ObjectInspectorCopyOption;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.JobConf;

/**
 * Table Scan Operator If the data is coming from the map-reduce framework, just
 * forward it. This will be needed as part of local work when data is not being
 * read as part of map-reduce framework
 **/
public class TableScanOperator extends Operator<TableScanDesc> implements
    Serializable {
  private static final long serialVersionUID = 1L;

  protected transient JobConf jc;
  private transient Configuration hconf;
  private transient String partitionSpecs;
  private transient boolean inputFileChanged = false;
  private TableDesc tableDesc;

  private transient Stat currentStat;
  private transient Map<String, Stat> stats;

  public TableDesc getTableDesc() {
    return tableDesc;
  }

  public void setTableDesc(TableDesc tableDesc) {
    this.tableDesc = tableDesc;
  }

  /**
   * Other than gathering statistics for the ANALYZE command, the table scan operator
   * does not do anything special other than just forwarding the row. Since the table
   * data is always read as part of the map-reduce framework by the mapper. But, when this
   * assumption stops to be true, i.e table data won't be only read by the mapper, this
   * operator will be enhanced to read the table.
   **/
  @Override
  public void processOp(Object row, int tag) throws HiveException {
    if (conf != null && conf.isGatherStats()) {
      gatherStats(row);
    }
    forward(row, inputObjInspectors[tag]);
  }

  // Change the table partition for collecting stats
  @Override
  public void cleanUpInputFileChangedOp() throws HiveException {
    inputFileChanged = true;
  }

  private void gatherStats(Object row) {
    // first row/call or a new partition

    if ((currentStat == null) || inputFileChanged) {
      inputFileChanged = false;
      if (conf.getPartColumns() == null || conf.getPartColumns().size() == 0) {
        partitionSpecs = ""; // non-partitioned
      } else {
        // Figure out the partition spec from the input.
        // This is only done once for the first row (when stat == null)
        // since all rows in the same mapper should be from the same partition.
        List<Object> writable;
        List<String> values;
        int dpStartCol; // the first position of partition column
        assert inputObjInspectors[0].getCategory() == ObjectInspector.Category.STRUCT : "input object inspector is not struct";

        writable = new ArrayList<Object>(conf.getPartColumns().size());
        values = new ArrayList<String>(conf.getPartColumns().size());
        dpStartCol = 0;
        StructObjectInspector soi = (StructObjectInspector) inputObjInspectors[0];
        for (StructField sf : soi.getAllStructFieldRefs()) {
          String fn = sf.getFieldName();
          if (!conf.getPartColumns().contains(fn)) {
            dpStartCol++;
          } else {
            break;
          }
        }
        ObjectInspectorUtils.partialCopyToStandardObject(writable, row, dpStartCol, conf
            .getPartColumns().size(),
            (StructObjectInspector) inputObjInspectors[0], ObjectInspectorCopyOption.WRITABLE);

        for (Object o : writable) {
          assert (o != null && o.toString().length() > 0);
          values.add(o.toString());
        }
        partitionSpecs = FileUtils.makePartName(conf.getPartColumns(), values);
        LOG.info("Stats Gathering found a new partition spec = " + partitionSpecs);
      }
      // find which column contains the raw data size (both partitioned and non partitioned
      int uSizeColumn = -1;
      StructObjectInspector soi = (StructObjectInspector) inputObjInspectors[0];
      for (int i = 0; i < soi.getAllStructFieldRefs().size(); i++) {
        if (soi.getAllStructFieldRefs().get(i).getFieldName()
            .equals(VirtualColumn.RAWDATASIZE.getName().toLowerCase())) {
          uSizeColumn = i;
          break;
        }
      }
      currentStat = stats.get(partitionSpecs);
      if (currentStat == null) {
        currentStat = new Stat();
        currentStat.setBookkeepingInfo(StatsSetupConst.RAW_DATA_SIZE, uSizeColumn);
        stats.put(partitionSpecs, currentStat);
      }
    }

    // increase the row count
    currentStat.addToStat(StatsSetupConst.ROW_COUNT, 1);

    // extract the raw data size, and update the stats for the current partition
    int rdSizeColumn = currentStat.getBookkeepingInfo(StatsSetupConst.RAW_DATA_SIZE);
    if(rdSizeColumn != -1) {
      List<Object> rdSize = new ArrayList<Object>(1);
      ObjectInspectorUtils.partialCopyToStandardObject(rdSize, row,
          rdSizeColumn, 1, (StructObjectInspector) inputObjInspectors[0],
          ObjectInspectorCopyOption.WRITABLE);
      currentStat.addToStat(StatsSetupConst.RAW_DATA_SIZE, (((LongWritable)rdSize.get(0)).get()));
    }

  }

  @Override
  protected void initializeOp(Configuration hconf) throws HiveException {
    initializeChildren(hconf);
    inputFileChanged = false;

    if (conf == null) {
      return;
    }
    if (!conf.isGatherStats()) {
      return;
    }

    this.hconf = hconf;
    if (hconf instanceof JobConf) {
      jc = (JobConf) hconf;
    } else {
      // test code path
      jc = new JobConf(hconf, ExecDriver.class);
    }

    currentStat = null;
    stats = new HashMap<String, Stat>();
    partitionSpecs = null;
    if (conf.getPartColumns() == null || conf.getPartColumns().size() == 0) {
      // NON PARTITIONED table
      return;
    }

  }

  @Override
  public void closeOp(boolean abort) throws HiveException {
    if (conf != null) {
      if (conf.isGatherStats() && stats.size() != 0) {
        publishStats();
      }
    }
  }

  /**
   * The operator name for this operator type. This is used to construct the
   * rule for an operator
   *
   * @return the operator name
   **/
  @Override
  public String getName() {
    return "TS";
  }

  // this 'neededColumnIDs' field is included in this operator class instead of
  // its desc class.The reason is that 1)tableScanDesc can not be instantiated,
  // and 2) it will fail some join and union queries if this is added forcibly
  // into tableScanDesc
  java.util.ArrayList<Integer> neededColumnIDs;

  public void setNeededColumnIDs(java.util.ArrayList<Integer> orign_columns) {
    neededColumnIDs = orign_columns;
  }

  public java.util.ArrayList<Integer> getNeededColumnIDs() {
    return neededColumnIDs;
  }

  @Override
  public OperatorType getType() {
    return OperatorType.TABLESCAN;
  }

  private void publishStats() {
    // Initializing a stats publisher
    StatsPublisher statsPublisher = Utilities.getStatsPublisher(jc);
    if (!statsPublisher.connect(jc)) {
      // just return, stats gathering should not block the main query.
      LOG.info("StatsPublishing error: cannot connect to database.");
      return;
    }

    String key;
    String taskID = Utilities.getTaskIdFromFilename(Utilities.getTaskId(hconf));
    Map<String, String> statsToPublish = new HashMap<String, String>();

    for (String pspecs : stats.keySet()) {
      statsToPublish.clear();
      if (pspecs.isEmpty()) {
        // In case of a non-partitioned table, the key for temp storage is just
        // "tableName + taskID"
        key = conf.getStatsAggPrefix() + taskID;
      } else {
        // In case of a partition, the key for temp storage is
        // "tableName + partitionSpecs + taskID"
        key = conf.getStatsAggPrefix() + pspecs + Path.SEPARATOR + taskID;
      }
      for(String statType : stats.get(pspecs).getStoredStats()) {
        statsToPublish.put(statType, Long.toString(stats.get(pspecs).getStat(statType)));
      }
      statsPublisher.publishStat(key, statsToPublish);
      LOG.info("publishing : " + key + " : " + statsToPublish.toString());
    }
    statsPublisher.closeConnection();
  }
}
