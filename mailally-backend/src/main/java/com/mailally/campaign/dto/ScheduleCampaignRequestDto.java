package com.mailally.campaign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO for scheduling a Campaign execution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleCampaignRequestDto {

    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledAt;

    public ScheduleCampaignRequestDto() {}

    public ScheduleCampaignRequestDto(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
}
