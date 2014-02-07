package org.wso2.andes.util.concurrent;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import java.util.LinkedList;
import java.util.Queue;

/**
 * SynchQueue completes the {@link BatchSynchQueueBase} abstract class by providing an implementation of the underlying
 * queue as a linked list. This uses FIFO ordering for the queue and allows the queue to grow to accomodate more
 * elements as needed.
 *
 * <p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Provide linked list FIFO queue to create a batch synched queue around.
 * </table>
 */
public class SynchQueue<E> extends BatchSynchQueueBase<E>
{
    /**
     * Returns an empty queue, implemented as a linked list.
     *
     * @return An empty queue, implemented as a linked list.
     */
    protected <T> Queue<T> createQueue()
    {
        return new LinkedList<T>();
    }
}
