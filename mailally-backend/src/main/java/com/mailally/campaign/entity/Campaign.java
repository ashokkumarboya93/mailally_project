package com.mailally.campaign.entity;

import com.mailally.organization.entity.Organization;
import com.mailally.segment.entity.Segment;
import com.mailally.template.entity.Template;
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
 * Entity representing an Email Campaign with template/segment attachment and lifecycle status tracking.
 */
@Entity
@Table(name = "campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Template template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Segment segment;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "sender_name", length = 150)
    private String senderName;

    @Column(name = "from_name", length = 150)
    private String fromName;

    @Column(name = "from_email", length = 255)
    private String fromEmail;

    @Column(name = "sender_email", length = 255)
    private String senderEmail;

    @Column(name = "reply_to", length = 255)
    private String replyTo;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "total_recipients")
    private Integer totalRecipients;

    @Column(name = "sent_count")
    private Integer sentCount;

    @Column(name = "failed_count")
    private Integer failedCount;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Campaign() {}

    public Campaign(Long id, Organization organization, Template template, Segment segment, String name,
                    String subject, String senderName, String fromName, String fromEmail, String senderEmail, String replyTo, String status,
                    LocalDateTime scheduledAt, Integer totalRecipients, Integer sentCount, Integer failedCount,
                    Long createdBy, Long updatedBy, Long deletedBy, LocalDateTime createdAt, LocalDateTime updatedAt,
                    LocalDateTime deletedAt, Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
        this.template = template;
        this.segment = segment;
        this.name = name;
        this.subject = subject;
        this.senderName = senderName;
        this.fromName = fromName;
        this.fromEmail = fromEmail;
        this.senderEmail = senderEmail;
        this.replyTo = replyTo;
        this.status = status;
        this.scheduledAt = scheduledAt;
        this.totalRecipients = totalRecipients;
        this.sentCount = sentCount;
        this.failedCount = failedCount;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.deletedBy = deletedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public Template getTemplate() { return template; }
    public void setTemplate(Template template) { this.template = template; }
    public Segment getSegment() { return segment; }
    public void setSegment(Segment segment) { this.segment = segment; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getFromName() { return fromName; }
    public void setFromName(String fromName) { this.fromName = fromName; }
    public String getFromEmail() { return fromEmail; }
    public void setFromEmail(String fromEmail) { this.fromEmail = fromEmail; }
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
    public Long getDeletedBy() { return deletedBy; }
    public void setDeletedBy(Long deletedBy) { this.deletedBy = deletedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = "DRAFT";
        if (this.isDeleted == null) this.isDeleted = false;
        if (this.totalRecipients == null) this.totalRecipients = 0;
        if (this.sentCount == null) this.sentCount = 0;
        if (this.failedCount == null) this.failedCount = 0;
        if (this.fromName == null || this.fromName.isBlank()) {
            this.fromName = (this.senderName != null && !this.senderName.isBlank()) ? this.senderName : "Marcamor";
        }
        if (this.senderName == null || this.senderName.isBlank()) {
            this.senderName = this.fromName;
        }
        if (this.fromEmail == null || this.fromEmail.isBlank()) {
            this.fromEmail = (this.senderEmail != null && !this.senderEmail.isBlank()) ? this.senderEmail : "info@marcamor.com";
        }
        if (this.senderEmail == null || this.senderEmail.isBlank()) {
            this.senderEmail = this.fromEmail;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static CampaignBuilder builder() { return new CampaignBuilder(); }

    public static class CampaignBuilder {
        private Long id;
        private Organization organization;
        private Template template;
        private Segment segment;
        private String name;
        private String subject;
        private String senderName;
        private String fromName;
        private String fromEmail;
        private String senderEmail;
        private String replyTo;
        private String status;
        private LocalDateTime scheduledAt;
        private Integer totalRecipients;
        private Integer sentCount;
        private Integer failedCount;
        private Long createdBy;
        private Long updatedBy;
        private Long deletedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
        private Boolean isDeleted;

        CampaignBuilder() {}

        public CampaignBuilder id(Long id) { this.id = id; return this; }
        public CampaignBuilder organization(Organization organization) { this.organization = organization; return this; }
        public CampaignBuilder template(Template template) { this.template = template; return this; }
        public CampaignBuilder segment(Segment segment) { this.segment = segment; return this; }
        public CampaignBuilder name(String name) { this.name = name; return this; }
        public CampaignBuilder subject(String subject) { this.subject = subject; return this; }
        public CampaignBuilder senderName(String senderName) { this.senderName = senderName; return this; }
        public CampaignBuilder fromName(String fromName) { this.fromName = fromName; return this; }
        public CampaignBuilder fromEmail(String fromEmail) { this.fromEmail = fromEmail; return this; }
        public CampaignBuilder senderEmail(String senderEmail) { this.senderEmail = senderEmail; return this; }
        public CampaignBuilder replyTo(String replyTo) { this.replyTo = replyTo; return this; }
        public CampaignBuilder status(String status) { this.status = status; return this; }
        public CampaignBuilder scheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; return this; }
        public CampaignBuilder totalRecipients(Integer totalRecipients) { this.totalRecipients = totalRecipients; return this; }
        public CampaignBuilder sentCount(Integer sentCount) { this.sentCount = sentCount; return this; }
        public CampaignBuilder failedCount(Integer failedCount) { this.failedCount = failedCount; return this; }
        public CampaignBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public CampaignBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public CampaignBuilder deletedBy(Long deletedBy) { this.deletedBy = deletedBy; return this; }
        public CampaignBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CampaignBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public CampaignBuilder deletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; return this; }
        public CampaignBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Campaign build() {
            return new Campaign(id, organization, template, segment, name, subject, senderName, fromName, fromEmail, senderEmail, replyTo,
                    status, scheduledAt, totalRecipients, sentCount, failedCount, createdBy, updatedBy, deletedBy,
                    createdAt, updatedAt, deletedAt, isDeleted);
        }
    }
}
