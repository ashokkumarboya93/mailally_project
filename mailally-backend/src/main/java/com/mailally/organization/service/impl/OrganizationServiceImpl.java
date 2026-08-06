package com.mailally.organization.service.impl;

import com.mailally.exception.CustomException;
import com.mailally.organization.dto.OrganizationDto;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.mapper.OrganizationMapper;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.organization.service.OrganizationService;
import com.mailally.subscription.entity.Subscription;
import com.mailally.subscription.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for managing {@link Organization} operations.
 */
@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   SubscriptionRepository subscriptionRepository,
                                   OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public OrganizationDto createOrganization(OrganizationDto dto) {
        if (organizationRepository.existsBySlug(dto.getSlug())) {
            throw new CustomException("Organization with slug '" + dto.getSlug() + "' already exists");
        }

        Subscription subscription = subscriptionRepository.findById(dto.getSubscriptionId())
                .orElseThrow(() -> new CustomException("Subscription not found with ID: " + dto.getSubscriptionId()));

        Organization organization = organizationMapper.toEntity(dto, subscription);
        Organization savedOrganization = organizationRepository.save(organization);
        return organizationMapper.toDto(savedOrganization);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationDto getOrganizationById(Long id) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Organization not found with ID: " + id));
        return organizationMapper.toDto(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationDto getOrganizationBySlug(String slug) {
        Organization organization = organizationRepository.findBySlugAndIsDeletedFalse(slug)
                .orElseThrow(() -> new CustomException("Organization not found with slug: " + slug));
        return organizationMapper.toDto(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationDto> getAllOrganizations() {
        return organizationRepository.findAllByIsDeletedFalse().stream()
                .map(organizationMapper::toDto)
                .toList();
    }

    @Override
    public OrganizationDto updateOrganization(Long id, OrganizationDto dto) {
        Organization existingOrg = organizationRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Organization not found with ID: " + id));

        if (!existingOrg.getSlug().equals(dto.getSlug()) && organizationRepository.existsBySlug(dto.getSlug())) {
            throw new CustomException("Organization with slug '" + dto.getSlug() + "' already exists");
        }

        if (dto.getSubscriptionId() != null && !existingOrg.getSubscription().getId().equals(dto.getSubscriptionId())) {
            Subscription newSubscription = subscriptionRepository.findById(dto.getSubscriptionId())
                    .orElseThrow(() -> new CustomException("Subscription not found with ID: " + dto.getSubscriptionId()));
            existingOrg.setSubscription(newSubscription);
        }

        existingOrg.setName(dto.getName());
        existingOrg.setSlug(dto.getSlug());
        existingOrg.setDomain(dto.getDomain());
        if (dto.getStatus() != null) {
            existingOrg.setStatus(dto.getStatus());
        }

        Organization updatedOrg = organizationRepository.save(existingOrg);
        return organizationMapper.toDto(updatedOrg);
    }

    @Override
    public void deleteOrganization(Long id) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException("Organization not found with ID: " + id));
        organization.setIsDeleted(true);
        organizationRepository.save(organization);
    }
}

