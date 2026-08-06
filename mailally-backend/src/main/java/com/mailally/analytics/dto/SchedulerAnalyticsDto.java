package com.mailally.analytics.dto;

/**
 * Analytics breakdown for Scheduler execution status.
 */
public class SchedulerAnalyticsDto {

    private long totalScheduledTasks;
    private long activeTasks;
    private long upcomingTasks;
    private long completedTasks;
    private long failedTasks;
    private long pausedTasks;
    private long cancelledTasks;
    private double successPercentage;

    public SchedulerAnalyticsDto() {}

    public SchedulerAnalyticsDto(long totalScheduledTasks, long activeTasks, long upcomingTasks,
                                 long completedTasks, long failedTasks, long pausedTasks,
                                 long cancelledTasks, double successPercentage) {
        this.totalScheduledTasks = totalScheduledTasks;
        this.activeTasks = activeTasks;
        this.upcomingTasks = upcomingTasks;
        this.completedTasks = completedTasks;
        this.failedTasks = failedTasks;
        this.pausedTasks = pausedTasks;
        this.cancelledTasks = cancelledTasks;
        this.successPercentage = successPercentage;
    }

    public long getTotalScheduledTasks() { return totalScheduledTasks; }
    public void setTotalScheduledTasks(long totalScheduledTasks) { this.totalScheduledTasks = totalScheduledTasks; }
    public long getActiveTasks() { return activeTasks; }
    public void setActiveTasks(long activeTasks) { this.activeTasks = activeTasks; }
    public long getUpcomingTasks() { return upcomingTasks; }
    public void setUpcomingTasks(long upcomingTasks) { this.upcomingTasks = upcomingTasks; }
    public long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }
    public long getFailedTasks() { return failedTasks; }
    public void setFailedTasks(long failedTasks) { this.failedTasks = failedTasks; }
    public long getPausedTasks() { return pausedTasks; }
    public void setPausedTasks(long pausedTasks) { this.pausedTasks = pausedTasks; }
    public long getCancelledTasks() { return cancelledTasks; }
    public void setCancelledTasks(long cancelledTasks) { this.cancelledTasks = cancelledTasks; }
    public double getSuccessPercentage() { return successPercentage; }
    public void setSuccessPercentage(double successPercentage) { this.successPercentage = successPercentage; }

    public static SchedulerAnalyticsDtoBuilder builder() { return new SchedulerAnalyticsDtoBuilder(); }

    public static class SchedulerAnalyticsDtoBuilder {
        private long totalScheduledTasks;
        private long activeTasks;
        private long upcomingTasks;
        private long completedTasks;
        private long failedTasks;
        private long pausedTasks;
        private long cancelledTasks;
        private double successPercentage;

        SchedulerAnalyticsDtoBuilder() {}

        public SchedulerAnalyticsDtoBuilder totalScheduledTasks(long totalScheduledTasks) { this.totalScheduledTasks = totalScheduledTasks; return this; }
        public SchedulerAnalyticsDtoBuilder activeTasks(long activeTasks) { this.activeTasks = activeTasks; return this; }
        public SchedulerAnalyticsDtoBuilder upcomingTasks(long upcomingTasks) { this.upcomingTasks = upcomingTasks; return this; }
        public SchedulerAnalyticsDtoBuilder completedTasks(long completedTasks) { this.completedTasks = completedTasks; return this; }
        public SchedulerAnalyticsDtoBuilder failedTasks(long failedTasks) { this.failedTasks = failedTasks; return this; }
        public SchedulerAnalyticsDtoBuilder pausedTasks(long pausedTasks) { this.pausedTasks = pausedTasks; return this; }
        public SchedulerAnalyticsDtoBuilder cancelledTasks(long cancelledTasks) { this.cancelledTasks = cancelledTasks; return this; }
        public SchedulerAnalyticsDtoBuilder successPercentage(double successPercentage) { this.successPercentage = successPercentage; return this; }

        public SchedulerAnalyticsDto build() {
            return new SchedulerAnalyticsDto(totalScheduledTasks, activeTasks, upcomingTasks, completedTasks,
                    failedTasks, pausedTasks, cancelledTasks, successPercentage);
        }
    }
}
