package com.mailally.campaign.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Campaign.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class CampaignDto {

    private Long id;
    private Long organizationId;
    private String name;
    private String type;
    private String status;
    private String senderName;
    private String senderEmail;
    private Long templateId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CampaignDto() {}

    public CampaignDto(Long id, Long organizationId, String name, String type, String status, String senderName, String senderEmail, Long templateId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.templateId = templateId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    public Long getTemplateId() { return templateId; }
    public void setTemplateId(Long templateId) { this.templateId = templateId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static CampaignDtoBuilder builder() { return new CampaignDtoBuilder(); }

    public static class CampaignDtoBuilder {
        private Long id;
        private Long organizationId;
        private String name;
        private String type;
        private String status;
        private String senderName;
        private String senderEmail;
        private Long templateId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        CampaignDtoBuilder() {}

        public CampaignDtoBuilder id(Long id) { this.id = id; return this; }
        public CampaignDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public CampaignDtoBuilder name(String name) { this.name = name; return this; }
        public CampaignDtoBuilder type(String type) { this.type = type; return this; }
        public CampaignDtoBuilder status(String status) { this.status = status; return this; }
        public CampaignDtoBuilder senderName(String senderName) { this.senderName = senderName; return this; }
        public CampaignDtoBuilder senderEmail(String senderEmail) { this.senderEmail = senderEmail; return this; }
        public CampaignDtoBuilder templateId(Long templateId) { this.templateId = templateId; return this; }
        public CampaignDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CampaignDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public CampaignDto build() {
            return new CampaignDto(id, organizationId, name, type, status, senderName, senderEmail, templateId, createdAt, updatedAt);
        }
    }
}
