package com.mailally.email.service;

import com.mailally.email.dto.BulkEmailRequestDto;
import com.mailally.email.dto.CampaignProgressDto;
import com.mailally.email.dto.DeliveryStatsDto;
import com.mailally.email.dto.EmailLogResponseDto;
import com.mailally.email.dto.EmailQueueResponseDto;
import com.mailally.email.dto.LaunchCampaignRequestDto;
import com.mailally.email.dto.ProviderHealthDto;
import com.mailally.email.dto.SendEmailRequestDto;
import com.mailally.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * Service interface for Email Engine delivery, queue management, progress tracking, and statistics.
 */
public interface EmailService {

    EmailLogResponseDto sendSingleEmail(CustomUserDetails currentUser, SendEmailRequestDto dto);

    List<EmailLogResponseDto> sendBulkEmail(CustomUserDetails currentUser, BulkEmailRequestDto dto);

    CampaignProgressDto launchCampaign(CustomUserDetails currentUser, LaunchCampaignRequestDto dto);

    /**
     * Launches campaign asynchronously in a background thread.
     * Returns initial progress immediately while emails are dispatched in background.
     */
    CampaignProgressDto launchCampaignAsync(CustomUserDetails currentUser, LaunchCampaignRequestDto dto);

    /**
     * Creates an SSE emitter to stream real-time campaign progress events.
     */
    SseEmitter streamCampaignProgress(Long campaignId, Long organizationId);

    CampaignProgressDto retryFailedEmails(CustomUserDetails currentUser, Long campaignId);

    CampaignProgressDto cancelSending(CustomUserDetails currentUser, Long campaignId);

    EmailLogResponseDto getEmailStatus(CustomUserDetails currentUser, Long emailLogId);

    CampaignProgressDto getCampaignProgress(CustomUserDetails currentUser, Long campaignId);

    Page<EmailQueueResponseDto> getQueueStatus(CustomUserDetails currentUser, Long campaignId, int page, int size);

    Page<EmailLogResponseDto> getEmailLogs(CustomUserDetails currentUser, Long campaignId, String status, String recipientEmail, int page, int size, String sortBy, String sortDir);

    List<ProviderHealthDto> getProviderHealth(CustomUserDetails currentUser);

    DeliveryStatsDto getDeliveryStats(CustomUserDetails currentUser, Long campaignId);
}

