package org.wso2.andes.kernel;

import com.lmax.disruptor.EventFactory;

public class AndesAckData {
    
    public AndesAckData(){}
    
    public AndesAckData(long messageID, String qName) {
        super();
        this.messageID = messageID;
        this.qName = qName;
    }
    public long messageID; 
    public String qName; 
    
    public static class AndesAckDataEventFactory implements EventFactory<AndesAckData> {
        @Override
        public AndesAckData newInstance() {
            return new AndesAckData();
        }
    }

    public static EventFactory<AndesAckData> getFactory() {
        return new AndesAckDataEventFactory();
    }

}
