package com.mailally.notification.dto;

/**
 * Unread count response DTO for polling or header indicators.
 */
public class NotificationCountDto {

    private long unreadCount;
    private long totalCount;

    public NotificationCountDto() {}

    public NotificationCountDto(long unreadCount, long totalCount) {
        this.unreadCount = unreadCount;
        this.totalCount = totalCount;
    }

    public long getUnreadCount() { return unreadCount; }
    public void setUnreadCount(long unreadCount) { this.unreadCount = unreadCount; }
    public long getTotalCount() { return totalCount; }
    public void setTotalCount(long totalCount) { this.totalCount = totalCount; }
}
