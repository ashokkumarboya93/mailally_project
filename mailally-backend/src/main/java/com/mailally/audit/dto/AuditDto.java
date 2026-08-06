package com.mailally.audit.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Audit.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class AuditDto {

    private Long id;
    private Long organizationId;
    private String action;
    private String module;
    private String description;
    private LocalDateTime timestamp;

    public AuditDto() {}

    public AuditDto(Long id, Long organizationId, String action, String module, String description, LocalDateTime timestamp) {
        this.id = id;
        this.organizationId = organizationId;
        this.action = action;
        this.module = module;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static AuditDtoBuilder builder() { return new AuditDtoBuilder(); }

    public static class AuditDtoBuilder {
        private Long id;
        private Long organizationId;
        private String action;
        private String module;
        private String description;
        private LocalDateTime timestamp;

        AuditDtoBuilder() {}

        public AuditDtoBuilder id(Long id) { this.id = id; return this; }
        public AuditDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public AuditDtoBuilder action(String action) { this.action = action; return this; }
        public AuditDtoBuilder module(String module) { this.module = module; return this; }
        public AuditDtoBuilder description(String description) { this.description = description; return this; }
        public AuditDtoBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public AuditDto build() {
            return new AuditDto(id, organizationId, action, module, description, timestamp);
        }
    }
}
