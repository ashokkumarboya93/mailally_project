package com.mailally.audit.entity;

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
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity representing an audit log entry in the audit_logs table.
 */
@Entity
@Table(name = "audit_logs")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "module", nullable = false, length = 50)
    private String module; // AUTH, USER, CONTACT, CAMPAIGN, SCHEDULER, BILLING, SETTINGS, SUBSCRIPTION, NOTIFICATION, AI

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "browser", length = 255)
    private String browser;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "success", nullable = false)
    private Boolean success;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "resource_type")
    private String resourceType = "GENERAL";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Audit() {}

    public Audit(Long id, Organization organization, User user, String action, String module, String description,
                 String ipAddress, String browser, LocalDateTime timestamp, Boolean success, String failureReason,
                 Long referenceId, LocalDateTime createdAt, Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
        this.user = user;
        this.action = action;
        this.module = module;
        this.description = description;
        this.ipAddress = ipAddress;
        this.browser = browser;
        this.timestamp = timestamp;
        this.success = success;
        this.failureReason = failureReason;
        this.referenceId = referenceId;
        this.resourceType = "GENERAL";
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.timestamp == null) this.timestamp = LocalDateTime.now();
        if (this.module == null) this.module = "SYSTEM";
        if (this.resourceType == null) this.resourceType = "GENERAL";
        if (this.success == null) this.success = true;
        if (this.isDeleted == null) this.isDeleted = false;
    }

    public static AuditBuilder builder() { return new AuditBuilder(); }

    public static class AuditBuilder {
        private Long id;
        private Organization organization;
        private User user;
        private String action;
        private String module;
        private String description;
        private String ipAddress;
        private String browser;
        private LocalDateTime timestamp;
        private Boolean success;
        private String failureReason;
        private Long referenceId;
        private String resourceType = "GENERAL";
        private LocalDateTime createdAt;
        private Boolean isDeleted;

        AuditBuilder() {}

        public AuditBuilder id(Long id) { this.id = id; return this; }
        public AuditBuilder organization(Organization organization) { this.organization = organization; return this; }
        public AuditBuilder user(User user) { this.user = user; return this; }
        public AuditBuilder action(String action) { this.action = action; return this; }
        public AuditBuilder module(String module) { this.module = module; return this; }
        public AuditBuilder description(String description) { this.description = description; return this; }
        public AuditBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuditBuilder browser(String browser) { this.browser = browser; return this; }
        public AuditBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AuditBuilder success(Boolean success) { this.success = success; return this; }
        public AuditBuilder failureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public AuditBuilder referenceId(Long referenceId) { this.referenceId = referenceId; return this; }
        public AuditBuilder resourceType(String resourceType) { this.resourceType = resourceType; return this; }
        public AuditBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AuditBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Audit build() {
            Audit audit = new Audit(id, organization, user, action, module, description, ipAddress, browser,
                    timestamp, success, failureReason, referenceId, createdAt, isDeleted);
            audit.setResourceType(resourceType != null ? resourceType : "GENERAL");
            return audit;
        }
    }
}
