package com.mailally.dashboard.dto;

/**
 * Real-time live status DTO for active execution, queue size, provider status, worker status.
 */
public class DashboardLiveStatusDto {

    private String currentRunningCampaignName;
    private Long currentRunningCampaignId;
    private long queuePendingCount;
    private String activeProvider;
    private String providerHealth;
    private String workerStatus;
    private String databaseStatus;
    private String applicationStatus;

    public DashboardLiveStatusDto() {}

    public DashboardLiveStatusDto(String currentRunningCampaignName, Long currentRunningCampaignId,
                                  long queuePendingCount, String activeProvider, String providerHealth,
                                  String workerStatus, String databaseStatus, String applicationStatus) {
        this.currentRunningCampaignName = currentRunningCampaignName;
        this.currentRunningCampaignId = currentRunningCampaignId;
        this.queuePendingCount = queuePendingCount;
        this.activeProvider = activeProvider;
        this.providerHealth = providerHealth;
        this.workerStatus = workerStatus;
        this.databaseStatus = databaseStatus;
        this.applicationStatus = applicationStatus;
    }

    public String getCurrentRunningCampaignName() { return currentRunningCampaignName; }
    public void setCurrentRunningCampaignName(String currentRunningCampaignName) { this.currentRunningCampaignName = currentRunningCampaignName; }
    public Long getCurrentRunningCampaignId() { return currentRunningCampaignId; }
    public void setCurrentRunningCampaignId(Long currentRunningCampaignId) { this.currentRunningCampaignId = currentRunningCampaignId; }
    public long getQueuePendingCount() { return queuePendingCount; }
    public void setQueuePendingCount(long queuePendingCount) { this.queuePendingCount = queuePendingCount; }
    public String getActiveProvider() { return activeProvider; }
    public void setActiveProvider(String activeProvider) { this.activeProvider = activeProvider; }
    public String getProviderHealth() { return providerHealth; }
    public void setProviderHealth(String providerHealth) { this.providerHealth = providerHealth; }
    public String getWorkerStatus() { return workerStatus; }
    public void setWorkerStatus(String workerStatus) { this.workerStatus = workerStatus; }
    public String getDatabaseStatus() { return databaseStatus; }
    public void setDatabaseStatus(String databaseStatus) { this.databaseStatus = databaseStatus; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }

    public static DashboardLiveStatusDtoBuilder builder() { return new DashboardLiveStatusDtoBuilder(); }

    public static class DashboardLiveStatusDtoBuilder {
        private String currentRunningCampaignName;
        private Long currentRunningCampaignId;
        private long queuePendingCount;
        private String activeProvider;
        private String providerHealth;
        private String workerStatus;
        private String databaseStatus;
        private String applicationStatus;

        DashboardLiveStatusDtoBuilder() {}

        public DashboardLiveStatusDtoBuilder currentRunningCampaignName(String currentRunningCampaignName) { this.currentRunningCampaignName = currentRunningCampaignName; return this; }
        public DashboardLiveStatusDtoBuilder currentRunningCampaignId(Long currentRunningCampaignId) { this.currentRunningCampaignId = currentRunningCampaignId; return this; }
        public DashboardLiveStatusDtoBuilder queuePendingCount(long queuePendingCount) { this.queuePendingCount = queuePendingCount; return this; }
        public DashboardLiveStatusDtoBuilder activeProvider(String activeProvider) { this.activeProvider = activeProvider; return this; }
        public DashboardLiveStatusDtoBuilder providerHealth(String providerHealth) { this.providerHealth = providerHealth; return this; }
        public DashboardLiveStatusDtoBuilder workerStatus(String workerStatus) { this.workerStatus = workerStatus; return this; }
        public DashboardLiveStatusDtoBuilder databaseStatus(String databaseStatus) { this.databaseStatus = databaseStatus; return this; }
        public DashboardLiveStatusDtoBuilder applicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; return this; }

        public DashboardLiveStatusDto build() {
            return new DashboardLiveStatusDto(currentRunningCampaignName, currentRunningCampaignId, queuePendingCount,
                    activeProvider, providerHealth, workerStatus, databaseStatus, applicationStatus);
        }
    }
}
