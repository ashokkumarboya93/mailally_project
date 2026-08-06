package com.mailally.contact.dto;

import java.time.LocalDateTime;

public class DynamicFieldRegistryDto {
    private Long id;
    private Long organizationId;
    private String fieldKey;
    private String displayName;
    private String dataType;
    private Boolean isFilterable;
    private Boolean isSortable;
    private Boolean isVisible;
    private Boolean defaultVisible;
    private Integer orderIndex;
    private String sampleValue;
    private Long sourceBatchId;
    private LocalDateTime createdAt;

    public DynamicFieldRegistryDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

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
