package com.mailally.organization.mapper;

import com.mailally.organization.dto.OrganizationDto;
import com.mailally.organization.entity.Organization;
import com.mailally.subscription.entity.Subscription;
import org.springframework.stereotype.Component;

/**
 * Component responsible for mapping between {@link Organization} entity and {@link OrganizationDto}.
 */
@Component
public class OrganizationMapper {

    public OrganizationDto toDto(Organization entity) {
        if (entity == null) {
            return null;
        }
        return OrganizationDto.builder()
                .id(entity.getId())
                .subscriptionId(entity.getSubscription() != null ? entity.getSubscription().getId() : null)
                .name(entity.getName())
                .slug(entity.getSlug())
                .domain(entity.getDomain())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .isDeleted(entity.getIsDeleted())
                .build();
    }

    public Organization toEntity(OrganizationDto dto, Subscription subscription) {
        if (dto == null) {
            return null;
        }
        return Organization.builder()
                .id(dto.getId())
                .subscription(subscription)
                .name(dto.getName())
                .slug(dto.getSlug())
                .domain(dto.getDomain())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .isDeleted(dto.getIsDeleted())
                .build();
    }
}

