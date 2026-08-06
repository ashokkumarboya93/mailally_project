package com.mailally.email.scheduler;

import com.mailally.campaign.entity.Campaign;
import com.mailally.campaign.repository.CampaignRepository;
import com.mailally.email.cache.RedisProgressCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scheduled sync task flushing Redis progress stats to the relational database.
 */
@Service
public class ProgressSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(ProgressSyncScheduler.class);

    private final CampaignRepository campaignRepository;
    private final RedisProgressCache progressCache;
    private final SimpMessagingTemplate messagingTemplate;

    public ProgressSyncScheduler(CampaignRepository campaignRepository,
                                  RedisProgressCache progressCache,
                                  SimpMessagingTemplate messagingTemplate) {
        this.campaignRepository = campaignRepository;
        this.progressCache = progressCache;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedDelay = 5000) // Execute sync task every 5 seconds
    @Transactional
    public void syncProgressToDatabase() {
        // Query running campaigns
        List<Campaign> runningCampaigns = campaignRepository.findByStatus("RUNNING");
        if (runningCampaigns.isEmpty()) {
            return;
        }

        log.debug("ProgressSyncScheduler: Syncing progress parameters for {} active campaigns", runningCampaigns.size());

        for (Campaign campaign : runningCampaigns) {
            Long id = campaign.getId();
            Map<String, Integer> progress = progressCache.getProgress(id);

            int sent = progress.get("sent");
            int failed = progress.get("failed");

            // Update database records
            campaign.setSentCount(sent);
            campaign.setFailedCount(failed);
            campaignRepository.save(campaign);

            // Push updated metrics over WebSocket STOMP topic channel
            Map<String, Object> payload = new HashMap<>();
            payload.put("campaignId", id);
            payload.put("status", "RUNNING");
            payload.put("sent", sent);
            payload.put("failed", failed);
            payload.put("total", campaign.getTotalRecipients());

            messagingTemplate.convertAndSend(String.format("/topic/campaigns/%d/progress", id), (Object) payload);
            
            log.info("ProgressSyncScheduler: Synced and published metrics for campaignId={}: sent={}, failed={}", id, sent, failed);
        }
    }
}
