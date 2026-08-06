package com.mailally.dashboard.dto;

import com.mailally.analytics.dto.ProviderAnalyticsDto;

import java.util.List;

/**
 * Dashboard widgets for Email Engine status, provider performance, and queue health.
 */
public class DashboardEmailWidgetDto {

    private List<ProviderAnalyticsDto> providers;
    private String activeProvider;
    private long pendingQueueSize;
    private long totalSent;
    private long totalFailed;
    private double averageDeliveryRate;

    public DashboardEmailWidgetDto() {}

    public DashboardEmailWidgetDto(List<ProviderAnalyticsDto> providers, String activeProvider,
                                  long pendingQueueSize, long totalSent, long totalFailed,
                                  double averageDeliveryRate) {
        this.providers = providers;
        this.activeProvider = activeProvider;
        this.pendingQueueSize = pendingQueueSize;
        this.totalSent = totalSent;
        this.totalFailed = totalFailed;
        this.averageDeliveryRate = averageDeliveryRate;
    }

    public List<ProviderAnalyticsDto> getProviders() { return providers; }
    public void setProviders(List<ProviderAnalyticsDto> providers) { this.providers = providers; }
    public String getActiveProvider() { return activeProvider; }
    public void setActiveProvider(String activeProvider) { this.activeProvider = activeProvider; }
    public long getPendingQueueSize() { return pendingQueueSize; }
    public void setPendingQueueSize(long pendingQueueSize) { this.pendingQueueSize = pendingQueueSize; }
    public long getTotalSent() { return totalSent; }
    public void setTotalSent(long totalSent) { this.totalSent = totalSent; }
    public long getTotalFailed() { return totalFailed; }
    public void setTotalFailed(long totalFailed) { this.totalFailed = totalFailed; }
    public double getAverageDeliveryRate() { return averageDeliveryRate; }
    public void setAverageDeliveryRate(double averageDeliveryRate) { this.averageDeliveryRate = averageDeliveryRate; }

    public static DashboardEmailWidgetDtoBuilder builder() { return new DashboardEmailWidgetDtoBuilder(); }

    public static class DashboardEmailWidgetDtoBuilder {
        private List<ProviderAnalyticsDto> providers;
        private String activeProvider;
        private long pendingQueueSize;
        private long totalSent;
        private long totalFailed;
        private double averageDeliveryRate;

        DashboardEmailWidgetDtoBuilder() {}

        public DashboardEmailWidgetDtoBuilder providers(List<ProviderAnalyticsDto> providers) { this.providers = providers; return this; }
        public DashboardEmailWidgetDtoBuilder activeProvider(String activeProvider) { this.activeProvider = activeProvider; return this; }
        public DashboardEmailWidgetDtoBuilder pendingQueueSize(long pendingQueueSize) { this.pendingQueueSize = pendingQueueSize; return this; }
        public DashboardEmailWidgetDtoBuilder totalSent(long totalSent) { this.totalSent = totalSent; return this; }
        public DashboardEmailWidgetDtoBuilder totalFailed(long totalFailed) { this.totalFailed = totalFailed; return this; }
        public DashboardEmailWidgetDtoBuilder averageDeliveryRate(double averageDeliveryRate) { this.averageDeliveryRate = averageDeliveryRate; return this; }

        public DashboardEmailWidgetDto build() {
            return new DashboardEmailWidgetDto(providers, activeProvider, pendingQueueSize, totalSent, totalFailed, averageDeliveryRate);
        }
    }
}
