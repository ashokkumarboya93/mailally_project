package com.mailally.template.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Template.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class TemplateDto {

    private Long id;
    private Long organizationId;
    private String name;
    private String subject;
    private String htmlContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TemplateDto() {}

    public TemplateDto(Long id, Long organizationId, String name, String subject, String htmlContent, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.subject = subject;
        this.htmlContent = htmlContent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getHtmlContent() { return htmlContent; }
    public void setHtmlContent(String htmlContent) { this.htmlContent = htmlContent; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static TemplateDtoBuilder builder() { return new TemplateDtoBuilder(); }

    public static class TemplateDtoBuilder {
        private Long id;
        private Long organizationId;
        private String name;
        private String subject;
        private String htmlContent;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        TemplateDtoBuilder() {}

        public TemplateDtoBuilder id(Long id) { this.id = id; return this; }
        public TemplateDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public TemplateDtoBuilder name(String name) { this.name = name; return this; }
        public TemplateDtoBuilder subject(String subject) { this.subject = subject; return this; }
        public TemplateDtoBuilder htmlContent(String htmlContent) { this.htmlContent = htmlContent; return this; }
        public TemplateDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TemplateDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public TemplateDto build() {
            return new TemplateDto(id, organizationId, name, subject, htmlContent, createdAt, updatedAt);
        }
    }
}
