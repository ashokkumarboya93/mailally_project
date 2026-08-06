package com.mailally.segment.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Segment.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class SegmentDto {

    private Long id;
    private Long organizationId;
    private String name;
    private String description;
    private String filterCriteria;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SegmentDto() {}

    public SegmentDto(Long id, Long organizationId, String name, String description, String filterCriteria, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.description = description;
        this.filterCriteria = filterCriteria;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFilterCriteria() { return filterCriteria; }
    public void setFilterCriteria(String filterCriteria) { this.filterCriteria = filterCriteria; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static SegmentDtoBuilder builder() { return new SegmentDtoBuilder(); }

    public static class SegmentDtoBuilder {
        private Long id;
        private Long organizationId;
        private String name;
        private String description;
        private String filterCriteria;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        SegmentDtoBuilder() {}

        public SegmentDtoBuilder id(Long id) { this.id = id; return this; }
        public SegmentDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public SegmentDtoBuilder name(String name) { this.name = name; return this; }
        public SegmentDtoBuilder description(String description) { this.description = description; return this; }
        public SegmentDtoBuilder filterCriteria(String filterCriteria) { this.filterCriteria = filterCriteria; return this; }
        public SegmentDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SegmentDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public SegmentDto build() {
            return new SegmentDto(id, organizationId, name, description, filterCriteria, createdAt, updatedAt);
        }
    }
}
