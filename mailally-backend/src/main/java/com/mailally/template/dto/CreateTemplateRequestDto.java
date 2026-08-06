package com.mailally.template.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new Email Template.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTemplateRequestDto {

    @NotBlank(message = "Template name is required")
    @Size(max = 200, message = "Template name must not exceed 200 characters")
    private String name;

    @Size(max = 500, message = "Subject must not exceed 500 characters")
    private String subject;

    private String htmlContent;

    private String textContent;

    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;

    public CreateTemplateRequestDto() {}

    public CreateTemplateRequestDto(String name, String subject, String htmlContent, String textContent, String status) {
        this.name = name;
        this.subject = subject;
        this.htmlContent = htmlContent;
        this.textContent = textContent;
        this.status = status;
    }

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
}
