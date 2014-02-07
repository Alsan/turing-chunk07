/*
 *  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.andes.server.cluster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.server.ClusterResourceHolder;
import org.wso2.andes.server.cassandra.CassandraQueueMessage;
import org.wso2.andes.server.cassandra.ClusteringEnabledSubscriptionManager;
import org.wso2.andes.server.stats.PerformanceCounter;
import org.wso2.andes.server.store.CassandraMessageStore;

import java.util.*;

/**
 * <code>GlobalQueueWorker</code> is responsible for polling global queues and
 * distribute messages to the subscriber userQueues.
 */
public class GlobalQueueWorker implements Runnable {

    private static Log log = LogFactory.getLog(GlobalQueueWorker.class);

    private String globalQueueName;
    private boolean running;
    private int messageCountToReadFromCasssandra;
    private long lastProcessedMessageId;
    private CassandraMessageStore cassandraMessageStore;
    private long totMsgMoved = 0;


    public GlobalQueueWorker(String queueName, CassandraMessageStore cassandraMessageStore,
                             int messageCountToReadFromCasssandra) {
        this.cassandraMessageStore = cassandraMessageStore;
        this.globalQueueName = queueName;
        this.messageCountToReadFromCasssandra = messageCountToReadFromCasssandra;
        this.lastProcessedMessageId = 0;
    }

    public void run() {
        int queueWorkerWaitTime = ClusterResourceHolder.getInstance().getClusterConfiguration()
                .getQueueWorkerInterval();

        ClusteringEnabledSubscriptionManager csm = ClusterResourceHolder.getInstance().getSubscriptionManager();
        int repeatedSleepingCounter = 0;
        while (running) {
            try {
                /**
                 * Steps
                 *
                 * 1)Poll Global queue and get chunk of messages 2) Put messages
                 * one by one to node queues and delete them
                 */
                List<CassandraQueueMessage> cassandraMessages = cassandraMessageStore.getMessagesFromGlobalQueue(
                        globalQueueName, lastProcessedMessageId, messageCountToReadFromCasssandra);
                int size = cassandraMessages.size();
                PerformanceCounter.recordGlobalQueueMsgMove(size);

                if (csm == null) {
                    csm = ClusterResourceHolder.getInstance().getSubscriptionManager();
                }
                //Checking whether we have messages from the Global Queue
                if (cassandraMessages != null && cassandraMessages.size() > 0) {
                    repeatedSleepingCounter = 0;
                    Iterator<CassandraQueueMessage> messageIterator = cassandraMessages.iterator();
                    while (messageIterator.hasNext()) {
                        CassandraQueueMessage msg = messageIterator.next();
                        String destinationQueue = msg.getDestinationQueueName();
                        Random random = new Random();
                        //check if the cluster has some subscriptions for that message and distribute to relevant node queues
                        if (csm.getNodeQueuesHavingSubscriptionsForQueue(destinationQueue) != null &&
                                csm.getNodeQueuesHavingSubscriptionsForQueue(destinationQueue).size() > 0) {

                            int index = random.nextInt(csm.getNodeQueuesHavingSubscriptionsForQueue(destinationQueue).size());
                            String nodeQueue = csm.getNodeQueuesHavingSubscriptionsForQueue(destinationQueue).get(index);
                            msg.setNodeQueue(nodeQueue);

                        } else {
                            //if there is no node queue to move message we skip
                            messageIterator.remove();
                        }

                        lastProcessedMessageId = msg.getMessageId();
                    }

                    cassandraMessageStore.transferMessageBatchFromGlobalQueueToNodeQueue(cassandraMessages, globalQueueName);

                    totMsgMoved = totMsgMoved + cassandraMessages.size();
                    if (log.isDebugEnabled()) {
                        log.debug("[Global, " + globalQueueName + "] moved " + cassandraMessages.size()
                                + " to node queues, tot = " + totMsgMoved + " ,Last ID:" + lastProcessedMessageId);
                    }
                } else {
                    try {
                        Thread.sleep(queueWorkerWaitTime);
                        repeatedSleepingCounter++;
                        if (repeatedSleepingCounter > 1) {
                            resetMessageReading();
                        }
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            } catch (Exception e) {
                log.error("Error in moving messages from global queue to node queue", e);
            }
        }

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void resetMessageReading() {
        this.lastProcessedMessageId = 0;
        if (log.isDebugEnabled()) {
            log.debug("Worker for Global Queue " + globalQueueName + " is reset");
        }
    }
}
