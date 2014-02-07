package org.wso2.andes.server.cassandra;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.server.AMQChannel;
import org.wso2.andes.server.ClusterResourceHolder;
import org.wso2.andes.server.configuration.ClusterConfiguration;
import org.wso2.andes.server.message.AMQMessage;
import org.wso2.andes.server.protocol.AMQProtocolSession;
import org.wso2.andes.server.queue.AMQQueue;
import org.wso2.andes.server.queue.QueueEntry;
import org.wso2.andes.server.store.CassandraMessageStore;
import org.wso2.andes.server.subscription.Subscription;
import org.wso2.andes.server.subscription.SubscriptionImpl;
import org.wso2.andes.server.util.AndesUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <code>QueueDeliveryWorker</code> Handles the task of polling the user queues and flushing
 * the messages to subscribers
 * There will be one Flusher per Queue Per Node
 */
public class QueueDeliveryWorker extends Thread {
    private AMQQueue queue;
    private String nodeQueue;
    private boolean running = true;
    private static Log log = LogFactory.getLog(QueueDeliveryWorker.class);

    private int messageCountToRead = 50;
    private int maxMessageCountToRead = 300;
    private int minMessageCountToRead = 20;


    private int maxNumberOfUnAckedMessages = 20000;
    private int maxNumberOfReadButUndeliveredMessages = 1000;

    private long lastProcessedId = 0;

    private int resetCounter;

    private int maxRestCounter = 50;

    private long totMsgSent = 0;
    private long totMsgRead = 0;

    private long lastRestTime = 0;

    private int queueWorkerWaitInterval;

    private int queueMsgDeliveryCurserResetTimeInterval;

    private OnflightMessageTracker onflightMessageTracker;


    private long iterations = 0;

    private long failureCount = 0;

    private Map<String, Map<String, CassandraSubscription>> subscriptionMap =
            new ConcurrentHashMap<String, Map<String, CassandraSubscription>>();

    private int totalReadButUndeliveredMessages = 0;
    private boolean isInMemoryMode = false;

    private final AtomicInteger pendingMessagesToSent = new AtomicInteger();

    private class QueueDeliveryInfo {
        private String queueName;
        private Iterator<CassandraSubscription> iterator;
        private List<QueueEntry> readButUndeliveredMessages = new ArrayList<QueueEntry>();
        private boolean hasQueueFullAndMessagesIgnored = false;
        private long lastReadMessageId;
        private long lastAckedMessageId;
        private long ignoredFirstMessageId = -1;
        private HashSet<Long> alreadyAddedMessages = new HashSet<Long>();

    }

    private Map<String, QueueDeliveryInfo> subscriptionCursar4QueueMap = new HashMap<String, QueueDeliveryInfo>();

    /**
     * Get the next subscription for the given queue. If at end of the subscriptions, it circles around to the first one
     *
     * @param queueName
     * @return
     */
    public CassandraSubscription findNextSubscriptionToSent(String queueName) {
        Map<String, CassandraSubscription> subscriptions = subscriptionMap.get(queueName);
        if (subscriptions == null || subscriptions.size() == 0) {
            subscriptionCursar4QueueMap.remove(queueName);
            return null;
        }

        QueueDeliveryInfo queueDeliveryInfo = getQueueDeliveryInfo(queueName);
        Iterator<CassandraSubscription> it = queueDeliveryInfo.iterator;
        if (it.hasNext()) {
            return it.next();
        } else {
            it = subscriptions.values().iterator();
            queueDeliveryInfo.iterator = it;
            if (it.hasNext()) {
                return it.next();
            } else {
                return null;
            }
        }
    }

    private QueueDeliveryInfo getQueueDeliveryInfo(String queueName) {
        QueueDeliveryInfo queueDeliveryInfo = subscriptionCursar4QueueMap.get(queueName);
        if (queueDeliveryInfo == null) {
            queueDeliveryInfo = new QueueDeliveryInfo();
            queueDeliveryInfo.queueName = queueName;
            Map<String, CassandraSubscription> subscriptions = subscriptionMap.get(queueName);
            if (subscriptions != null) {
                queueDeliveryInfo.iterator = subscriptions.values().iterator();
            } else {
                // if subscriptions map is null, we are returning an iterator for an empty list to avoid
                // queueDeliveryInfo.iterator being null
                queueDeliveryInfo.iterator = Collections.<CassandraSubscription>emptyList().iterator();
            }
            subscriptionCursar4QueueMap.put(queueName, queueDeliveryInfo);
        }
        return queueDeliveryInfo;
    }

    public QueueDeliveryWorker(String nodeQueue, AMQQueue queue, Map<String, Map<String, CassandraSubscription>> subscriptionMap,
                               int queueWorkerWaitInterval, boolean isInMemoryMode) {
        this.queue = queue;
        this.nodeQueue = nodeQueue;

        ClusterConfiguration clusterConfiguration = ClusterResourceHolder.getInstance().getClusterConfiguration();
        this.messageCountToRead = clusterConfiguration.getMessageBatchSizeForSubscribers();
        this.maxMessageCountToRead = clusterConfiguration.getMaxMessageBatchSizeForSubscribers();
        this.minMessageCountToRead = clusterConfiguration.getMinMessageBatchSizeForSubscribers();
        this.maxNumberOfUnAckedMessages = clusterConfiguration.getMaxNumberOfUnackedMessages();
        this.maxNumberOfReadButUndeliveredMessages = clusterConfiguration.getMaxNumberOfReadButUndeliveredMessages();
        this.queueMsgDeliveryCurserResetTimeInterval = clusterConfiguration.getQueueMsgDeliveryCurserResetTimeInterval();
        this.queueWorkerWaitInterval = queueWorkerWaitInterval;
        this.subscriptionMap = subscriptionMap;
        onflightMessageTracker = OnflightMessageTracker.getInstance();
        this.isInMemoryMode = isInMemoryMode;

        log.info("Queue worker started for queue: " + queue.getResourceName() + " with on flight message checks");
    }

    @Override
    public void run() {
        iterations = 0;
        lastRestTime = System.currentTimeMillis();
        failureCount = 0;

        /**
         * Please read following carefully if you plan to change the code here. 
         * Goal of this code is to read messages from Cassandra and send them to subscribers. Then subscriber sends an Ack back. There is a class called
         * OnflightMessageTracker that keeps track of messages that has been sent and Ack timeouts. 
         * This code loops though the Cassandra, reads messages and when come to end, it waits for some time and start from the begining. We will consult
         * OnflightMessageTracker to not send any message twice. 
         * Loop has three parts
         * <ol>
         * <li>1) Check should we read Cassandra at all</li>
         * <li>2) Read messages and put them in to QueueDeliveryInfo object</li>
         * <li>2) Try to send messages if there is room</li>
         * </ol>
         *
         */
        while (running) {
            try {
                /**
                 * Part 1: Check do we need to read Cassandra at all
                 */
                // http://mechanitis.blogspot.com/2011/06/dissecting-disruptor-whats-so-special.html
                // http://stackoverflow.com/questions/6559308/how-does-lmaxs-disruptor-pattern-work
                // No need to check worker queue size anymore as it automatically blocked when queue is full
                // But as a improvement we can send a warning when queue is full
                if (onflightMessageTracker.getSentButNotAckedMessageCount() > 1000) {
                    /**
                     * If we send too many messages before they are acked, the returning Acks can queues up and 
                     * server will timeout the message before it receives the Ack. This check avoids the problem. 
                     *
                     * When the problem occurred, the time for ack to receives grows very fast, which leads messages to time out
                     * and MB will have to resend them. 
                     */
                    log.info("skipping cassandra reading thread as there are more than 1000 pending acks to recive");
                    sleep4waitInterval();
                    continue;
                }

                resetOffsetAtCassandraQueueIfNeeded(false);

                /**
                 * Part 2: Following reads from Cassandara, it reads only if there are not enough messages loaded in memory
                 */
                int msgReadThisTime = 0;
                List<QueueEntry> messagesFromCassandra = null;
                if (isInMemoryMode) {
                    CassandraMessageStore messageStore =
                            ClusterResourceHolder.getInstance().getCassandraMessageStore();

                    messagesFromCassandra = messageStore.
                            getNextIgnoredQueueMessagesToDeliver(queue, messageCountToRead);

                    if (messagesFromCassandra.size() == 0) {
                        messagesFromCassandra = messageStore.
                                getNextQueueMessagesToDeliver(queue, messageCountToRead);
                    }
                    for (QueueEntry message : messagesFromCassandra) {
                        String queueName = ((AMQMessage) message.getMessage()).
                                getMessageMetaData().getMessagePublishInfo().getRoutingKey().toString();
                        QueueDeliveryInfo queueDeliveryInfo = getQueueDeliveryInfo(queueName);
                        if (!queueDeliveryInfo.hasQueueFullAndMessagesIgnored) {
                            if (queueDeliveryInfo.readButUndeliveredMessages.size() < maxNumberOfReadButUndeliveredMessages) {
                                queueDeliveryInfo.readButUndeliveredMessages.add(message);
                                totalReadButUndeliveredMessages++;
                                lastProcessedId = message.getMessage().getMessageNumber();
                            } else {
                                queueDeliveryInfo.hasQueueFullAndMessagesIgnored = true;
                            }
                        }

                        if (queueDeliveryInfo.hasQueueFullAndMessagesIgnored) {
                            // TODO : There is a better way to do this, instead of polling
                            // pendingMessageIdsQueue messages from here, we can just get the
                            // messages and remove once the ack comes so we can preserve sequence i
                            // n clean manner. This w can think about in next version of MB
                            lastProcessedId = message.getMessage().getMessageNumber();
                            messageStore.setNextIgnoredQueueMessageId(lastProcessedId);
                        }
                    }

                    if (messagesFromCassandra.size() == 0) {
                        sleep4waitInterval();
                    }

                    //If we have read all messages we asked for, we increase the reading count. Else we reduce it.
                    //TODO we might want to take into account the size of the message while we change the batch size
                    if (messagesFromCassandra.size() == messageCountToRead) {
                        messageCountToRead += 100;
                        if (messageCountToRead > maxMessageCountToRead) {
                            messageCountToRead = maxMessageCountToRead;
                        }
                    } else {
                        messageCountToRead -= 50;
                        if (messageCountToRead < minMessageCountToRead) {
                            messageCountToRead = minMessageCountToRead;
                        }
                    }
                    totMsgRead = totMsgRead + messagesFromCassandra.size();
                    msgReadThisTime = messagesFromCassandra.size();
                } else {
                    CassandraMessageStore messageStore = ClusterResourceHolder.getInstance().getCassandraMessageStore();
                    messagesFromCassandra = messageStore.getMessagesFromNodeQueue(nodeQueue, queue, messageCountToRead, lastProcessedId);
                    //We read messages from Cassandra, and try to send
                    for (QueueEntry message : messagesFromCassandra) {
                        /**
                         * If this is a message that had sent already, just drop them.
                         */
                        if (!onflightMessageTracker.testMessage(message.getMessage().getMessageNumber())) {
                            continue;
                        }
                        String queueName = ((AMQMessage) message.getMessage()).getMessageMetaData().getMessagePublishInfo().getRoutingKey().toString();

                        /**
                         * Inside each QueueDeliveryInfo object, we keep list of messages to be delivered.
                         */
                        QueueDeliveryInfo queueDeliveryInfo = getQueueDeliveryInfo(queueName);
                        if (!queueDeliveryInfo.hasQueueFullAndMessagesIgnored) {
                            if (queueDeliveryInfo.readButUndeliveredMessages.size() < maxNumberOfReadButUndeliveredMessages) {

                                long currentMessageId = message.getMessage().getMessageNumber();

                             //We only add messages , if they are not already read and added to the queue
//                                if (!queueDeliveryInfo.alreadyAddedMessages.contains(currentMessageId)) {
                                    queueDeliveryInfo.readButUndeliveredMessages.add(message);
                                    totalReadButUndeliveredMessages++;
                                    queueDeliveryInfo.lastReadMessageId = currentMessageId;
//                                }
                            } else {
                                queueDeliveryInfo.hasQueueFullAndMessagesIgnored = true;
                            }
                        } else {


                            if(queueDeliveryInfo.hasQueueFullAndMessagesIgnored && queueDeliveryInfo.ignoredFirstMessageId == -1){
                                queueDeliveryInfo.ignoredFirstMessageId = message.getMessage().getMessageNumber();
                            }
                            //All subscription in this queue are full and we were forced to ignore messages. We have to rest
                            //Cursor location at the Cassandra Queue before adding new messages to avoid loosing the message order.
                            //When Cursor (Offset) reset happened, this will be set to false
                        }
                        lastProcessedId = message.getMessage().getMessageNumber();

                    }

                    if (messagesFromCassandra.size() == 0) {
                        sleep4waitInterval();
                    }

                    /**
                     * When we read messages from Cassandra, If we have read all messages we asked for, we increase the reading count. Else we reduce it.
                     * TODO we might want to take into account the size of the message while we change the batch size
                     */
                    if (messagesFromCassandra.size() == messageCountToRead) {
                        messageCountToRead += 100;
                        if (messageCountToRead > maxMessageCountToRead) {
                            messageCountToRead = maxMessageCountToRead;
                        }
                    } else {
                        messageCountToRead -= 50;
                        if (messageCountToRead < minMessageCountToRead) {
                            messageCountToRead = minMessageCountToRead;
                        }
                    }
                    totMsgRead = totMsgRead + messagesFromCassandra.size();
                    msgReadThisTime = messagesFromCassandra.size();
                }

                //Then we schedule them to be sent to subscribers
                int sentMessageCount = 0;
                for (QueueDeliveryInfo queueDeliveryInfo : subscriptionCursar4QueueMap.values()) {
                    sentMessageCount = sendMessagesToSubscriptions(queueDeliveryInfo.queueName, queueDeliveryInfo.readButUndeliveredMessages);
                }

                if (iterations % 20 == 0) {
                    if (log.isDebugEnabled()) {
                        log.info("[Flusher" + this + "]readNow=" + msgReadThisTime + " totRead=" + totMsgRead + " totprocessed= " + totMsgSent + ", totalReadButNotSent=" +
                                totalReadButUndeliveredMessages + " lastID=" + lastProcessedId);
                    }
                }
                iterations++;
                //Message reading work is over in this iteration. If read message count in this iteration is 0 definitely
                // we have to force reset the counter
                if (msgReadThisTime == 0) {
                    resetOffsetAtCassandraQueueIfNeeded(false);
                }
                //on every 10th, we sleep a bit to give cassandra a break, we do the same if we have not sent any messages
                if (sentMessageCount == 0 || iterations % 10 == 0) {
                    sleep4waitInterval();
                }
                failureCount = 0;
            } catch (Throwable e) {
                /**
                 * When there is a error, we will wait to avoid looping.  
                 */
                long waitTime = queueWorkerWaitInterval;
                failureCount++;
                long faultWaitTime = Math.max(waitTime * 5, failureCount * waitTime);
                try {
                    Thread.sleep(faultWaitTime);
                } catch (InterruptedException e1) {
                }
                log.error("Error running Cassandra Message Flusher" + e.getMessage(), e);
            }
        }
    }

    private void sleep4waitInterval() {
        try {
            Thread.sleep(queueWorkerWaitInterval);
        } catch (InterruptedException ignored) {
        }
    }

    private boolean isThisSubscriptionHasRoom(CassandraSubscription cassandraSubscription) {
        AMQChannel channel = null;
        if (cassandraSubscription != null && cassandraSubscription.getSubscription() instanceof SubscriptionImpl.AckSubscription) {
            channel = ((SubscriptionImpl.AckSubscription) cassandraSubscription.getSubscription()).getChannel();
        }
        //is that queue has too many messages pending
        int notAckedMsgCount = channel.getNotAckedMessageCount();

        //Here we ignore messages that has been scheduled but not executed, so it might send few messages than maxNumberOfUnAckedMessages
        if (notAckedMsgCount < maxNumberOfUnAckedMessages) {
            return true;
        } else {

            if (log.isDebugEnabled()) {
                log.debug("Not selected, channel=" + queue.getName() + "/" + channel + " pending count =" + (notAckedMsgCount));
            }
            return false;
        }
    }

    /**
     * Following code send messages to subscriptions for this queue in round robin manner.
     *
     * @param targetQueue
     * @param messages
     * @return
     */
    public int sendMessagesToSubscriptions(String targetQueue, List<QueueEntry> messages) {
        int sentMessageCount = 0;
        ArrayList<QueueEntry> previouslyUndeliveredMessages =   onflightMessageTracker.getUndeliveredMessagesOfQueue(targetQueue);

        if (previouslyUndeliveredMessages != null && previouslyUndeliveredMessages.size() >0) {
            messages.addAll(previouslyUndeliveredMessages);
            Collections.sort(messages);
        }
        Iterator<QueueEntry> iterator = messages.iterator();
        while (iterator.hasNext()) {
            QueueEntry message = iterator.next();
            boolean messageSent = false;
            Map<String, CassandraSubscription> subscriptions4Queue = subscriptionMap.get(targetQueue);
            if (subscriptions4Queue != null) {
                /**
                 * we do this in a for loop to avoid iterating for a subscriptions for ever. We only iterate as 
                 * once for each subscription 
                 */
                for (int j = 0; j < subscriptions4Queue.size(); j++) {
                    CassandraSubscription cassandraSubscription = findNextSubscriptionToSent(targetQueue);
                    if (isThisSubscriptionHasRoom(cassandraSubscription)) {
                        AMQProtocolSession session = cassandraSubscription.getSession();

                        ((AMQMessage) message.getMessage()).setClientIdentifier(session);

                        if (log.isDebugEnabled()) {
                            log.debug("readFromCassandra" + AndesUtils.printAMQMessage(message));
                        }
                        deliverAsynchronously(cassandraSubscription.getSubscription(), message);
                        totMsgSent++;
                        sentMessageCount++;
                        totalReadButUndeliveredMessages--;
                        messageSent = true;
                        iterator.remove();
                        break;
                    }
                }
                if (!messageSent) {
                    log.debug("All subscriptions for queue " + targetQueue + " have max Unacked messages " + queue.getName());
                }
            } else {
                // TODO : All subscriptions deleted for the queue, should we move messages back to global queue?
            }
        }
        return sentMessageCount;
    }

    public AMQQueue getQueue() {
        return queue;
    }

    private void deliverAsynchronously(final Subscription subscription, final QueueEntry message) {
        if (onflightMessageTracker.testMessage(message.getMessage().getMessageNumber())) {
            AMQChannel channel = null;
            if (subscription instanceof SubscriptionImpl.AckSubscription) {
                channel = ((SubscriptionImpl.AckSubscription) subscription).getChannel();
            }
            channel.incrementNonAckedMessageCount();
            if (log.isDebugEnabled()) {
                log.debug("sent out message for channel id=" + channel + " " + queue.getName());
            }

            try {
                ClusterResourceHolder.getInstance().getCassandraMessageStore().
                        getDisruptorBasedExecutor().messagesReadyToBeSent(subscription, message);
            } catch (Throwable e) {
                log.error("Error while delivering message ", e);
            } finally {
                pendingMessagesToSent.decrementAndGet();
            }
        }
    }

    public void stopFlusher() {
        running = false;
        log.debug("Shutting down the message flusher for the queue " + queue.getName());
    }

    public void startFlusher() {
        log.debug("staring flusher for " + queue.getName());
        running = true;
    }

    private boolean resetOffsetAtCassandraQueueIfNeeded(boolean force) {
        resetCounter++;
        if (force || (resetCounter > maxRestCounter && (System.currentTimeMillis() - lastRestTime) > queueMsgDeliveryCurserResetTimeInterval)) {
            resetCounter = 0;
            lastRestTime = System.currentTimeMillis();

            lastProcessedId = getStartingIndex();
            if (log.isDebugEnabled()) {
                log.debug("Reset called and new starting index is = " + lastProcessedId);
            }
            return true;
        }
        return false;
    }

    private long getStartingIndex() {
        long startingIndex = Long.MAX_VALUE;
        if (subscriptionCursar4QueueMap.values().size() == 0) {
            startingIndex = 0;
        }
        for (QueueDeliveryInfo queueDeliveryInfo : subscriptionCursar4QueueMap.values()) {

            if (queueDeliveryInfo.readButUndeliveredMessages.size() < maxNumberOfReadButUndeliveredMessages / 2) {
                queueDeliveryInfo.hasQueueFullAndMessagesIgnored = false;
            }

            if (!queueDeliveryInfo.hasQueueFullAndMessagesIgnored || startingIndex == Long.MAX_VALUE) {
                if (startingIndex > queueDeliveryInfo.ignoredFirstMessageId) {
                    startingIndex = queueDeliveryInfo.ignoredFirstMessageId;
                }
                queueDeliveryInfo.ignoredFirstMessageId = -1;
            }
        }
        return startingIndex;
    }
}
