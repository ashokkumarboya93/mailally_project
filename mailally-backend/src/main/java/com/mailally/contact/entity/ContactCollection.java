package com.mailally.contact.entity;

import com.mailally.organization.entity.Organization;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_collections")
public class ContactCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "type", nullable = false, length = 50)
    private String type; // IMPORT, MANUAL, SMART, SAVED_SEARCH

    @Column(name = "rules_json", columnDefinition = "TEXT")
    private String rulesJson;

    @Column(name = "color_code", length = 20)
    private String colorCode = "#3B82F6";

    @Column(name = "tag", length = 100)
    private String tag;

    @Column(name = "contact_count")
    private Integer contactCount = 0;

    @Column(name = "subscribed_count")
    private Integer subscribedCount = 0;

    @Column(name = "unsubscribed_count")
    private Integer unsubscribedCount = 0;

    @Column(name = "invalid_count")
    private Integer invalidCount = 0;

    @Column(name = "duplicate_count")
    private Integer duplicateCount = 0;

    @Column(name = "source_type", length = 50)
    private String sourceType = "EXCEL";

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public ContactCollection() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isDeleted == null) this.isDeleted = false;
        if (this.type == null || this.type.isBlank()) this.type = "IMPORT";
        if (this.contactCount == null) this.contactCount = 0;
        if (this.subscribedCount == null) this.subscribedCount = 0;
        if (this.unsubscribedCount == null) this.unsubscribedCount = 0;
        if (this.invalidCount == null) this.invalidCount = 0;
        if (this.duplicateCount == null) this.duplicateCount = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public Integer getContactCount() { return contactCount; }
    public void setContactCount(Integer contactCount) { this.contactCount = contactCount; }

    public Integer getSubscribedCount() { return subscribedCount; }
    public void setSubscribedCount(Integer subscribedCount) { this.subscribedCount = subscribedCount; }

    public Integer getUnsubscribedCount() { return unsubscribedCount; }
    public void setUnsubscribedCount(Integer unsubscribedCount) { this.unsubscribedCount = unsubscribedCount; }

    public Integer getInvalidCount() { return invalidCount; }
    public void setInvalidCount(Integer invalidCount) { this.invalidCount = invalidCount; }

    public Integer getDuplicateCount() { return duplicateCount; }
    public void setDuplicateCount(Integer duplicateCount) { this.duplicateCount = duplicateCount; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

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
}
