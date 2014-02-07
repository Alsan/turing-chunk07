package org.wso2.andes.kernel;

import java.util.Map;

import org.wso2.andes.tools.utils.DisruptorBasedExecutor.PendingJob;

public class AndesMessageMetadata {
    public long getMessageID() {
        return messageID;
    }
    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }
    public byte[] getMetadata() {
        return metadata;
    }
    public void setMetadata(byte[] metadata) {
        this.metadata = metadata;
    }
    long messageID; 
    byte[] metadata;
    long expirationTime; 
    boolean isTopic; 
    private String queue; 
    
    Map<Long, PendingJob> pendingJobsTracker;
    public Map<Long, PendingJob> getPendingJobsTracker() {
        return pendingJobsTracker;
    }
    public void setPendingJobsTracker(Map<Long, PendingJob> pendingJobsTracker) {
        this.pendingJobsTracker = pendingJobsTracker;
    }
    public long getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
    public boolean isTopic() {
        return isTopic;
    }
    public void setTopic(boolean isTopic) {
        this.isTopic = isTopic;
    }
    public String getQueue() {
        return queue;
    }
    public void setQueue(String queue) {
        this.queue = queue;
    }
    
    
}
