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
import org.wso2.andes.server.message.AMQMessage;
import org.wso2.andes.server.protocol.AMQProtocolSession;
import org.wso2.andes.server.queue.AMQQueue;
import org.wso2.andes.server.queue.IncomingMessage;
import org.wso2.andes.server.queue.QueueEntry;
import org.wso2.andes.server.queue.SimpleQueueEntryList;
import org.wso2.andes.server.store.CassandraMessageStore;
import org.wso2.andes.server.subscription.Subscription;
import org.wso2.andes.server.subscription.SubscriptionImpl;
import org.wso2.andes.server.util.AndesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;

/**
 * From JMS Spec
 * -----------------
 *
 * A client uses a QueueBrowser to look at messages on a queue without removing
 * them.
 * The browse methods return a java.util.Enumeration that is used to scan the
 * queue’s messages. It may be an enumeration of the entire content of a queue or
 * it may only contain the messages matching a message selector.
 * Messages may be arriving and expiring while the scan is done. JMS does not
 * require the content of an enumeration to be a static snapshot of queue content.
 * Whether these changes are visible or not depends on the JMS provider.
 * 
 * When someone made a QueueBroswer Subscription, we read messages for that queue and 
 * send them to that subscription. 
 */

public class QueueBrowserDeliveryWorker {

    private Subscription subscription;
    private AMQQueue queue;
    private AMQProtocolSession session;
    private String id;
    private int defaultMessageCount = Integer.MAX_VALUE;
    private int messageCount;
    private int messageBatchSize;
    private boolean isInMemoryMode = false;

    private static Log log = LogFactory.getLog(QueueBrowserDeliveryWorker.class);

    private HashMap<String,Long> lastReadMessageIdMap = new HashMap<String,Long>();

    public QueueBrowserDeliveryWorker(Subscription subscription, AMQQueue queue, AMQProtocolSession session){
        this(subscription,queue,session,false);
    }

    public QueueBrowserDeliveryWorker(Subscription subscription, AMQQueue queue, AMQProtocolSession session, boolean isInMemoryMode) {
        this.subscription = subscription;
        this.queue = queue;
        this.session = session;
        this.id = "" + subscription.getSubscriptionID();
        this.isInMemoryMode = isInMemoryMode;
        this.messageCount = defaultMessageCount;
        this.messageBatchSize = ClusterResourceHolder.getInstance().getClusterConfiguration().
                getMessageBatchSizeForBrowserSubscriptions();

    }


    public void send() {
        if (isInMemoryMode) {
            List<QueueEntry> messages;
            try {
                messages = getSortedMessagesForInMemoryQueue(queue);
                sendMessagesToClient(messages);
            } catch (AMQStoreException e) {
                log.error("Error while sending message for Browser subscription", e);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                // It is essential to confirm auto close , since in the client side it waits to know the end of the messages
                subscription.confirmAutoClose();
            }
        } else {
            List<QueueEntry> messages = null;
            try {
                messages = getSortedMessages();
                sendMessagesToClient(messages);
            } catch (AMQStoreException e) {
                log.error("Error while sending message for Browser subscription", e);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // It is essential to confirm auto close , since in the client side it waits to know the end of the messages
                subscription.confirmAutoClose();
            }
        }

    }

    /**
     * Sends the browser subscription's messages to client
     * @param messages - matching messages of queue
     */
    private void sendMessagesToClient(List<QueueEntry> messages){
            if (messages.size() > 0) {
                int count = messageBatchSize;
                if (messages.size() < messageBatchSize) {
                    count = messages.size();
                }
                for (int i = 0; i < count; i++) {
                    QueueEntry message = messages.get(i);
                    try {
                        if (subscription instanceof SubscriptionImpl.BrowserSubscription) {
                            subscription.send(message);
                        }

                    } catch (Exception e) {
                        log.error("Unexpected Error in Message Flusher Task " +
                                "while delivering the message : ", e);
                    }
                }
            }
    }

    /**
     * When using queue browser in InMemory mode this method is used to get the messages list for the given queue
     * @param queue - the corresponding queue
     * @return - sorted QueueEntry list with messages published into queue
     * @throws AMQStoreException
     */
    private List<QueueEntry> getSortedMessagesForInMemoryQueue(AMQQueue queue) throws AMQStoreException {
        List<IncomingMessage> messages = new ArrayList<IncomingMessage>();
        CassandraMessageStore messageStore = ClusterResourceHolder.getInstance().
                getCassandraMessageStore();
        Hashtable<Long, IncomingMessage> incomingQueueMessageHashtable = messageStore.getIncomingQueueMessageHashtable();
        Enumeration<IncomingMessage> enu = incomingQueueMessageHashtable.elements();
        while (enu.hasMoreElements()){
            IncomingMessage inMessage = enu.nextElement();
            if(inMessage.getRoutingKey().equals(queue.getName())){
                messages.add(inMessage);
            }
        }
        InMemoryMessageComparator orderComparator = new InMemoryMessageComparator();
        Collections.sort(messages, orderComparator);
        return getBrowserMessagesForInMemoryMode(queue, messages);
    }

    /**
     * Creates QueueEntries from an IncomingMessage list of an in-memory queue
     * @param queue - the queue to be browsed
     * @param incomingMessages - list of messages in the queue
     * @return - list of QueueEntries for the queue
     * @throws AMQStoreException
     */
    private List<QueueEntry> getBrowserMessagesForInMemoryMode(AMQQueue queue, List<IncomingMessage> incomingMessages) throws AMQStoreException {
        List<QueueEntry> amqMessageList = new ArrayList<QueueEntry>();
        SimpleQueueEntryList list = new SimpleQueueEntryList(queue);
        AMQMessage message;
        for(IncomingMessage incomingMessage: incomingMessages){
            message = new AMQMessage(incomingMessage.getStoredMessage());
            amqMessageList.add(list.add(message));
        }
        return amqMessageList;
    }

    private List<QueueEntry> getSortedMessages() throws Exception {
        CassandraMessageStore messageStore = ClusterResourceHolder.getInstance().
                getCassandraMessageStore();
        List<CassandraQueueMessage> queueMessages = new ArrayList<CassandraQueueMessage>();
        queueMessages = readMessages(queueMessages, messageBatchSize);
        int retryCount = 2;
        while (queueMessages.size() < messageBatchSize && retryCount < 5) {
            queueMessages = readMessages(queueMessages, messageBatchSize * retryCount);
            retryCount++;
        }
        if(queueMessages.size() < messageBatchSize){
            queueMessages = readMessagesFromGlobalQueue(queueMessages,messageBatchSize);
        }
        CustomComparator orderComparator = new CustomComparator();
        Collections.sort(queueMessages, orderComparator);
        return messageStore.getPreparedBrowserMessages(queue, session, queueMessages);
    }

    private List<CassandraQueueMessage> readMessages(List<CassandraQueueMessage> messages, int messageBatchSize) throws Exception {
        CassandraMessageStore messageStore = ClusterResourceHolder.getInstance().
                getCassandraMessageStore();
        List<String> nodeQueues = messageStore.getNodeQueuesForDestinationQueue(queue.getResourceName());
        long lastReadMessageId = 0;
        for (String nodeQueue : nodeQueues) {
            if(lastReadMessageIdMap.get(nodeQueue) != null){
                lastReadMessageId = lastReadMessageIdMap.get(nodeQueue);
            }
            List<CassandraQueueMessage> allMessages = messageStore.getMessagesFromNodeQueue(nodeQueue, messageBatchSize, lastReadMessageId);
            for (CassandraQueueMessage message : allMessages) {
                if (message.getDestinationQueueName().equals(queue.getResourceName())) {
                    messages.add(message);
                }
                lastReadMessageIdMap.put(nodeQueue,message.getMessageId());
            }
        }
        return messages;
    }

    private List<CassandraQueueMessage> readMessagesFromGlobalQueue(List<CassandraQueueMessage> messages, int messageBatchSize) throws Exception {
        CassandraMessageStore messageStore = ClusterResourceHolder.getInstance().getCassandraMessageStore();
        String globalQueueName = AndesUtils.getGlobalQueueNameForDestinationQueue(queue.getResourceName());
        Queue<CassandraQueueMessage> cassandraMessages = messageStore.getMessagesFromGlobalQueue(globalQueueName, messageBatchSize);
        for (CassandraQueueMessage message : cassandraMessages) {
            if (message.getDestinationQueueName().equals(queue.getResourceName())) {
                messages.add(message);
            }
        }
        return messages;
    }

    public class CustomComparator implements Comparator<CassandraQueueMessage>{

        public int compare(CassandraQueueMessage message1, CassandraQueueMessage message2) {
            return (int) (message1.getMessageId()-message2.getMessageId());
        }
    }

    public class InMemoryMessageComparator implements Comparator<IncomingMessage>{

        public int compare(IncomingMessage message1, IncomingMessage message2) {
            return (int) (message1.getMessageNumber()-message2.getMessageNumber());
        }
    }

}
