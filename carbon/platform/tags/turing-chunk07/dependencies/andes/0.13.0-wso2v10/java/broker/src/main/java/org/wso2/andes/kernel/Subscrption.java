package org.wso2.andes.kernel;

import java.util.List;

import org.wso2.andes.kernel.distrupter.SubscriptionDataEvent;

public interface Subscrption {
    public void sendAsynchronouslyToQueueEndPoint(final List<SubscriptionDataEvent> messageList);

    public void sendAsynchronouslyToTopicEndPoint(final List<SubscriptionDataEvent> messageList);
}
