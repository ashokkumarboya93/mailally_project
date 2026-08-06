package com.mailally.email.dto;

/**
 * Progress status metric for an active or completed campaign launch.
 */
public class CampaignProgressDto {

    private Long campaignId;
    private String campaignName;
    private String campaignStatus;
    private int totalRecipients;
    private int sentCount;
    private int failedCount;
    private int pendingCount;
    private double progressPercentage;

    public CampaignProgressDto() {}

    public CampaignProgressDto(Long campaignId, String campaignName, String campaignStatus, int totalRecipients,
                               int sentCount, int failedCount, int pendingCount, double progressPercentage) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.campaignStatus = campaignStatus;
        this.totalRecipients = totalRecipients;
        this.sentCount = sentCount;
        this.failedCount = failedCount;
        this.pendingCount = pendingCount;
        this.progressPercentage = progressPercentage;
    }

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }
    public String getCampaignStatus() { return campaignStatus; }
    public void setCampaignStatus(String campaignStatus) { this.campaignStatus = campaignStatus; }
    public int getTotalRecipients() { return totalRecipients; }
    public void setTotalRecipients(int totalRecipients) { this.totalRecipients = totalRecipients; }
    public int getSentCount() { return sentCount; }
    public void setSentCount(int sentCount) { this.sentCount = sentCount; }
    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
    public int getPendingCount() { return pendingCount; }
    public void setPendingCount(int pendingCount) { this.pendingCount = pendingCount; }
    public double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(double progressPercentage) { this.progressPercentage = progressPercentage; }

    public static CampaignProgressDtoBuilder builder() { return new CampaignProgressDtoBuilder(); }

    public static class CampaignProgressDtoBuilder {
        private Long campaignId;
        private String campaignName;
        private String campaignStatus;
        private int totalRecipients;
        private int sentCount;
        private int failedCount;
        private int pendingCount;
        private double progressPercentage;

        CampaignProgressDtoBuilder() {}

        public CampaignProgressDtoBuilder campaignId(Long campaignId) { this.campaignId = campaignId; return this; }
        public CampaignProgressDtoBuilder campaignName(String campaignName) { this.campaignName = campaignName; return this; }
        public CampaignProgressDtoBuilder campaignStatus(String campaignStatus) { this.campaignStatus = campaignStatus; return this; }
        public CampaignProgressDtoBuilder totalRecipients(int totalRecipients) { this.totalRecipients = totalRecipients; return this; }
        public CampaignProgressDtoBuilder sentCount(int sentCount) { this.sentCount = sentCount; return this; }
        public CampaignProgressDtoBuilder failedCount(int failedCount) { this.failedCount = failedCount; return this; }
        public CampaignProgressDtoBuilder pendingCount(int pendingCount) { this.pendingCount = pendingCount; return this; }
        public CampaignProgressDtoBuilder progressPercentage(double progressPercentage) { this.progressPercentage = progressPercentage; return this; }

        public CampaignProgressDto build() {
            return new CampaignProgressDto(campaignId, campaignName, campaignStatus, totalRecipients, sentCount,
                    failedCount, pendingCount, progressPercentage);
        }
    }
}
