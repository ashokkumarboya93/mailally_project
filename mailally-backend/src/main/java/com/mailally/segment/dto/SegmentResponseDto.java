package com.mailally.segment.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a Segment.
 */
public class SegmentResponseDto {

    private Long id;
    private Long organizationId;
    private String name;
    private String description;
    private String type;
    private String rulesJson;
    private Integer contactCount;
    private String status;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SegmentResponseDto() {}

    public SegmentResponseDto(Long id, Long organizationId, String name, String description, String type,
                              String rulesJson, Integer contactCount, String status, Long createdBy,
                              Long updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rulesJson = rulesJson;
        this.contactCount = contactCount;
        this.status = status;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static SegmentResponseDtoBuilder builder() { return new SegmentResponseDtoBuilder(); }

    public static class SegmentResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private String name;
        private String description;
        private String type;
        private String rulesJson;
        private Integer contactCount;
        private String status;
        private Long createdBy;
        private Long updatedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        SegmentResponseDtoBuilder() {}

        public SegmentResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public SegmentResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public SegmentResponseDtoBuilder name(String name) { this.name = name; return this; }
        public SegmentResponseDtoBuilder description(String description) { this.description = description; return this; }
        public SegmentResponseDtoBuilder type(String type) { this.type = type; return this; }
        public SegmentResponseDtoBuilder rulesJson(String rulesJson) { this.rulesJson = rulesJson; return this; }
        public SegmentResponseDtoBuilder contactCount(Integer contactCount) { this.contactCount = contactCount; return this; }
        public SegmentResponseDtoBuilder status(String status) { this.status = status; return this; }
        public SegmentResponseDtoBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public SegmentResponseDtoBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public SegmentResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SegmentResponseDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public SegmentResponseDto build() {
            return new SegmentResponseDto(id, organizationId, name, description, type, rulesJson, contactCount,
                    status, createdBy, updatedBy, createdAt, updatedAt);
        }
    }
}
