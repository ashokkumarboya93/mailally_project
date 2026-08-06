package com.mailally.analytics.dto;

/**
 * Analytics metric representation for a single campaign or aggregated campaigns.
 */
public class CampaignAnalyticsDto {

    private Long campaignId;
    private String campaignName;
    private String status;
    private long totalRecipients;
    private long sentCount;
    private long deliveredCount;
    private long failedCount;
    private long pendingCount;
    private long cancelledCount;
    private double deliveryRate;
    private double bounceRate;
    private double openRate;
    private double clickRate;
    private double complaintRate;
    private double unsubscribeRate;

    public CampaignAnalyticsDto() {}

    public CampaignAnalyticsDto(Long campaignId, String campaignName, String status, long totalRecipients,
                                long sentCount, long deliveredCount, long failedCount, long pendingCount,
                                long cancelledCount, double deliveryRate, double bounceRate, double openRate,
                                double clickRate, double complaintRate, double unsubscribeRate) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.status = status;
        this.totalRecipients = totalRecipients;
        this.sentCount = sentCount;
        this.deliveredCount = deliveredCount;
        this.failedCount = failedCount;
        this.pendingCount = pendingCount;
        this.cancelledCount = cancelledCount;
        this.deliveryRate = deliveryRate;
        this.bounceRate = bounceRate;
        this.openRate = openRate;
        this.clickRate = clickRate;
        this.complaintRate = complaintRate;
        this.unsubscribeRate = unsubscribeRate;
    }

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getTotalRecipients() { return totalRecipients; }
    public void setTotalRecipients(long totalRecipients) { this.totalRecipients = totalRecipients; }
    public long getSentCount() { return sentCount; }
    public void setSentCount(long sentCount) { this.sentCount = sentCount; }
    public long getDeliveredCount() { return deliveredCount; }
    public void setDeliveredCount(long deliveredCount) { this.deliveredCount = deliveredCount; }
    public long getFailedCount() { return failedCount; }
    public void setFailedCount(long failedCount) { this.failedCount = failedCount; }
    public long getPendingCount() { return pendingCount; }
    public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }
    public long getCancelledCount() { return cancelledCount; }
    public void setCancelledCount(long cancelledCount) { this.cancelledCount = cancelledCount; }
    public double getDeliveryRate() { return deliveryRate; }
    public void setDeliveryRate(double deliveryRate) { this.deliveryRate = deliveryRate; }
    public double getBounceRate() { return bounceRate; }
    public void setBounceRate(double bounceRate) { this.bounceRate = bounceRate; }
    public double getOpenRate() { return openRate; }
    public void setOpenRate(double openRate) { this.openRate = openRate; }
    public double getClickRate() { return clickRate; }
    public void setClickRate(double clickRate) { this.clickRate = clickRate; }
    public double getComplaintRate() { return complaintRate; }
    public void setComplaintRate(double complaintRate) { this.complaintRate = complaintRate; }
    public double getUnsubscribeRate() { return unsubscribeRate; }
    public void setUnsubscribeRate(double unsubscribeRate) { this.unsubscribeRate = unsubscribeRate; }

    public static CampaignAnalyticsDtoBuilder builder() { return new CampaignAnalyticsDtoBuilder(); }

    public static class CampaignAnalyticsDtoBuilder {
        private Long campaignId;
        private String campaignName;
        private String status;
        private long totalRecipients;
        private long sentCount;
        private long deliveredCount;
        private long failedCount;
        private long pendingCount;
        private long cancelledCount;
        private double deliveryRate;
        private double bounceRate;
        private double openRate;
        private double clickRate;
        private double complaintRate;
        private double unsubscribeRate;

        CampaignAnalyticsDtoBuilder() {}

        public CampaignAnalyticsDtoBuilder campaignId(Long campaignId) { this.campaignId = campaignId; return this; }
        public CampaignAnalyticsDtoBuilder campaignName(String campaignName) { this.campaignName = campaignName; return this; }
        public CampaignAnalyticsDtoBuilder status(String status) { this.status = status; return this; }
        public CampaignAnalyticsDtoBuilder totalRecipients(long totalRecipients) { this.totalRecipients = totalRecipients; return this; }
        public CampaignAnalyticsDtoBuilder sentCount(long sentCount) { this.sentCount = sentCount; return this; }
        public CampaignAnalyticsDtoBuilder deliveredCount(long deliveredCount) { this.deliveredCount = deliveredCount; return this; }
        public CampaignAnalyticsDtoBuilder failedCount(long failedCount) { this.failedCount = failedCount; return this; }
        public CampaignAnalyticsDtoBuilder pendingCount(long pendingCount) { this.pendingCount = pendingCount; return this; }
        public CampaignAnalyticsDtoBuilder cancelledCount(long cancelledCount) { this.cancelledCount = cancelledCount; return this; }
        public CampaignAnalyticsDtoBuilder deliveryRate(double deliveryRate) { this.deliveryRate = deliveryRate; return this; }
        public CampaignAnalyticsDtoBuilder bounceRate(double bounceRate) { this.bounceRate = bounceRate; return this; }
        public CampaignAnalyticsDtoBuilder openRate(double openRate) { this.openRate = openRate; return this; }
        public CampaignAnalyticsDtoBuilder clickRate(double clickRate) { this.clickRate = clickRate; return this; }
        public CampaignAnalyticsDtoBuilder complaintRate(double complaintRate) { this.complaintRate = complaintRate; return this; }
        public CampaignAnalyticsDtoBuilder unsubscribeRate(double unsubscribeRate) { this.unsubscribeRate = unsubscribeRate; return this; }

        public CampaignAnalyticsDto build() {
            return new CampaignAnalyticsDto(campaignId, campaignName, status, totalRecipients, sentCount,
                    deliveredCount, failedCount, pendingCount, cancelledCount, deliveryRate, bounceRate,
                    openRate, clickRate, complaintRate, unsubscribeRate);
        }
    }
}
