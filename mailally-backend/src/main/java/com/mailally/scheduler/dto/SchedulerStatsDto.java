package com.mailally.scheduler.dto;

/**
 * Aggregated statistics and summary metrics for the Scheduler module.
 */
public class SchedulerStatsDto {

    private long totalSchedules;
    private long activeSchedules;
    private long upcomingSchedules;
    private long completedSchedules;
    private long failedSchedules;
    private long pausedSchedules;
    private long cancelledSchedules;

    public SchedulerStatsDto() {}

    public SchedulerStatsDto(long totalSchedules, long activeSchedules, long upcomingSchedules,
                             long completedSchedules, long failedSchedules, long pausedSchedules,
                             long cancelledSchedules) {
        this.totalSchedules = totalSchedules;
        this.activeSchedules = activeSchedules;
        this.upcomingSchedules = upcomingSchedules;
        this.completedSchedules = completedSchedules;
        this.failedSchedules = failedSchedules;
        this.pausedSchedules = pausedSchedules;
        this.cancelledSchedules = cancelledSchedules;
    }

    public long getTotalSchedules() { return totalSchedules; }
    public void setTotalSchedules(long totalSchedules) { this.totalSchedules = totalSchedules; }
    public long getActiveSchedules() { return activeSchedules; }
    public void setActiveSchedules(long activeSchedules) { this.activeSchedules = activeSchedules; }
    public long getUpcomingSchedules() { return upcomingSchedules; }
    public void setUpcomingSchedules(long upcomingSchedules) { this.upcomingSchedules = upcomingSchedules; }
    public long getCompletedSchedules() { return completedSchedules; }
    public void setCompletedSchedules(long completedSchedules) { this.completedSchedules = completedSchedules; }
    public long getFailedSchedules() { return failedSchedules; }
    public void setFailedSchedules(long failedSchedules) { this.failedSchedules = failedSchedules; }
    public long getPausedSchedules() { return pausedSchedules; }
    public void setPausedSchedules(long pausedSchedules) { this.pausedSchedules = pausedSchedules; }
    public long getCancelledSchedules() { return cancelledSchedules; }
    public void setCancelledSchedules(long cancelledSchedules) { this.cancelledSchedules = cancelledSchedules; }

    public static SchedulerStatsDtoBuilder builder() { return new SchedulerStatsDtoBuilder(); }

    public static class SchedulerStatsDtoBuilder {
        private long totalSchedules;
        private long activeSchedules;
        private long upcomingSchedules;
        private long completedSchedules;
        private long failedSchedules;
        private long pausedSchedules;
        private long cancelledSchedules;

        SchedulerStatsDtoBuilder() {}

        public SchedulerStatsDtoBuilder totalSchedules(long totalSchedules) { this.totalSchedules = totalSchedules; return this; }
        public SchedulerStatsDtoBuilder activeSchedules(long activeSchedules) { this.activeSchedules = activeSchedules; return this; }
        public SchedulerStatsDtoBuilder upcomingSchedules(long upcomingSchedules) { this.upcomingSchedules = upcomingSchedules; return this; }
        public SchedulerStatsDtoBuilder completedSchedules(long completedSchedules) { this.completedSchedules = completedSchedules; return this; }
        public SchedulerStatsDtoBuilder failedSchedules(long failedSchedules) { this.failedSchedules = failedSchedules; return this; }
        public SchedulerStatsDtoBuilder pausedSchedules(long pausedSchedules) { this.pausedSchedules = pausedSchedules; return this; }
        public SchedulerStatsDtoBuilder cancelledSchedules(long cancelledSchedules) { this.cancelledSchedules = cancelledSchedules; return this; }

        public SchedulerStatsDto build() {
            return new SchedulerStatsDto(totalSchedules, activeSchedules, upcomingSchedules,
                    completedSchedules, failedSchedules, pausedSchedules, cancelledSchedules);
        }
    }
}
