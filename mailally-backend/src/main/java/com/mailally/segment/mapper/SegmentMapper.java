package com.mailally.segment.mapper;

import com.mailally.organization.entity.Organization;
import com.mailally.segment.dto.CreateSegmentRequestDto;
import com.mailally.segment.dto.SegmentResponseDto;
import com.mailally.segment.dto.UpdateSegmentRequestDto;
import com.mailally.segment.entity.Segment;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Segment entities and DTOs.
 */
@Component
public class SegmentMapper {

    public Segment toSegmentEntity(CreateSegmentRequestDto dto, Organization organization, Long createdByUserId) {
        if (dto == null) return null;
        return Segment.builder()
                .organization(organization)
                .name(dto.getName() != null ? dto.getName().trim() : null)
                .description(dto.getDescription())
                .type(dto.getType() != null ? dto.getType().trim().toUpperCase() : "STATIC")
                .rulesJson(dto.getRulesJson())
                .contactCount(0)
                .status("ACTIVE")
                .createdBy(createdByUserId)
                .updatedBy(createdByUserId)
                .isDeleted(false)
                .build();
    }

    public SegmentResponseDto toSegmentResponseDto(Segment segment) {
        if (segment == null) return null;
        return SegmentResponseDto.builder()
                .id(segment.getId())
                .organizationId(segment.getOrganization() != null ? segment.getOrganization().getId() : null)
                .name(segment.getName())
                .description(segment.getDescription())
                .type(segment.getType())
                .rulesJson(segment.getRulesJson())
                .contactCount(segment.getContactCount())
                .status(segment.getStatus())
                .createdBy(segment.getCreatedBy())
                .updatedBy(segment.getUpdatedBy())
                .createdAt(segment.getCreatedAt())
                .updatedAt(segment.getUpdatedAt())
                .build();
    }

    public void updateSegmentFromDto(Segment segment, UpdateSegmentRequestDto dto, Long updatedByUserId) {
        if (segment == null || dto == null) return;
        if (dto.getName() != null) segment.setName(dto.getName().trim());
        if (dto.getDescription() != null) segment.setDescription(dto.getDescription());
        if (dto.getRulesJson() != null) segment.setRulesJson(dto.getRulesJson());
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            segment.setStatus(dto.getStatus().trim().toUpperCase());
        }
        segment.setUpdatedBy(updatedByUserId);
    }
}
