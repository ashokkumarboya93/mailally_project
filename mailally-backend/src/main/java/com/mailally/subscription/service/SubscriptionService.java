package com.mailally.subscription.service;

import com.mailally.security.CustomUserDetails;
import com.mailally.subscription.dto.PlanUpgradeRequestDto;
import com.mailally.subscription.dto.QuotaCheckResponseDto;
import com.mailally.subscription.dto.SubscriptionResponseDto;

/**
 * Service interface for organization plan management, tier upgrades, renewals, and quota enforcement.
 */
public interface SubscriptionService {

    SubscriptionResponseDto getOrganizationSubscription(CustomUserDetails currentUser);

    SubscriptionResponseDto upgradePlan(CustomUserDetails currentUser, PlanUpgradeRequestDto dto);

    SubscriptionResponseDto downgradePlan(CustomUserDetails currentUser, PlanUpgradeRequestDto dto);

    SubscriptionResponseDto renewSubscription(CustomUserDetails currentUser);

    QuotaCheckResponseDto checkQuota(CustomUserDetails currentUser, String feature);

    boolean canCreateCampaign(CustomUserDetails currentUser);

    boolean canSendEmails(CustomUserDetails currentUser, long emailCount);

    boolean canImportContacts(CustomUserDetails currentUser, long contactCount);

    boolean canUseAI(CustomUserDetails currentUser);

    boolean hasRemainingQuota(CustomUserDetails currentUser, String featureName);
}
