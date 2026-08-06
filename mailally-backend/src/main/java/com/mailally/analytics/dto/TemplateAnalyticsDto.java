package com.mailally.analytics.dto;

/**
 * Analytics metrics for Email Templates (usage count, version, performance ranking).
 */
public class TemplateAnalyticsDto {

    private Long templateId;
    private String templateName;
    private String status;
    private int version;
    private long timesUsedInCampaigns;
    private long totalEmailsSent;
    private double averageDeliveryRate;

    public TemplateAnalyticsDto() {}

    public TemplateAnalyticsDto(Long templateId, String templateName, String status, int version,
                                long timesUsedInCampaigns, long totalEmailsSent, double averageDeliveryRate) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.status = status;
        this.version = version;
        this.timesUsedInCampaigns = timesUsedInCampaigns;
        this.totalEmailsSent = totalEmailsSent;
        this.averageDeliveryRate = averageDeliveryRate;
    }

    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public long getTimesUsedInCampaigns() { return timesUsedInCampaigns; }
    public void setTimesUsedInCampaigns(long timesUsedInCampaigns) { this.timesUsedInCampaigns = timesUsedInCampaigns; }
    public long getTotalEmailsSent() { return totalEmailsSent; }
    public void setTotalEmailsSent(long totalEmailsSent) { this.totalEmailsSent = totalEmailsSent; }
    public double getAverageDeliveryRate() { return averageDeliveryRate; }
    public void setAverageDeliveryRate(double averageDeliveryRate) { this.averageDeliveryRate = averageDeliveryRate; }

    public static TemplateAnalyticsDtoBuilder builder() { return new TemplateAnalyticsDtoBuilder(); }

    public static class TemplateAnalyticsDtoBuilder {
        private Long templateId;
        private String templateName;
        private String status;
        private int version;
        private long timesUsedInCampaigns;
        private long totalEmailsSent;
        private double averageDeliveryRate;

        TemplateAnalyticsDtoBuilder() {}

        public TemplateAnalyticsDtoBuilder templateId(Long templateId) { this.templateId = templateId; return this; }
        public TemplateAnalyticsDtoBuilder templateName(String templateName) { this.templateName = templateName; return this; }
        public TemplateAnalyticsDtoBuilder status(String status) { this.status = status; return this; }
        public TemplateAnalyticsDtoBuilder version(int version) { this.version = version; return this; }
        public TemplateAnalyticsDtoBuilder timesUsedInCampaigns(long timesUsedInCampaigns) { this.timesUsedInCampaigns = timesUsedInCampaigns; return this; }
        public TemplateAnalyticsDtoBuilder totalEmailsSent(long totalEmailsSent) { this.totalEmailsSent = totalEmailsSent; return this; }
        public TemplateAnalyticsDtoBuilder averageDeliveryRate(double averageDeliveryRate) { this.averageDeliveryRate = averageDeliveryRate; return this; }

        public TemplateAnalyticsDto build() {
            return new TemplateAnalyticsDto(templateId, templateName, status, version, timesUsedInCampaigns,
                    totalEmailsSent, averageDeliveryRate);
        }
    }
}
