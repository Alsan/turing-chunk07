package org.wso2.andes.server.cassandra;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.AMQException;
import org.wso2.andes.AMQStoreException;
import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.server.AMQChannel;
import org.wso2.andes.server.ClusterResourceHolder;
import org.wso2.andes.server.message.AMQMessage;
import org.wso2.andes.server.queue.QueueEntry;
import org.wso2.andes.server.stats.PerformanceCounter;
import org.wso2.andes.server.store.CassandraMessageStore;
import org.wso2.andes.server.util.AndesUtils;
import org.wso2.andes.tools.utils.DisruptorBasedExecutor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class OnflightMessageTracker {

    private static Log log = LogFactory.getLog(OnflightMessageTracker.class);

    private int ackTimeout = 10000;
    private int maximumRedeliveryTimes = 1;

    /**
     * In memory map keeping sent messages. If this map does not have an entry for a delivery scheduled
     * message it is a new message. Otherwise it is a redelivery
     */
    private LinkedHashMap<Long,MsgData> msgId2MsgData = new LinkedHashMap<Long,MsgData>();

    private Map<String,Long> deliveryTag2MsgID = new HashMap<String,Long>();
    private ConcurrentHashMap<UUID,HashSet<Long>> channelToMsgIDMap = new ConcurrentHashMap<UUID,HashSet<Long>>();
    private ConcurrentHashMap<Long,QueueEntry> messageIdToQueueEntryMap = new ConcurrentHashMap<Long,QueueEntry>();

    /**
     * In memory set keeping track of sent messageIds. Used to prevent duplicate message count
     * decrements
     */
    private HashSet<Long> deliveredButNotAckedMessages = new HashSet<Long>();
    
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private boolean isInMemoryMode = false;
    
    
    private AtomicLong sendMessageCount = new AtomicLong();
    private AtomicLong sendButNotAckedMessageCount = new AtomicLong();
    private ConcurrentHashMap<String,ArrayList<QueueEntry>> undeliveredMessagesMap = new ConcurrentHashMap<String, ArrayList<QueueEntry>>();

    
    private long startTime = -1;

    /**
     * Class to keep tracking data of a message
     */
    public class MsgData{

        final long msgID;
        boolean ackReceived = false;
        final String queue; 
        final long timestamp; 
        final String deliveryID; 
        final AMQChannel channel;
        int numOfDeliveries;
        boolean ackWaitTimedOut;

        public MsgData(long msgID, boolean ackreceived, String queue, long timestamp, String deliveryID, AMQChannel channel, int numOfDeliveries, boolean ackWaitTimedOut) {
            this.msgID = msgID;
            this.ackReceived = ackReceived;
            this.queue = queue; 
            this.timestamp = timestamp;
            this.deliveryID = deliveryID;
            this.channel = channel;
            this.numOfDeliveries = numOfDeliveries;
            this.ackWaitTimedOut = ackWaitTimedOut;
        }
    }
    
    private static OnflightMessageTracker instance = new OnflightMessageTracker();

    public static OnflightMessageTracker getInstance() {
        return instance; 
    }

    
    private OnflightMessageTracker(){

        this.ackTimeout = ClusterResourceHolder.getInstance().getClusterConfiguration().getMaxAckWaitTime()*1000;
        this.maximumRedeliveryTimes = ClusterResourceHolder.getInstance().getClusterConfiguration().getNumberOfMaximumDeliveryCount();
        /*
         * for all add and remove, following is executed, and it will remove the oldest entry if needed
         */
        msgId2MsgData = new LinkedHashMap<Long, MsgData>() {
            private static final long serialVersionUID = -8681132571102532817L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, MsgData> eldest) {
                MsgData msgData = eldest.getValue(); 
                boolean todelete = (System.currentTimeMillis() - msgData.timestamp) > (ackTimeout *10);
                if(todelete){
                    if(!msgData.ackReceived){
                        //reduce messages on flight on this channel
                        msgData.channel.decrementNonAckedMessageCount();
                        log.warn("No ack received for delivery tag " + msgData.deliveryID + " and message id "+ msgData.msgID); 
                        //TODO notify the QueueDeliveryWorker to resend (it work now as well as flusher loops around, but this will be faster)
                    }else{
                        //log.info("Removed after ack "+msgData.msgID);
                    }
                    if(deliveryTag2MsgID.remove(msgData.deliveryID) == null){
                        log.error("Cannot find delivery tag " + msgData.deliveryID + " and message id "+ msgData.msgID);
                    }
                }
                return todelete;
            }
        };

        /**
         * This thread will removed acked messages or messages that breached max redelivery count from tracking
         * These messages are already scheduled to be removed from message store.
         */
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //TODO replace this with Gvava Cache if possible
                synchronized (this) {
                    Iterator<MsgData> iterator = msgId2MsgData.values().iterator();
                    while(iterator.hasNext()){
                        MsgData mdata = iterator.next();
                        if(mdata.ackReceived || (mdata.numOfDeliveries) > maximumRedeliveryTimes){
                            iterator.remove();
                            deliveryTag2MsgID.remove(mdata.deliveryID);
                            if((mdata.numOfDeliveries) > maximumRedeliveryTimes){
                                log.warn("Message "+ mdata.msgID + " with "+ mdata.deliveryID + " removed as it has gone though max redeliveries");
                            }
                        }
                    }
                }
            }
        },  5, 10, TimeUnit.SECONDS);

        isInMemoryMode = ClusterResourceHolder.getInstance().getClusterConfiguration().isInMemoryMode();
    }

    public void stampMessageAsAckTimedOut(long deliveryTag, UUID channelId) {
        long newTimeStamp = System.currentTimeMillis();
        String deliveryID = new StringBuffer(channelId.toString()).append("/").append(deliveryTag).toString();
        Long messageId= deliveryTag2MsgID.get(deliveryID);
        if(messageId != null) {
            MsgData msgData = msgId2MsgData.get(messageId);
            msgData.ackWaitTimedOut = true;
        }
    }
    /**
     * Message is allowed to be sent if and only if it is a new message or an already sent message whose ack wait time
     * out has happened
     * @param messageId
     * @return boolean if the message should be sent
     */
    public synchronized boolean testMessage(long messageId){
        long currentTime = System.currentTimeMillis();
        MsgData mdata = msgId2MsgData.get(messageId);
        //we do not redeliver the message until ack-timeout is breached
        if (mdata == null || (!mdata.ackReceived && mdata.ackWaitTimedOut)) {
            if(mdata != null && !mdata.ackReceived){
                //mdata.channel.decrementNonAckedMessageCount();
                //log.warn("Message "+ mdata.msgID + " with "+ mdata.deliveryID + " has timed out ack="+mdata.ackReceived);
            }
            return true; 
        }else{
            return false;
        }
    }
    
    /** 
     * This cleanup the current message ID form tracking. Useful for undo changes in case of a failure
     * @param deliveryTag
     * @param messageId
     * @param channel
     */
    public void removeMessage(AMQChannel channel, long deliveryTag, long messageId){
        String deliveryID = new StringBuffer(channel.getId().toString()).append("/").append(deliveryTag).toString(); 
        Long messageIDStored = deliveryTag2MsgID.remove(deliveryID);
        
        if(messageIDStored != null && messageIDStored.longValue() != messageId){
            throw new RuntimeException("Delivery Tag "+deliveryID+ " reused for " +messageId + " and " + messageIDStored +" , this should not happen");
        }
        msgId2MsgData.remove(messageId);
        log.info("unexpected removed1 "+ messageId);
    }

    public synchronized boolean testAndAddMessage(QueueEntry queueEntry, long deliveryTag, AMQChannel channel) throws AMQException {
        long messageId = queueEntry.getMessage().getMessageNumber();
        if(!testMessage(messageId)){
            return false; 
        }

        String queue = ((AMQMessage)queueEntry.getMessage()).getMessageMetaData().getMessagePublishInfo()
                .getRoutingKey().toString();
        if(channel.isClosing()){
            ArrayList<QueueEntry> undeliveredMessages = undeliveredMessagesMap.get(queue);
            if(undeliveredMessages == null){
                undeliveredMessages = new ArrayList<QueueEntry>();
                undeliveredMessages.add(queueEntry);
                undeliveredMessagesMap.put(queue,undeliveredMessages);
            }else {
                undeliveredMessages.add(queueEntry);
            }
            return false;
        }

        String nodeSpecificQueueName = queue + "_" + ClusterResourceHolder.getInstance().getClusterManager().getNodeId();

        String deliveryID = new StringBuffer(channel.getId().toString()).append("/").append(deliveryTag).toString();

        long currentTime = System.currentTimeMillis();
        MsgData mdata = msgId2MsgData.get(messageId);
        int numOfDeliveriesOfCurrentMsg = 0;

        if (deliveryTag2MsgID.containsKey(deliveryID)) {
            throw new RuntimeException("Delivery Tag "+deliveryID+" reused, this should not happen");
        }
        if(mdata == null) {
        //this is a new message
            deliveredButNotAckedMessages.add(messageId);
            if(sendMessageCount.incrementAndGet()%1000 == 0){
                if(startTime == -1){
                    startTime = System.currentTimeMillis();
                }else{
                     log.info("["+ sendMessageCount.get() + "] delivered Thoughput="+ (int)(sendMessageCount.get()*1000d/(System.currentTimeMillis() - startTime)));
                }
            }
            PerformanceCounter.recordMessageSentToConsumer();
        }
        //this is an already sent but ack wait time expired message
        else {
            /**
             * If Medata data is not null, there are several cases 
             * 1) We have sent a message, and waiting for a ack (on which case, it returns
             * at the testMessage(..), never get here) 
             * 2) We have received the ack, and wait for cleanup. Then nothing to do 
             * 3) Message ack has
             * timed out, so we send the message again with a new delivery tag
             */
            if (mdata != null && !mdata.ackReceived) {
                log.warn("Message " + mdata.msgID + " with " + mdata.deliveryID + " has timed out with ackReceived="
                        + mdata.ackReceived);
            }
            numOfDeliveriesOfCurrentMsg = mdata.numOfDeliveries;
            // entry should have "ReDelivery" header
            queueEntry.setRedelivered();
            // message has sent once, we will clean lists and consider it a new
            // message, but with delivery times tracked
            deliveryTag2MsgID.remove(mdata.deliveryID);
            msgId2MsgData.remove(messageId);
        }
        numOfDeliveriesOfCurrentMsg++;
        deliveryTag2MsgID.put(deliveryID, messageId);
        msgId2MsgData.put(messageId, new MsgData(messageId, false, nodeSpecificQueueName, currentTime, deliveryID, channel,numOfDeliveriesOfCurrentMsg,false));
        sendButNotAckedMessageCount.incrementAndGet();

        HashSet<Long> messagesDeliveredThroughThisChannel = channelToMsgIDMap.get(channel.getId());
        if(messagesDeliveredThroughThisChannel == null){
            messagesDeliveredThroughThisChannel = new HashSet<Long>();
            messagesDeliveredThroughThisChannel.add(messageId);
            channelToMsgIDMap.put(channel.getId(),messagesDeliveredThroughThisChannel);
        }else {
            messagesDeliveredThroughThisChannel.add(messageId);
        }
        messageIdToQueueEntryMap.put(messageId,queueEntry);
        /**
         * any custom checks or procedures that should be executed before message delivery should happen here. Any message
         * rejected at this stage will be dropped from the node queue permanently.
         */

        //check if number of redelivery tries has breached. If it is breached, then we drop the message and delete it from the 
        //Node queue
        if(numOfDeliveriesOfCurrentMsg > ClusterResourceHolder.getInstance().getClusterConfiguration().getNumberOfMaximumDeliveryCount()) {
            log.warn("Number of Maximum Redelivery Tries Has Breached with "+numOfDeliveriesOfCurrentMsg+". Dropping The Message: "+ messageId + "From Queue " + queue);
            OnflightMessageTracker.getInstance().removeNodeQueueMessageFromStorePermanentlyAndDecrementMsgCount(messageId, queue);
            return false;
           //check if queue entry has expired. Any expired message will not be delivered
        }  else if(queueEntry.expired()) {
            log.warn("Message is expired. Dropping The Message: "+messageId);
            OnflightMessageTracker.getInstance().removeNodeQueueMessageFromStorePermanentlyAndDecrementMsgCount(messageId, queue);
            return false;
        }
        return true;
    }

//    public void ackReceived(AMQChannel channel, long deliveryTag) throws AMQStoreException{
        public void ackReceived(AMQChannel channel, long messageID) throws AMQStoreException{

        //String deliveryID = new StringBuffer(channel.getId().toString()).append("/").append(deliveryTag).toString(); 
        MsgData msgData = null; 
        synchronized (this) {
            //Long messageId = deliveryTag2MsgID.get(deliveryID);
            //if(messageID != null){
                msgData = msgId2MsgData.get(messageID);
                if(msgData != null){
                    if(msgData.ackReceived){
                        log.warn("Received a duplicate ack for the message "+ messageID);
                    }
                    msgData.ackReceived = true;
                    //continue to code below
                }else{
                    //throw new RuntimeException("No message data found for messageId "+ messageId);
                    log.warn("unexpected ack for "+ messageID  +" from channel ID " + channel.getChannelId());
                }
//            }else{
//                log.warn("unexpected ack for "+ deliveryID);
//                //TODO We ignore as this happens only with publish case. May be there is a better way to handle this 
//                return;
//            }
        }
        //We do this out side the sync block as following do IO
        if(msgData != null){
            channel.decrementNonAckedMessageCount();
            handleMessageRemovalWhenAcked(msgData);
            // then update the tracker
            
            long timeTook = (System.currentTimeMillis() - msgData.timestamp);
            //log.warn("Ack:" + msgData.msgID + " " + "" + " took "+ timeTook + "ms");
//            if(timeTook > 1000){
//                log.warn("Ack:" + msgData.msgID + " " + "" + " took "+ timeTook + "ms, which is too long");    
//            }else if(log.isDebugEnabled()){
//                log.debug("Ack:" + msgData.msgID + " " + "" + " took "+ timeTook + "ms");
//            }
            PerformanceCounter.recordAckReceived(msgData.queue, (int) timeTook);
            sendButNotAckedMessageCount.decrementAndGet();
            channelToMsgIDMap.get(channel.getId()).remove(messageID);
            messageIdToQueueEntryMap.remove(messageID);
            if (log.isDebugEnabled()) {
                log.debug("Ack received for the message id = " + messageID + " from channel  " + channel.getId());
            }
        }

    }

    public void releaseAckTrackingSinceChannelClosed(AMQChannel channel) {
        HashSet<Long> sentButNotAckedMessages = channelToMsgIDMap.get(channel.getId());

        if (sentButNotAckedMessages != null && sentButNotAckedMessages.size() > 0) {
            Iterator iterator = sentButNotAckedMessages.iterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    long messageId = (Long) iterator.next();
                    if (msgId2MsgData.get(messageId) != null) {
                        String queueName = msgId2MsgData.remove(messageId).queue;
                        sendButNotAckedMessageCount.decrementAndGet();
                        QueueEntry queueEntry = messageIdToQueueEntryMap.remove(messageId);
                        ArrayList<QueueEntry> undeliveredMessages = undeliveredMessagesMap.get(queueName);
                        if (undeliveredMessages == null) {
                            undeliveredMessages = new ArrayList<QueueEntry>();
                            undeliveredMessages.add(queueEntry);
                            undeliveredMessagesMap.put(queueName, undeliveredMessages);
                        } else {
                            undeliveredMessages.add(queueEntry);
                        }
                    }
                }
            }
        }
        channelToMsgIDMap.remove(channel.getId());
    }

    private void handleMessageRemovalWhenAcked(MsgData msgData) throws AMQStoreException {
        if (deliveredButNotAckedMessages.contains(msgData.msgID)) {
            String destinationQueueName = msgData.queue.substring(0, msgData.queue.lastIndexOf("_"));
            //schedule to remove message from message store
            CassandraMessageStore cassandraMessageStore = ClusterResourceHolder.getInstance().getCassandraMessageStore();
            if (isInMemoryMode) {
                cassandraMessageStore.removeIncomingQueueMessage(msgData.msgID);
            } else {
                //remove message from node queue instantly (prevent redelivery) 
//                String nodeQueueName = AndesUtils.getMyNodeQueueName();
//                   cassandraMessageStore.removeMessageFromNodeQueue(nodeQueueName, msgData.msgID);
//                //schedule message content and properties to be deleted
//                cassandraMessageStore.addContentDeletionTask(msgData.msgID);
//                //schedule message-queue mapping to be removed
//                cassandraMessageStore.addMessageQueueMappingDeletionTask(destinationQueueName, msgData.msgID);
                DisruptorBasedExecutor batchexec = ClusterResourceHolder.getInstance().getCassandraMessageStore().getDisruptorBasedExecutor();
                batchexec.ackReceived(new AndesAckData(msgData.msgID, destinationQueueName));
                
                
            }

            //decrement message count from relevant queue at Message Store
            //ClusterResourceHolder.getInstance().getCassandraMessageStore().decrementQueueCount(destinationQueueName,1L);

            //deliveredButNotAckedMessages.remove(msgData.msgID);
        }
    }
    
    public synchronized void updateDeliveredButNotAckedMessages(long messageID){
        deliveredButNotAckedMessages.remove(messageID);
        //log.info("unexpected removed 2"+ messageID);
    }
    
    

    /**
     * Delete a given message with all its properties and trackings from Message store
     * @param messageId message ID
     * @param destinationQueueName  destination queue name
     */
    public void removeNodeQueueMessageFromStorePermanentlyAndDecrementMsgCount(long messageId, String destinationQueueName) {

        try {
            CassandraMessageStore cassandraMessageStore = ClusterResourceHolder.getInstance().getCassandraMessageStore();

            //we need to remove message from the store. At this moment message is at node queue space, not at global space
            //remove message from node queue instantly (prevent redelivery)
            String nodeQueueName = AndesUtils.getMyNodeQueueName();

            cassandraMessageStore.removeMessageFromNodeQueue(nodeQueueName, messageId);
            //schedule message content and properties to be removed
            cassandraMessageStore.addContentDeletionTask(messageId);
            //schedule message-queue mapping to be removed as well
            cassandraMessageStore.addMessageQueueMappingDeletionTask(destinationQueueName, messageId);
            //decrement number of messages
            cassandraMessageStore.decrementQueueCount(destinationQueueName, 1L);

            //if it is an already sent but not acked message we will not decrement message count again
            MsgData messageData = msgId2MsgData.get(messageId);
            if (messageData != null) {
                //we do following to stop trying to delete message again when acked someday
                deliveredButNotAckedMessages.remove(messageId);
            }
        } catch (AMQStoreException e) {
            log.error("Error In Removing Message From Node Queue. ID: " + messageId);
        }
    }
    
    public long getSentButNotAckedMessageCount(){
        return sendButNotAckedMessageCount.get();
    }
    
    public ArrayList<QueueEntry> getUndeliveredMessagesOfQueue(String queueName){
       return  undeliveredMessagesMap.remove(queueName);
    }
}
