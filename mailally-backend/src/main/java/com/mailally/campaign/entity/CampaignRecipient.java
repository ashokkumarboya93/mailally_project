package com.mailally.campaign.entity;

import com.mailally.contact.entity.Contact;
import com.mailally.organization.entity.Organization;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "campaign_recipients", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"campaign_id", "contact_id"})
})
public class CampaignRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "status", nullable = false, length = 50)
    private String status = "QUEUED"; // QUEUED, SENDING, DELIVERED, OPENED, CLICKED, SPAM, BOUNCE, COMPLAINT, UNSUBSCRIBED

    @Column(name = "queued_at", nullable = false)
    private LocalDateTime queuedAt;

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

    @Column(name = "complaint_at")
    private LocalDateTime complaintAt;

    @Column(name = "unsubscribed_at")
    private LocalDateTime unsubscribedAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    @Column(name = "response_id", length = 255)
    private String responseId;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    public CampaignRecipient() {}

    @PrePersist
    protected void onCreate() {
        if (this.queuedAt == null) this.queuedAt = LocalDateTime.now();
        if (this.status == null) this.status = "QUEUED";
        if (this.retryCount == null) this.retryCount = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }

    public Contact getContact() { return contact; }
    public void setContact(Contact contact) { this.contact = contact; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getQueuedAt() { return queuedAt; }
    public void setQueuedAt(LocalDateTime queuedAt) { this.queuedAt = queuedAt; }

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

    public LocalDateTime getComplaintAt() { return complaintAt; }
    public void setComplaintAt(LocalDateTime complaintAt) { this.complaintAt = complaintAt; }

    public LocalDateTime getUnsubscribedAt() { return unsubscribedAt; }
    public void setUnsubscribedAt(LocalDateTime unsubscribedAt) { this.unsubscribedAt = unsubscribedAt; }

    public LocalDateTime getFailedAt() { return failedAt; }
    public void setFailedAt(LocalDateTime failedAt) { this.failedAt = failedAt; }

    public String getResponseId() { return responseId; }
    public void setResponseId(String responseId) { this.responseId = responseId; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
