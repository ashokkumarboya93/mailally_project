package com.mailally.analytics.dto;

/**
 * Provider performance metrics DTO (SMTP, Brevo, SES).
 * Streamlined for MVP: totalSent, totalFailed, successPercentage, failurePercentage.
 */
public class ProviderAnalyticsDto {

    private String providerName;
    private boolean isConfigured;
    private boolean isActive;
    private long totalSent;
    private long totalFailed;
    private double successPercentage;
    private double failurePercentage;

    public ProviderAnalyticsDto() {}

    public ProviderAnalyticsDto(String providerName, boolean isConfigured, boolean isActive,
                                long totalSent, long totalFailed, double successPercentage,
                                double failurePercentage) {
        this.providerName = providerName;
        this.isConfigured = isConfigured;
        this.isActive = isActive;
        this.totalSent = totalSent;
        this.totalFailed = totalFailed;
        this.successPercentage = successPercentage;
        this.failurePercentage = failurePercentage;
    }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public boolean isConfigured() { return isConfigured; }
    public void setConfigured(boolean configured) { isConfigured = configured; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public long getTotalSent() { return totalSent; }
    public void setTotalSent(long totalSent) { this.totalSent = totalSent; }
    public long getTotalFailed() { return totalFailed; }
    public void setTotalFailed(long totalFailed) { this.totalFailed = totalFailed; }
    public double getSuccessPercentage() { return successPercentage; }
    public void setSuccessPercentage(double successPercentage) { this.successPercentage = successPercentage; }
    public double getFailurePercentage() { return failurePercentage; }
    public void setFailurePercentage(double failurePercentage) { this.failurePercentage = failurePercentage; }

    public static ProviderAnalyticsDtoBuilder builder() { return new ProviderAnalyticsDtoBuilder(); }

    public static class ProviderAnalyticsDtoBuilder {
        private String providerName;
        private boolean isConfigured;
        private boolean isActive;
        private long totalSent;
        private long totalFailed;
        private double successPercentage;
        private double failurePercentage;

        ProviderAnalyticsDtoBuilder() {}

        public ProviderAnalyticsDtoBuilder providerName(String providerName) { this.providerName = providerName; return this; }
        public ProviderAnalyticsDtoBuilder isConfigured(boolean isConfigured) { this.isConfigured = isConfigured; return this; }
        public ProviderAnalyticsDtoBuilder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public ProviderAnalyticsDtoBuilder totalSent(long totalSent) { this.totalSent = totalSent; return this; }
        public ProviderAnalyticsDtoBuilder totalFailed(long totalFailed) { this.totalFailed = totalFailed; return this; }
        public ProviderAnalyticsDtoBuilder successPercentage(double successPercentage) { this.successPercentage = successPercentage; return this; }
        public ProviderAnalyticsDtoBuilder failurePercentage(double failurePercentage) { this.failurePercentage = failurePercentage; return this; }

        public ProviderAnalyticsDto build() {
            return new ProviderAnalyticsDto(providerName, isConfigured, isActive, totalSent, totalFailed,
                    successPercentage, failurePercentage);
        }
    }
}
