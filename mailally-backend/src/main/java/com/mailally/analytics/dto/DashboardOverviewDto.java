package com.mailally.analytics.dto;

/**
 * Top-level dashboard summary metrics DTO.
 */
public class DashboardOverviewDto {

    private long totalCampaigns;
    private long runningCampaigns;
    private long completedCampaigns;
    private long failedCampaigns;
    private long todayEmailsSent;
    private long weeklyEmailsSent;
    private long monthlyEmailsSent;
    private long totalContacts;
    private long activeContacts;
    private long totalTemplates;
    private long totalSegments;
    private double averageDeliveryRate;
    private double averageOpenRate;
    private double averageClickRate;
    private String systemHealth;
    private String queueHealth;
    private String providerHealth;

    public DashboardOverviewDto() {}

    public DashboardOverviewDto(long totalCampaigns, long runningCampaigns, long completedCampaigns,
                                long failedCampaigns, long todayEmailsSent, long weeklyEmailsSent,
                                long monthlyEmailsSent, long totalContacts, long activeContacts,
                                long totalTemplates, long totalSegments, double averageDeliveryRate,
                                double averageOpenRate, double averageClickRate, String systemHealth,
                                String queueHealth, String providerHealth) {
        this.totalCampaigns = totalCampaigns;
        this.runningCampaigns = runningCampaigns;
        this.completedCampaigns = completedCampaigns;
        this.failedCampaigns = failedCampaigns;
        this.todayEmailsSent = todayEmailsSent;
        this.weeklyEmailsSent = weeklyEmailsSent;
        this.monthlyEmailsSent = monthlyEmailsSent;
        this.totalContacts = totalContacts;
        this.activeContacts = activeContacts;
        this.totalTemplates = totalTemplates;
        this.totalSegments = totalSegments;
        this.averageDeliveryRate = averageDeliveryRate;
        this.averageOpenRate = averageOpenRate;
        this.averageClickRate = averageClickRate;
        this.systemHealth = systemHealth;
        this.queueHealth = queueHealth;
        this.providerHealth = providerHealth;
    }

    public long getTotalCampaigns() { return totalCampaigns; }
    public void setTotalCampaigns(long totalCampaigns) { this.totalCampaigns = totalCampaigns; }
    public long getRunningCampaigns() { return runningCampaigns; }
    public void setRunningCampaigns(long runningCampaigns) { this.runningCampaigns = runningCampaigns; }
    public long getCompletedCampaigns() { return completedCampaigns; }
    public void setCompletedCampaigns(long completedCampaigns) { this.completedCampaigns = completedCampaigns; }
    public long getFailedCampaigns() { return failedCampaigns; }
    public void setFailedCampaigns(long failedCampaigns) { this.failedCampaigns = failedCampaigns; }
    public long getTodayEmailsSent() { return todayEmailsSent; }
    public void setTodayEmailsSent(long todayEmailsSent) { this.todayEmailsSent = todayEmailsSent; }
    public long getWeeklyEmailsSent() { return weeklyEmailsSent; }
    public void setWeeklyEmailsSent(long weeklyEmailsSent) { this.weeklyEmailsSent = weeklyEmailsSent; }
    public long getMonthlyEmailsSent() { return monthlyEmailsSent; }
    public void setMonthlyEmailsSent(long monthlyEmailsSent) { this.monthlyEmailsSent = monthlyEmailsSent; }
    public long getTotalContacts() { return totalContacts; }
    public void setTotalContacts(long totalContacts) { this.totalContacts = totalContacts; }
    public long getActiveContacts() { return activeContacts; }
    public void setActiveContacts(long activeContacts) { this.activeContacts = activeContacts; }
    public long getTotalTemplates() { return totalTemplates; }
    public void setTotalTemplates(long totalTemplates) { this.totalTemplates = totalTemplates; }
    public long getTotalSegments() { return totalSegments; }
    public void setTotalSegments(long totalSegments) { this.totalSegments = totalSegments; }
    public double getAverageDeliveryRate() { return averageDeliveryRate; }
    public void setAverageDeliveryRate(double averageDeliveryRate) { this.averageDeliveryRate = averageDeliveryRate; }
    public double getAverageOpenRate() { return averageOpenRate; }
    public void setAverageOpenRate(double averageOpenRate) { this.averageOpenRate = averageOpenRate; }
    public double getAverageClickRate() { return averageClickRate; }
    public void setAverageClickRate(double averageClickRate) { this.averageClickRate = averageClickRate; }
    public String getSystemHealth() { return systemHealth; }
    public void setSystemHealth(String systemHealth) { this.systemHealth = systemHealth; }
    public String getQueueHealth() { return queueHealth; }
    public void setQueueHealth(String queueHealth) { this.queueHealth = queueHealth; }
    public String getProviderHealth() { return providerHealth; }
    public void setProviderHealth(String providerHealth) { this.providerHealth = providerHealth; }

    public static DashboardOverviewDtoBuilder builder() { return new DashboardOverviewDtoBuilder(); }

    public static class DashboardOverviewDtoBuilder {
        private long totalCampaigns;
        private long runningCampaigns;
        private long completedCampaigns;
        private long failedCampaigns;
        private long todayEmailsSent;
        private long weeklyEmailsSent;
        private long monthlyEmailsSent;
        private long totalContacts;
        private long activeContacts;
        private long totalTemplates;
        private long totalSegments;
        private double averageDeliveryRate;
        private double averageOpenRate;
        private double averageClickRate;
        private String systemHealth;
        private String queueHealth;
        private String providerHealth;

        DashboardOverviewDtoBuilder() {}

        public DashboardOverviewDtoBuilder totalCampaigns(long totalCampaigns) { this.totalCampaigns = totalCampaigns; return this; }
        public DashboardOverviewDtoBuilder runningCampaigns(long runningCampaigns) { this.runningCampaigns = runningCampaigns; return this; }
        public DashboardOverviewDtoBuilder completedCampaigns(long completedCampaigns) { this.completedCampaigns = completedCampaigns; return this; }
        public DashboardOverviewDtoBuilder failedCampaigns(long failedCampaigns) { this.failedCampaigns = failedCampaigns; return this; }
        public DashboardOverviewDtoBuilder todayEmailsSent(long todayEmailsSent) { this.todayEmailsSent = todayEmailsSent; return this; }
        public DashboardOverviewDtoBuilder weeklyEmailsSent(long weeklyEmailsSent) { this.weeklyEmailsSent = weeklyEmailsSent; return this; }
        public DashboardOverviewDtoBuilder monthlyEmailsSent(long monthlyEmailsSent) { this.monthlyEmailsSent = monthlyEmailsSent; return this; }
        public DashboardOverviewDtoBuilder totalContacts(long totalContacts) { this.totalContacts = totalContacts; return this; }
        public DashboardOverviewDtoBuilder activeContacts(long activeContacts) { this.activeContacts = activeContacts; return this; }
        public DashboardOverviewDtoBuilder totalTemplates(long totalTemplates) { this.totalTemplates = totalTemplates; return this; }
        public DashboardOverviewDtoBuilder totalSegments(long totalSegments) { this.totalSegments = totalSegments; return this; }
        public DashboardOverviewDtoBuilder averageDeliveryRate(double averageDeliveryRate) { this.averageDeliveryRate = averageDeliveryRate; return this; }
        public DashboardOverviewDtoBuilder averageOpenRate(double averageOpenRate) { this.averageOpenRate = averageOpenRate; return this; }
        public DashboardOverviewDtoBuilder averageClickRate(double averageClickRate) { this.averageClickRate = averageClickRate; return this; }
        public DashboardOverviewDtoBuilder systemHealth(String systemHealth) { this.systemHealth = systemHealth; return this; }
        public DashboardOverviewDtoBuilder queueHealth(String queueHealth) { this.queueHealth = queueHealth; return this; }
        public DashboardOverviewDtoBuilder providerHealth(String providerHealth) { this.providerHealth = providerHealth; return this; }

        public DashboardOverviewDto build() {
            return new DashboardOverviewDto(totalCampaigns, runningCampaigns, completedCampaigns, failedCampaigns,
                    todayEmailsSent, weeklyEmailsSent, monthlyEmailsSent, totalContacts, activeContacts, totalTemplates,
                    totalSegments, averageDeliveryRate, averageOpenRate, averageClickRate, systemHealth, queueHealth, providerHealth);
        }
    }
}
