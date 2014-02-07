package org.wso2.andes.messageStore;

import java.util.List;

import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.andes.kernel.AndesMessagePart;
import org.wso2.andes.kernel.MessageStore;

public class InMemoryMessageStore implements MessageStore{

	@Override
	public void storeMessagePart(List<AndesMessagePart> part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMessageParts(long messageID, byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMessageParts(long messageID, int indexStart, int indexEnd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMessageMetadataToQueue(List<AndesMessageMetadata> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMessageMetadataFromQueue(String queue, long[] messageIDs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveMessageMetadata(String fromQueue, String toQueue,
			long[] messageIDs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AndesMessageMetadata[] getNextNMessageMetadataFromQueue(
			String qNmae, long startMsgID, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ackReceived(List<AndesAckData> ackList) {
		// TODO Auto-generated method stub
		
	}

}
