package com.mailally.contact.entity;

import com.mailally.organization.entity.Organization;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_field_registry", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"organization_id", "field_key"})
})
public class DynamicFieldRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "field_key", nullable = false, length = 100)
    private String fieldKey;

    @Column(name = "display_name", nullable = false, length = 150)
    private String displayName;

    @Column(name = "data_type", nullable = false, length = 50)
    private String dataType = "TEXT"; // TEXT, NUMBER, CURRENCY, DATE, EMAIL, URL

    @Column(name = "is_filterable")
    private Boolean isFilterable = true;

    @Column(name = "is_sortable")
    private Boolean isSortable = true;

    @Column(name = "is_visible")
    private Boolean isVisible = true;

    @Column(name = "default_visible")
    private Boolean defaultVisible = true;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    @Column(name = "sample_value", length = 255)
    private String sampleValue;

    @Column(name = "source_batch_id")
    private Long sourceBatchId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public DynamicFieldRegistry() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isFilterable == null) this.isFilterable = true;
        if (this.isSortable == null) this.isSortable = true;
        if (this.isVisible == null) this.isVisible = true;
        if (this.defaultVisible == null) this.defaultVisible = true;
        if (this.orderIndex == null) this.orderIndex = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public String getFieldKey() { return fieldKey; }
    public void setFieldKey(String fieldKey) { this.fieldKey = fieldKey; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public Boolean getIsFilterable() { return isFilterable; }
    public void setIsFilterable(Boolean isFilterable) { this.isFilterable = isFilterable; }

    public Boolean getIsSortable() { return isSortable; }
    public void setIsSortable(Boolean isSortable) { this.isSortable = isSortable; }

    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }

    public Boolean getDefaultVisible() { return defaultVisible; }
    public void setDefaultVisible(Boolean defaultVisible) { this.defaultVisible = defaultVisible; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }

    public String getSampleValue() { return sampleValue; }
    public void setSampleValue(String sampleValue) { this.sampleValue = sampleValue; }

    public Long getSourceBatchId() { return sourceBatchId; }
    public void setSourceBatchId(Long sourceBatchId) { this.sourceBatchId = sourceBatchId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
