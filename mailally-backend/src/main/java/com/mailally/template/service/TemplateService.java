package com.mailally.template.service;

import com.mailally.security.CustomUserDetails;
import com.mailally.template.dto.CreateTemplateRequestDto;
import com.mailally.template.dto.TemplateResponseDto;
import com.mailally.template.dto.UpdateTemplateRequestDto;
import org.springframework.data.domain.Page;

/**
 * Service interface for Template management operations.
 */
public interface TemplateService {

    TemplateResponseDto createTemplate(CustomUserDetails currentUser, CreateTemplateRequestDto dto);

    TemplateResponseDto getTemplateById(CustomUserDetails currentUser, Long id);

    Page<TemplateResponseDto> listTemplates(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir);

    TemplateResponseDto updateTemplate(CustomUserDetails currentUser, Long id, UpdateTemplateRequestDto dto);

    TemplateResponseDto cloneTemplate(CustomUserDetails currentUser, Long id);

    String previewTemplate(CustomUserDetails currentUser, Long id);

    void softDeleteTemplate(CustomUserDetails currentUser, Long id);

    Page<TemplateResponseDto> searchTemplates(CustomUserDetails currentUser, String name, String status,
                                              int page, int size, String sortBy, String sortDir);
}
