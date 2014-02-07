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

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.AMQStoreException;
import org.wso2.andes.server.AMQChannel;
import org.wso2.andes.server.ClusterResourceHolder;
import org.wso2.andes.server.queue.AMQQueue;
import org.wso2.andes.server.store.CassandraMessageStore;
import org.wso2.andes.server.store.util.CassandraDataAccessException;
import org.wso2.andes.server.subscription.SubscriptionImpl;
import org.wso2.andes.server.util.AndesUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

public class DefaultClusteringEnabledSubscriptionManager implements ClusteringEnabledSubscriptionManager{

    private static Log log = LogFactory.getLog(DefaultClusteringEnabledSubscriptionManager.class);

    /**
     * keep in memory map of <destination queue, list of node queues having
     * subscriptions for destination queue> across whole cluster
     */
    private Map<String,List<String>> globalSubscriptionsMap = new ConcurrentHashMap<String,List<String>>();

    //keep in memory map of destination queues created in the cluster
    private Set<String> destinationQueues = Collections.synchronizedSet(new HashSet<String>());

    private Map<String,QueueDeliveryWorker> workMap =
            new ConcurrentHashMap<String,QueueDeliveryWorker>();

    /**
     * Keeps Subscription that have for this given queue locally
     */
    private Map<String,Map<String,CassandraSubscription>> subscriptionMap =
            new ConcurrentHashMap<String,Map<String,CassandraSubscription>>();

    private ExecutorService messageFlusherExecutor =  null;

    /**
     * Hash map that keeps the unacked messages.
     */
    private Map<AMQChannel, Map<Long, Semaphore>> unAckedMessagelocks =
            new ConcurrentHashMap<AMQChannel, Map<Long, Semaphore>>();


    private Map<AMQChannel,QueueSubscriptionAcknowledgementHandler> acknowledgementHandlerMap =
            new ConcurrentHashMap<AMQChannel,QueueSubscriptionAcknowledgementHandler>();

    private int queueWorkerWaitInterval;


    public void init()  {
        // TODO : Disruptor - remove all unwanted configurations
        ThreadFactory qDWNamedFactory = new ThreadFactoryBuilder().setNameFormat("QueueDeliveryWorker-%d").build();
        messageFlusherExecutor =  Executors.newFixedThreadPool(ClusterResourceHolder.getInstance().getClusterConfiguration().
                                      getFlusherPoolSize(), qDWNamedFactory);
        queueWorkerWaitInterval = ClusterResourceHolder.getInstance().getClusterConfiguration().
                getQueueWorkerInterval();
        // TODO : Disruptor - put this to disruptor too
        clearAndUpdateDestinationQueueList();
    }

    /**
     * Register a subscription for a Given Queue
     * This will handle the subscription addition task.
     * @param queue
     * @param subscription
     */
    public void addSubscription(AMQQueue queue, CassandraSubscription subscription) {
        try {
            if (subscription.getSubscription() instanceof SubscriptionImpl.BrowserSubscription) {
                boolean isInMemoryMode = ClusterResourceHolder.getInstance().getClusterConfiguration().isInMemoryMode();
                QueueBrowserDeliveryWorker deliveryWorker = new QueueBrowserDeliveryWorker(subscription.getSubscription(),queue,subscription.getSession(),isInMemoryMode);
                deliveryWorker.send();
            } else {

                Map<String, CassandraSubscription> subscriptions = subscriptionMap.get(queue.getResourceName());

                if (subscriptions == null || subscriptions.size() == 0) {
                    synchronized (subscriptionMap) {
                        subscriptions = subscriptionMap.get(queue.getResourceName());
                        if (subscriptions == null || subscriptions.size() == 0) {
                            subscriptions = subscriptionMap.get(queue.getResourceName());
                            if (subscriptions == null) {
                                subscriptions = new ConcurrentHashMap<String, CassandraSubscription>();
                                subscriptions.put(subscription.getSubscription().getSubscriptionID() + "",
                                        subscription);
                                subscriptionMap.put(queue.getResourceName(), subscriptions);
                                //for topic subscriptions no need to handleSubscription
                                if(!queue.checkIfBoundToTopicExchange()) {
                                    handleSubscription(queue);
                                }
                            } else if (subscriptions.size() == 0) {
                                subscriptions.put(subscription.getSubscription().getSubscriptionID() + "",
                                        subscription);
                                //for topic subscriptions no need to handleSubscription
                                if(!queue.checkIfBoundToTopicExchange()) {
                                    handleSubscription(queue);
                                }
                            }
                            incrementSubscriptionCount(true,queue.getResourceName());
                        } else {

                            subscriptions.put(subscription.getSubscription().getSubscriptionID() + "", subscription);
                        }
                    }
                } else {
                    subscriptions.put(subscription.getSubscription().getSubscriptionID() + "", subscription);
                    incrementSubscriptionCount(false,queue.getResourceName());
                }

                log.info("Binding Subscription with SUB_ID: "+subscription.getSubscription().getSubscriptionID()+" to queue "+queue.getName());

            }
            //if in stand-alone mode update destination queue-node queue map and reset global queue workers
            if(!ClusterResourceHolder.getInstance().getClusterConfiguration().isClusteringEnabled()) {
                synchronized (destinationQueues) {
                    List<String> destinationQueuesHavingFreshSubscriptions = ClusterResourceHolder.getInstance().
                            getSubscriptionManager().updateNodeQueuesForDestinationQueueMap();
                    //say global queue workers of this node to read from beginning for above queues
                    for(String destinationQueue : destinationQueuesHavingFreshSubscriptions) {
                        String globalQueueName = AndesUtils.getGlobalQueueNameForDestinationQueue(destinationQueue);
                        ClusterResourceHolder.getInstance().getClusterManager().getGlobalQueueManager().resetGlobalQueueWorkerIfRunning(globalQueueName);
                    }
                }
            } else {
                //notify the subscription change to the cluster nodes
                ClusterResourceHolder.getInstance().getSubscriptionCoordinationManager().handleSubscriptionChange();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Clear and update destination queue list
     */
    public void clearAndUpdateDestinationQueueList() {
        destinationQueues.clear();
        try {
            List<String> destinationQueues = ClusterResourceHolder.getInstance().getCassandraMessageStore().getDestinationQueues();
            for(String q : destinationQueues) {
                this.destinationQueues.add(q);
            }
        } catch (AMQStoreException e) {
            log.error("Error in updating in-memory list of destination queues" , e);
        }
    }

    /**
     * update in memory map keeping which nodes has subscriptions for given destination queue name
     */
    public List<String> updateNodeQueuesForDestinationQueueMap() {
        List<String> destinationQueuesHavingFreshSubscriptions = new ArrayList<String>();
        try {

            //record subscription count in cluster for each destination queue before update
            HashMap<String, Integer> destinationQueuesNumOfSubscriptionMap = new HashMap<String, Integer>();
            for (String destinationQueue : destinationQueues) {
                if (globalSubscriptionsMap.get(destinationQueue) == null) {
                    destinationQueuesNumOfSubscriptionMap.put(destinationQueue, 0);
                } else {
                    destinationQueuesNumOfSubscriptionMap.put(destinationQueue, globalSubscriptionsMap.get(destinationQueue).size());
                }
            }
            CassandraMessageStore messageStore = ClusterResourceHolder.getInstance().getCassandraMessageStore();
            globalSubscriptionsMap.clear();
            for (String destinationQueueName : destinationQueues) {
                List<String> nodeQueuesList = messageStore.getNodeQueuesForDestinationQueue(destinationQueueName);
                if (nodeQueuesList.size() > 0) {
                    List<String> queueList = new ArrayList<String>();
                    for (String nodeQueueName : nodeQueuesList) {
                        for (long i = 0; i < messageStore.getSubscriptionCountForQueue(destinationQueueName, nodeQueueName); i++) {
                            queueList.add(nodeQueueName);
                        }
                    }
                    globalSubscriptionsMap.put(destinationQueueName, queueList);
                }
            }
            //compare with previous state and identify to which queues subscriptions newly came
            for (String destinationQueue : destinationQueues) {
                int currentSubscriptionCount;
                if(globalSubscriptionsMap.get(destinationQueue) == null) {
                    currentSubscriptionCount = 0;
                } else {
                    currentSubscriptionCount = globalSubscriptionsMap.get(destinationQueue).size();
                }
                int previousSubscriptionCount = destinationQueuesNumOfSubscriptionMap.get(destinationQueue);
                if (currentSubscriptionCount > 0 && previousSubscriptionCount == 0) {
                    destinationQueuesHavingFreshSubscriptions.add(destinationQueue);
                }
            }
        }
        catch (CassandraDataAccessException ce){
            log.error("Error in getting the Node Queues as cassandra connection is down");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return destinationQueuesHavingFreshSubscriptions;
    }

    /**
     * Handle Subscription removal for a queue.
     * @param queue  queue for this Subscription
     * @param subId  SubscriptionId
     */
    public void removeSubscription(String queue, String subId, boolean isBoundToTopics ) {
        try {
            Map<String,CassandraSubscription> subs = subscriptionMap.get(queue);
            if (subs != null && subs.containsKey(subId)) {
                subs.remove(subId);
                log.info("Removing Subscription " + subId + " from queue " + queue);
                ClusterResourceHolder.getInstance().getCassandraMessageStore().decrementSubscriptionCount(queue, AndesUtils.getMyNodeQueueName(), 1);
                if (subs.size() == 0) {
                    log.debug("Executing subscription removal handler to minimize message losses");
                    //if in clustered mode copy messages addressed to that queue back to global queue
                    if(ClusterResourceHolder.getInstance().getClusterConfiguration().isClusteringEnabled()) {
                        handleMessageRemoval(queue, AndesUtils.getGlobalQueueNameForDestinationQueue(queue));
                    }
                    ClusterResourceHolder.getInstance().getCassandraMessageStore().removeSubscriptionCounterForQueue(queue, AndesUtils.getMyNodeQueueName());
                }
            }
        } catch (Exception e) {
            log.error("Error while removing subscription for queue: " + queue,e);
        }

        try {
            if(ClusterResourceHolder.getInstance().getClusterConfiguration().isClusteringEnabled()) {
                ClusterResourceHolder.getInstance().getSubscriptionCoordinationManager().handleSubscriptionChange();
            } else {
                //update destination queue node queue map
                List<String> destinationQueuesHavingFreshSubscriptions = ClusterResourceHolder.getInstance().
                        getSubscriptionManager().updateNodeQueuesForDestinationQueueMap();
                //say global queue workers of this node to read from beginning for above queues
                for(String destinationQueue : destinationQueuesHavingFreshSubscriptions) {
                    String globalQueueName = AndesUtils.getGlobalQueueNameForDestinationQueue(destinationQueue);
                    ClusterResourceHolder.getInstance().getClusterManager().getGlobalQueueManager().resetGlobalQueueWorkerIfRunning(globalQueueName);
                }
            }

        } catch (Exception e) {
            log.error("Error while notifying Subscription change");
        }

    }

    /**
     * get a List of nodes having subscriptions to the given destination queue
     * @param destinationQueue destination queue name
     * @return list of nodes
     */
    public List<String> getNodeQueuesHavingSubscriptionsForQueue(String destinationQueue) {
        return globalSubscriptionsMap.get(destinationQueue);
    }

    public void  handleFreshSubscriptionsJoiningToCluster() {
        //update destination queue node queue map
        List<String> destinationQueuesHavingFreshSubscriptions = updateNodeQueuesForDestinationQueueMap();
        //say global queue workers of this node to read from beginning for above queues
        for(String destinationQueue : destinationQueuesHavingFreshSubscriptions) {
            String globalQueueName = AndesUtils.getGlobalQueueNameForDestinationQueue(destinationQueue);
            ClusterResourceHolder.getInstance().getClusterManager().getGlobalQueueManager().resetGlobalQueueWorkerIfRunning(globalQueueName);
        }

    }

    private void handleMessageRemoval(String destinationQueue , String globalQueue) throws AMQStoreException {
        try {
            CassandraMessageStore messageStore = ClusterResourceHolder.getInstance().getCassandraMessageStore();
            String nodeQueueName = AndesUtils.getMyNodeQueueName();
            int numberOfMessagesMoved = 0;
            long lastProcessedMessageID = 0;
            List<CassandraQueueMessage> messages = messageStore.getMessagesFromNodeQueue(nodeQueueName, 40, lastProcessedMessageID);
            while (messages.size() != 0) {
                for (CassandraQueueMessage msg : messages) {
                    String destinationQueueName = msg.getDestinationQueueName();
                    //move messages addressed to this destination queue only
                    if(destinationQueueName.equals(destinationQueue)) {
                        numberOfMessagesMoved++;
                        messageStore.removeMessageFromNodeQueue(nodeQueueName, msg.getMessageId());

                        try {
                            //when adding back to global queue we mark it as an message that was already came in (as un-acked)
                            //we do not evaluate if message addressed queue is bound to topics as it is not used. Just pass false for that.
                            //for message properties  just pass default values as they will not be written to Cassandra again.
                            //we should add it to relevant globalQueue also
                            messageStore.addMessageToGlobalQueue(globalQueue, msg.getNodeQueue(), msg.getMessageId(), msg.getMessage(),false,0, false);
                        } catch (Exception e) {
                            log.error(e);
                        }
                    }
                    lastProcessedMessageID = msg.getMessageId();
                }
                messages = messageStore.getMessagesFromNodeQueue(nodeQueueName, 40, lastProcessedMessageID);
            }
            log.info("Moved " + numberOfMessagesMoved + " Number of Messages Addressed to Queue " +
                    destinationQueue + " from Node Queue " + nodeQueueName + "to Global Queue");

        } catch (AMQStoreException e) {
            log.error("Error removing messages addressed to " + destinationQueue + "from relevant node queue");
        }
    }

    private void handleSubscription(AMQQueue queue) {
        try {
            String globalQueueName = AndesUtils.getGlobalQueueNameForDestinationQueue(queue.getResourceName());
            String nodeQueueName = AndesUtils.getMyNodeQueueName();
            ClusterResourceHolder.getInstance().getCassandraMessageStore().addNodeQueueToGlobalQueue(globalQueueName, nodeQueueName);
            ClusterResourceHolder.getInstance().getCassandraMessageStore().addNodeQueueToDestinationQueue(queue.getResourceName(), nodeQueueName);
            ClusterResourceHolder.getInstance().getCassandraMessageStore().addMessageCounterForQueue(queue.getName());
            if (workMap.get(nodeQueueName) == null) {
                boolean isInMemoryMode = ClusterResourceHolder.getInstance().getClusterConfiguration().isInMemoryMode();
                QueueDeliveryWorker work = new QueueDeliveryWorker(nodeQueueName, queue,
                        subscriptionMap, queueWorkerWaitInterval,isInMemoryMode);
                workMap.put(nodeQueueName, work);
                messageFlusherExecutor.execute(work);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error while adding subscription to queue :" + queue, e);
        }

    }

    public void incrementSubscriptionCount(boolean instantiateColumn, String destinationQueueName) {
        String nodeQueueName = AndesUtils.getMyNodeQueueName();
        if (instantiateColumn) {
            ClusterResourceHolder.getInstance().getCassandraMessageStore().removeSubscriptionCounterForQueue(destinationQueueName, nodeQueueName);
            ClusterResourceHolder.getInstance().getCassandraMessageStore().addSubscriptionCounterForQueue(destinationQueueName, nodeQueueName);
        }
        ClusterResourceHolder.getInstance().getCassandraMessageStore().incrementSubscriptionCount(destinationQueueName, nodeQueueName, 1);
    }

    public void markSubscriptionForRemovel(String queue) {
        QueueDeliveryWorker work = workMap.get(queue);

        if (work != null) {
            work.stopFlusher();
        }

    }

    public int getNumberOfSubscriptionsForQueue(String queueName) {
        int numberOfSubscriptions = 0;
        Map<String,CassandraSubscription> subs = subscriptionMap.get(queueName);
        if(subs != null){
            numberOfSubscriptions = subs.size();
        }
        return numberOfSubscriptions;
    }

    public void stopAllMessageFlushers() {
        Collection<QueueDeliveryWorker> workers = workMap.values();
        for (QueueDeliveryWorker flusher : workers) {
            flusher.stopFlusher();
        }
    }

    public void startAllMessageFlushers() {
        Collection<QueueDeliveryWorker> workers = workMap.values();
        for (QueueDeliveryWorker flusher : workers) {
            flusher.startFlusher();
        }
    }

    public Map<String, QueueDeliveryWorker> getWorkMap() {
        return workMap;
    }

    public Map<AMQChannel, Map<Long, Semaphore>> getUnAcknowledgedMessageLocks() {
        return unAckedMessagelocks;
    }

    @Override
    public Map<AMQChannel, QueueSubscriptionAcknowledgementHandler> getAcknowledgementHandlerMap() {
        return acknowledgementHandlerMap;
    }

}
