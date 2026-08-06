package com.mailally.email.entity;

import com.mailally.campaign.entity.Campaign;
import com.mailally.contact.entity.Contact;
import com.mailally.organization.entity.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity representing an email delivery log record.
 * Tracks the full lifecycle of each individual email sent: QUEUED → SENDING → SENT → DELIVERED → OPENED → CLICKED → BOUNCED → FAILED.
 */
@Entity
@Table(name = "emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Contact contact;

    @Column(name = "recipient_email", nullable = false, length = 255)
    private String recipientEmail;

    @Column(name = "recipient_name", length = 200)
    private String recipientName;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "provider", length = 50)
    private String provider;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "response_id", length = 255)
    private String responseId;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "max_retries")
    private Integer maxRetries;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "opened_at")
    private LocalDateTime openedAt;

    @Column(name = "clicked_at")
    private LocalDateTime clickedAt;

    @Column(name = "bounced_at")
    private LocalDateTime bouncedAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Email() {}

    public Email(Long id, Organization organization, Campaign campaign, Contact contact, String recipientEmail, String recipientName,
                 String subject, String provider, String status, String responseId, String errorMessage,
                 Integer retryCount, Integer maxRetries, LocalDateTime sentAt, LocalDateTime deliveredAt,
                 LocalDateTime openedAt, LocalDateTime clickedAt, LocalDateTime bouncedAt, LocalDateTime failedAt,
                 Long createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organization = organization;
        this.campaign = campaign;
        this.contact = contact;
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
        this.subject = subject;
        this.provider = provider;
        this.status = status;
        this.responseId = responseId;
        this.errorMessage = errorMessage;
        this.retryCount = retryCount;
        this.maxRetries = maxRetries;
        this.sentAt = sentAt;
        this.deliveredAt = deliveredAt;
        this.openedAt = openedAt;
        this.clickedAt = clickedAt;
        this.bouncedAt = bouncedAt;
        this.failedAt = failedAt;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }
    public Contact getContact() { return contact; }
    public void setContact(Contact contact) { this.contact = contact; }
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResponseId() { return responseId; }
    public void setResponseId(String responseId) { this.responseId = responseId; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Integer getMaxRetries() { return maxRetries; }
    public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public void setOpenedAt(LocalDateTime openedAt) { this.openedAt = openedAt; }
    public LocalDateTime getClickedAt() { return clickedAt; }
    public void setClickedAt(LocalDateTime clickedAt) { this.clickedAt = clickedAt; }
    public LocalDateTime getBouncedAt() { return bouncedAt; }
    public void setBouncedAt(LocalDateTime bouncedAt) { this.bouncedAt = bouncedAt; }
    public LocalDateTime getFailedAt() { return failedAt; }
    public void setFailedAt(LocalDateTime failedAt) { this.failedAt = failedAt; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = "QUEUED";
        if (this.retryCount == null) this.retryCount = 0;
        if (this.maxRetries == null) this.maxRetries = 3;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static EmailBuilder builder() { return new EmailBuilder(); }

    public static class EmailBuilder {
        private Long id;
        private Organization organization;
        private Campaign campaign;
        private Contact contact;
        private String recipientEmail;
        private String recipientName;
        private String subject;
        private String provider;
        private String status;
        private String responseId;
        private String errorMessage;
        private Integer retryCount;
        private Integer maxRetries;
        private LocalDateTime sentAt;
        private LocalDateTime deliveredAt;
        private LocalDateTime openedAt;
        private LocalDateTime clickedAt;
        private LocalDateTime bouncedAt;
        private LocalDateTime failedAt;
        private Long createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        EmailBuilder() {}

        public EmailBuilder id(Long id) { this.id = id; return this; }
        public EmailBuilder organization(Organization organization) { this.organization = organization; return this; }
        public EmailBuilder campaign(Campaign campaign) { this.campaign = campaign; return this; }
        public EmailBuilder contact(Contact contact) { this.contact = contact; return this; }
        public EmailBuilder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public EmailBuilder recipientName(String recipientName) { this.recipientName = recipientName; return this; }
        public EmailBuilder subject(String subject) { this.subject = subject; return this; }
        public EmailBuilder provider(String provider) { this.provider = provider; return this; }
        public EmailBuilder status(String status) { this.status = status; return this; }
        public EmailBuilder responseId(String responseId) { this.responseId = responseId; return this; }
        public EmailBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public EmailBuilder retryCount(Integer retryCount) { this.retryCount = retryCount; return this; }
        public EmailBuilder maxRetries(Integer maxRetries) { this.maxRetries = maxRetries; return this; }
        public EmailBuilder sentAt(LocalDateTime sentAt) { this.sentAt = sentAt; return this; }
        public EmailBuilder deliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; return this; }
        public EmailBuilder openedAt(LocalDateTime openedAt) { this.openedAt = openedAt; return this; }
        public EmailBuilder clickedAt(LocalDateTime clickedAt) { this.clickedAt = clickedAt; return this; }
        public EmailBuilder bouncedAt(LocalDateTime bouncedAt) { this.bouncedAt = bouncedAt; return this; }
        public EmailBuilder failedAt(LocalDateTime failedAt) { this.failedAt = failedAt; return this; }
        public EmailBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public EmailBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public EmailBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Email build() {
            return new Email(id, organization, campaign, contact, recipientEmail, recipientName, subject, provider, status,
                    responseId, errorMessage, retryCount, maxRetries, sentAt, deliveredAt, openedAt, clickedAt,
                    bouncedAt, failedAt, createdBy, createdAt, updatedAt);
        }
    }
}
