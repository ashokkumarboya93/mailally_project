package com.mailally.email.service.impl;

import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.contact.entity.Contact;
import com.mailally.contact.repository.ContactRepository;
import com.mailally.email.config.EmailEngineConfig;
import com.mailally.email.dto.BulkEmailRequestDto;
import com.mailally.email.dto.CampaignProgressDto;
import com.mailally.email.dto.DeliveryStatsDto;
import com.mailally.email.dto.EmailLogResponseDto;
import com.mailally.email.dto.EmailQueueResponseDto;
import com.mailally.email.dto.LaunchCampaignRequestDto;
import com.mailally.email.dto.ProviderHealthDto;
import com.mailally.email.dto.SendEmailRequestDto;
import com.mailally.email.orchestrator.CampaignOrchestrator;
import com.mailally.email.entity.Email;
import com.mailally.email.entity.EmailQueue;
import com.mailally.email.mapper.EmailMapper;
import com.mailally.email.provider.EmailProvider;
import com.mailally.email.provider.EmailProviderFactory;
import com.mailally.email.provider.EmailSendResult;
import com.mailally.email.renderer.TemplateRenderer;
import com.mailally.email.repository.EmailQueueRepository;
import com.mailally.email.repository.EmailRepository;
import com.mailally.email.service.EmailService;
import com.mailally.email.validator.EmailValidator;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.template.entity.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enterprise Email Engine implementation.
 * Handles single sends, bulk dispatches, async campaign execution with SSE live progress streaming,
 * provider failover, delivery logging, retry handling, and stats aggregation.
 */
@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final EmailRepository emailRepository;
    private final EmailQueueRepository emailQueueRepository;
    private final CampaignRepository campaignRepository;
    private final ContactRepository contactRepository;
    private final OrganizationRepository organizationRepository;
    private final EmailProviderFactory providerFactory;
    private final TemplateRenderer templateRenderer;
    private final EmailValidator emailValidator;
    private final EmailMapper emailMapper;
    private final EmailEngineConfig config;
    private final CampaignAsyncExecutor campaignAsyncExecutor;
    private final CampaignOrchestrator campaignOrchestrator;

    public EmailServiceImpl(EmailRepository emailRepository,
                            EmailQueueRepository emailQueueRepository,
                            CampaignRepository campaignRepository,
                            ContactRepository contactRepository,
                            OrganizationRepository organizationRepository,
                            EmailProviderFactory providerFactory,
                            TemplateRenderer templateRenderer,
                            EmailValidator emailValidator,
                            EmailMapper emailMapper,
                            EmailEngineConfig config,
                            CampaignAsyncExecutor campaignAsyncExecutor,
                            CampaignOrchestrator campaignOrchestrator) {
        this.emailRepository = emailRepository;
        this.emailQueueRepository = emailQueueRepository;
        this.campaignRepository = campaignRepository;
        this.contactRepository = contactRepository;
        this.organizationRepository = organizationRepository;
        this.providerFactory = providerFactory;
        this.templateRenderer = templateRenderer;
        this.emailValidator = emailValidator;
        this.emailMapper = emailMapper;
        this.config = config;
        this.campaignAsyncExecutor = campaignAsyncExecutor;
        this.campaignOrchestrator = campaignOrchestrator;
    }

    @Override
    public EmailLogResponseDto sendSingleEmail(CustomUserDetails currentUser, SendEmailRequestDto dto) {
        emailValidator.validateAdminOrManager(currentUser);

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        String senderName = dto.getSenderName() != null ? dto.getSenderName() : config.getDefaultSenderName();
        String senderEmail = dto.getSenderEmail() != null ? dto.getSenderEmail() : config.getDefaultSenderEmail();

        EmailSendResult result;
        if (dto.getProvider() != null && !dto.getProvider().isBlank()) {
            EmailProvider provider = providerFactory.getProvider(dto.getProvider());
            result = provider.send(dto.getRecipientEmail(), dto.getRecipientName(), senderEmail, senderName,
                    dto.getReplyTo(), dto.getSubject(), dto.getHtmlBody());
        } else {
            result = providerFactory.sendWithFailover(dto.getRecipientEmail(), dto.getRecipientName(), senderEmail, senderName,
                    dto.getReplyTo(), dto.getSubject(), dto.getHtmlBody());
        }

        Email emailLog = Email.builder()
                .organization(org)
                .recipientEmail(dto.getRecipientEmail())
                .recipientName(dto.getRecipientName())
                .subject(dto.getSubject())
                .provider(result.getProviderName())
                .status(result.isSuccess() ? "SENT" : "FAILED")
                .responseId(result.getResponseId())
                .errorMessage(result.getErrorMessage())
                .sentAt(result.isSuccess() ? LocalDateTime.now() : null)
                .failedAt(result.isSuccess() ? null : LocalDateTime.now())
                .createdBy(currentUser.getUserId())
                .build();

        Email saved = emailRepository.save(emailLog);
        return emailMapper.toEmailLogResponseDto(saved);
    }

    @Override
    public List<EmailLogResponseDto> sendBulkEmail(CustomUserDetails currentUser, BulkEmailRequestDto dto) {
        emailValidator.validateAdminOrManager(currentUser);
        List<EmailLogResponseDto> results = Collections.synchronizedList(new ArrayList<>());
        if (dto.getEmails() != null && !dto.getEmails().isEmpty()) {
            try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
                for (SendEmailRequestDto singleDto : dto.getEmails()) {
                    executor.submit(() -> {
                        try {
                            results.add(sendSingleEmail(currentUser, singleDto));
                        } catch (Exception e) {
                            log.error("Bulk email error for {}: {}", singleDto.getRecipientEmail(), e.getMessage());
                        }
                    });
                }
            }
        }
        return results;
    }

    @Override
    public CampaignProgressDto launchCampaign(CustomUserDetails currentUser, LaunchCampaignRequestDto dto) {
        emailValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(dto.getCampaignId(), currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + dto.getCampaignId()));

        emailValidator.validateCampaignReadyForLaunch(campaign);

        campaign.setStatus("RUNNING");
        campaignRepository.save(campaign);

        List<Contact> contacts = contactRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId()).stream()
                .filter(contact -> "SUBSCRIBED".equalsIgnoreCase(contact.getStatus()) || "ACTIVE".equalsIgnoreCase(contact.getStatus()))
                .collect(Collectors.toList());
        Template template = campaign.getTemplate();

        String fromName = campaign.getFromName() != null ? campaign.getFromName()
                : campaign.getSenderName() != null ? campaign.getSenderName() : config.getDefaultSenderName();
        String fromEmail = campaign.getSenderEmail() != null ? campaign.getSenderEmail() : config.getDefaultSenderEmail();
        int batchSize = dto.getBatchSize() != null && dto.getBatchSize() > 0 ? dto.getBatchSize() : 500;

        int sent = 0;
        int failed = 0;
        int index = 0;

        for (Contact contact : contacts) {
            int batchNumber = (index / batchSize) + 1;
            String personalizedSubject = templateRenderer.render(
                    campaign.getSubject() != null ? campaign.getSubject() : template.getSubject(), contact);
            String personalizedBody = templateRenderer.render(template.getHtmlContent(), contact);

            EmailSendResult result = providerFactory.sendWithFailover(
                    contact.getEmail(),
                    contact.getFirstName(),
                    fromEmail,
                    fromName,
                    campaign.getReplyTo(),
                    personalizedSubject,
                    personalizedBody
            );

            Email emailLog = Email.builder()
                    .organization(campaign.getOrganization())
                    .campaign(campaign)
                    .contact(contact)
                    .recipientEmail(contact.getEmail())
                    .recipientName(contact.getFirstName())
                    .subject(personalizedSubject)
                    .provider(result.getProviderName())
                    .status(result.isSuccess() ? "SENT" : "FAILED")
                    .responseId(result.getResponseId())
                    .errorMessage(result.getErrorMessage())
                    .sentAt(result.isSuccess() ? LocalDateTime.now() : null)
                    .failedAt(result.isSuccess() ? null : LocalDateTime.now())
                    .createdBy(currentUser.getUserId())
                    .build();

            emailRepository.save(emailLog);
            emailQueueRepository.save(EmailQueue.builder()
                    .organization(campaign.getOrganization())
                    .campaign(campaign)
                    .contact(contact)
                    .recipientEmail(contact.getEmail())
                    .recipientName(contact.getFirstName())
                    .personalizedSubject(personalizedSubject)
                    .personalizedHtml(personalizedBody)
                    .provider(result.getProviderName())
                    .status(result.isSuccess() ? "SENT" : "FAILED")
                    .retryCount(0)
                    .maxRetries(config.getMaxRetries())
                    .failureReason(result.getErrorMessage())
                    .batchNumber(batchNumber)
                    .processedAt(LocalDateTime.now())
                    .createdBy(currentUser.getUserId())
                    .build());

            if (result.isSuccess()) {
                sent++;
            } else {
                failed++;
            }
            index++;
        }

        campaign.setTotalRecipients(contacts.size());
        campaign.setSentCount(sent);
        campaign.setFailedCount(failed);
        campaign.setStatus("COMPLETED");
        campaignRepository.save(campaign);

        double progress = contacts.size() > 0 ? ((double) (sent + failed) / contacts.size()) * 100.0 : 100.0;

        return CampaignProgressDto.builder()
                .campaignId(campaign.getId())
                .campaignName(campaign.getName())
                .campaignStatus(campaign.getStatus())
                .totalRecipients(contacts.size())
                .sentCount(sent)
                .failedCount(failed)
                .pendingCount(0)
                .progressPercentage(progress)
                .build();
    }

    // =====================================================================
    // ASYNC CAMPAIGN LAUNCH WITH LIVE SSE PROGRESS STREAMING
    // =====================================================================

    @Override
    public CampaignProgressDto launchCampaignAsync(CustomUserDetails currentUser, LaunchCampaignRequestDto dto) {
        emailValidator.validateAdminOrManager(currentUser);

        campaignOrchestrator.launchCampaign(
                dto.getCampaignId(),
                currentUser.getOrganizationId(),
                currentUser.getUserId(),
                dto.getOverrideProvider(),
                dto.getPriority()
        );

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(dto.getCampaignId(), currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found"));

        int total = campaign.getTotalRecipients() != null ? campaign.getTotalRecipients() : 0;

        return CampaignProgressDto.builder()
                .campaignId(campaign.getId())
                .campaignName(campaign.getName())
                .campaignStatus("QUEUED")
                .totalRecipients(total)
                .sentCount(0)
                .failedCount(0)
                .pendingCount(total)
                .progressPercentage(0.0)
                .build();
    }

    @Override
    public SseEmitter streamCampaignProgress(Long campaignId, Long organizationId) {
        // Verify campaign exists and belongs to organization
        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(campaignId, organizationId)
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + campaignId));

        // Delegate SSE emitter management to the async executor bean
        return campaignAsyncExecutor.createProgressEmitter(campaignId, campaign);
    }

    // =====================================================================
    // EXISTING METHODS (unchanged)
    // =====================================================================

    @Override
    public CampaignProgressDto retryFailedEmails(CustomUserDetails currentUser, Long campaignId) {
        emailValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(campaignId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + campaignId));

        List<Email> failedLogs = emailRepository.findByOrganizationIdAndCampaignIdAndStatus(
                currentUser.getOrganizationId(), campaignId, "FAILED");

        String fromName = campaign.getSenderName() != null ? campaign.getSenderName() : config.getDefaultSenderName();
        String fromEmail = campaign.getSenderEmail() != null ? campaign.getSenderEmail() : config.getDefaultSenderEmail();

        int reSentCount = 0;
        for (Email log : failedLogs) {
            if (log.getRetryCount() >= log.getMaxRetries()) {
                continue; // Max retries exceeded
            }

            EmailSendResult result = providerFactory.sendWithFailover(
                    log.getRecipientEmail(),
                    log.getRecipientName(),
                    fromEmail,
                    fromName,
                    campaign.getReplyTo(),
                    log.getSubject(),
                    campaign.getTemplate() != null ? campaign.getTemplate().getHtmlContent() : ""
            );

            log.setRetryCount(log.getRetryCount() + 1);
            if (result.isSuccess()) {
                log.setStatus("SENT");
                log.setResponseId(result.getResponseId());
                log.setSentAt(LocalDateTime.now());
                log.setErrorMessage(null);
                reSentCount++;
            } else {
                log.setErrorMessage(result.getErrorMessage());
                log.setFailedAt(LocalDateTime.now());
            }
            emailRepository.save(log);
        }

        if (reSentCount > 0) {
            long newSentTotal = emailRepository.countByOrganizationIdAndCampaignIdAndStatus(
                    currentUser.getOrganizationId(), campaignId, "SENT");
            long newFailedTotal = emailRepository.countByOrganizationIdAndCampaignIdAndStatus(
                    currentUser.getOrganizationId(), campaignId, "FAILED");
            campaign.setSentCount((int) newSentTotal);
            campaign.setFailedCount((int) newFailedTotal);
            campaignRepository.save(campaign);
        }

        return getCampaignProgress(currentUser, campaignId);
    }

    @Override
    public CampaignProgressDto cancelSending(CustomUserDetails currentUser, Long campaignId) {
        emailValidator.validateAdminOrManager(currentUser);

        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(campaignId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + campaignId));

        List<EmailQueue> pendingQueue = emailQueueRepository.findByOrganizationIdAndCampaignIdAndStatus(
                currentUser.getOrganizationId(), campaignId, "PENDING");

        for (EmailQueue queueItem : pendingQueue) {
            queueItem.setStatus("CANCELLED");
            emailQueueRepository.save(queueItem);
        }

        campaign.setStatus("CANCELLED");
        campaignRepository.save(campaign);

        return getCampaignProgress(currentUser, campaignId);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailLogResponseDto getEmailStatus(CustomUserDetails currentUser, Long emailLogId) {
        Email email = emailRepository.findByIdAndOrganizationId(emailLogId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Email log record not found with ID: " + emailLogId));
        return emailMapper.toEmailLogResponseDto(email);
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignProgressDto getCampaignProgress(CustomUserDetails currentUser, Long campaignId) {
        Campaign campaign = campaignRepository.findByIdAndOrganizationIdAndIsDeletedFalse(campaignId, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Campaign not found with ID: " + campaignId));

        long total = campaign.getTotalRecipients() != null ? campaign.getTotalRecipients() : 0;
        long sent = emailRepository.countByOrganizationIdAndCampaignIdAndStatus(currentUser.getOrganizationId(), campaignId, "SENT");
        long failed = emailRepository.countByOrganizationIdAndCampaignIdAndStatus(currentUser.getOrganizationId(), campaignId, "FAILED");
        long pending = emailQueueRepository.countByOrganizationIdAndCampaignIdAndStatus(currentUser.getOrganizationId(), campaignId, "PENDING");

        double progress = total > 0 ? ((double) (sent + failed) / total) * 100.0 : 0.0;

        return CampaignProgressDto.builder()
                .campaignId(campaign.getId())
                .campaignName(campaign.getName())
                .campaignStatus(campaign.getStatus())
                .totalRecipients((int) total)
                .sentCount((int) sent)
                .failedCount((int) failed)
                .pendingCount((int) pending)
                .progressPercentage(Math.min(progress, 100.0))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmailQueueResponseDto> getQueueStatus(CustomUserDetails currentUser, Long campaignId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<EmailQueue> queuePage = emailQueueRepository.findByOrganizationIdAndCampaignId(
                currentUser.getOrganizationId(), campaignId, pageable);
        return queuePage.map(emailMapper::toEmailQueueResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmailLogResponseDto> getEmailLogs(CustomUserDetails currentUser, Long campaignId, String status,
                                                  String recipientEmail, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Email> logs = emailRepository.searchEmailLogs(
                currentUser.getOrganizationId(),
                campaignId,
                (status != null && !status.isBlank()) ? status.trim().toUpperCase() : null,
                (recipientEmail != null && !recipientEmail.isBlank()) ? recipientEmail.trim() : null,
                pageable
        );
        return logs.map(emailMapper::toEmailLogResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderHealthDto> getProviderHealth(CustomUserDetails currentUser) {
        Map<String, EmailProvider> providers = providerFactory.getAllProviders();
        String activeProviderName = config.getActiveProvider();

        return providers.values().stream()
                .map(provider -> ProviderHealthDto.builder()
                        .providerName(provider.getProviderName())
                        .available(provider.isAvailable())
                        .active(provider.getProviderName().equalsIgnoreCase(activeProviderName))
                        .statusMessage(provider.isAvailable() ? "Healthy & Ready" : "Not Configured / Unavailable")
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryStatsDto getDeliveryStats(CustomUserDetails currentUser, Long campaignId) {
        long sent = emailRepository.countByOrganizationIdAndCampaignIdAndStatus(currentUser.getOrganizationId(), campaignId, "SENT");
        long failed = emailRepository.countByOrganizationIdAndCampaignIdAndStatus(currentUser.getOrganizationId(), campaignId, "FAILED");
        long total = sent + failed;

        double deliveryRate = total > 0 ? ((double) sent / total) * 100.0 : 0.0;
        double bounceRate = total > 0 ? ((double) failed / total) * 100.0 : 0.0;

        return DeliveryStatsDto.builder()
                .totalSent(sent)
                .totalDelivered(sent) // Delivered equals sent in SMTP MVP mode
                .totalBounced(0)
                .totalFailed(failed)
                .totalOpened(0)
                .totalClicked(0)
                .deliveryRate(deliveryRate)
                .bounceRate(bounceRate)
                .openRate(0.0)
                .clickRate(0.0)
                .build();
    }
}
