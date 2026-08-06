package com.mailally.notification.channel;

import com.mailally.notification.entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * In-App delivery channel handler component.
 */
@Component
public class InAppNotificationChannelHandler implements NotificationChannelHandler {

    private static final Logger log = LoggerFactory.getLogger(InAppNotificationChannelHandler.class);
    public static final String CHANNEL_NAME = "IN_APP";

    @Override
    public boolean supportsChannel(String channel) {
        return channel == null || channel.isBlank() || CHANNEL_NAME.equalsIgnoreCase(channel);
    }

    @Override
    public void dispatch(Notification notification) {
        log.info("Dispatched In-App Notification [ID: {}, Title: '{}'] for User ID: {}",
                notification.getId(), notification.getTitle(), notification.getUser() != null ? notification.getUser().getId() : "N/A");
    }
}
