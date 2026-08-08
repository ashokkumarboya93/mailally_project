package com.mailally.email.entity;

import com.mailally.campaign.entity.Campaign;
import com.mailally.email.constant.EmailEventType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an immutable delivery or engagement event log record.
 */
@Entity
@Table(name = "email_events")
public class EmailEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id")
    private Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CampaignRecipientLog recipient;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    private EmailEventType eventType = EmailEventType.SENT;

    @Column(name = "provider", length = 50)
    private String provider = "BREVO";

    @Column(name = "provider_message_id", length = 255)
    private String providerMessageId;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public EmailEvent() {}

    public EmailEvent(Long id, Long organizationId, Campaign campaign, CampaignRecipientLog recipient, EmailEventType eventType,
                      String provider, String providerMessageId, String userAgent, String ipAddress, String metadata,
                      LocalDateTime occurredAt, LocalDateTime timestamp, LocalDateTime createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.campaign = campaign;
        this.recipient = recipient;
        this.eventType = eventType != null ? eventType : EmailEventType.SENT;
        this.provider = provider != null ? provider : "BREVO";
        this.providerMessageId = providerMessageId;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.metadata = metadata;
        this.occurredAt = occurredAt != null ? occurredAt : LocalDateTime.now();
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }

    public CampaignRecipientLog getRecipient() { return recipient; }
    public void setRecipient(CampaignRecipientLog recipient) { this.recipient = recipient; }

    public EmailEventType getEventType() { return eventType; }
    public void setEventType(EmailEventType eventType) { this.eventType = eventType; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getProviderMessageId() { return providerMessageId; }
    public void setProviderMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.timestamp == null) this.timestamp = LocalDateTime.now();
        if (this.occurredAt == null) this.occurredAt = this.timestamp;
    }

    public static EmailEventBuilder builder() { return new EmailEventBuilder(); }

    public static class EmailEventBuilder {
        private Long id;
        private Long organizationId;
        private Campaign campaign;
        private CampaignRecipientLog recipient;
        private EmailEventType eventType = EmailEventType.SENT;
        private String provider = "BREVO";
        private String providerMessageId;
        private String userAgent;
        private String ipAddress;
        private String metadata;
        private LocalDateTime occurredAt;
        private LocalDateTime timestamp;
        private LocalDateTime createdAt;

        EmailEventBuilder() {}

        public EmailEventBuilder id(Long id) { this.id = id; return this; }
        public EmailEventBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public EmailEventBuilder campaign(Campaign campaign) { this.campaign = campaign; return this; }
        public EmailEventBuilder recipient(CampaignRecipientLog recipient) { this.recipient = recipient; return this; }
        public EmailEventBuilder eventType(EmailEventType eventType) { this.eventType = eventType; return this; }
        public EmailEventBuilder eventType(String eventTypeStr) { this.eventType = EmailEventType.fromString(eventTypeStr); return this; }
        public EmailEventBuilder provider(String provider) { this.provider = provider; return this; }
        public EmailEventBuilder providerMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; return this; }
        public EmailEventBuilder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public EmailEventBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public EmailEventBuilder metadata(String metadata) { this.metadata = metadata; return this; }
        public EmailEventBuilder occurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; return this; }
        public EmailEventBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public EmailEventBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public EmailEvent build() {
            return new EmailEvent(id, organizationId, campaign, recipient, eventType, provider, providerMessageId,
                    userAgent, ipAddress, metadata, occurredAt, timestamp, createdAt);
        }
    }
}
