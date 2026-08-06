package com.mailally.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO for requesting a scheduled campaign execution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleCampaignRequestDto {

    @NotNull(message = "Campaign ID is required")
    private Long campaignId;

    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledTime;

    private String executionType; // IMMEDIATE or SCHEDULED

    public ScheduleCampaignRequestDto() {}

    public ScheduleCampaignRequestDto(Long campaignId, LocalDateTime scheduledTime, String executionType) {
        this.campaignId = campaignId;
        this.scheduledTime = scheduledTime;
        this.executionType = executionType;
    }

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getExecutionType() { return executionType; }
    public void setExecutionType(String executionType) { this.executionType = executionType; }
}
