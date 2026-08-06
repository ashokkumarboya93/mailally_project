package com.mailally.notification.dto;

/**
 * Summary metrics DTO breakdown for notifications.
 */
public class NotificationSummaryDto {

    private long totalUnread;
    private long totalRead;
    private long totalArchived;
    private long highPriorityCount;
    private long criticalPriorityCount;

    public NotificationSummaryDto() {}

    public NotificationSummaryDto(long totalUnread, long totalRead, long totalArchived,
                                  long highPriorityCount, long criticalPriorityCount) {
        this.totalUnread = totalUnread;
        this.totalRead = totalRead;
        this.totalArchived = totalArchived;
        this.highPriorityCount = highPriorityCount;
        this.criticalPriorityCount = criticalPriorityCount;
    }

    public long getTotalUnread() { return totalUnread; }
    public void setTotalUnread(long totalUnread) { this.totalUnread = totalUnread; }
    public long getTotalRead() { return totalRead; }
    public void setTotalRead(long totalRead) { this.totalRead = totalRead; }
    public long getTotalArchived() { return totalArchived; }
    public void setTotalArchived(long totalArchived) { this.totalArchived = totalArchived; }
    public long getHighPriorityCount() { return highPriorityCount; }
    public void setHighPriorityCount(long highPriorityCount) { this.highPriorityCount = highPriorityCount; }
    public long getCriticalPriorityCount() { return criticalPriorityCount; }
    public void setCriticalPriorityCount(long criticalPriorityCount) { this.criticalPriorityCount = criticalPriorityCount; }

    public static NotificationSummaryDtoBuilder builder() { return new NotificationSummaryDtoBuilder(); }

    public static class NotificationSummaryDtoBuilder {
        private long totalUnread;
        private long totalRead;
        private long totalArchived;
        private long highPriorityCount;
        private long criticalPriorityCount;

        NotificationSummaryDtoBuilder() {}

        public NotificationSummaryDtoBuilder totalUnread(long totalUnread) { this.totalUnread = totalUnread; return this; }
        public NotificationSummaryDtoBuilder totalRead(long totalRead) { this.totalRead = totalRead; return this; }
        public NotificationSummaryDtoBuilder totalArchived(long totalArchived) { this.totalArchived = totalArchived; return this; }
        public NotificationSummaryDtoBuilder highPriorityCount(long highPriorityCount) { this.highPriorityCount = highPriorityCount; return this; }
        public NotificationSummaryDtoBuilder criticalPriorityCount(long criticalPriorityCount) { this.criticalPriorityCount = criticalPriorityCount; return this; }

        public NotificationSummaryDto build() {
            return new NotificationSummaryDto(totalUnread, totalRead, totalArchived, highPriorityCount, criticalPriorityCount);
        }
    }
}
