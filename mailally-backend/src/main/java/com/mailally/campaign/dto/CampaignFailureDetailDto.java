package com.mailally.campaign.dto;

import java.time.LocalDateTime;

public class CampaignFailureDetailDto {
    private Long recipientId;
    private String recipientEmail;
    private String recipientName;
    private String status;
    private String failureReason;
    private Integer retryCount;
    private LocalDateTime failedAt;

    public CampaignFailureDetailDto() {}

    public CampaignFailureDetailDto(Long recipientId, String recipientEmail, String recipientName, String status, String failureReason, Integer retryCount, LocalDateTime failedAt) {
        this.recipientId = recipientId;
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
        this.status = status;
        this.failureReason = failureReason;
        this.retryCount = retryCount;
        this.failedAt = failedAt;
    }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public LocalDateTime getFailedAt() { return failedAt; }
    public void setFailedAt(LocalDateTime failedAt) { this.failedAt = failedAt; }
}
