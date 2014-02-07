/*
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
package org.apache.cassandra.db.commitlog;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractCommitLogExecutorService extends AbstractExecutorService implements ICommitLogExecutorService
{
    protected volatile long completedTaskCount = 0;

    /**
     * Get the number of completed tasks
     */
    public long getCompletedTasks()
    {
        return completedTaskCount;
    }

    public boolean isTerminated()
    {
        throw new UnsupportedOperationException();
    }

    public boolean isShutdown()
    {
        throw new UnsupportedOperationException();
    }

    public List<Runnable> shutdownNow()
    {
        throw new UnsupportedOperationException();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
    {
        throw new UnsupportedOperationException();
    }
}
