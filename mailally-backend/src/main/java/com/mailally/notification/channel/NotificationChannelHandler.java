package com.mailally.notification.channel;

import com.mailally.notification.entity.Notification;

/**
 * Strategy interface for pluggable notification delivery channels (IN_APP, EMAIL, SMS, PUSH, SLACK, TEAMS, WHATSAPP, WEBSOCKET).
 */
public interface NotificationChannelHandler {

    /**
     * Checks whether this handler supports the specified channel identifier.
     */
    boolean supportsChannel(String channel);

    /**
     * Dispatches notification over the target delivery channel.
     */
    void dispatch(Notification notification);
}
