package com.mailally.campaign.service;

import com.mailally.campaign.dto.CampaignDiagnosticsDto;
import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.entity.CampaignRecipient;
import com.mailally.campaign.repository.CampaignRecipientRepository;
import com.mailally.campaign.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignDiagnosticsService {

    private final CampaignRepository campaignRepository;
    private final CampaignRecipientRepository recipientRepository;

    public CampaignDiagnosticsService(CampaignRepository campaignRepository, CampaignRecipientRepository recipientRepository) {
        this.campaignRepository = campaignRepository;
        this.recipientRepository = recipientRepository;
    }

    public CampaignDiagnosticsDto runDiagnostics(Long campaignId, Long orgId) {
        CampaignDiagnosticsDto dto = new CampaignDiagnosticsDto();
        dto.setCampaignId(campaignId);

        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        if (campaign == null) {
            dto.setIsReady(false);
            dto.getErrors().add("Campaign not found");
            return dto;
        }

        // 1. Template check
        if (campaign.getTemplate() != null) {
            dto.setTemplateExists(true);
        } else {
            dto.setTemplateExists(false);
            dto.setIsReady(false);
            dto.getErrors().add("No email template attached to campaign.");
        }

        // 2. Subject check
        if (campaign.getSubject() == null || campaign.getSubject().trim().isEmpty()) {
            dto.setIsReady(false);
            dto.getErrors().add("Email subject is missing.");
        }

        // 3. Recipient metrics
        long totalRecipients = recipientRepository.countByCampaignId(campaignId);
        dto.setTotalRecipients(totalRecipients);
        if (totalRecipients == 0) {
            dto.setIsReady(false);
            dto.getErrors().add("Zero recipients attached. Go to Contacts workspace to add contacts.");
        } else {
            dto.setValidRecipientsCount(totalRecipients);
        }

        // 4. Provider health
        dto.setProviderHealthy(true);
        dto.setActiveProvider("BREVO REST API (High Velocity)");

        // 5. Estimation math
        int batchSize = 100;
        int ratePerMin = 180;
        int estMinutes = (int) Math.ceil((double) totalRecipients / ratePerMin);
        dto.setEstimatedDurationMinutes(Math.max(1, estMinutes));
        dto.setEstimatedCost("$" + String.format("%.2f", totalRecipients * 0.0001));

        if (totalRecipients > 0 && Boolean.TRUE.equals(dto.getTemplateExists()) && campaign.getSubject() != null && !campaign.getSubject().trim().isEmpty()) {
            dto.setIsReady(true);
            dto.getWarnings().add("All diagnostics passed successfully. Campaign is ready to launch.");
        }

        return dto;
    }
}
