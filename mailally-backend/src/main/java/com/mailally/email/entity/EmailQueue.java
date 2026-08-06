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
 * Entity representing a queued email record for campaign dispatch.
 * Status lifecycle: PENDING → PROCESSING → SENT / FAILED / CANCELLED / RETRYING.
 * Designed for future batch/worker upgrade while supporting MVP simple-loop sending.
 */
@Entity
@Table(name = "email_queue")
public class EmailQueue {

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

    @Column(name = "personalized_subject", length = 500)
    private String personalizedSubject;

    @Column(name = "personalized_html", columnDefinition = "LONGTEXT")
    private String personalizedHtml;

    @Column(name = "provider", length = 50)
    private String provider;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "max_retries")
    private Integer maxRetries;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @Column(name = "batch_number")
    private Integer batchNumber;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public EmailQueue() {}

    public EmailQueue(Long id, Organization organization, Campaign campaign, Contact contact, String recipientEmail,
                      String recipientName, String personalizedSubject, String personalizedHtml, String provider,
                      String status, Integer retryCount, Integer maxRetries, String failureReason,
                      Integer batchNumber, LocalDateTime scheduledAt, LocalDateTime processedAt,
                      Long createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organization = organization;
        this.campaign = campaign;
        this.contact = contact;
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
        this.personalizedSubject = personalizedSubject;
        this.personalizedHtml = personalizedHtml;
        this.provider = provider;
        this.status = status;
        this.retryCount = retryCount;
        this.maxRetries = maxRetries;
        this.failureReason = failureReason;
        this.batchNumber = batchNumber;
        this.scheduledAt = scheduledAt;
        this.processedAt = processedAt;
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
    public String getPersonalizedSubject() { return personalizedSubject; }
    public void setPersonalizedSubject(String personalizedSubject) { this.personalizedSubject = personalizedSubject; }
    public String getPersonalizedHtml() { return personalizedHtml; }
    public void setPersonalizedHtml(String personalizedHtml) { this.personalizedHtml = personalizedHtml; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public Integer getMaxRetries() { return maxRetries; }
    public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public Integer getBatchNumber() { return batchNumber; }
    public void setBatchNumber(Integer batchNumber) { this.batchNumber = batchNumber; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
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
        if (this.status == null) this.status = "PENDING";
        if (this.retryCount == null) this.retryCount = 0;
        if (this.maxRetries == null) this.maxRetries = 3;
        if (this.batchNumber == null) this.batchNumber = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static EmailQueueBuilder builder() { return new EmailQueueBuilder(); }

    public static class EmailQueueBuilder {
        private Long id;
        private Organization organization;
        private Campaign campaign;
        private Contact contact;
        private String recipientEmail;
        private String recipientName;
        private String personalizedSubject;
        private String personalizedHtml;
        private String provider;
        private String status;
        private Integer retryCount;
        private Integer maxRetries;
        private String failureReason;
        private Integer batchNumber;
        private LocalDateTime scheduledAt;
        private LocalDateTime processedAt;
        private Long createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        EmailQueueBuilder() {}

        public EmailQueueBuilder id(Long id) { this.id = id; return this; }
        public EmailQueueBuilder organization(Organization organization) { this.organization = organization; return this; }
        public EmailQueueBuilder campaign(Campaign campaign) { this.campaign = campaign; return this; }
        public EmailQueueBuilder contact(Contact contact) { this.contact = contact; return this; }
        public EmailQueueBuilder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public EmailQueueBuilder recipientName(String recipientName) { this.recipientName = recipientName; return this; }
        public EmailQueueBuilder personalizedSubject(String personalizedSubject) { this.personalizedSubject = personalizedSubject; return this; }
        public EmailQueueBuilder personalizedHtml(String personalizedHtml) { this.personalizedHtml = personalizedHtml; return this; }
        public EmailQueueBuilder provider(String provider) { this.provider = provider; return this; }
        public EmailQueueBuilder status(String status) { this.status = status; return this; }
        public EmailQueueBuilder retryCount(Integer retryCount) { this.retryCount = retryCount; return this; }
        public EmailQueueBuilder maxRetries(Integer maxRetries) { this.maxRetries = maxRetries; return this; }
        public EmailQueueBuilder failureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public EmailQueueBuilder batchNumber(Integer batchNumber) { this.batchNumber = batchNumber; return this; }
        public EmailQueueBuilder scheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; return this; }
        public EmailQueueBuilder processedAt(LocalDateTime processedAt) { this.processedAt = processedAt; return this; }
        public EmailQueueBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public EmailQueueBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public EmailQueueBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public EmailQueue build() {
            return new EmailQueue(id, organization, campaign, contact, recipientEmail, recipientName, personalizedSubject,
                    personalizedHtml, provider, status, retryCount, maxRetries, failureReason, batchNumber,
                    scheduledAt, processedAt, createdBy, createdAt, updatedAt);
        }
    }
}
