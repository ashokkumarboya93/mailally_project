package com.mailally.notification.entity;

import com.mailally.organization.entity.Organization;
import com.mailally.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
 * Entity representing an in-app or channel notification dispatch.
 */
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "type", nullable = false, length = 50)
    private String type; // e.g. CAMPAIGN_COMPLETED, EMAIL_FAILED, LOGIN_SUCCESS, etc.

    @Column(name = "priority", nullable = false, length = 20)
    private String priority; // LOW, NORMAL, HIGH, CRITICAL

    @Column(name = "status", nullable = false, length = 20)
    private String status; // UNREAD, READ, ARCHIVED, DELETED

    @Column(name = "channel", nullable = false, length = 30)
    private String channel; // IN_APP, EMAIL, SMS, PUSH, SLACK, TEAMS, WHATSAPP, WEBSOCKET

    @Column(name = "source_module", nullable = false, length = 50)
    private String sourceModule; // AUTH, USER, CONTACT, CAMPAIGN, SCHEDULER, EMAIL_ENGINE, etc.

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "action_url", length = 500)
    private String actionUrl;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Notification() {}

    public Notification(Long id, Organization organization, User user, String title, String message, String type,
                        String priority, String status, String channel, String sourceModule, Long referenceId,
                        String actionUrl, String icon, String color, Boolean isRead, LocalDateTime readAt,
                        LocalDateTime expiresAt, Long createdBy, Long updatedBy, LocalDateTime createdAt,
                        LocalDateTime updatedAt, Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
        this.user = user;
        this.title = title;
        this.message = message;
        this.type = type;
        this.priority = priority;
        this.status = status;
        this.channel = channel;
        this.sourceModule = sourceModule;
        this.referenceId = referenceId;
        this.actionUrl = actionUrl;
        this.icon = icon;
        this.color = color;
        this.isRead = isRead;
        this.readAt = readAt;
        this.expiresAt = expiresAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public String getSourceModule() { return sourceModule; }
    public void setSourceModule(String sourceModule) { this.sourceModule = sourceModule; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getActionUrl() { return actionUrl; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = "UNREAD";
        if (this.priority == null) this.priority = "NORMAL";
        if (this.channel == null) this.channel = "IN_APP";
        if (this.type == null) this.type = "CUSTOM";
        if (this.sourceModule == null) this.sourceModule = "SYSTEM";
        if (this.isRead == null) this.isRead = false;
        if (this.isDeleted == null) this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static NotificationBuilder builder() { return new NotificationBuilder(); }

    public static class NotificationBuilder {
        private Long id;
        private Organization organization;
        private User user;
        private String title;
        private String message;
        private String type;
        private String priority;
        private String status;
        private String channel;
        private String sourceModule;
        private Long referenceId;
        private String actionUrl;
        private String icon;
        private String color;
        private Boolean isRead;
        private LocalDateTime readAt;
        private LocalDateTime expiresAt;
        private Long createdBy;
        private Long updatedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean isDeleted;

        NotificationBuilder() {}

        public NotificationBuilder id(Long id) { this.id = id; return this; }
        public NotificationBuilder organization(Organization organization) { this.organization = organization; return this; }
        public NotificationBuilder user(User user) { this.user = user; return this; }
        public NotificationBuilder title(String title) { this.title = title; return this; }
        public NotificationBuilder message(String message) { this.message = message; return this; }
        public NotificationBuilder type(String type) { this.type = type; return this; }
        public NotificationBuilder priority(String priority) { this.priority = priority; return this; }
        public NotificationBuilder status(String status) { this.status = status; return this; }
        public NotificationBuilder channel(String channel) { this.channel = channel; return this; }
        public NotificationBuilder sourceModule(String sourceModule) { this.sourceModule = sourceModule; return this; }
        public NotificationBuilder referenceId(Long referenceId) { this.referenceId = referenceId; return this; }
        public NotificationBuilder actionUrl(String actionUrl) { this.actionUrl = actionUrl; return this; }
        public NotificationBuilder icon(String icon) { this.icon = icon; return this; }
        public NotificationBuilder color(String color) { this.color = color; return this; }
        public NotificationBuilder isRead(Boolean isRead) { this.isRead = isRead; return this; }
        public NotificationBuilder readAt(LocalDateTime readAt) { this.readAt = readAt; return this; }
        public NotificationBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public NotificationBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public NotificationBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public NotificationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public NotificationBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public NotificationBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Notification build() {
            return new Notification(id, organization, user, title, message, type, priority, status, channel,
                    sourceModule, referenceId, actionUrl, icon, color, isRead, readAt, expiresAt, createdBy,
                    updatedBy, createdAt, updatedAt, isDeleted);
        }
    }
}
