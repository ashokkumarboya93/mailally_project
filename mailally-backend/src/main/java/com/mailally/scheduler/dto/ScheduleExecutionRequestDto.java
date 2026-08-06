package com.mailally.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO for requesting a scheduled campaign execution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleExecutionRequestDto {

    @NotNull(message = "Campaign ID is required")
    private Long campaignId;

    private LocalDateTime scheduledTime;

    public ScheduleExecutionRequestDto() {}

    public ScheduleExecutionRequestDto(Long campaignId, LocalDateTime scheduledTime) {
        this.campaignId = campaignId;
        this.scheduledTime = scheduledTime;
    }

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
}
