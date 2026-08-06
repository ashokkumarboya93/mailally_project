package com.mailally.segment.entity;

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
 * Entity representing an audience Segment for email campaigns.
 * Supports STATIC (manually curated) and DYNAMIC (rule-based) segment types.
 */
@Entity
@Table(name = "segments")
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Column(name = "rules_json", columnDefinition = "TEXT")
    private String rulesJson;

    @Column(name = "contact_count")
    private Integer contactCount;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

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

    public Segment() {
    }

    public Segment(Long id, Organization organization, String name, String description, String type,
                   String rulesJson, Integer contactCount, String status, Long createdBy, Long updatedBy,
                   Long deletedBy, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt,
                   Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rulesJson = rulesJson;
        this.contactCount = contactCount;
        this.status = status;
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

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRulesJson() { return rulesJson; }
    public void setRulesJson(String rulesJson) { this.rulesJson = rulesJson; }

    public Integer getContactCount() { return contactCount; }
    public void setContactCount(Integer contactCount) { this.contactCount = contactCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

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
        if (this.status == null) this.status = "ACTIVE";
        if (this.isDeleted == null) this.isDeleted = false;
        if (this.contactCount == null) this.contactCount = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static SegmentBuilder builder() {
        return new SegmentBuilder();
    }

    public static class SegmentBuilder {
        private Long id;
        private Organization organization;
        private String name;
        private String description;
        private String type;
        private String rulesJson;
        private Integer contactCount;
        private String status;
        private Long createdBy;
        private Long updatedBy;
        private Long deletedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
        private Boolean isDeleted;

        SegmentBuilder() {}

        public SegmentBuilder id(Long id) { this.id = id; return this; }
        public SegmentBuilder organization(Organization organization) { this.organization = organization; return this; }
        public SegmentBuilder name(String name) { this.name = name; return this; }
        public SegmentBuilder description(String description) { this.description = description; return this; }
        public SegmentBuilder type(String type) { this.type = type; return this; }
        public SegmentBuilder rulesJson(String rulesJson) { this.rulesJson = rulesJson; return this; }
        public SegmentBuilder contactCount(Integer contactCount) { this.contactCount = contactCount; return this; }
        public SegmentBuilder status(String status) { this.status = status; return this; }
        public SegmentBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public SegmentBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public SegmentBuilder deletedBy(Long deletedBy) { this.deletedBy = deletedBy; return this; }
        public SegmentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SegmentBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public SegmentBuilder deletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; return this; }
        public SegmentBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Segment build() {
            return new Segment(id, organization, name, description, type, rulesJson, contactCount, status,
                    createdBy, updatedBy, deletedBy, createdAt, updatedAt, deletedAt, isDeleted);
        }
    }
}
