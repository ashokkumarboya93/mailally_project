package com.mailally.email.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.email.dto.BulkEmailRequestDto;
import com.mailally.email.dto.CampaignProgressDto;
import com.mailally.email.dto.DeliveryStatsDto;
import com.mailally.email.dto.EmailLogResponseDto;
import com.mailally.email.dto.EmailQueueResponseDto;
import com.mailally.email.dto.LaunchCampaignRequestDto;
import com.mailally.email.dto.ProviderHealthDto;
import com.mailally.email.dto.SendEmailRequestDto;
import com.mailally.email.service.EmailService;
import com.mailally.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Email Engine delivery management, campaign launch, retry, queue, and stats.
 * Supports async campaign launch with SSE live progress streaming.
 */
@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<EmailLogResponseDto>> sendSingleEmail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SendEmailRequestDto dto) {
        EmailLogResponseDto result = emailService.sendSingleEmail(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<EmailLogResponseDto>builder()
                .success(true).message("Single email dispatched").data(result).timestamp(LocalDateTime.now()).build(),
                HttpStatus.OK);
    }

    @PostMapping("/send-bulk")
    public ResponseEntity<ApiResponse<List<EmailLogResponseDto>>> sendBulkEmail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody BulkEmailRequestDto dto) {
        List<EmailLogResponseDto> results = emailService.sendBulkEmail(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<List<EmailLogResponseDto>>builder()
                .success(true).message("Bulk email dispatch completed").data(results).timestamp(LocalDateTime.now()).build());
    }

    /**
     * Launches a campaign ASYNCHRONOUSLY. Returns immediately with initial RUNNING status.
     * Use the SSE stream endpoint to track live progress.
     */
    @PostMapping("/launch-campaign")
    public ResponseEntity<ApiResponse<CampaignProgressDto>> launchCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody LaunchCampaignRequestDto dto) {
        CampaignProgressDto progress = emailService.launchCampaignAsync(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<CampaignProgressDto>builder()
                .success(true).message("Campaign launch initiated — track progress via SSE stream").data(progress).timestamp(LocalDateTime.now()).build());
    }

    /**
     * SSE endpoint for live campaign progress streaming.
     * Returns a text/event-stream with real-time progress events.
     */
    @GetMapping(value = "/campaign-progress/{campaignId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamCampaignProgress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long campaignId) {
        return emailService.streamCampaignProgress(campaignId, userDetails.getOrganizationId());
    }

    @PostMapping("/retry/{campaignId}")
    public ResponseEntity<ApiResponse<CampaignProgressDto>> retryFailedEmails(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long campaignId) {
        CampaignProgressDto progress = emailService.retryFailedEmails(userDetails, campaignId);
        return ResponseEntity.ok(ApiResponse.<CampaignProgressDto>builder()
                .success(true).message("Failed email retry sequence completed").data(progress).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/cancel/{campaignId}")
    public ResponseEntity<ApiResponse<CampaignProgressDto>> cancelSending(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long campaignId) {
        CampaignProgressDto progress = emailService.cancelSending(userDetails, campaignId);
        return ResponseEntity.ok(ApiResponse.<CampaignProgressDto>builder()
                .success(true).message("Campaign sending cancelled").data(progress).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/status/{emailLogId}")
    public ResponseEntity<ApiResponse<EmailLogResponseDto>> getEmailStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long emailLogId) {
        EmailLogResponseDto result = emailService.getEmailStatus(userDetails, emailLogId);
        return ResponseEntity.ok(ApiResponse.<EmailLogResponseDto>builder()
                .success(true).message("Email status retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/campaign-progress/{campaignId}")
    public ResponseEntity<ApiResponse<CampaignProgressDto>> getCampaignProgress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long campaignId) {
        CampaignProgressDto progress = emailService.getCampaignProgress(userDetails, campaignId);
        return ResponseEntity.ok(ApiResponse.<CampaignProgressDto>builder()
                .success(true).message("Campaign progress retrieved").data(progress).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/queue-status/{campaignId}")
    public ResponseEntity<ApiResponse<Page<EmailQueueResponseDto>>> getQueueStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long campaignId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<EmailQueueResponseDto> queue = emailService.getQueueStatus(userDetails, campaignId, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<EmailQueueResponseDto>>builder()
                .success(true).message("Queue status retrieved").data(queue).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/logs")
    public ResponseEntity<ApiResponse<Page<EmailLogResponseDto>>> getEmailLogs(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long campaignId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String recipientEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<EmailLogResponseDto> logs = emailService.getEmailLogs(userDetails, campaignId, status, recipientEmail, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<EmailLogResponseDto>>builder()
                .success(true).message("Email logs retrieved").data(logs).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/provider-health")
    public ResponseEntity<ApiResponse<List<ProviderHealthDto>>> getProviderHealth(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ProviderHealthDto> health = emailService.getProviderHealth(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<ProviderHealthDto>>builder()
                .success(true).message("Provider health status retrieved").data(health).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/stats/{campaignId}")
    public ResponseEntity<ApiResponse<DeliveryStatsDto>> getDeliveryStats(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long campaignId) {
        DeliveryStatsDto stats = emailService.getDeliveryStats(userDetails, campaignId);
        return ResponseEntity.ok(ApiResponse.<DeliveryStatsDto>builder()
                .success(true).message("Delivery statistics calculated").data(stats).timestamp(LocalDateTime.now()).build());
    }
}
