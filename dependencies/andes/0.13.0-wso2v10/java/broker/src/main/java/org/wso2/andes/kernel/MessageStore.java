package org.wso2.andes.kernel;

import java.util.List;

public interface MessageStore {
    public void storeMessagePart(List<AndesMessagePart> part);
    public void deleteMessageParts(long messageID, byte[] data);
    public void getMessageParts(long messageID, int indexStart, int indexEnd);
    
    public void addMessageMetadataToQueue(List<AndesMessageMetadata> list);
    public void deleteMessageMetadataFromQueue(String queue, long[] messageIDs);
    public void moveMessageMetadata(String fromQueue, String toQueue, long[] messageIDs);
    
    public AndesMessageMetadata[] getNextNMessageMetadataFromQueue(String qNmae, long startMsgID, int count); 
    public void ackReceived(List<AndesAckData> ackList);

}
