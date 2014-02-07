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
package org.wso2.andes.messageStore;

import org.wso2.andes.amqp.AMQPUtils;
import org.wso2.andes.kernel.*;
import org.wso2.andes.server.store.StorableMessageMetaData;
import org.wso2.andes.server.store.StoredMessage;
import org.wso2.andes.server.store.TransactionLog;

import java.nio.ByteBuffer;

public class StoredAMQPMessage implements StoredMessage {

    private final long _messageId;
    private StorableMessageMetaData metaData;
    private String channelID;
    private String exchange;
    private MessageStore messageStore;


    /**
     * Create a stored cassandra message combining metadata and message ID
     * @param messageId message ID
     * @param metaData  metadata of message
     */
    //todo; we can generalize this as StoredMessage (based on metada info it will refer relevant store)
    public StoredAMQPMessage(long messageId, StorableMessageMetaData metaData) {
        this._messageId = messageId;
        this.metaData = metaData;

        //we switch the store according to persistance parameter
        if (metaData.isPersistent()) {
            this.messageStore = MessagingEngine.getInstance().getCassandraBasedMessageStore();
        } else {
            this.messageStore = MessagingEngine.getInstance().getCassandraBasedMessageStore();
        }

    }

    @Override
    public StorableMessageMetaData getMetaData() {
        if (metaData == null) {
            metaData = AMQPUtils.convertAndesMetadataToAMQMetadata(messageStore.getMetaData(_messageId));
        }
        return metaData;
    }

    @Override
    public long getMessageNumber() {
        return _messageId;
    }

    @Override
    /**
     * write content to the message store
     */
    public void addContent(int offsetInMessage, ByteBuffer src) {
        addContentInPersistentMode(offsetInMessage, src);
    }

    @Override
    public void duplicateMessageContent(long messageId, long messageIdOfClone) throws AndesException {

    }

    /**
     * write the message content to cassandra (we submit this task to AndesExecutor pool)
     *
     * @param offsetInMessage Int message content offset
     * @param src             ByteBuffer message content
     */
    private void addContentInPersistentMode(final int offsetInMessage, ByteBuffer src) {

        AndesMessagePart part = new AndesMessagePart();
        src = src.slice();
        final byte[] chunkData = new byte[src.limit()];

        src.duplicate().get(chunkData);

        part.setData(chunkData);
        part.setMessageID(_messageId);
        part.setOffSet(offsetInMessage);
        part.setDataLength(chunkData.length);

        MessagingEngine.getInstance().messageContentReceived(part);
    }

    @Override
    /**
     * get content for offset in a message
     */
    public int getContent(int offsetInMessage, ByteBuffer dst) {
        int c;
        c = messageStore.getContent(_messageId + "", offsetInMessage, dst);
        return c;
    }

    @Override
    public TransactionLog.StoreFuture flushToStore() {

        throw new UnsupportedOperationException();
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    @Override
    public void remove() {
        //Todo:when this is called we have to remove content from the storage?? we have to do buffering here. but both queue and topic deletions come here
        //Todo: or is it a remove metadata from this object, no need to keep in-memory?
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
