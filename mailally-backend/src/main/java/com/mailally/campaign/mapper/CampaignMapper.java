package com.mailally.campaign.mapper;

import com.mailally.campaign.dto.CampaignResponseDto;
import com.mailally.campaign.dto.CreateCampaignRequestDto;
import com.mailally.campaign.dto.UpdateCampaignRequestDto;
import com.mailally.campaign.entity.Campaign;
import com.mailally.organization.entity.Organization;
import com.mailally.segment.entity.Segment;
import com.mailally.template.entity.Template;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Campaign entities and DTOs.
 */
@Component
public class CampaignMapper {

    public Campaign toCampaignEntity(CreateCampaignRequestDto dto, Organization organization,
                                     Template template, Segment segment, Long createdByUserId) {
        if (dto == null) return null;
        String effectiveEmail = (dto.getSenderEmail() != null && !dto.getSenderEmail().isBlank()) ? dto.getSenderEmail().trim() : "info@marcamor.com";
        String effectiveName = (dto.getSenderName() != null && !dto.getSenderName().isBlank()) ? dto.getSenderName().trim() : "Marcamor";

        return Campaign.builder()
                .organization(organization)
                .template(template)
                .segment(segment)
                .name(dto.getName() != null ? dto.getName().trim() : null)
                .subject(dto.getSubject())
                .senderName(effectiveName)
                .fromName(effectiveName)
                .fromEmail(effectiveEmail)
                .senderEmail(effectiveEmail)
                .replyTo(dto.getReplyTo() != null ? dto.getReplyTo() : effectiveEmail)
                .status("DRAFT")
                .totalRecipients(segment != null ? segment.getContactCount() : 0)
                .sentCount(0)
                .failedCount(0)
                .createdBy(createdByUserId)
                .updatedBy(createdByUserId)
                .isDeleted(false)
                .build();
    }

    public CampaignResponseDto toCampaignResponseDto(Campaign campaign) {
        if (campaign == null) return null;
        return CampaignResponseDto.builder()
                .id(campaign.getId())
                .organizationId(campaign.getOrganization() != null ? campaign.getOrganization().getId() : null)
                .templateId(campaign.getTemplate() != null ? campaign.getTemplate().getId() : null)
                .templateName(campaign.getTemplate() != null ? campaign.getTemplate().getName() : null)
                .segmentId(campaign.getSegment() != null ? campaign.getSegment().getId() : null)
                .segmentName(campaign.getSegment() != null ? campaign.getSegment().getName() : null)
                .name(campaign.getName())
                .subject(campaign.getSubject())
                .senderName(campaign.getSenderName())
                .senderEmail(campaign.getSenderEmail())
                .replyTo(campaign.getReplyTo())
                .status(campaign.getStatus())
                .scheduledAt(campaign.getScheduledAt())
                .totalRecipients(campaign.getTotalRecipients())
                .sentCount(campaign.getSentCount())
                .failedCount(campaign.getFailedCount())
                .createdBy(campaign.getCreatedBy())
                .updatedBy(campaign.getUpdatedBy())
                .createdAt(campaign.getCreatedAt())
                .updatedAt(campaign.getUpdatedAt())
                .build();
    }

    public void updateCampaignFromDto(Campaign campaign, UpdateCampaignRequestDto dto, Long updatedByUserId) {
        if (campaign == null || dto == null) return;
        if (dto.getName() != null) campaign.setName(dto.getName().trim());
        if (dto.getSubject() != null) campaign.setSubject(dto.getSubject());
        if (dto.getSenderName() != null) {
            campaign.setSenderName(dto.getSenderName());
            campaign.setFromName(dto.getSenderName());
        }
        if (dto.getSenderEmail() != null) {
            campaign.setSenderEmail(dto.getSenderEmail());
            campaign.setFromEmail(dto.getSenderEmail());
        }
        if (dto.getReplyTo() != null) campaign.setReplyTo(dto.getReplyTo());
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            campaign.setStatus(dto.getStatus().trim().toUpperCase());
        }
        campaign.setUpdatedBy(updatedByUserId);
    }
}
