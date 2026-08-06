package com.mailally.scheduler.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Scheduler.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class SchedulerDto {

    private Long id;
    private Long campaignId;
    private String campaignName;
    private LocalDateTime scheduledAt;
    private String status;

    public SchedulerDto() {}

    public SchedulerDto(Long id, Long campaignId, String campaignName, LocalDateTime scheduledAt, String status) {
        this.id = id;
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.scheduledAt = scheduledAt;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static SchedulerDtoBuilder builder() { return new SchedulerDtoBuilder(); }

    public static class SchedulerDtoBuilder {
        private Long id;
        private Long campaignId;
        private String campaignName;
        private LocalDateTime scheduledAt;
        private String status;

        SchedulerDtoBuilder() {}

        public SchedulerDtoBuilder id(Long id) { this.id = id; return this; }
        public SchedulerDtoBuilder campaignId(Long campaignId) { this.campaignId = campaignId; return this; }
        public SchedulerDtoBuilder campaignName(String campaignName) { this.campaignName = campaignName; return this; }
        public SchedulerDtoBuilder scheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; return this; }
        public SchedulerDtoBuilder status(String status) { this.status = status; return this; }

        public SchedulerDto build() {
            return new SchedulerDto(id, campaignId, campaignName, scheduledAt, status);
        }
    }
}
