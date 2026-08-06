package com.mailally.email.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing an email delivery log record (`Email.java`).
 */
public class EmailLogResponseDto {

    private Long id;
    private Long organizationId;
    private Long campaignId;
    private String campaignName;
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String provider;
    private String status;
    private String responseId;
    private String errorMessage;
    private Integer retryCount;
    private Integer maxRetries;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime openedAt;
    private LocalDateTime clickedAt;
    private LocalDateTime bouncedAt;
    private LocalDateTime failedAt;
    private LocalDateTime createdAt;

    public EmailLogResponseDto() {}

    public EmailLogResponseDto(Long id, Long organizationId, Long campaignId, String campaignName,
                               String recipientEmail, String recipientName, String subject, String provider,
                               String status, String responseId, String errorMessage, Integer retryCount,
                               Integer maxRetries, LocalDateTime sentAt, LocalDateTime deliveredAt,
                               LocalDateTime openedAt, LocalDateTime clickedAt, LocalDateTime bouncedAt,
                               LocalDateTime failedAt, LocalDateTime createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
        this.subject = subject;
        this.provider = provider;
        this.status = status;
        this.responseId = responseId;
        this.errorMessage = errorMessage;
        this.retryCount = retryCount;
        this.maxRetries = maxRetries;
        this.sentAt = sentAt;
        this.deliveredAt = deliveredAt;
        this.openedAt = openedAt;
        this.clickedAt = clickedAt;
        this.bouncedAt = bouncedAt;
        this.failedAt = failedAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResponseId() { return responseId; }
    public void setResponseId(String responseId) { this.responseId = responseId; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Integer getMaxRetries() { return maxRetries; }
    public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public void setOpenedAt(LocalDateTime openedAt) { this.openedAt = openedAt; }
    public LocalDateTime getClickedAt() { return clickedAt; }
    public void setClickedAt(LocalDateTime clickedAt) { this.clickedAt = clickedAt; }
    public LocalDateTime getBouncedAt() { return bouncedAt; }
    public void setBouncedAt(LocalDateTime bouncedAt) { this.bouncedAt = bouncedAt; }
    public LocalDateTime getFailedAt() { return failedAt; }
    public void setFailedAt(LocalDateTime failedAt) { this.failedAt = failedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static EmailLogResponseDtoBuilder builder() { return new EmailLogResponseDtoBuilder(); }

    public static class EmailLogResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private Long campaignId;
        private String campaignName;
        private String recipientEmail;
        private String recipientName;
        private String subject;
        private String provider;
        private String status;
        private String responseId;
        private String errorMessage;
        private Integer retryCount;
        private Integer maxRetries;
        private LocalDateTime sentAt;
        private LocalDateTime deliveredAt;
        private LocalDateTime openedAt;
        private LocalDateTime clickedAt;
        private LocalDateTime bouncedAt;
        private LocalDateTime failedAt;
        private LocalDateTime createdAt;

        EmailLogResponseDtoBuilder() {}

        public EmailLogResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public EmailLogResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public EmailLogResponseDtoBuilder campaignId(Long campaignId) { this.campaignId = campaignId; return this; }
        public EmailLogResponseDtoBuilder campaignName(String campaignName) { this.campaignName = campaignName; return this; }
        public EmailLogResponseDtoBuilder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public EmailLogResponseDtoBuilder recipientName(String recipientName) { this.recipientName = recipientName; return this; }
        public EmailLogResponseDtoBuilder subject(String subject) { this.subject = subject; return this; }
        public EmailLogResponseDtoBuilder provider(String provider) { this.provider = provider; return this; }
        public EmailLogResponseDtoBuilder status(String status) { this.status = status; return this; }
        public EmailLogResponseDtoBuilder responseId(String responseId) { this.responseId = responseId; return this; }
        public EmailLogResponseDtoBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public EmailLogResponseDtoBuilder retryCount(Integer retryCount) { this.retryCount = retryCount; return this; }
        public EmailLogResponseDtoBuilder maxRetries(Integer maxRetries) { this.maxRetries = maxRetries; return this; }
        public EmailLogResponseDtoBuilder sentAt(LocalDateTime sentAt) { this.sentAt = sentAt; return this; }
        public EmailLogResponseDtoBuilder deliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; return this; }
        public EmailLogResponseDtoBuilder openedAt(LocalDateTime openedAt) { this.openedAt = openedAt; return this; }
        public EmailLogResponseDtoBuilder clickedAt(LocalDateTime clickedAt) { this.clickedAt = clickedAt; return this; }
        public EmailLogResponseDtoBuilder bouncedAt(LocalDateTime bouncedAt) { this.bouncedAt = bouncedAt; return this; }
        public EmailLogResponseDtoBuilder failedAt(LocalDateTime failedAt) { this.failedAt = failedAt; return this; }
        public EmailLogResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public EmailLogResponseDto build() {
            return new EmailLogResponseDto(id, organizationId, campaignId, campaignName, recipientEmail, recipientName,
                    subject, provider, status, responseId, errorMessage, retryCount, maxRetries, sentAt, deliveredAt,
                    openedAt, clickedAt, bouncedAt, failedAt, createdAt);
        }
    }
}
