package com.mailally.email.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing an email queue record (`EmailQueue.java`).
 */
public class EmailQueueResponseDto {

    private Long id;
    private Long organizationId;
    private Long campaignId;
    private String recipientEmail;
    private String recipientName;
    private String personalizedSubject;
    private String provider;
    private String status;
    private Integer retryCount;
    private Integer maxRetries;
    private String failureReason;
    private Integer batchNumber;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    public EmailQueueResponseDto() {}

    public EmailQueueResponseDto(Long id, Long organizationId, Long campaignId, String recipientEmail,
                                String recipientName, String personalizedSubject, String provider, String status,
                                Integer retryCount, Integer maxRetries, String failureReason, Integer batchNumber,
                                LocalDateTime createdAt, LocalDateTime processedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.campaignId = campaignId;
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
        this.personalizedSubject = personalizedSubject;
        this.provider = provider;
        this.status = status;
        this.retryCount = retryCount;
        this.maxRetries = maxRetries;
        this.failureReason = failureReason;
        this.batchNumber = batchNumber;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getPersonalizedSubject() { return personalizedSubject; }
    public void setPersonalizedSubject(String personalizedSubject) { this.personalizedSubject = personalizedSubject; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Integer getMaxRetries() { return maxRetries; }
    public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public Integer getBatchNumber() { return batchNumber; }
    public void setBatchNumber(Integer batchNumber) { this.batchNumber = batchNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public static EmailQueueResponseDtoBuilder builder() { return new EmailQueueResponseDtoBuilder(); }

    public static class EmailQueueResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private Long campaignId;
        private String recipientEmail;
        private String recipientName;
        private String personalizedSubject;
        private String provider;
        private String status;
        private Integer retryCount;
        private Integer maxRetries;
        private String failureReason;
        private Integer batchNumber;
        private LocalDateTime createdAt;
        private LocalDateTime processedAt;

        EmailQueueResponseDtoBuilder() {}

        public EmailQueueResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public EmailQueueResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public EmailQueueResponseDtoBuilder campaignId(Long campaignId) { this.campaignId = campaignId; return this; }
        public EmailQueueResponseDtoBuilder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public EmailQueueResponseDtoBuilder recipientName(String recipientName) { this.recipientName = recipientName; return this; }
        public EmailQueueResponseDtoBuilder personalizedSubject(String personalizedSubject) { this.personalizedSubject = personalizedSubject; return this; }
        public EmailQueueResponseDtoBuilder provider(String provider) { this.provider = provider; return this; }
        public EmailQueueResponseDtoBuilder status(String status) { this.status = status; return this; }
        public EmailQueueResponseDtoBuilder retryCount(Integer retryCount) { this.retryCount = retryCount; return this; }
        public EmailQueueResponseDtoBuilder maxRetries(Integer maxRetries) { this.maxRetries = maxRetries; return this; }
        public EmailQueueResponseDtoBuilder failureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public EmailQueueResponseDtoBuilder batchNumber(Integer batchNumber) { this.batchNumber = batchNumber; return this; }
        public EmailQueueResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public EmailQueueResponseDtoBuilder processedAt(LocalDateTime processedAt) { this.processedAt = processedAt; return this; }

        public EmailQueueResponseDto build() {
            return new EmailQueueResponseDto(id, organizationId, campaignId, recipientEmail, recipientName,
                    personalizedSubject, provider, status, retryCount, maxRetries, failureReason, batchNumber,
                    createdAt, processedAt);
        }
    }
}
