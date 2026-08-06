package com.mailally.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO for rescheduling an existing scheduled execution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RescheduleCampaignRequestDto {

    @NotNull(message = "New scheduled time is required")
    private LocalDateTime newScheduledTime;

    public RescheduleCampaignRequestDto() {}

    public RescheduleCampaignRequestDto(LocalDateTime newScheduledTime) {
        this.newScheduledTime = newScheduledTime;
    }

    public LocalDateTime getNewScheduledTime() { return newScheduledTime; }
    public void setNewScheduledTime(LocalDateTime newScheduledTime) { this.newScheduledTime = newScheduledTime; }
}
