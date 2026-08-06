package com.mailally.email.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Email.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class EmailDto {

    private Long id;
    private Long organizationId;
    private Long campaignId;
    private String recipientEmail;
    private String subject;
    private String body;
    private String status;
    private LocalDateTime sentAt;

    public EmailDto() {}

    public EmailDto(Long id, Long organizationId, Long campaignId, String recipientEmail, String subject, String body, String status, LocalDateTime sentAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.campaignId = campaignId;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.status = status;
        this.sentAt = sentAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public static EmailDtoBuilder builder() { return new EmailDtoBuilder(); }

    public static class EmailDtoBuilder {
        private Long id;
        private Long organizationId;
        private Long campaignId;
        private String recipientEmail;
        private String subject;
        private String body;
        private String status;
        private LocalDateTime sentAt;

        EmailDtoBuilder() {}

        public EmailDtoBuilder id(Long id) { this.id = id; return this; }
        public EmailDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public EmailDtoBuilder campaignId(Long campaignId) { this.campaignId = campaignId; return this; }
        public EmailDtoBuilder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public EmailDtoBuilder subject(String subject) { this.subject = subject; return this; }
        public EmailDtoBuilder body(String body) { this.body = body; return this; }
        public EmailDtoBuilder status(String status) { this.status = status; return this; }
        public EmailDtoBuilder sentAt(LocalDateTime sentAt) { this.sentAt = sentAt; return this; }

        public EmailDto build() {
            return new EmailDto(id, organizationId, campaignId, recipientEmail, subject, body, status, sentAt);
        }
    }
}
