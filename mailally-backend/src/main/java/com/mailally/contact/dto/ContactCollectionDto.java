package com.mailally.contact.dto;

import java.time.LocalDateTime;

public class ContactCollectionDto {
    private Long id;
    private Long organizationId;
    private String name;
    private String description;
    private String type;
    private String rulesJson;
    private String colorCode;
    private String tag;
    private Integer contactCount;
    private Integer subscribedCount;
    private Integer unsubscribedCount;
    private Integer invalidCount;
    private Integer duplicateCount;
    private String sourceType;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ContactCollectionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
