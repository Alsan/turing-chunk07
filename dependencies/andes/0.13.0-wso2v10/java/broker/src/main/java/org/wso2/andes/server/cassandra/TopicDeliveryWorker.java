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
import org.wso2.andes.exchange.ExchangeDefaults;
import org.wso2.andes.server.ClusterResourceHolder;
import org.wso2.andes.server.binding.Binding;
import org.wso2.andes.server.exchange.AbstractExchange;
import org.wso2.andes.server.exchange.Exchange;
import org.wso2.andes.server.exchange.ExchangeRegistry;
import org.wso2.andes.server.message.AMQMessage;
import org.wso2.andes.server.protocol.AMQProtocolSession;
import org.wso2.andes.server.queue.AMQQueue;
import org.wso2.andes.server.queue.QueueEntry;
import org.wso2.andes.server.queue.SimpleAMQQueue;
import org.wso2.andes.server.store.CassandraMessageStore;
import org.wso2.andes.server.util.AndesUtils;
import org.wso2.andes.server.virtualhost.VirtualHost;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>TopicDeliveryWorker</code>
 * Handle the task of publishing messages to all the subscribers
 * of a topic
 * */
public class TopicDeliveryWorker extends Thread{
    private AMQProtocolSession session;
    private Binding binding ;
    private SimpleAMQQueue queue;
    private AbstractExchange exchange;
    private long lastDeliveredMessageID = 0;
    private VirtualHost virtualHost;
    private boolean working = false;
    private boolean markedForRemoval;
    private String id;
    private String topicNodeQueueName;
    private CassandraMessageStore messageStore = null;
    private boolean isInMemoryMode = false;

    private static Log log = LogFactory.getLog(TopicDeliveryWorker.class);

    private SequentialThreadPoolExecutor executor;

    public TopicDeliveryWorker(Binding binding, AMQQueue queue, Exchange exchange, VirtualHost virtualHost,boolean isInMemoryMode){
        this.binding = binding;
        this.exchange = (AbstractExchange) exchange;
        this.queue = (SimpleAMQQueue) queue;
        this.virtualHost = virtualHost;
        this.topicNodeQueueName = AndesUtils.getTopicNodeQueueName();
        this.id = topicNodeQueueName;
        this.messageStore = ClusterResourceHolder.getInstance().
                getCassandraMessageStore();
        this.isInMemoryMode = isInMemoryMode;
        messageStore.registerSubscriberForTopic(binding.getBindingKey(), topicNodeQueueName,queue.getResourceName());
    }

    /**
     * 1. Get messages for the queue from last delivered message id
     * 2. Enqueue the retrived message to the queue
     * 3. Remove delivered messaged IDs from the data base
     * */
    @Override
    public void run() {
        if (isInMemoryMode) {
            try {
                working = true;

                List<AMQMessage> messages = messageStore.getNextTopicMessageToDeliver();
                if (messages != null) {
                    List<Long> publishedMids = new ArrayList<Long>();
                    try {
                        for (AMQMessage message : messages) {
                            enqueueMessageToWorkerDestinationQueue(message);
                            publishedMids.add(message.getMessageNumber());
                            lastDeliveredMessageID = message.getMessageNumber();
                            if (log.isDebugEnabled()) {
                                log.debug("Sending message  " + lastDeliveredMessageID + "from cassandra topic publisher");
                            }
                        }

                    } catch (Exception e) {
                        log.error("Error on enqueue messages to relevant queue:" + e.getMessage(), e);
                    }
                    messageStore.removeDeliveredTopicMessageIdsFromIncomingMessagesTable(publishedMids);

                } else {
                    try {
                        Thread.sleep(ClusterResourceHolder.getInstance().getClusterConfiguration().
                                getQueueWorkerInterval());
                    } catch (InterruptedException e) {
                        //silently ignore
                    }
                }
            } catch (Exception e) {
                log.error("Error in sending message out in in memory mode ", e);
            } finally {
                working = false;
            }
        } else {
            try {
                working = true;

                List<AMQMessage> messages = messageStore.getSubscriberMessages(topicNodeQueueName,
                        lastDeliveredMessageID++);
                if (messages != null && messages.size() > 0) {
                    List<Long> publishedMids = new ArrayList<Long>();
                    for (AMQMessage message : messages) {
                        try {
                            enqueueMessage(message);
                            publishedMids.add(message.getMessageNumber());
                            lastDeliveredMessageID = message.getMessageNumber();
                            if (log.isDebugEnabled()) {
                                log.debug("Sending message  " + lastDeliveredMessageID + "from cassandra topic publisher" + queue.getName());
                            }
                        } catch (Exception e) {
                            log.error("Error on enqueue messages to relevant queue:" + e.getMessage(), e);
                        }
                    }
                    //we do not consider acknowledgements. We think qpid simpleAMQQueue model will handle re-delivery, expiration etc
                    //we remove stored messages here.
                    messageStore.removeDeliveredTopicMessageIds(publishedMids, topicNodeQueueName);
                } else {
                    try {
                        Thread.sleep(ClusterResourceHolder.getInstance().getClusterConfiguration().
                                getQueueWorkerInterval());
                    } catch (InterruptedException e) {
                        //silently ignore
                    }
                }
            } catch (AMQStoreException e) {
                log.error("Error removing delivered Message Ids from Message store ", e);
            } finally {
                working = false;
            }
        }
    }

    /**
     * Enqueue a given message to all subscriber queues bound to TOPIC_EXCHANGE matching with routing key
     * @param message AMQ message
     */
    private void enqueueMessage(AMQMessage message) {
        Exchange exchange;
        ExchangeRegistry exchangeRegistry = virtualHost.getExchangeRegistry();
        exchange = exchangeRegistry.getExchange(ExchangeDefaults.TOPIC_EXCHANGE_NAME);
        if (exchange != null) {
            /**
             * There can be more than one binding to the same topic
             * We need to publish the message to the exact matching queues
             * */
            String queueName = message.getMessageMetaData().getMessagePublishInfo().getRoutingKey().toString();
            //TODO Srinath, it might be better to publish messages directly to the client like we do with queues rather than going through the enqueue path
            for(Binding binding: exchange.getBindings()){
                  if(isMatching(binding.getBindingKey(),queueName)){
                    message.setTopicMessage(true);
                    deliverAsynchronously(binding,message);
                    if(log.isDebugEnabled()){
                         log.info("sent1 ("+ message.getMessageNumber() + ")" + AndesUtils.printAMQMessage((QueueEntry)message));
                     }
                }
            }
        }
    }

    /**
     * Enqueue a given message to the subscriber queue assigned with this worker thread
     * @param message AMQ message
     */
    private void enqueueMessageToWorkerDestinationQueue(AMQMessage message) {
        Exchange exchange;
        ExchangeRegistry exchangeRegistry = virtualHost.getExchangeRegistry();
        exchange = exchangeRegistry.getExchange(ExchangeDefaults.TOPIC_EXCHANGE_NAME);

        if (exchange != null) {
            /**
             * There can be more bindings. But only one queue will be there of the name
             * assigned to this worker thread
             * */
            String topicName = message.getMessageMetaData().getMessagePublishInfo().getRoutingKey().toString();
             for(Binding binding: exchange.getBindings()){
                if(isMatching(binding.getBindingKey(), topicName)){
                    message.setTopicMessage(true);
                    deliverAsynchronously(binding,message);
                    if(log.isDebugEnabled()){
                        log.info("sent1 ("+ message.getMessageNumber() + ")" + AndesUtils.printAMQMessage((QueueEntry)message));
                    }

                }
            }
        }

    }

    public boolean isMatching(String binding, String topic) {
        boolean isMatching = false;
        if (binding.equals(topic)) {
            isMatching = true;
        } else if (binding.indexOf(".#") > 1) {
            String p = binding.substring(0, binding.indexOf(".#"));
            Pattern pattern = Pattern.compile(p + ".*");
            Matcher matcher = pattern.matcher(topic);
            isMatching = matcher.matches();
        } else if (binding.indexOf(".*") > 1) {
            String p = binding.substring(0, binding.indexOf(".*"));
            Pattern pattern = Pattern.compile("^" + p + "[.][^.]+$");
            Matcher matcher = pattern.matcher(topic);
            isMatching = matcher.matches();
        }
        return isMatching;
    }


    /**
     * Set this topic delivery task active
     * @return
     */
    public boolean isWorking() {
        return working;
    }

    /**
     * Check if this delivery thread is marked to be inactive
     * @return state
     */
    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    /**
     * Mark or un-mark this delivery thread to be removed
     * @param markedForRemoval if to mark for removal
     */
    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }

    public String getQueueId() {
        return id;
    }

    private void deliverAsynchronously(final Binding binding , final AMQMessage message) {

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                         binding.getQueue().enqueue(message);
                    } catch (Throwable e) {
                         log.error("Error while delivering message " ,e);
                    }
                }
            };
            long subscriptionId = Math.abs(binding.getId().hashCode());
            ClusterResourceHolder.getInstance().getCassandraTopicPublisherManager()
                    .getMessagePublishingExecutor().submit(r, subscriptionId);
    }

}
