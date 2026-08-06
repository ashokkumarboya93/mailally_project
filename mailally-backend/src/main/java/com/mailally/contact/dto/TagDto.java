package com.mailally.contact.dto;

import java.time.LocalDateTime;

public class TagDto {
    private Long id;
    private Long organizationId;
    private String name;
    private String colorCode;
    private Integer usageCount;
    private LocalDateTime createdAt;

    public TagDto() {
    }

    public TagDto(Long id, Long organizationId, String name, String colorCode, Integer usageCount, LocalDateTime createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.colorCode = colorCode;
        this.usageCount = usageCount;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
