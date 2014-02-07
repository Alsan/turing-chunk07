package org.wso2.andes.kernel.distrupter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.kernel.Subscrption;
import org.wso2.andes.server.queue.QueueEntry;
import org.wso2.andes.server.subscription.Subscription;
import org.wso2.andes.server.subscription.SubscriptionImpl;

import java.util.List;

public class SimplyfiedDataSender implements Subscrption {
    private static Log log = LogFactory.getLog(SimplyfiedDataSender.class);

    public void sendAsynchronouslyToQueueEndPoint(final List<SubscriptionDataEvent> messageList){
        Subscription subscription;
        QueueEntry message;
        for (SubscriptionDataEvent subscriptionDataEvent : messageList) {
            try {
                subscription = subscriptionDataEvent.subscription;
                message = subscriptionDataEvent.message;
                if (subscription instanceof SubscriptionImpl.AckSubscription) {
                    subscription.send(message);
                } else {
                    log.error("Unexpected Subscription Implementation : " +
                            subscription != null ? subscription.getClass().getName() : null);
                }
            } catch (Throwable e) {
                log.error("Error while delivering message ", e);
            }
        }
    }

    public void sendSynchronouslyToQueueEndPoint(final List<SubscriptionDataEvent> messageList){
    }

    public void sendAsynchronouslyToTopicEndPoint(final List<SubscriptionDataEvent> messageList){
    }

    public void sendSynchronouslyToTopicEndPoint(final List<SubscriptionDataEvent> messageList){
    }
}
