package com.mailally.subscription.mapper;

import com.mailally.subscription.dto.SubscriptionResponseDto;
import com.mailally.subscription.entity.Subscription;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Subscription entity and SubscriptionResponseDto.
 */
@Component
public class SubscriptionMapper {

    public SubscriptionResponseDto toSubscriptionResponseDto(Subscription subscription) {
        if (subscription == null) return null;
        return SubscriptionResponseDto.builder()
                .id(subscription.getId())
                .organizationId(subscription.getOrganization() != null ? subscription.getOrganization().getId() : null)
                .name(subscription.getName())
                .code(subscription.getCode())
                .price(subscription.getPrice())
                .currency(subscription.getCurrency())
                .maxContacts(subscription.getMaxContacts())
                .maxEmailsPerMonth(subscription.getMaxEmailsPerMonth())
                .maxUsers(subscription.getMaxUsers())
                .maxCampaigns(subscription.getMaxCampaigns())
                .storageLimitMb(subscription.getStorageLimitMb())
                .apiLimit(subscription.getApiLimit())
                .aiLimit(subscription.getAiLimit())
                .status(subscription.getStatus())
                .trialEndsAt(subscription.getTrialEndsAt())
                .gracePeriodEndsAt(subscription.getGracePeriodEndsAt())
                .build();
    }
}
