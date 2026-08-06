package com.mailally.analytics.mapper;

import com.mailally.analytics.dto.CampaignAnalyticsDto;
import com.mailally.campaign.entity.Campaign;
import org.springframework.stereotype.Component;

/**
 * Mapper utility component converting campaign and email entities into analytics DTOs.
 */
@Component
public class AnalyticsMapper {

    public CampaignAnalyticsDto toCampaignAnalyticsDto(Campaign campaign, long sent, long failed, long pending) {
        if (campaign == null) return null;
        long totalRecipients = campaign.getTotalRecipients() != null ? campaign.getTotalRecipients() : (sent + failed + pending);
        double deliveryRate = totalRecipients > 0 ? ((double) sent / totalRecipients) * 100.0 : 0.0;
        double bounceRate = totalRecipients > 0 ? ((double) failed / totalRecipients) * 100.0 : 0.0;

        return CampaignAnalyticsDto.builder()
                .campaignId(campaign.getId())
                .campaignName(campaign.getName())
                .status(campaign.getStatus())
                .totalRecipients(totalRecipients)
                .sentCount(sent)
                .deliveredCount(sent)
                .failedCount(failed)
                .pendingCount(pending)
                .cancelledCount(0)
                .deliveryRate(Math.min(deliveryRate, 100.0))
                .bounceRate(Math.min(bounceRate, 100.0))
                .openRate(0.0)
                .clickRate(0.0)
                .complaintRate(0.0)
                .unsubscribeRate(0.0)
                .build();
    }
}
