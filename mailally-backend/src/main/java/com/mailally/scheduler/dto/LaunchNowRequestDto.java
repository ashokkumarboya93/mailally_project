package com.mailally.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for requesting immediate campaign launch through the Scheduler.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LaunchNowRequestDto {

    @NotNull(message = "Campaign ID is required")
    private Long campaignId;

    public LaunchNowRequestDto() {}

    public LaunchNowRequestDto(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
}
