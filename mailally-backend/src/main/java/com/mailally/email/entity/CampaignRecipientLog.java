package com.mailally.email.entity;

import com.mailally.campaign.entity.Campaign;
import com.mailally.contact.entity.Contact;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing recipient delivery details and performance diagnostics for V2.
 */
@Entity
@Table(name = "campaign_recipient_logs")
public class CampaignRecipientLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Contact contact;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "provider", length = 50)
    private String provider;

    @Column(name = "attempts")
    private Integer attempts;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "smtp_response_code", length = 50)
    private String smtpResponseCode;

    @Column(name = "worker_thread_id", length = 100)
    private String workerThreadId;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public CampaignRecipientLog() {}

    public CampaignRecipientLog(Long id, Campaign campaign, Contact contact, String email, String status, String provider,
                                Integer attempts, String lastError, String smtpResponseCode, String workerThreadId,
                                Integer durationMs, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.campaign = campaign;
        this.contact = contact;
        this.email = email;
        this.status = status;
        this.provider = provider;
        this.attempts = attempts;
        this.lastError = lastError;
        this.smtpResponseCode = smtpResponseCode;
        this.workerThreadId = workerThreadId;
        this.durationMs = durationMs;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }

    public Contact getContact() { return contact; }
    public void setContact(Contact contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public Integer getAttempts() { return attempts; }
    public void setAttempts(Integer attempts) { this.attempts = attempts; }

    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }

    public String getSmtpResponseCode() { return smtpResponseCode; }
    public void setSmtpResponseCode(String smtpResponseCode) { this.smtpResponseCode = smtpResponseCode; }

    public String getWorkerThreadId() { return workerThreadId; }
    public void setWorkerThreadId(String workerThreadId) { this.workerThreadId = workerThreadId; }

    public Integer getDurationMs() { return durationMs; }
    public void setDurationMs(Integer durationMs) { this.durationMs = durationMs; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.attempts == null) this.attempts = 0;
        if (this.status == null) this.status = "QUEUED";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static CampaignRecipientLogBuilder builder() { return new CampaignRecipientLogBuilder(); }

    public static class CampaignRecipientLogBuilder {
        private Long id;
        private Campaign campaign;
        private Contact contact;
        private String email;
        private String status;
        private String provider;
        private Integer attempts;
        private String lastError;
        private String smtpResponseCode;
        private String workerThreadId;
        private Integer durationMs;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        CampaignRecipientLogBuilder() {}

        public CampaignRecipientLogBuilder id(Long id) { this.id = id; return this; }
        public CampaignRecipientLogBuilder campaign(Campaign campaign) { this.campaign = campaign; return this; }
        public CampaignRecipientLogBuilder contact(Contact contact) { this.contact = contact; return this; }
        public CampaignRecipientLogBuilder email(String email) { this.email = email; return this; }
        public CampaignRecipientLogBuilder status(String status) { this.status = status; return this; }
        public CampaignRecipientLogBuilder provider(String provider) { this.provider = provider; return this; }
        public CampaignRecipientLogBuilder attempts(Integer attempts) { this.attempts = attempts; return this; }
        public CampaignRecipientLogBuilder lastError(String lastError) { this.lastError = lastError; return this; }
        public CampaignRecipientLogBuilder smtpResponseCode(String smtpResponseCode) { this.smtpResponseCode = smtpResponseCode; return this; }
        public CampaignRecipientLogBuilder workerThreadId(String workerThreadId) { this.workerThreadId = workerThreadId; return this; }
        public CampaignRecipientLogBuilder durationMs(Integer durationMs) { this.durationMs = durationMs; return this; }
        public CampaignRecipientLogBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CampaignRecipientLogBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public CampaignRecipientLog build() {
            return new CampaignRecipientLog(id, campaign, contact, email, status, provider, attempts, lastError,
                    smtpResponseCode, workerThreadId, durationMs, createdAt, updatedAt);
        }
    }
}
