package com.mailally.campaign.controller;

import com.mailally.campaign.dto.CampaignResponseDto;
import com.mailally.campaign.dto.CreateCampaignRequestDto;
import com.mailally.campaign.dto.ScheduleCampaignRequestDto;
import com.mailally.campaign.dto.UpdateCampaignRequestDto;
import com.mailally.campaign.service.CampaignService;
import com.mailally.common.response.ApiResponse;
import com.mailally.security.CustomUserDetails;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST Controller for Campaign Management APIs.
 */
@RestController
@RequestMapping("/api/v1/campaigns")
public class CampaignController {

    private final CampaignService campaignService;
    private final com.mailally.campaign.service.CampaignBatchProcessor batchProcessor;
    private final com.mailally.campaign.service.CampaignDiagnosticsService diagnosticsService;
    private final com.mailally.contact.repository.ContactRepository contactRepository;
    private final com.mailally.campaign.repository.CampaignRecipientRepository recipientRepository;
    private final com.mailally.organization.repository.OrganizationRepository organizationRepository;
    private final com.mailally.campaign.repository.CampaignRepository campaignRepository;

    public CampaignController(CampaignService campaignService,
                              com.mailally.campaign.service.CampaignBatchProcessor batchProcessor,
                              com.mailally.campaign.service.CampaignDiagnosticsService diagnosticsService,
                              com.mailally.contact.repository.ContactRepository contactRepository,
                              com.mailally.campaign.repository.CampaignRecipientRepository recipientRepository,
                              com.mailally.organization.repository.OrganizationRepository organizationRepository,
                              com.mailally.campaign.repository.CampaignRepository campaignRepository) {
        this.campaignService = campaignService;
        this.batchProcessor = batchProcessor;
        this.diagnosticsService = diagnosticsService;
        this.contactRepository = contactRepository;
        this.recipientRepository = recipientRepository;
        this.organizationRepository = organizationRepository;
        this.campaignRepository = campaignRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CampaignResponseDto>> createCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateCampaignRequestDto dto) {
        CampaignResponseDto result = campaignService.createCampaign(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<CampaignResponseDto>builder()
                .success(true).message("Campaign created successfully").data(result).timestamp(LocalDateTime.now()).build(),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CampaignResponseDto>> getCampaignById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        CampaignResponseDto result = campaignService.getCampaignById(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<CampaignResponseDto>builder()
                .success(true).message("Campaign retrieved successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CampaignResponseDto>>> listCampaigns(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<CampaignResponseDto> result = campaignService.listCampaigns(userDetails, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<CampaignResponseDto>>builder()
                .success(true).message("Campaigns retrieved successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CampaignResponseDto>> updateCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCampaignRequestDto dto) {
        CampaignResponseDto result = campaignService.updateCampaign(userDetails, id, dto);
        return ResponseEntity.ok(ApiResponse.<CampaignResponseDto>builder()
                .success(true).message("Campaign updated successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/{id}/attach-template/{templateId}")
    public ResponseEntity<ApiResponse<CampaignResponseDto>> attachTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @PathVariable Long templateId) {
        CampaignResponseDto result = campaignService.attachTemplate(userDetails, id, templateId);
        return ResponseEntity.ok(ApiResponse.<CampaignResponseDto>builder()
                .success(true).message("Template attached to campaign successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/{id}/attach-segment/{segmentId}")
    public ResponseEntity<ApiResponse<CampaignResponseDto>> attachSegment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @PathVariable Long segmentId) {
        CampaignResponseDto result = campaignService.attachSegment(userDetails, id, segmentId);
        return ResponseEntity.ok(ApiResponse.<CampaignResponseDto>builder()
                .success(true).message("Segment attached to campaign successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/{id}/schedule")
    public ResponseEntity<ApiResponse<CampaignResponseDto>> scheduleCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody ScheduleCampaignRequestDto dto) {
        CampaignResponseDto result = campaignService.scheduleCampaign(userDetails, id, dto);
        return ResponseEntity.ok(ApiResponse.<CampaignResponseDto>builder()
                .success(true).message("Campaign scheduled successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<CampaignResponseDto>> cancelCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        CampaignResponseDto result = campaignService.cancelCampaign(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<CampaignResponseDto>builder()
                .success(true).message("Campaign cancelled successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        campaignService.softDeleteCampaign(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Campaign soft deleted successfully").data(null).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CampaignResponseDto>>> searchCampaigns(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<CampaignResponseDto> result = campaignService.searchCampaigns(userDetails, name, status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<CampaignResponseDto>>builder()
                .success(true).message("Campaign search completed").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/{id}/add-collection/{collectionId}")
    public ResponseEntity<ApiResponse<Long>> addCollectionToCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @PathVariable Long collectionId) {
        List<com.mailally.contact.entity.Contact> contacts = contactRepository.findByOrganizationIdAndCollectionIdAndIsDeletedFalse(userDetails.getOrganizationId(), collectionId);
        com.mailally.campaign.entity.Campaign campaign = campaignRepository.findById(id).orElse(null);

        long added = 0;
        if (campaign != null && contacts != null && !contacts.isEmpty()) {
            java.util.List<com.mailally.campaign.entity.CampaignRecipient> existingRecipients = recipientRepository.findByCampaignId(id);
            java.util.Set<Long> existingContactIds = existingRecipients.stream()
                    .map(r -> r.getContact().getId())
                    .collect(java.util.stream.Collectors.toSet());

            java.util.List<com.mailally.campaign.entity.CampaignRecipient> newRecipients = new java.util.ArrayList<>();
            com.mailally.organization.entity.Organization org = campaign.getOrganization();

            java.util.Set<Long> addedContactIds = new java.util.HashSet<>(existingContactIds);
            for (com.mailally.contact.entity.Contact c : contacts) {
                if (!addedContactIds.contains(c.getId())) {
                    com.mailally.campaign.entity.CampaignRecipient recipient = new com.mailally.campaign.entity.CampaignRecipient();
                    recipient.setCampaign(campaign);
                    recipient.setContact(c);
                    recipient.setOrganization(org);
                    newRecipients.add(recipient);
                    addedContactIds.add(c.getId());
                }
            }

            if (!newRecipients.isEmpty()) {
                recipientRepository.saveAll(newRecipients);
                added = newRecipients.size();
            }

            long totalCount = recipientRepository.countByCampaignId(id);
            campaign.setTotalRecipients((int) totalCount);
            campaignRepository.save(campaign);
        }

        return ResponseEntity.ok(ApiResponse.<Long>builder()
                .success(true).message("Collection added to campaign successfully").data(added).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/{id}/diagnostics")
    public ResponseEntity<ApiResponse<com.mailally.campaign.dto.CampaignDiagnosticsDto>> getDiagnostics(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        com.mailally.campaign.dto.CampaignDiagnosticsDto dto = diagnosticsService.runDiagnostics(id, userDetails.getOrganizationId());
        return ResponseEntity.ok(ApiResponse.<com.mailally.campaign.dto.CampaignDiagnosticsDto>builder()
                .success(true).message("Campaign diagnostics completed").data(dto).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/{id}/launch")
    public ResponseEntity<ApiResponse<Void>> launchCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        batchProcessor.executeCampaignAsync(id, userDetails.getOrganizationId());
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Campaign execution launched successfully").timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/{id}/live-progress")
    public ResponseEntity<ApiResponse<com.mailally.campaign.dto.CampaignLiveProgressDto>> getLiveProgress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        com.mailally.campaign.dto.CampaignLiveProgressDto progress = batchProcessor.getLiveProgress(id);
        return ResponseEntity.ok(ApiResponse.<com.mailally.campaign.dto.CampaignLiveProgressDto>builder()
                .success(true).message("Live progress retrieved").data(progress).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/{id}/failures")
    public ResponseEntity<ApiResponse<List<com.mailally.campaign.dto.CampaignFailureDetailDto>>> getCampaignFailures(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        List<com.mailally.campaign.dto.CampaignFailureDetailDto> failures = batchProcessor.getFailedRecipients(id);
        return ResponseEntity.ok(ApiResponse.<List<com.mailally.campaign.dto.CampaignFailureDetailDto>>builder()
                .success(true).message("Failed recipient details retrieved").data(failures).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/{id}/control")
    public ResponseEntity<ApiResponse<Void>> controlCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @RequestParam String action) {
        if ("PAUSE".equalsIgnoreCase(action)) {
            batchProcessor.pauseCampaign(id);
        } else if ("RESUME".equalsIgnoreCase(action)) {
            batchProcessor.resumeCampaign(id);
        } else if ("CANCEL".equalsIgnoreCase(action)) {
            batchProcessor.cancelCampaign(id);
        }
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Campaign control action executed").timestamp(LocalDateTime.now()).build());
    }
}
