package com.mailally.template.entity;

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
 * Entity representing an Email Template with HTML content, versioning, and status lifecycle.
 */
@Entity
@Table(name = "email_templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "html_content", columnDefinition = "LONGTEXT")
    private String htmlContent;

    @Column(name = "text_content", columnDefinition = "LONGTEXT")
    private String textContent;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "version", nullable = false)
    private Integer version;

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

    public Template() {}

    public Template(Long id, Organization organization, String name, String subject, String htmlContent,
                    String textContent, String status, Integer version, Long createdBy, Long updatedBy,
                    Long deletedBy, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                    Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
        this.name = name;
        this.subject = subject;
        this.htmlContent = htmlContent;
        this.textContent = textContent;
        this.status = status;
        this.version = version;
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
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getHtmlContent() { return htmlContent; }
    public void setHtmlContent(String htmlContent) { this.htmlContent = htmlContent; }
    public String getTextContent() { return textContent; }
    public void setTextContent(String textContent) { this.textContent = textContent; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
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
        if (this.version == null) this.version = 1;
        if (this.isDeleted == null) this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static TemplateBuilder builder() { return new TemplateBuilder(); }

    public static class TemplateBuilder {
        private Long id;
        private Organization organization;
        private String name;
        private String subject;
        private String htmlContent;
        private String textContent;
        private String status;
        private Integer version;
        private Long createdBy;
        private Long updatedBy;
        private Long deletedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
        private Boolean isDeleted;

        TemplateBuilder() {}

        public TemplateBuilder id(Long id) { this.id = id; return this; }
        public TemplateBuilder organization(Organization organization) { this.organization = organization; return this; }
        public TemplateBuilder name(String name) { this.name = name; return this; }
        public TemplateBuilder subject(String subject) { this.subject = subject; return this; }
        public TemplateBuilder htmlContent(String htmlContent) { this.htmlContent = htmlContent; return this; }
        public TemplateBuilder textContent(String textContent) { this.textContent = textContent; return this; }
        public TemplateBuilder status(String status) { this.status = status; return this; }
        public TemplateBuilder version(Integer version) { this.version = version; return this; }
        public TemplateBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public TemplateBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public TemplateBuilder deletedBy(Long deletedBy) { this.deletedBy = deletedBy; return this; }
        public TemplateBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TemplateBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public TemplateBuilder deletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; return this; }
        public TemplateBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Template build() {
            return new Template(id, organization, name, subject, htmlContent, textContent, status, version,
                    createdBy, updatedBy, deletedBy, createdAt, updatedAt, deletedAt, isDeleted);
        }
    }
}
