package com.mailally.email.mapper;

import com.mailally.email.dto.EmailLogResponseDto;
import com.mailally.email.dto.EmailQueueResponseDto;
import com.mailally.email.entity.Email;
import com.mailally.email.entity.EmailQueue;
import org.springframework.stereotype.Component;

/**
 * Mapper component providing manual mapping between Email entities and DTOs.
 */
@Component
public class EmailMapper {

    public EmailLogResponseDto toEmailLogResponseDto(Email email) {
        if (email == null) return null;
        return EmailLogResponseDto.builder()
                .id(email.getId())
                .organizationId(email.getOrganization() != null ? email.getOrganization().getId() : null)
                .campaignId(email.getCampaign() != null ? email.getCampaign().getId() : null)
                .campaignName(email.getCampaign() != null ? email.getCampaign().getName() : null)
                .recipientEmail(email.getRecipientEmail())
                .recipientName(email.getRecipientName())
                .subject(email.getSubject())
                .provider(email.getProvider())
                .status(email.getStatus())
                .responseId(email.getResponseId())
                .errorMessage(email.getErrorMessage())
                .retryCount(email.getRetryCount())
                .maxRetries(email.getMaxRetries())
                .sentAt(email.getSentAt())
                .deliveredAt(email.getDeliveredAt())
                .openedAt(email.getOpenedAt())
                .clickedAt(email.getClickedAt())
                .bouncedAt(email.getBouncedAt())
                .failedAt(email.getFailedAt())
                .createdAt(email.getCreatedAt())
                .build();
    }

    public EmailQueueResponseDto toEmailQueueResponseDto(EmailQueue queue) {
        if (queue == null) return null;
        return EmailQueueResponseDto.builder()
                .id(queue.getId())
                .organizationId(queue.getOrganization() != null ? queue.getOrganization().getId() : null)
                .campaignId(queue.getCampaign() != null ? queue.getCampaign().getId() : null)
                .recipientEmail(queue.getRecipientEmail())
                .recipientName(queue.getRecipientName())
                .personalizedSubject(queue.getPersonalizedSubject())
                .provider(queue.getProvider())
                .status(queue.getStatus())
                .retryCount(queue.getRetryCount())
                .maxRetries(queue.getMaxRetries())
                .failureReason(queue.getFailureReason())
                .batchNumber(queue.getBatchNumber())
                .createdAt(queue.getCreatedAt())
                .processedAt(queue.getProcessedAt())
                .build();
    }
}
