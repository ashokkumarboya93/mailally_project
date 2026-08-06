package com.mailally.subscription.service.impl;

import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.contact.repository.ContactRepository;
import com.mailally.email.repository.EmailRepository;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.subscription.dto.PlanUpgradeRequestDto;
import com.mailally.subscription.dto.QuotaCheckResponseDto;
import com.mailally.subscription.dto.SubscriptionResponseDto;
import com.mailally.subscription.entity.Subscription;
import com.mailally.subscription.mapper.SubscriptionMapper;
import com.mailally.subscription.repository.SubscriptionRepository;
import com.mailally.subscription.service.SubscriptionService;
import com.mailally.subscription.validator.SubscriptionValidator;
import com.mailally.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service implementation for organization plan management, tier upgrades, renewals, and quota enforcement.
 */
@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    private final SubscriptionRepository subscriptionRepository;
    private final OrganizationRepository organizationRepository;
    private final ContactRepository contactRepository;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final SubscriptionValidator subscriptionValidator;
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository,
                                   OrganizationRepository organizationRepository,
                                   ContactRepository contactRepository,
                                   CampaignRepository campaignRepository,
                                   UserRepository userRepository,
                                   EmailRepository emailRepository,
                                   SubscriptionValidator subscriptionValidator,
                                   SubscriptionMapper subscriptionMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.organizationRepository = organizationRepository;
        this.contactRepository = contactRepository;
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.subscriptionValidator = subscriptionValidator;
        this.subscriptionMapper = subscriptionMapper;
    }

    @Override
    public SubscriptionResponseDto getOrganizationSubscription(CustomUserDetails currentUser) {
        subscriptionValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();

        Subscription sub = subscriptionRepository.findByOrganizationIdAndIsDeletedFalse(orgId)
                .orElseGet(() -> {
                    Organization org = organizationRepository.findById(orgId)
                            .orElseThrow(() -> new CustomException("Organization not found"));
                    return initializeDefaultFreeSubscription(org);
                });

        return subscriptionMapper.toSubscriptionResponseDto(sub);
    }

    @Override
    public SubscriptionResponseDto upgradePlan(CustomUserDetails currentUser, PlanUpgradeRequestDto dto) {
        subscriptionValidator.validateAdminRole(currentUser);
        subscriptionValidator.validatePlanCode(dto.getPlanCode());
        Long orgId = currentUser.getOrganizationId();

        Subscription sub = subscriptionRepository.findByOrganizationIdAndIsDeletedFalse(orgId)
                .orElseGet(() -> {
                    Organization org = organizationRepository.findById(orgId)
                            .orElseThrow(() -> new CustomException("Organization not found"));
                    return initializeDefaultFreeSubscription(org);
                });

        applyPlanTier(sub, dto.getPlanCode().trim().toUpperCase());

        Subscription saved = subscriptionRepository.save(sub);
        log.info("Upgraded Organization ID {} to Plan Tier '{}'", orgId, saved.getCode());
        return subscriptionMapper.toSubscriptionResponseDto(saved);
    }

    @Override
    public SubscriptionResponseDto downgradePlan(CustomUserDetails currentUser, PlanUpgradeRequestDto dto) {
        return upgradePlan(currentUser, dto);
    }

    @Override
    public SubscriptionResponseDto renewSubscription(CustomUserDetails currentUser) {
        subscriptionValidator.validateAdminRole(currentUser);
        Long orgId = currentUser.getOrganizationId();

        Subscription sub = subscriptionRepository.findByOrganizationIdAndIsDeletedFalse(orgId)
                .orElseThrow(() -> new CustomException("Subscription record not found"));

        sub.setStatus("ACTIVE");
        sub.setTrialEndsAt(null);
        sub.setGracePeriodEndsAt(null);

        Subscription saved = subscriptionRepository.save(sub);
        return subscriptionMapper.toSubscriptionResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QuotaCheckResponseDto checkQuota(CustomUserDetails currentUser, String feature) {
        subscriptionValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();

        Subscription sub = subscriptionRepository.findByOrganizationIdAndIsDeletedFalse(orgId)
                .orElseGet(() -> initializeDefaultFreeSubscription(null));

        String feat = feature != null ? feature.trim().toUpperCase() : "EMAILS";
        long current = 0;
        long limit = 0;

        switch (feat) {
            case "CAMPAIGNS":
                current = campaignRepository.countByOrganizationIdAndIsDeletedFalse(orgId);
                limit = sub.getMaxCampaigns();
                break;
            case "CONTACTS":
                current = contactRepository.countByOrganizationIdAndIsDeletedFalse(orgId);
                limit = sub.getMaxContacts();
                break;
            case "USERS":
                current = userRepository.countByOrganizationIdAndIsDeletedFalse(orgId);
                limit = sub.getMaxUsers();
                break;
            case "AI":
                current = 0; // AI usage
                limit = sub.getAiLimit();
                break;
            case "EMAILS":
            default:
                current = emailRepository.countByOrganizationIdAndCampaignId(orgId, null);
                limit = sub.getMaxEmailsPerMonth();
                break;
        }

        long remaining = Math.max(0, limit - current);
        boolean allowed = current < limit;

        return new QuotaCheckResponseDto(allowed, feat, current, limit, remaining);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canCreateCampaign(CustomUserDetails currentUser) {
        return checkQuota(currentUser, "CAMPAIGNS").isAllowed();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSendEmails(CustomUserDetails currentUser, long emailCount) {
        QuotaCheckResponseDto quota = checkQuota(currentUser, "EMAILS");
        return quota.getRemaining() >= emailCount;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canImportContacts(CustomUserDetails currentUser, long contactCount) {
        QuotaCheckResponseDto quota = checkQuota(currentUser, "CONTACTS");
        return quota.getRemaining() >= contactCount;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canUseAI(CustomUserDetails currentUser) {
        return checkQuota(currentUser, "AI").isAllowed();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRemainingQuota(CustomUserDetails currentUser, String featureName) {
        return checkQuota(currentUser, featureName).isAllowed();
    }

    private Subscription initializeDefaultFreeSubscription(Organization org) {
        Subscription sub = Subscription.builder()
                .organization(org)
                .name("Free Plan")
                .code("FREE")
                .price(BigDecimal.ZERO)
                .currency("USD")
                .maxContacts(1000)
                .maxEmailsPerMonth(5000)
                .maxUsers(2)
                .maxCampaigns(10)
                .storageLimitMb(100)
                .apiLimit(1000)
                .aiLimit(50)
                .status("ACTIVE")
                .build();
        if (org != null) {
            return subscriptionRepository.save(sub);
        }
        return sub;
    }

    private void applyPlanTier(Subscription sub, String code) {
        sub.setCode(code);
        switch (code) {
            case "STARTER":
                sub.setName("Starter Plan");
                sub.setPrice(new BigDecimal("29.00"));
                sub.setMaxContacts(5000);
                sub.setMaxEmailsPerMonth(25000);
                sub.setMaxUsers(5);
                sub.setMaxCampaigns(50);
                sub.setStorageLimitMb(500);
                sub.setApiLimit(5000);
                sub.setAiLimit(250);
                break;
            case "PRO":
                sub.setName("Professional Plan");
                sub.setPrice(new BigDecimal("99.00"));
                sub.setMaxContacts(25000);
                sub.setMaxEmailsPerMonth(150000);
                sub.setMaxUsers(15);
                sub.setMaxCampaigns(200);
                sub.setStorageLimitMb(2000);
                sub.setApiLimit(25000);
                sub.setAiLimit(1000);
                break;
            case "BUSINESS":
                sub.setName("Business Plan");
                sub.setPrice(new BigDecimal("299.00"));
                sub.setMaxContacts(100000);
                sub.setMaxEmailsPerMonth(750000);
                sub.setMaxUsers(50);
                sub.setMaxCampaigns(1000);
                sub.setStorageLimitMb(10000);
                sub.setApiLimit(100000);
                sub.setAiLimit(5000);
                break;
            case "ENTERPRISE":
                sub.setName("Enterprise Plan");
                sub.setPrice(new BigDecimal("999.00"));
                sub.setMaxContacts(1000000);
                sub.setMaxEmailsPerMonth(10000000);
                sub.setMaxUsers(500);
                sub.setMaxCampaigns(10000);
                sub.setStorageLimitMb(100000);
                sub.setApiLimit(1000000);
                sub.setAiLimit(50000);
                break;
            case "FREE":
            default:
                sub.setName("Free Plan");
                sub.setPrice(BigDecimal.ZERO);
                sub.setMaxContacts(1000);
                sub.setMaxEmailsPerMonth(5000);
                sub.setMaxUsers(2);
                sub.setMaxCampaigns(10);
                sub.setStorageLimitMb(100);
                sub.setApiLimit(1000);
                sub.setAiLimit(50);
                break;
        }
    }
}
