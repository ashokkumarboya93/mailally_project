package com.mailally.template.service.impl;

import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.template.dto.CreateTemplateRequestDto;
import com.mailally.template.dto.TemplateResponseDto;
import com.mailally.template.dto.UpdateTemplateRequestDto;
import com.mailally.template.entity.Template;
import com.mailally.template.mapper.TemplateMapper;
import com.mailally.template.repository.TemplateRepository;
import com.mailally.template.service.TemplateService;
import com.mailally.template.validator.TemplateValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service implementation for Template management with versioning, cloning, preview, and soft deletion.
 */
@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final OrganizationRepository organizationRepository;
    private final TemplateValidator templateValidator;
    private final TemplateMapper templateMapper;

    public TemplateServiceImpl(TemplateRepository templateRepository,
                               OrganizationRepository organizationRepository,
                               TemplateValidator templateValidator,
                               TemplateMapper templateMapper) {
        this.templateRepository = templateRepository;
        this.organizationRepository = organizationRepository;
        this.templateValidator = templateValidator;
        this.templateMapper = templateMapper;
    }

    @Override
    public TemplateResponseDto createTemplate(CustomUserDetails currentUser, CreateTemplateRequestDto dto) {
        templateValidator.validateAdminOrManager(currentUser);
        templateValidator.validateCreate(dto.getName(), currentUser.getOrganizationId());
        if (dto.getStatus() != null) templateValidator.validateStatus(dto.getStatus());

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        Template template = templateMapper.toTemplateEntity(dto, org, currentUser.getUserId());
        Template saved = templateRepository.save(template);
        return templateMapper.toTemplateResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TemplateResponseDto getTemplateById(CustomUserDetails currentUser, Long id) {
        Template template = templateRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Template not found with ID: " + id));
        return templateMapper.toTemplateResponseDto(template);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TemplateResponseDto> listTemplates(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Template> templatesPage = templateRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId(), pageable);
        return templatesPage.map(templateMapper::toTemplateResponseDto);
    }

    @Override
    public TemplateResponseDto updateTemplate(CustomUserDetails currentUser, Long id, UpdateTemplateRequestDto dto) {
        templateValidator.validateAdminOrManager(currentUser);
        if (dto.getStatus() != null) templateValidator.validateStatus(dto.getStatus());

        Template template = templateRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Template not found with ID: " + id));

        templateMapper.updateTemplateFromDto(template, dto, currentUser.getUserId());
        Template updated = templateRepository.save(template);
        return templateMapper.toTemplateResponseDto(updated);
    }

    @Override
    public TemplateResponseDto cloneTemplate(CustomUserDetails currentUser, Long id) {
        templateValidator.validateAdminOrManager(currentUser);

        Template source = templateRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Template not found with ID: " + id));

        Template clone = Template.builder()
                .organization(source.getOrganization())
                .name(source.getName() + " (Copy)")
                .subject(source.getSubject())
                .htmlContent(source.getHtmlContent())
                .textContent(source.getTextContent())
                .status("DRAFT")
                .version(1)
                .createdBy(currentUser.getUserId())
                .updatedBy(currentUser.getUserId())
                .isDeleted(false)
                .build();

        Template saved = templateRepository.save(clone);
        return templateMapper.toTemplateResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public String previewTemplate(CustomUserDetails currentUser, Long id) {
        Template template = templateRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Template not found with ID: " + id));
        return template.getHtmlContent() != null ? template.getHtmlContent() : template.getTextContent();
    }

    @Override
    public void softDeleteTemplate(CustomUserDetails currentUser, Long id) {
        templateValidator.validateAdminOrManager(currentUser);

        Template template = templateRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Template not found with ID: " + id));

        template.setIsDeleted(true);
        template.setDeletedBy(currentUser.getUserId());
        template.setDeletedAt(LocalDateTime.now());
        templateRepository.save(template);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TemplateResponseDto> searchTemplates(CustomUserDetails currentUser, String name, String status,
                                                     int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Template> results = templateRepository.searchTemplates(
                currentUser.getOrganizationId(),
                (name != null && !name.isBlank()) ? name.trim() : null,
                (status != null && !status.isBlank()) ? status.trim().toUpperCase() : null,
                pageable
        );
        return results.map(templateMapper::toTemplateResponseDto);
    }
}
