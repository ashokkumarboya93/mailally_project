package com.mailally.scheduler.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a Scheduler execution record.
 */
public class SchedulerResponseDto {

    private Long id;
    private Long organizationId;
    private Long campaignId;
    private String campaignName;
    private String executionType;
    private String status;
    private LocalDateTime scheduledTime;
    private LocalDateTime executedTime;
    private String errorMessage;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SchedulerResponseDto() {}

    public SchedulerResponseDto(Long id, Long organizationId, Long campaignId, String campaignName,
                                String executionType, String status, LocalDateTime scheduledTime,
                                LocalDateTime executedTime, String errorMessage, Long createdBy, Long updatedBy,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.executionType = executionType;
        this.status = status;
        this.scheduledTime = scheduledTime;
        this.executedTime = executedTime;
        this.errorMessage = errorMessage;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }
    public String getExecutionType() { return executionType; }
    public void setExecutionType(String executionType) { this.executionType = executionType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    public LocalDateTime getExecutedTime() { return executedTime; }
    public void setExecutedTime(LocalDateTime executedTime) { this.executedTime = executedTime; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static SchedulerResponseDtoBuilder builder() { return new SchedulerResponseDtoBuilder(); }

    public static class SchedulerResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private Long campaignId;
        private String campaignName;
        private String executionType;
        private String status;
        private LocalDateTime scheduledTime;
        private LocalDateTime executedTime;
        private String errorMessage;
        private Long createdBy;
        private Long updatedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        SchedulerResponseDtoBuilder() {}

        public SchedulerResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public SchedulerResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public SchedulerResponseDtoBuilder campaignId(Long campaignId) { this.campaignId = campaignId; return this; }
        public SchedulerResponseDtoBuilder campaignName(String campaignName) { this.campaignName = campaignName; return this; }
        public SchedulerResponseDtoBuilder executionType(String executionType) { this.executionType = executionType; return this; }
        public SchedulerResponseDtoBuilder status(String status) { this.status = status; return this; }
        public SchedulerResponseDtoBuilder scheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; return this; }
        public SchedulerResponseDtoBuilder executedTime(LocalDateTime executedTime) { this.executedTime = executedTime; return this; }
        public SchedulerResponseDtoBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public SchedulerResponseDtoBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public SchedulerResponseDtoBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public SchedulerResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SchedulerResponseDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public SchedulerResponseDto build() {
            return new SchedulerResponseDto(id, organizationId, campaignId, campaignName, executionType, status,
                    scheduledTime, executedTime, errorMessage, createdBy, updatedBy, createdAt, updatedAt);
        }
    }
}
