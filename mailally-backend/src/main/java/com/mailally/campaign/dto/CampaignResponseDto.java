package com.mailally.campaign.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a Campaign.
 */
public class CampaignResponseDto {

    private Long id;
    private Long organizationId;
    private Long templateId;
    private String templateName;
    private Long segmentId;
    private String segmentName;
    private String name;
    private String subject;
    private String senderName;
    private String senderEmail;
    private String replyTo;
    private String status;
    private LocalDateTime scheduledAt;
    private Integer totalRecipients;
    private Integer sentCount;
    private Integer failedCount;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CampaignResponseDto() {}

    public CampaignResponseDto(Long id, Long organizationId, Long templateId, String templateName,
                                Long segmentId, String segmentName, String name, String subject,
                                String senderName, String senderEmail, String replyTo, String status,
                                LocalDateTime scheduledAt, Integer totalRecipients, Integer sentCount,
                                Integer failedCount, Long createdBy, Long updatedBy,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.templateId = templateId;
        this.templateName = templateName;
        this.segmentId = segmentId;
        this.segmentName = segmentName;
        this.name = name;
        this.subject = subject;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.replyTo = replyTo;
        this.status = status;
        this.scheduledAt = scheduledAt;
        this.totalRecipients = totalRecipients;
        this.sentCount = sentCount;
        this.failedCount = failedCount;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public Long getSegmentId() { return segmentId; }
    public void setSegmentId(Long segmentId) { this.segmentId = segmentId; }
    public String getSegmentName() { return segmentName; }
    public void setSegmentName(String segmentName) { this.segmentName = segmentName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    public String getReplyTo() { return replyTo; }
    public void setReplyTo(String replyTo) { this.replyTo = replyTo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public Integer getTotalRecipients() { return totalRecipients; }
    public void setTotalRecipients(Integer totalRecipients) { this.totalRecipients = totalRecipients; }
    public Integer getSentCount() { return sentCount; }
    public void setSentCount(Integer sentCount) { this.sentCount = sentCount; }
    public Integer getFailedCount() { return failedCount; }
    public void setFailedCount(Integer failedCount) { this.failedCount = failedCount; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static CampaignResponseDtoBuilder builder() { return new CampaignResponseDtoBuilder(); }

    public static class CampaignResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private Long templateId;
        private String templateName;
        private Long segmentId;
        private String segmentName;
        private String name;
        private String subject;
        private String senderName;
        private String senderEmail;
        private String replyTo;
        private String status;
        private LocalDateTime scheduledAt;
        private Integer totalRecipients;
        private Integer sentCount;
        private Integer failedCount;
        private Long createdBy;
        private Long updatedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        CampaignResponseDtoBuilder() {}

        public CampaignResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public CampaignResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public CampaignResponseDtoBuilder templateId(Long templateId) { this.templateId = templateId; return this; }
        public CampaignResponseDtoBuilder templateName(String templateName) { this.templateName = templateName; return this; }
        public CampaignResponseDtoBuilder segmentId(Long segmentId) { this.segmentId = segmentId; return this; }
        public CampaignResponseDtoBuilder segmentName(String segmentName) { this.segmentName = segmentName; return this; }
        public CampaignResponseDtoBuilder name(String name) { this.name = name; return this; }
        public CampaignResponseDtoBuilder subject(String subject) { this.subject = subject; return this; }
        public CampaignResponseDtoBuilder senderName(String senderName) { this.senderName = senderName; return this; }
        public CampaignResponseDtoBuilder senderEmail(String senderEmail) { this.senderEmail = senderEmail; return this; }
        public CampaignResponseDtoBuilder replyTo(String replyTo) { this.replyTo = replyTo; return this; }
        public CampaignResponseDtoBuilder status(String status) { this.status = status; return this; }
        public CampaignResponseDtoBuilder scheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; return this; }
        public CampaignResponseDtoBuilder totalRecipients(Integer totalRecipients) { this.totalRecipients = totalRecipients; return this; }
        public CampaignResponseDtoBuilder sentCount(Integer sentCount) { this.sentCount = sentCount; return this; }
        public CampaignResponseDtoBuilder failedCount(Integer failedCount) { this.failedCount = failedCount; return this; }
        public CampaignResponseDtoBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public CampaignResponseDtoBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public CampaignResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CampaignResponseDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public CampaignResponseDto build() {
            return new CampaignResponseDto(id, organizationId, templateId, templateName, segmentId, segmentName,
                    name, subject, senderName, senderEmail, replyTo, status, scheduledAt, totalRecipients,
                    sentCount, failedCount, createdBy, updatedBy, createdAt, updatedAt);
        }
    }
}
