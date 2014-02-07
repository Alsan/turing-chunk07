package org.wso2.andes.kernel.distrupter;

import java.util.ArrayList;
import java.util.List;

import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.andes.kernel.AndesMessagePart;
import org.wso2.andes.kernel.MessageStore;

import com.lmax.disruptor.EventHandler;

/**
 * We do this to make Listener take turns while running. So we can run many copies of these and control number
 * of IO threads through that.
 */
public class AlternatingCassandraWriter implements EventHandler<CassandraDataEvent> {
    int totalPendingEventLength = 0;
    private int writerCount;
    private int turn;
    private MessageStore messageStore;
    private List<AndesMessageMetadata> metaList = new ArrayList<AndesMessageMetadata>();

    private List<AndesMessagePart> partList = new ArrayList<AndesMessagePart>();
    private int MAX_DATA_LENGTH = 128000;

    public AlternatingCassandraWriter(int writerCount, int turn, MessageStore messageStore) {
        this.writerCount = writerCount;
        this.turn = turn;
        this.messageStore = messageStore;
    }

    public void onEvent(final CassandraDataEvent event, final long sequence, final boolean endOfBatch) throws Exception {
        if (event.isPart) {
            //if part, we write randomly
            int calculatedTurn = (int) Math.abs(event.part.getMessageID() % writerCount);

            if (calculatedTurn == turn) {
                //Message parts we write on the fly. It is tradeoff of memory vs. batching
                //May be we need better handling .. batch that data as well
                partList.add(event.part);
                totalPendingEventLength += event.part.getDataLength();
            }
        } else {
            //if messageID, we write in sequence per queue
            int calculatedTurn = Math.abs(event.metadata.getQueue().hashCode() % writerCount);

            if (calculatedTurn == turn) {
                metaList.add(event.metadata);
                totalPendingEventLength += event.metadata.getMetadata().length;
            }
        }

        if (totalPendingEventLength > MAX_DATA_LENGTH || (endOfBatch)) {
            // Write message part list to cassandra
            if (partList.size() > 0) {
                messageStore.storeMessagePart(partList);
                partList.clear();
            }

            // Write message meta list to cassandra
            if (metaList.size() > 0) {
                messageStore.addMessageMetadataToQueue(metaList);
                metaList.clear();
            }
            totalPendingEventLength = 0;
        }
    }
}
