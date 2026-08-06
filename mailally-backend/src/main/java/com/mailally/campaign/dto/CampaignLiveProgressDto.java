package com.mailally.campaign.dto;

import java.util.ArrayList;
import java.util.List;

public class CampaignLiveProgressDto {
    private Long campaignId;
    private String status = "DRAFT";
    private Integer progressPercentage = 0;
    private Long totalRecipients = 0L;
    private Long queuedCount = 0L;
    private Long sendingCount = 0L;
    private Long sentCount = 0L;
    private Long deliveredCount = 0L;
    private Long openedCount = 0L;
    private Long clickedCount = 0L;
    private Long failedCount = 0L;
    private Long retryingCount = 0L;
    private Long authFailures = 0L;
    private Long connectionFailures = 0L;
    private Long invalidRecipientFailures = 0L;
    private Long templateFailures = 0L;
    private Long providerErrors = 0L;
    private Long retryCount = 0L;
    private String activeProvider = "SMTP";
    private String providerStatus = "Healthy";
    private Integer emailsPerMinute = 180;
    private Integer remainingSeconds = 0;
    private List<WorkerThreadStatusDto> workers = new ArrayList<>();
    private List<String> recentActivity = new ArrayList<>();

    public CampaignLiveProgressDto() {}

    public static class WorkerThreadStatusDto {
        private String workerId;
        private String status;
        private Integer processedCount;

        public WorkerThreadStatusDto(String workerId, String status, Integer processedCount) {
            this.workerId = workerId;
            this.status = status;
            this.processedCount = processedCount;
        }

        public String getWorkerId() { return workerId; }
        public String getStatus() { return status; }
        public Integer getProcessedCount() { return processedCount; }
    }

    // Getters and Setters
    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }

    public Long getTotalRecipients() { return totalRecipients; }
    public void setTotalRecipients(Long totalRecipients) { this.totalRecipients = totalRecipients; }

    public Long getQueuedCount() { return queuedCount; }
    public void setQueuedCount(Long queuedCount) { this.queuedCount = queuedCount; }

    public Long getSendingCount() { return sendingCount; }
    public void setSendingCount(Long sendingCount) { this.sendingCount = sendingCount; }

    public Long getSentCount() { return sentCount; }
    public void setSentCount(Long sentCount) { this.sentCount = sentCount; }

    public Long getDeliveredCount() { return deliveredCount; }
    public void setDeliveredCount(Long deliveredCount) { this.deliveredCount = deliveredCount; }

    public Long getOpenedCount() { return openedCount; }
    public void setOpenedCount(Long openedCount) { this.openedCount = openedCount; }

    public Long getClickedCount() { return clickedCount; }
    public void setClickedCount(Long clickedCount) { this.clickedCount = clickedCount; }

    public Long getFailedCount() { return failedCount; }
    public void setFailedCount(Long failedCount) { this.failedCount = failedCount; }

    public Long getRetryingCount() { return retryingCount; }
    public void setRetryingCount(Long retryingCount) { this.retryingCount = retryingCount; }

    public Long getAuthFailures() { return authFailures; }
    public void setAuthFailures(Long authFailures) { this.authFailures = authFailures; }

    public Long getConnectionFailures() { return connectionFailures; }
    public void setConnectionFailures(Long connectionFailures) { this.connectionFailures = connectionFailures; }

    public Long getInvalidRecipientFailures() { return invalidRecipientFailures; }
    public void setInvalidRecipientFailures(Long invalidRecipientFailures) { this.invalidRecipientFailures = invalidRecipientFailures; }

    public Long getTemplateFailures() { return templateFailures; }
    public void setTemplateFailures(Long templateFailures) { this.templateFailures = templateFailures; }

    public Long getProviderErrors() { return providerErrors; }
    public void setProviderErrors(Long providerErrors) { this.providerErrors = providerErrors; }

    public Long getRetryCount() { return retryCount; }
    public void setRetryCount(Long retryCount) { this.retryCount = retryCount; }

    public String getActiveProvider() { return activeProvider; }
    public void setActiveProvider(String activeProvider) { this.activeProvider = activeProvider; }

    public String getProviderStatus() { return providerStatus; }
    public void setProviderStatus(String providerStatus) { this.providerStatus = providerStatus; }

    public Integer getEmailsPerMinute() { return emailsPerMinute; }
    public void setEmailsPerMinute(Integer emailsPerMinute) { this.emailsPerMinute = emailsPerMinute; }

    public Integer getRemainingSeconds() { return remainingSeconds; }
    public void setRemainingSeconds(Integer remainingSeconds) { this.remainingSeconds = remainingSeconds; }

    public List<WorkerThreadStatusDto> getWorkers() { return workers; }
    public void setWorkers(List<WorkerThreadStatusDto> workers) { this.workers = workers; }

    public List<String> getRecentActivity() { return recentActivity; }
    public void setRecentActivity(List<String> recentActivity) { this.recentActivity = recentActivity; }
}
