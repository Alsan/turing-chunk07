package org.wso2.andes.messageStore;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.andes.kernel.AndesMessagePart;
import org.wso2.andes.server.ClusterResourceHolder;
import org.wso2.andes.server.cassandra.OnflightMessageTracker;
import org.wso2.andes.server.stats.PerformanceCounter;
import org.wso2.andes.server.store.CassandraMessageStore;
import org.wso2.andes.server.store.util.CassandraDataAccessException;
import org.wso2.andes.server.store.util.CassandraDataAccessHelper;
import org.wso2.andes.server.util.AndesConstants;
import org.wso2.andes.server.util.AndesUtils;
import org.wso2.andes.tools.utils.DisruptorBasedExecutor.PendingJob;

import java.util.HashMap;
import java.util.List;

import static org.wso2.andes.server.store.CassandraMessageStore.*;

public class CassandraBasedMessageStoreImpl implements org.wso2.andes.kernel.MessageStore {
    private static Log log = LogFactory.getLog(CassandraBasedMessageStoreImpl.class);

    private Keyspace keyspace;
    private StringSerializer stringSerializer = StringSerializer.get();
    private LongSerializer longSerializer = LongSerializer.get();
    private BytesArraySerializer byteArraySerializer = BytesArraySerializer.get();

    public CassandraBasedMessageStoreImpl(Keyspace keyspace) {
        this.keyspace = keyspace;
    }

    public void storeMessagePart(List<AndesMessagePart> partList) {
        try {
            Mutator<String> messageMutator = HFactory.createMutator(keyspace, stringSerializer);
            for (AndesMessagePart part : partList) {
                final String rowKey = AndesConstants.MESSAGE_CONTENT_CASSANDRA_ROW_NAME_PREFIX
                        + part.getMessageID();
                CassandraDataAccessHelper.addIntegerByteArrayContentToRaw(MESSAGE_CONTENT_COLUMN_FAMILY,
                        rowKey, part.getOffSet(), part.getData(), messageMutator, false);
            }
            messageMutator.execute();
        } catch (CassandraDataAccessException e) {
            //TODO handle Cassandra failures
            //When a error happened, we should remember that and stop accepting messages
            log.error(e);
        }
    }

    public void addMessageMetadataToQueue(List<AndesMessageMetadata> list) {
        try {
            Mutator<String> messageMutator = HFactory.createMutator(keyspace, stringSerializer);
            HashMap<String,Integer> incomingMessagesMap = new HashMap<String,Integer>();
            for (AndesMessageMetadata md : list) {
                messageMutator.addInsertion(QMD_ROW_NAME, QMD_COLUMN_FAMILY, HFactory.createColumn(
                        md.getMessageID(), md.getMetadata(), longSerializer, byteArraySerializer));
                String globalQueueName = AndesUtils.getGlobalQueueNameForDestinationQueue(md.getQueue());

                // add message ID to global queue if it is only a queue message
                if (!md.isTopic()) {
                    CassandraDataAccessHelper.addMessageToQueue(
                            CassandraMessageStore.GLOBAL_QUEUES_COLUMN_FAMILY, globalQueueName,
                            md.getMessageID(), md.getMetadata(), messageMutator, false);
                    if(incomingMessagesMap.get(md.getQueue()) == null){
                        incomingMessagesMap.put(md.getQueue(),1);
                    }else {
                        incomingMessagesMap.put(md.getQueue(),incomingMessagesMap.get(md.getQueue())+1);
                    }
                }

                CassandraDataAccessHelper.addMappingToRaw(
                        CassandraMessageStore.GLOBAL_QUEUE_LIST_COLUMN_FAMILY,
                        CassandraMessageStore.GLOBAL_QUEUE_LIST_ROW, globalQueueName, globalQueueName,
                        messageMutator, false);
                PerformanceCounter.recordIncomingMessageWrittenToCassandra();
                // TODO :
            }
            long start = System.currentTimeMillis();
            messageMutator.execute();

            PerformanceCounter.recordIncomingMessageWrittenToCassandraLatency((int) (System.currentTimeMillis() - start));

            if(ClusterResourceHolder.getInstance().getClusterConfiguration().getViewMessageCounts()){
                for(AndesMessageMetadata md : list){
                    CassandraDataAccessHelper.incrementCounter(md.getQueue(), CassandraMessageStore.MESSAGE_COUNTERS_COLUMN_FAMILY, CassandraMessageStore.MESSAGE_COUNTERS_RAW_NAME, keyspace, 1);
                }
            }

            // Client waits for these message ID to be written, this signal those, if there is a error
            //we will not signal and client who tries to close the connection will timeout. 
            //We can do this better, but leaving this as is or now.
            for (AndesMessageMetadata md : list) {
                PendingJob jobData = md.getPendingJobsTracker().get(md.getMessageID());
                if (jobData != null) {
                    jobData.semaphore.release();
                }
            }
        } catch (Exception e) {
            //TODO handle Cassandra failures
            //TODO may be we can write those message to a disk, or do something. Currently we will just loose them
            log.error("Error writing incoming messages to Cassandra", e);
        }
    }

    public void moveMessageMetadata(String fromQueue, String toQueue, long[] messageIDs) {
    }

    public AndesMessageMetadata[] getNextNMessageMetadataFromQueue(String qNmae, long startMsgID, int count) {
        return null;
    }

    public void getMessageParts(long messageID, int indexStart, int indexEnd) {
    }

    public void deleteMessageParts(long messageID, byte[] data) {
    }

    public void deleteMessageMetadataFromQueue(String queue, long[] messageIDs) {
    }

    public void ackReceived(List<AndesAckData> ackList) {
        try {
            Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
            CassandraMessageStore cassandraMessageStore = ClusterResourceHolder.getInstance()
                    .getCassandraMessageStore();

            // remove message from node queue instantly (prevent redelivery)
            String nodeQueueName = AndesUtils.getMyNodeQueueName();

            long start = System.currentTimeMillis();
            for (AndesAckData ackData : ackList) {
                cassandraMessageStore.addContentDeletionTask(ackData.messageID);
                // schedule message-queue mapping to be removed
                cassandraMessageStore.addMessageQueueMappingDeletionTask(ackData.qName, ackData.messageID);
                mutator.addDeletion(nodeQueueName, NODE_QUEUES_COLUMN_FAMILY, ackData.messageID, longSerializer);
                PerformanceCounter.recordMessageRemovedAfterAck();
            }
            mutator.execute();
            int timeTook = (int) (System.currentTimeMillis() - start);
            PerformanceCounter.recordMessageRemovedAfterAckLatency(timeTook);

            OnflightMessageTracker onflightMessageTracker = OnflightMessageTracker.getInstance();
            for (AndesAckData ackData : ackList) {
                onflightMessageTracker.updateDeliveredButNotAckedMessages(ackData.messageID);
                if(ClusterResourceHolder.getInstance().getClusterConfiguration().getViewMessageCounts()){
                    CassandraDataAccessHelper.decrementCounter(ackData.qName, CassandraMessageStore.MESSAGE_COUNTERS_COLUMN_FAMILY, CassandraMessageStore.MESSAGE_COUNTERS_RAW_NAME,
                            keyspace, 1);
                }
            }
        } catch (Throwable e) {
            //TODO handle Cassandra failures
            log.error(e);
        }
    }

}
