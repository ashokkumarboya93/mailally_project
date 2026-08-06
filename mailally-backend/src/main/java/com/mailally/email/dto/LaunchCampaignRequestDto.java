package com.mailally.email.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for launching an email campaign dispatch.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LaunchCampaignRequestDto {

    @NotNull(message = "Campaign ID is required")
    private Long campaignId;

    private Integer batchSize;
    private String overrideProvider;
    private String priority;

    public LaunchCampaignRequestDto() {}

    public LaunchCampaignRequestDto(Long campaignId) {
        this.campaignId = campaignId;
    }

    public LaunchCampaignRequestDto(Long campaignId, Integer batchSize) {
        this.campaignId = campaignId;
        this.batchSize = batchSize;
    }

    public LaunchCampaignRequestDto(Long campaignId, Integer batchSize, String overrideProvider, String priority) {
        this.campaignId = campaignId;
        this.batchSize = batchSize;
        this.overrideProvider = overrideProvider;
        this.priority = priority;
    }

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public Integer getBatchSize() { return batchSize; }
    public void setBatchSize(Integer batchSize) { this.batchSize = batchSize; }
    public String getOverrideProvider() { return overrideProvider; }
    public void setOverrideProvider(String overrideProvider) { this.overrideProvider = overrideProvider; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
