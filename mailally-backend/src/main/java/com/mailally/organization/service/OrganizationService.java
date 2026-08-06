package com.mailally.organization.service;

import com.mailally.organization.dto.OrganizationDto;

import java.util.List;

/**
 * Service interface defining operations for managing {@link com.mailally.organization.entity.Organization} domain.
 */
public interface OrganizationService {

    OrganizationDto createOrganization(OrganizationDto dto);

    OrganizationDto getOrganizationById(Long id);

    OrganizationDto getOrganizationBySlug(String slug);

    List<OrganizationDto> getAllOrganizations();

    OrganizationDto updateOrganization(Long id, OrganizationDto dto);

    void deleteOrganization(Long id);
}

