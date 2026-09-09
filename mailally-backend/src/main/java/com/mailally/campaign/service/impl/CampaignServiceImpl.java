package com.mailally.campaign.service.impl;

import com.mailally.campaign.dto.CampaignResponseDto;
import com.mailally.campaign.dto.CreateCampaignRequestDto;
import com.mailally.campaign.dto.ScheduleCampaignRequestDto;
import com.mailally.campaign.dto.UpdateCampaignRequestDto;
import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.mapper.CampaignMapper;
import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.campaign.service.CampaignService;
import com.mailally.campaign.validator.CampaignValidator;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.segment.entity.Segment;
import com.mailally.segment.repository.SegmentRepository;
import com.mailally.template.entity.Template;
import com.mailally.template.repository.TemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service implementation for Campaign lifecycle management with template/segment attachment,
 * scheduling, cancellation, and soft deletion.
 */
@Service
@Transactional
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final OrganizationRepository organizationRepository;
    private final TemplateRepository templateRepository;
    private final SegmentRepository segmentRepository;
    private final CampaignValidator campaignValidator;
    private final CampaignMapper campaignMapper;
    private final com.mailally.scheduler.repository.SchedulerRepository schedulerRepository;
    private final com.mailally.notification.service.NotificationService notificationService;
    private final com.mailally.campaign.repository.CampaignRecipientRepository recipientRepository;
    private final com.mailally.contact.repository.ContactRepository contactRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository,
                               OrganizationRepository organizationRepository,
                               TemplateRepository templateRepository,
                               SegmentRepository segmentRepository,
                               CampaignValidator campaignValidator,
                               CampaignMapper campaignMapper,
                               com.mailally.scheduler.repository.SchedulerRepository schedulerRepository,
                               com.mailally.notification.service.NotificationService notificationService,
                               com.mailally.campaign.repository.CampaignRecipientRepository recipientRepository,
                               com.mailally.contact.repository.ContactRepository contactRepository) {
        this.campaignRepository = campaignRepository;
        this.organizationRepository = organizationRepository;
        this.templateRepository = templateRepository;
        this.segmentRepository = segmentRepository;
        this.campaignValidator = campaignValidator;
        this.campaignMapper = campaignMapper;
        this.schedulerRepository = schedulerRepository;
        this.notificationService = notificationService;
        this.recipientRepository = recipientRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public CampaignResponseDto createCampaign(CustomUserDetails currentUser, CreateCampaignRequestDto dto) {
        campaignValidator.validateAdminOrManager(currentUser);

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        Template template = null;
        if (dto.getTemplateId() != null) {
            template = templateRepository.findByIdAndOrganizationIdAndIsDeletedFalse(dto.getTemplateId(), currentUser.getOrganizationId())
                    .orElseThrow(() -> new CustomException("Template not found with ID: " + dto.getTemplateId()));
        }

        Segment segment = null;
        if (dto.getSegmentId() != null) {
            segment = segmentRepository.findByIdAndOrganizationIdAndIsDeletedFalse(dto.getSegmentId(), currentUser.getOrganizationId())
                    .orElseThrow(() -> new CustomException("Segment not found with ID: " + dto.getSegmentId()));
        }

        Campaign campaign = campaignMapper.toCampaignEntity(dto, org, template, segment, currentUser.getUserId());
        
        boolean isScheduled = dto.getScheduledAt() != null;
        if (isScheduled) {
            campaign.setStatus("SCHEDULED");
            campaign.setScheduledAt(dto.getScheduledAt());
        } else {
            campaign.setStatus("DRAFT");
        }

        Campaign saved = campaignRepository.save(campaign);

        if (dto.getContactIds() != null && !dto.getContactIds().isEmpty()) {
            final Campaign targetCampaign = saved;
            java.util.List<com.mailally.campaign.entity.CampaignRecipient> recipients = new java.util.ArrayList<>();
            for (Long cId : dto.getContactIds()) {
                var opt = contactRepository.findByIdAndOrganizationIdAndIsDeletedFalse(cId, currentUser.getOrganizationId());
                if (opt.isPresent()) {
                    com.mailally.campaign.entity.CampaignRecipient r = new com.mailally.campaign.entity.CampaignRecipient();
                    r.setCampaign(targetCampaign);
                    r.setContact(opt.get());
                    r.setOrganization(org);
                    r.setStatus("QUEUED");
                    recipients.add(r);
                }
            }
            if (!recipients.isEmpty()) {
                recipientRepository.saveAll(recipients);
                saved.setTotalRecipients(recipients.size());
                saved = campaignRepository.save(saved);
            }
        }

        if (isScheduled) {
            com.mailally.scheduler.entity.Scheduler scheduler = com.mailally.scheduler.entity.Scheduler.builder()
                    .organization(org)
                    .campaign(saved)
                    .executionType("SCHEDULED")
                    .status("SCHEDULED")
                    .scheduledTime(dto.getScheduledAt())
                    .createdBy(currentUser.getUserId())
                    .updatedBy(currentUser.getUserId())
                    .build();
            schedulerRepository.save(scheduler);

            try {
                notificationService.sendNotification(
                        currentUser.getOrganizationId(),
                        currentUser.getUserId(),
                        "CAMPAIGNS",
                        "Campaign Scheduled: " + saved.getName(),
                        "Campaign '" + saved.getName() + "' scheduled to launch automatically at " + dto.getScheduledAt() + ".",
                        "NORMAL",
                        "CAMPAIGNS",
                        saved.getId(),
                        "/scheduler"
                );
            } catch (Exception e) {
                // Ignore notification failure
            }
        } else {
            try {
                notificationService.sendNotification(
                        currentUser.getOrganizationId(),
                        currentUser.getUserId(),
                        "CAMPAIGNS",
                        "Campaign Created: " + saved.getName(),
                        "New instant/draft campaign '" + saved.getName() + "' created and ready for setup.",
                        "NORMAL",
                        "CAMPAIGNS",
                        saved.getId(),
                        "/campaigns"
                );
            } catch (Exception e) {
                // Ignore notification failure
            }
        }

        return campaignMapper.toCampaignResponseDto(saved);
    }

    @Override
    public CampaignResponseDto addContactsToCampaign(CustomUserDetails currentUser, Long campaignId, java.util.List<Long> contactIds) {
        campaignValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(campaignId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + campaignId));

        if (contactIds != null && !contactIds.isEmpty()) {
            final Campaign targetCampaign = campaign;
            java.util.List<com.mailally.campaign.entity.CampaignRecipient> newRecipients = new java.util.ArrayList<>();
            for (Long cId : contactIds) {
                if (!recipientRepository.existsByCampaignIdAndContactId(campaignId, cId)) {
                    var opt = contactRepository.findByIdAndOrganizationIdAndIsDeletedFalse(cId, currentUser.getOrganizationId());
                    if (opt.isPresent()) {
                        com.mailally.campaign.entity.CampaignRecipient r = new com.mailally.campaign.entity.CampaignRecipient();
                        r.setCampaign(targetCampaign);
                        r.setContact(opt.get());
                        r.setOrganization(targetCampaign.getOrganization());
                        r.setStatus("QUEUED");
                        newRecipients.add(r);
                    }
                }
            }
            if (!newRecipients.isEmpty()) {
                recipientRepository.saveAll(newRecipients);
                long totalCount = recipientRepository.countByCampaignId(campaignId);
                campaign.setTotalRecipients((int) totalCount);
                campaign = campaignRepository.save(campaign);
            }
        }

        return campaignMapper.toCampaignResponseDto(campaign);
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignResponseDto getCampaignById(CustomUserDetails currentUser, Long id) {
        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + id));
        return campaignMapper.toCampaignResponseDto(campaign);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CampaignResponseDto> listCampaigns(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Campaign> campaignsPage = campaignRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId(), pageable);
        return campaignsPage.map(campaignMapper::toCampaignResponseDto);
    }

    @Override
    public CampaignResponseDto updateCampaign(CustomUserDetails currentUser, Long id, UpdateCampaignRequestDto dto) {
        campaignValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + id));

        campaignValidator.validateCampaignEditable(campaign.getStatus());
        if (dto.getStatus() != null) campaignValidator.validateStatus(dto.getStatus());

        campaignMapper.updateCampaignFromDto(campaign, dto, currentUser.getUserId());
        Campaign updated = campaignRepository.save(campaign);
        return campaignMapper.toCampaignResponseDto(updated);
    }

    @Override
    public CampaignResponseDto attachTemplate(CustomUserDetails currentUser, Long campaignId, Long templateId) {
        campaignValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(campaignId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + campaignId));

        campaignValidator.validateCampaignEditable(campaign.getStatus());

        Template template = templateRepository.findByIdAndOrganizationIdAndIsDeletedFalse(templateId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Template not found with ID: " + templateId));

        campaign.setTemplate(template);
        campaign.setUpdatedBy(currentUser.getUserId());
        Campaign updated = campaignRepository.save(campaign);
        return campaignMapper.toCampaignResponseDto(updated);
    }

    @Override
    public CampaignResponseDto attachSegment(CustomUserDetails currentUser, Long campaignId, Long segmentId) {
        campaignValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(campaignId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + campaignId));

        campaignValidator.validateCampaignEditable(campaign.getStatus());

        Segment segment = segmentRepository.findByIdAndOrganizationIdAndIsDeletedFalse(segmentId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Segment not found with ID: " + segmentId));

        campaign.setSegment(segment);
        campaign.setTotalRecipients(segment.getContactCount() != null ? segment.getContactCount() : 0);
        campaign.setUpdatedBy(currentUser.getUserId());
        Campaign updated = campaignRepository.save(campaign);
        return campaignMapper.toCampaignResponseDto(updated);
    }

    @Override
    public CampaignResponseDto scheduleCampaign(CustomUserDetails currentUser, Long campaignId, ScheduleCampaignRequestDto dto) {
        campaignValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(campaignId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + campaignId));

        campaignValidator.validateCampaignSchedulable(campaign.getStatus());

        if (campaign.getTemplate() == null) {
            throw new CustomException("Cannot schedule campaign without an attached template");
        }
        if (campaign.getSegment() == null) {
            throw new CustomException("Cannot schedule campaign without an attached segment");
        }
        if (dto.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new CustomException("Scheduled time must be in the future");
        }

        campaign.setScheduledAt(dto.getScheduledAt());
        campaign.setStatus("SCHEDULED");
        campaign.setUpdatedBy(currentUser.getUserId());
        Campaign updated = campaignRepository.save(campaign);
        return campaignMapper.toCampaignResponseDto(updated);
    }

    @Override
    public CampaignResponseDto cancelCampaign(CustomUserDetails currentUser, Long campaignId) {
        campaignValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(campaignId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + campaignId));

        campaignValidator.validateCampaignCancellable(campaign.getStatus());

        campaign.setStatus("CANCELLED");
        campaign.setUpdatedBy(currentUser.getUserId());
        Campaign updated = campaignRepository.save(campaign);
        return campaignMapper.toCampaignResponseDto(updated);
    }

    @Override
    public void softDeleteCampaign(CustomUserDetails currentUser, Long id) {
        campaignValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + id));

        int rows = campaignRepository.softDeleteCampaignById(id, currentUser.getOrganizationId(), currentUser.getUserId(), LocalDateTime.now());
        if (rows == 0) {
            campaign.setIsDeleted(true);
            campaign.setDeletedBy(currentUser.getUserId());
            campaign.setDeletedAt(LocalDateTime.now());
            campaignRepository.save(campaign);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CampaignResponseDto> searchCampaigns(CustomUserDetails currentUser, String name, String status,
                                                     int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Campaign> results = campaignRepository.searchCampaigns(
                currentUser.getOrganizationId(),
                (name != null && !name.isBlank()) ? name.trim() : null,
                (status != null && !status.isBlank()) ? status.trim().toUpperCase() : null,
                pageable
        );
        return results.map(campaignMapper::toCampaignResponseDto);
    }
}
