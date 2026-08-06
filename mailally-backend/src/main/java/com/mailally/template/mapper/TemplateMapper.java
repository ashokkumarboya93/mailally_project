package com.mailally.template.mapper;

import com.mailally.organization.entity.Organization;
import com.mailally.template.dto.CreateTemplateRequestDto;
import com.mailally.template.dto.TemplateResponseDto;
import com.mailally.template.dto.UpdateTemplateRequestDto;
import com.mailally.template.entity.Template;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Template entities and DTOs.
 */
@Component
public class TemplateMapper {

    public Template toTemplateEntity(CreateTemplateRequestDto dto, Organization organization, Long createdByUserId) {
        if (dto == null) return null;
        return Template.builder()
                .organization(organization)
                .name(dto.getName() != null ? dto.getName().trim() : null)
                .subject(dto.getSubject())
                .htmlContent(dto.getHtmlContent())
                .textContent(dto.getTextContent())
                .status(dto.getStatus() != null && !dto.getStatus().isBlank() ? dto.getStatus().trim().toUpperCase() : "DRAFT")
                .version(1)
                .createdBy(createdByUserId)
                .updatedBy(createdByUserId)
                .isDeleted(false)
                .build();
    }

    public TemplateResponseDto toTemplateResponseDto(Template template) {
        if (template == null) return null;
        return TemplateResponseDto.builder()
                .id(template.getId())
                .organizationId(template.getOrganization() != null ? template.getOrganization().getId() : null)
                .name(template.getName())
                .subject(template.getSubject())
                .htmlContent(template.getHtmlContent())
                .textContent(template.getTextContent())
                .status(template.getStatus())
                .version(template.getVersion())
                .createdBy(template.getCreatedBy())
                .updatedBy(template.getUpdatedBy())
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .build();
    }

    public void updateTemplateFromDto(Template template, UpdateTemplateRequestDto dto, Long updatedByUserId) {
        if (template == null || dto == null) return;
        if (dto.getName() != null) template.setName(dto.getName().trim());
        if (dto.getSubject() != null) template.setSubject(dto.getSubject());
        if (dto.getHtmlContent() != null) template.setHtmlContent(dto.getHtmlContent());
        if (dto.getTextContent() != null) template.setTextContent(dto.getTextContent());
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            template.setStatus(dto.getStatus().trim().toUpperCase());
        }
        template.setVersion(template.getVersion() + 1);
        template.setUpdatedBy(updatedByUserId);
    }
}
