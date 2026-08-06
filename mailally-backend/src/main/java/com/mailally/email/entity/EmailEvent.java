package com.mailally.email.entity;

import com.mailally.campaign.entity.Campaign;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CampaignRecipientLog recipient;

    @Column(name = "event_type", nullable = false, length = 30)
    private String eventType;

    @Column(name = "provider_message_id", length = 255)
    private String providerMessageId;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public EmailEvent() {}

    public EmailEvent(Long id, Campaign campaign, CampaignRecipientLog recipient, String eventType, String providerMessageId,
                      String userAgent, String ipAddress, LocalDateTime timestamp, LocalDateTime createdAt) {
        this.id = id;
        this.campaign = campaign;
        this.recipient = recipient;
        this.eventType = eventType;
        this.providerMessageId = providerMessageId;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }

    public CampaignRecipientLog getRecipient() { return recipient; }
    public void setRecipient(CampaignRecipientLog recipient) { this.recipient = recipient; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getProviderMessageId() { return providerMessageId; }
    public void setProviderMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.timestamp == null) this.timestamp = LocalDateTime.now();
    }

    public static EmailEventBuilder builder() { return new EmailEventBuilder(); }

    public static class EmailEventBuilder {
        private Long id;
        private Campaign campaign;
        private CampaignRecipientLog recipient;
        private String eventType;
        private String providerMessageId;
        private String userAgent;
        private String ipAddress;
        private LocalDateTime timestamp;
        private LocalDateTime createdAt;

        EmailEventBuilder() {}

        public EmailEventBuilder id(Long id) { this.id = id; return this; }
        public EmailEventBuilder campaign(Campaign campaign) { this.campaign = campaign; return this; }
        public EmailEventBuilder recipient(CampaignRecipientLog recipient) { this.recipient = recipient; return this; }
        public EmailEventBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public EmailEventBuilder providerMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; return this; }
        public EmailEventBuilder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public EmailEventBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public EmailEventBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public EmailEventBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public EmailEvent build() {
            return new EmailEvent(id, campaign, recipient, eventType, providerMessageId, userAgent, ipAddress,
                    timestamp, createdAt);
        }
    }
}
