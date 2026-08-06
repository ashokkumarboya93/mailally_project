package com.mailally.dashboard.dto;

import com.mailally.scheduler.dto.SchedulerResponseDto;

import java.util.List;

/**
 * Dashboard widget DTO for Scheduler status, upcoming jobs, and history.
 */
public class DashboardSchedulerWidgetDto {

    private List<SchedulerResponseDto> upcomingJobs;
    private List<SchedulerResponseDto> runningJobs;
    private List<SchedulerResponseDto> pausedJobs;
    private List<SchedulerResponseDto> failedJobs;
    private long totalScheduledCount;

    public DashboardSchedulerWidgetDto() {}

    public DashboardSchedulerWidgetDto(List<SchedulerResponseDto> upcomingJobs, List<SchedulerResponseDto> runningJobs,
                                       List<SchedulerResponseDto> pausedJobs, List<SchedulerResponseDto> failedJobs,
                                       long totalScheduledCount) {
        this.upcomingJobs = upcomingJobs;
        this.runningJobs = runningJobs;
        this.pausedJobs = pausedJobs;
        this.failedJobs = failedJobs;
        this.totalScheduledCount = totalScheduledCount;
    }

    public List<SchedulerResponseDto> getUpcomingJobs() { return upcomingJobs; }
    public void setUpcomingJobs(List<SchedulerResponseDto> upcomingJobs) { this.upcomingJobs = upcomingJobs; }
    public List<SchedulerResponseDto> getRunningJobs() { return runningJobs; }
    public void setRunningJobs(List<SchedulerResponseDto> runningJobs) { this.runningJobs = runningJobs; }
    public List<SchedulerResponseDto> getPausedJobs() { return pausedJobs; }
    public void setPausedJobs(List<SchedulerResponseDto> pausedJobs) { this.pausedJobs = pausedJobs; }
    public List<SchedulerResponseDto> getFailedJobs() { return failedJobs; }
    public void setFailedJobs(List<SchedulerResponseDto> failedJobs) { this.failedJobs = failedJobs; }
    public long getTotalScheduledCount() { return totalScheduledCount; }
    public void setTotalScheduledCount(long totalScheduledCount) { this.totalScheduledCount = totalScheduledCount; }

    public static DashboardSchedulerWidgetDtoBuilder builder() { return new DashboardSchedulerWidgetDtoBuilder(); }

    public static class DashboardSchedulerWidgetDtoBuilder {
        private List<SchedulerResponseDto> upcomingJobs;
        private List<SchedulerResponseDto> runningJobs;
        private List<SchedulerResponseDto> pausedJobs;
        private List<SchedulerResponseDto> failedJobs;
        private long totalScheduledCount;

        DashboardSchedulerWidgetDtoBuilder() {}

        public DashboardSchedulerWidgetDtoBuilder upcomingJobs(List<SchedulerResponseDto> upcomingJobs) { this.upcomingJobs = upcomingJobs; return this; }
        public DashboardSchedulerWidgetDtoBuilder runningJobs(List<SchedulerResponseDto> runningJobs) { this.runningJobs = runningJobs; return this; }
        public DashboardSchedulerWidgetDtoBuilder pausedJobs(List<SchedulerResponseDto> pausedJobs) { this.pausedJobs = pausedJobs; return this; }
        public DashboardSchedulerWidgetDtoBuilder failedJobs(List<SchedulerResponseDto> failedJobs) { this.failedJobs = failedJobs; return this; }
        public DashboardSchedulerWidgetDtoBuilder totalScheduledCount(long totalScheduledCount) { this.totalScheduledCount = totalScheduledCount; return this; }

        public DashboardSchedulerWidgetDto build() {
            return new DashboardSchedulerWidgetDto(upcomingJobs, runningJobs, pausedJobs, failedJobs, totalScheduledCount);
        }
    }
}
