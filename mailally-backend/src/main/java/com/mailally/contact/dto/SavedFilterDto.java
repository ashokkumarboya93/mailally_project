package com.mailally.contact.dto;

import java.time.LocalDateTime;

public class SavedFilterDto {
    private Long id;
    private Long organizationId;
    private String name;
    private String filterJson;
    private Long createdBy;
    private LocalDateTime createdAt;

    public SavedFilterDto() {
    }

    public SavedFilterDto(Long id, Long organizationId, String name, String filterJson, Long createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.filterJson = filterJson;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFilterJson() { return filterJson; }
    public void setFilterJson(String filterJson) { this.filterJson = filterJson; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
