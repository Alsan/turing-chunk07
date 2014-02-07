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

package org.wso2.andes.server.cassandra;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.AMQStoreException;
import org.wso2.andes.server.ClusterResourceHolder;
import org.wso2.andes.server.store.CassandraMessageStore;
import org.wso2.andes.server.util.AndesUtils;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class will handle expired message removal from cassandra message store
 */
public class ExpiredCassandraMessageRemover {

    private static Log log = LogFactory.getLog(ExpiredCassandraMessageRemover.class);
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static ExpiredCassandraMessageRemover instance = new ExpiredCassandraMessageRemover();
    private CassandraMessageStore messageStore = ClusterResourceHolder.getInstance().getCassandraMessageStore();
    private int messageCountToRead = 200;

    public static ExpiredCassandraMessageRemover getInstance(){
        return instance;
    }

    private ExpiredCassandraMessageRemover() {

        log.info("Starting periodic expired messages cleaning task");

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        List<String> destinationQueueList = messageStore.getDestinationQueueNames();
                        for(String destinationQueue : destinationQueueList) {
                            String nodeQueueName = AndesUtils.getMyNodeQueueName();
                            //we are checking for oldest 200 messages
                            List<CassandraQueueMessage> messages = messageStore.getMessagesFromNodeQueue(nodeQueueName,messageCountToRead,0);
                            long now = System.currentTimeMillis();
                            for(CassandraQueueMessage cassandraMessage : messages) {
                                //check (now > expiration);
                                if(cassandraMessage.getAmqMessage().getExpiration() != 0L && now > cassandraMessage.getAmqMessage().getExpiration())    {
                                    removeNodeQueueMessagesFromCassandraStore(cassandraMessage.getMessageId(), destinationQueue);
                                    log.warn("Message is expired. Dropping message: "+cassandraMessage.getMessageId());
                                }
                            }
                        }

                        //check global queue also
                        List<String> workerRunningGlobalQueueNames = ClusterResourceHolder.getInstance().getClusterManager().
                                getGlobalQueueManager().getWorkerRunningGlobalQueueNames();

                        for(String globalQueue : workerRunningGlobalQueueNames) {
                            Queue<CassandraQueueMessage> messageQueue = messageStore.getMessagesFromGlobalQueue(globalQueue,messageCountToRead);
                            long now = System.currentTimeMillis();
                            for(CassandraQueueMessage cassandraMessage : messageQueue) {
                                String destinationQueue = cassandraMessage.getDestinationQueueName();
                                //check (now > expiration);
                                if(cassandraMessage.getAmqMessage().getExpiration() != 0L && now > cassandraMessage.getAmqMessage().getExpiration())    {
                                    removeGlobalQueueMessagesFromCassandraStore(cassandraMessage.getMessageId(), globalQueue, destinationQueue);
                                    log.warn("Message is expired. Dropping message: " + cassandraMessage.getMessageId());
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error in removing expired messages from MessageStore",e);
                    }
                }
            }
        },  10, 10, TimeUnit.SECONDS);
    }

    private void removeNodeQueueMessagesFromCassandraStore(long messageNumber, String destinationQueueName) {

        OnflightMessageTracker.getInstance().removeNodeQueueMessageFromStorePermanentlyAndDecrementMsgCount
                (messageNumber, destinationQueueName);

    }

    private void removeGlobalQueueMessagesFromCassandraStore(long messageNumber, String globalQueueName, String destinationQueueName) {

        //decrement number of messages
        ClusterResourceHolder.getInstance().getCassandraMessageStore().decrementQueueCount(destinationQueueName,1L);
        //remove message from global queue
        ClusterResourceHolder.getInstance().getCassandraMessageStore().removeMessageFromGlobalQueue(globalQueueName, messageNumber);
        //schedule message content and properties to be removed
        ClusterResourceHolder.getInstance().getCassandraMessageStore().addContentDeletionTask(messageNumber);
        //schedule message-queue mapping to be removed as well
        ClusterResourceHolder.getInstance().getCassandraMessageStore().
                addMessageQueueMappingDeletionTask(destinationQueueName,messageNumber);
    }

    public void stopTask() {
        scheduler.shutdownNow();
    }
}


