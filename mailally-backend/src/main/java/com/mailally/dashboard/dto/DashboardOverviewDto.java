package com.mailally.dashboard.dto;

/**
 * Executive dashboard overview DTO including organization plan, subscription metadata,
 * and high-level performance indicators.
 */
public class DashboardOverviewDto {

    private Long organizationId;
    private String organizationName;
    private String subscriptionPlan;
    private String subscriptionStatus;
    private long emailCreditsRemaining;
    private long campaignCreditsRemaining;
    private double storageUsedMb;
    private com.mailally.analytics.dto.DashboardOverviewDto analyticsSummary;
    private DashboardLiveStatusDto liveStatus;
    private DashboardHealthDto systemHealth;

    public DashboardOverviewDto() {}

    public DashboardOverviewDto(Long organizationId, String organizationName, String subscriptionPlan,
                                String subscriptionStatus, long emailCreditsRemaining,
                                long campaignCreditsRemaining, double storageUsedMb,
                                com.mailally.analytics.dto.DashboardOverviewDto analyticsSummary, DashboardLiveStatusDto liveStatus,
                                DashboardHealthDto systemHealth) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.subscriptionPlan = subscriptionPlan;
        this.subscriptionStatus = subscriptionStatus;
        this.emailCreditsRemaining = emailCreditsRemaining;
        this.campaignCreditsRemaining = campaignCreditsRemaining;
        this.storageUsedMb = storageUsedMb;
        this.analyticsSummary = analyticsSummary;
        this.liveStatus = liveStatus;
        this.systemHealth = systemHealth;
    }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    public String getSubscriptionPlan() { return subscriptionPlan; }
    public void setSubscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }
    public String getSubscriptionStatus() { return subscriptionStatus; }
    public void setSubscriptionStatus(String subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; }
    public long getEmailCreditsRemaining() { return emailCreditsRemaining; }
    public void setEmailCreditsRemaining(long emailCreditsRemaining) { this.emailCreditsRemaining = emailCreditsRemaining; }
    public long getCampaignCreditsRemaining() { return campaignCreditsRemaining; }
    public void setCampaignCreditsRemaining(long campaignCreditsRemaining) { this.campaignCreditsRemaining = campaignCreditsRemaining; }
    public double getStorageUsedMb() { return storageUsedMb; }
    public void setStorageUsedMb(double storageUsedMb) { this.storageUsedMb = storageUsedMb; }
    public com.mailally.analytics.dto.DashboardOverviewDto getAnalyticsSummary() { return analyticsSummary; }
    public void setAnalyticsSummary(com.mailally.analytics.dto.DashboardOverviewDto analyticsSummary) { this.analyticsSummary = analyticsSummary; }
    public DashboardLiveStatusDto getLiveStatus() { return liveStatus; }
    public void setLiveStatus(DashboardLiveStatusDto liveStatus) { this.liveStatus = liveStatus; }
    public DashboardHealthDto getSystemHealth() { return systemHealth; }
    public void setSystemHealth(DashboardHealthDto systemHealth) { this.systemHealth = systemHealth; }

    public static DashboardOverviewDtoBuilder builder() { return new DashboardOverviewDtoBuilder(); }

    public static class DashboardOverviewDtoBuilder {
        private Long organizationId;
        private String organizationName;
        private String subscriptionPlan;
        private String subscriptionStatus;
        private long emailCreditsRemaining;
        private long campaignCreditsRemaining;
        private double storageUsedMb;
        private com.mailally.analytics.dto.DashboardOverviewDto analyticsSummary;
        private DashboardLiveStatusDto liveStatus;
        private DashboardHealthDto systemHealth;

        DashboardOverviewDtoBuilder() {}

        public DashboardOverviewDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public DashboardOverviewDtoBuilder organizationName(String organizationName) { this.organizationName = organizationName; return this; }
        public DashboardOverviewDtoBuilder subscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; return this; }
        public DashboardOverviewDtoBuilder subscriptionStatus(String subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; return this; }
        public DashboardOverviewDtoBuilder emailCreditsRemaining(long emailCreditsRemaining) { this.emailCreditsRemaining = emailCreditsRemaining; return this; }
        public DashboardOverviewDtoBuilder campaignCreditsRemaining(long campaignCreditsRemaining) { this.campaignCreditsRemaining = campaignCreditsRemaining; return this; }
        public DashboardOverviewDtoBuilder storageUsedMb(double storageUsedMb) { this.storageUsedMb = storageUsedMb; return this; }
        public DashboardOverviewDtoBuilder analyticsSummary(com.mailally.analytics.dto.DashboardOverviewDto analyticsSummary) { this.analyticsSummary = analyticsSummary; return this; }
        public DashboardOverviewDtoBuilder liveStatus(DashboardLiveStatusDto liveStatus) { this.liveStatus = liveStatus; return this; }
        public DashboardOverviewDtoBuilder systemHealth(DashboardHealthDto systemHealth) { this.systemHealth = systemHealth; return this; }

        public DashboardOverviewDto build() {
            return new DashboardOverviewDto(organizationId, organizationName, subscriptionPlan, subscriptionStatus,
                    emailCreditsRemaining, campaignCreditsRemaining, storageUsedMb, analyticsSummary, liveStatus, systemHealth);
        }
    }
}
