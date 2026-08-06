package com.mailally.template.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a Template.
 */
public class TemplateResponseDto {

    private Long id;
    private Long organizationId;
    private String name;
    private String subject;
    private String htmlContent;
    private String textContent;
    private String status;
    private Integer version;
    private Long createdBy;
    private Long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TemplateResponseDto() {}

    public TemplateResponseDto(Long id, Long organizationId, String name, String subject, String htmlContent,
                               String textContent, String status, Integer version, Long createdBy, Long updatedBy,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.subject = subject;
        this.htmlContent = htmlContent;
        this.textContent = textContent;
        this.status = status;
        this.version = version;
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
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getHtmlContent() { return htmlContent; }
    public void setHtmlContent(String htmlContent) { this.htmlContent = htmlContent; }
    public String getTextContent() { return textContent; }
    public void setTextContent(String textContent) { this.textContent = textContent; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static TemplateResponseDtoBuilder builder() { return new TemplateResponseDtoBuilder(); }

    public static class TemplateResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private String name;
        private String subject;
        private String htmlContent;
        private String textContent;
        private String status;
        private Integer version;
        private Long createdBy;
        private Long updatedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        TemplateResponseDtoBuilder() {}

        public TemplateResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public TemplateResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public TemplateResponseDtoBuilder name(String name) { this.name = name; return this; }
        public TemplateResponseDtoBuilder subject(String subject) { this.subject = subject; return this; }
        public TemplateResponseDtoBuilder htmlContent(String htmlContent) { this.htmlContent = htmlContent; return this; }
        public TemplateResponseDtoBuilder textContent(String textContent) { this.textContent = textContent; return this; }
        public TemplateResponseDtoBuilder status(String status) { this.status = status; return this; }
        public TemplateResponseDtoBuilder version(Integer version) { this.version = version; return this; }
        public TemplateResponseDtoBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public TemplateResponseDtoBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public TemplateResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TemplateResponseDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public TemplateResponseDto build() {
            return new TemplateResponseDto(id, organizationId, name, subject, htmlContent, textContent, status,
                    version, createdBy, updatedBy, createdAt, updatedAt);
        }
    }
}
