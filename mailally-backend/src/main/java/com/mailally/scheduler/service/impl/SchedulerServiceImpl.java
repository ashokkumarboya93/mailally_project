package com.mailally.scheduler.service.impl;

import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.email.dto.LaunchCampaignRequestDto;
import com.mailally.email.service.EmailService;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.scheduler.dto.LaunchNowRequestDto;
import com.mailally.scheduler.dto.RescheduleCampaignRequestDto;
import com.mailally.scheduler.dto.ScheduleCampaignRequestDto;
import com.mailally.scheduler.dto.SchedulerResponseDto;
import com.mailally.scheduler.dto.SchedulerStatsDto;
import com.mailally.scheduler.entity.Scheduler;
import com.mailally.scheduler.mapper.SchedulerMapper;
import com.mailally.scheduler.repository.SchedulerRepository;
import com.mailally.scheduler.service.SchedulerService;
import com.mailally.scheduler.validator.SchedulerValidator;
import com.mailally.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for Scheduler module.
 * Coordinates campaign execution by invoking the Email Engine.
 * Supports immediate launch, future scheduling, pause, resume, cancel, reschedule, and background triggers.
 */
@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerServiceImpl.class);

    private final SchedulerRepository schedulerRepository;
    private final CampaignRepository campaignRepository;
    private final OrganizationRepository organizationRepository;
    private final EmailService emailService;
    private final SchedulerValidator schedulerValidator;
    private final SchedulerMapper schedulerMapper;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository,
                                CampaignRepository campaignRepository,
                                OrganizationRepository organizationRepository,
                                EmailService emailService,
                                SchedulerValidator schedulerValidator,
                                SchedulerMapper schedulerMapper) {
        this.schedulerRepository = schedulerRepository;
        this.campaignRepository = campaignRepository;
        this.organizationRepository = organizationRepository;
        this.emailService = emailService;
        this.schedulerValidator = schedulerValidator;
        this.schedulerMapper = schedulerMapper;
    }

    @Override
    public SchedulerResponseDto scheduleCampaign(CustomUserDetails currentUser, ScheduleCampaignRequestDto dto) {
        schedulerValidator.validateAdminOrManager(currentUser);
        schedulerValidator.validateScheduledTimeInFuture(dto.getScheduledTime());

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(dto.getCampaignId(), currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + dto.getCampaignId()));

        schedulerValidator.validateCampaignForExecution(campaign);

        Scheduler scheduler = Scheduler.builder()
                .organization(org)
                .campaign(campaign)
                .executionType(dto.getExecutionType() != null ? dto.getExecutionType().toUpperCase() : "SCHEDULED")
                .status("SCHEDULED")
                .scheduledTime(dto.getScheduledTime())
                .createdBy(currentUser.getUserId())
                .updatedBy(currentUser.getUserId())
                .build();

        Scheduler saved = schedulerRepository.save(scheduler);

        campaign.setStatus("SCHEDULED");
        campaign.setScheduledAt(dto.getScheduledTime());
        campaignRepository.save(campaign);

        log.info("Campaign ID {} scheduled for execution at {}", campaign.getId(), dto.getScheduledTime());
        return schedulerMapper.toSchedulerResponseDto(saved);
    }

    @Override
    public SchedulerResponseDto launchImmediately(CustomUserDetails currentUser, LaunchNowRequestDto dto) {
        schedulerValidator.validateAdminOrManager(currentUser);

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(dto.getCampaignId(), currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + dto.getCampaignId()));

        schedulerValidator.validateCampaignForExecution(campaign);

        Scheduler scheduler = Scheduler.builder()
                .organization(org)
                .campaign(campaign)
                .executionType("IMMEDIATE")
                .status("RUNNING")
                .scheduledTime(LocalDateTime.now())
                .executedTime(LocalDateTime.now())
                .createdBy(currentUser.getUserId())
                .updatedBy(currentUser.getUserId())
                .build();

        Scheduler saved = schedulerRepository.save(scheduler);

        try {
            log.info("Triggering Email Engine launch for Campaign ID {}", campaign.getId());
            emailService.launchCampaign(currentUser, new LaunchCampaignRequestDto(campaign.getId()));
            saved.setStatus("COMPLETED");
        } catch (Exception ex) {
            log.error("Failed immediate launch for Campaign ID {}: {}", campaign.getId(), ex.getMessage(), ex);
            saved.setStatus("FAILED");
            saved.setErrorMessage(ex.getMessage());
        }

        Scheduler updated = schedulerRepository.save(saved);
        return schedulerMapper.toSchedulerResponseDto(updated);
    }

    @Override
    public SchedulerResponseDto rescheduleCampaign(CustomUserDetails currentUser, Long id, RescheduleCampaignRequestDto dto) {
        schedulerValidator.validateAdminOrManager(currentUser);
        schedulerValidator.validateScheduledTimeInFuture(dto.getNewScheduledTime());

        Scheduler scheduler = schedulerRepository.findByIdAndOrganizationId(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Scheduler record not found with ID: " + id));

        schedulerValidator.validateRescheduleState(scheduler.getStatus());

        scheduler.setScheduledTime(dto.getNewScheduledTime());
        scheduler.setStatus("SCHEDULED");
        scheduler.setUpdatedBy(currentUser.getUserId());

        Campaign campaign = scheduler.getCampaign();
        if (campaign != null) {
            campaign.setScheduledAt(dto.getNewScheduledTime());
            campaign.setStatus("SCHEDULED");
            campaignRepository.save(campaign);
        }

        Scheduler saved = schedulerRepository.save(scheduler);
        log.info("Scheduler ID {} rescheduled to {}", id, dto.getNewScheduledTime());
        return schedulerMapper.toSchedulerResponseDto(saved);
    }

    @Override
    public SchedulerResponseDto pauseScheduler(CustomUserDetails currentUser, Long id) {
        schedulerValidator.validateAdminOrManager(currentUser);

        Scheduler scheduler = schedulerRepository.findByIdAndOrganizationId(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Scheduler record not found with ID: " + id));

        schedulerValidator.validatePauseState(scheduler.getStatus());

        scheduler.setStatus("PAUSED");
        scheduler.setUpdatedBy(currentUser.getUserId());

        Campaign campaign = scheduler.getCampaign();
        if (campaign != null) {
            campaign.setStatus("PAUSED");
            campaignRepository.save(campaign);
        }

        Scheduler saved = schedulerRepository.save(scheduler);
        log.info("Scheduler ID {} paused", id);
        return schedulerMapper.toSchedulerResponseDto(saved);
    }

    @Override
    public SchedulerResponseDto resumeScheduler(CustomUserDetails currentUser, Long id) {
        schedulerValidator.validateAdminOrManager(currentUser);

        Scheduler scheduler = schedulerRepository.findByIdAndOrganizationId(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Scheduler record not found with ID: " + id));

        schedulerValidator.validateResumeState(scheduler.getStatus());

        scheduler.setStatus("SCHEDULED");
        scheduler.setUpdatedBy(currentUser.getUserId());

        Campaign campaign = scheduler.getCampaign();
        if (campaign != null) {
            campaign.setStatus("SCHEDULED");
            campaignRepository.save(campaign);
        }

        Scheduler saved = schedulerRepository.save(scheduler);
        log.info("Scheduler ID {} resumed", id);
        return schedulerMapper.toSchedulerResponseDto(saved);
    }

    @Override
    public SchedulerResponseDto cancelScheduler(CustomUserDetails currentUser, Long id) {
        schedulerValidator.validateAdminOrManager(currentUser);

        Scheduler scheduler = schedulerRepository.findByIdAndOrganizationId(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Scheduler record not found with ID: " + id));

        schedulerValidator.validateCancelState(scheduler.getStatus());

        scheduler.setStatus("CANCELLED");
        scheduler.setUpdatedBy(currentUser.getUserId());

        Campaign campaign = scheduler.getCampaign();
        if (campaign != null) {
            emailService.cancelSending(currentUser, campaign.getId());
        }

        Scheduler saved = schedulerRepository.save(scheduler);
        log.info("Scheduler ID {} cancelled", id);
        return schedulerMapper.toSchedulerResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SchedulerResponseDto getSchedulerById(CustomUserDetails currentUser, Long id) {
        Scheduler scheduler = schedulerRepository.findByIdAndOrganizationId(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Scheduler record not found with ID: " + id));
        return schedulerMapper.toSchedulerResponseDto(scheduler);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerResponseDto> listAllSchedules(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Scheduler> pageResult = schedulerRepository.findByOrganizationId(currentUser.getOrganizationId(), pageable);
        return pageResult.map(schedulerMapper::toSchedulerResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerResponseDto> getUpcomingSchedules(CustomUserDetails currentUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("scheduledTime").ascending());
        Page<Scheduler> pageResult = schedulerRepository.findByOrganizationIdAndScheduledTimeAfterAndStatusIn(
                currentUser.getOrganizationId(),
                LocalDateTime.now(),
                List.of("SCHEDULED", "WAITING"),
                pageable
        );
        return pageResult.map(schedulerMapper::toSchedulerResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerResponseDto> getExecutionHistory(CustomUserDetails currentUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<Scheduler> pageResult = schedulerRepository.findByOrganizationIdAndStatusIn(
                currentUser.getOrganizationId(),
                List.of("COMPLETED", "FAILED", "CANCELLED"),
                pageable
        );
        return pageResult.map(schedulerMapper::toSchedulerResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public SchedulerStatsDto getSchedulerStatistics(CustomUserDetails currentUser) {
        Long orgId = currentUser.getOrganizationId();
        long total = schedulerRepository.countByOrganizationId(orgId);
        long active = schedulerRepository.countByOrganizationIdAndStatus(orgId, "RUNNING");
        long scheduled = schedulerRepository.countByOrganizationIdAndStatus(orgId, "SCHEDULED");
        long waiting = schedulerRepository.countByOrganizationIdAndStatus(orgId, "WAITING");
        long completed = schedulerRepository.countByOrganizationIdAndStatus(orgId, "COMPLETED");
        long failed = schedulerRepository.countByOrganizationIdAndStatus(orgId, "FAILED");
        long paused = schedulerRepository.countByOrganizationIdAndStatus(orgId, "PAUSED");
        long cancelled = schedulerRepository.countByOrganizationIdAndStatus(orgId, "CANCELLED");

        return SchedulerStatsDto.builder()
                .totalSchedules(total)
                .activeSchedules(active)
                .upcomingSchedules(scheduled + waiting)
                .completedSchedules(completed)
                .failedSchedules(failed)
                .pausedSchedules(paused)
                .cancelledSchedules(cancelled)
                .build();
    }

    /**
     * Background periodic runner checking for due scheduled tasks and triggering Email Engine.
     */
    @Scheduled(fixedRate = 30000)
    @Override
    public void processDueSchedules() {
        List<Scheduler> dueSchedules = schedulerRepository.findByScheduledTimeBeforeAndStatusIn(
                LocalDateTime.now(),
                List.of("SCHEDULED", "WAITING")
        );

        if (dueSchedules.isEmpty()) {
            return;
        }

        log.info("Scheduler runner found {} due campaign tasks for dispatch", dueSchedules.size());
        for (Scheduler scheduler : dueSchedules) {
            try {
                scheduler.setStatus("RUNNING");
                scheduler.setExecutedTime(LocalDateTime.now());
                schedulerRepository.save(scheduler);

                Campaign campaign = scheduler.getCampaign();
                if (campaign != null) {
                    CustomUserDetails systemPrincipal = createSystemUserDetails(scheduler);
                    emailService.launchCampaign(systemPrincipal, new LaunchCampaignRequestDto(campaign.getId()));
                    scheduler.setStatus("COMPLETED");
                }
            } catch (Exception ex) {
                log.error("Scheduler background execution failed for Scheduler ID {}: {}", scheduler.getId(), ex.getMessage(), ex);
                scheduler.setStatus("FAILED");
                scheduler.setErrorMessage(ex.getMessage());
            }
            schedulerRepository.save(scheduler);
        }
    }

    private CustomUserDetails createSystemUserDetails(Scheduler scheduler) {
        com.mailally.user.entity.User systemUser = com.mailally.user.entity.User.builder()
                .id(scheduler.getCreatedBy() != null ? scheduler.getCreatedBy() : 1L)
                .email("system@mailally.com")
                .organization(scheduler.getOrganization())
                .role("ADMIN")
                .status("ACTIVE")
                .isDeleted(false)
                .build();
        return new CustomUserDetails(systemUser, "");
    }
}
