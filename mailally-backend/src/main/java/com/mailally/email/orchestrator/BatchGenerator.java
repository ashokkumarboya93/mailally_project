package com.mailally.email.orchestrator;

import com.mailally.campaign.entity.Campaign;
import com.mailally.contact.entity.Contact;
import com.mailally.contact.repository.ContactRepository;
import com.mailally.email.entity.CampaignBatch;
import com.mailally.email.entity.CampaignRecipientLog;
import com.mailally.email.repository.CampaignBatchRepository;
import com.mailally.email.repository.CampaignRecipientLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for chunking contact lists and enqueuing batches to Redis Streams.
 */
@Service
@Transactional
public class BatchGenerator {

    private static final Logger log = LoggerFactory.getLogger(BatchGenerator.class);

    private final ContactRepository contactRepository;
    private final CampaignBatchRepository batchRepository;
    private final CampaignRecipientLogRepository recipientRepository;
    private final StringRedisTemplate redisTemplate;

    public BatchGenerator(ContactRepository contactRepository,
                          CampaignBatchRepository batchRepository,
                          CampaignRecipientLogRepository recipientRepository,
                          StringRedisTemplate redisTemplate) {
        this.contactRepository = contactRepository;
        this.batchRepository = batchRepository;
        this.recipientRepository = recipientRepository;
        this.redisTemplate = redisTemplate;
    }

    public void generateAndQueueBatches(Campaign campaign, String provider, Long userId, String priority) {
        log.info("BatchGenerator: Resolving contacts for campaign: {}", campaign.getName());

        List<Contact> contacts = contactRepository.findByOrganizationIdAndIsDeletedFalse(campaign.getOrganization().getId()).stream()
                .filter(c -> c.getEmail() != null && !c.getEmail().isBlank() && c.getEmail().contains("@"))
                .filter(c -> c.getStatus() == null || (
                        !"UNSUBSCRIBED".equalsIgnoreCase(c.getStatus()) &&
                        !"BOUNCED".equalsIgnoreCase(c.getStatus()) &&
                        !"SPAM".equalsIgnoreCase(c.getStatus()) &&
                        !"INACTIVE".equalsIgnoreCase(c.getStatus())
                ))
                .collect(Collectors.toList());

        int totalContacts = contacts.size();
        log.info("BatchGenerator: Found {} eligible contacts for campaignId={}", totalContacts, campaign.getId());

        int batchSize = getOptimalBatchSize(provider);
        int totalBatches = (int) Math.ceil((double) totalContacts / batchSize);

        log.info("BatchGenerator: Chunking into {} batches (batchSize={}) for provider: {}", totalBatches, batchSize, provider);

        for (int i = 0; i < totalBatches; i++) {
            int fromIndex = i * batchSize;
            int toIndex = Math.min(fromIndex + batchSize, totalContacts);
            List<Contact> batchContacts = contacts.subList(fromIndex, toIndex);

            // Create CampaignBatch
            CampaignBatch batch = CampaignBatch.builder()
                    .campaign(campaign)
                    .status("PENDING")
                    .optimalSize(batchSize)
                    .retryCount(0)
                    .build();
            CampaignBatch savedBatch = batchRepository.save(batch);

            // Create CampaignRecipientLog records
            for (Contact contact : batchContacts) {
                CampaignRecipientLog recipient = CampaignRecipientLog.builder()
                        .campaign(campaign)
                        .contact(contact)
                        .email(contact.getEmail())
                        .status("QUEUED")
                        .provider(provider)
                        .attempts(0)
                        .build();
                recipientRepository.save(recipient);
            }

            // Push Batch Job to Redis Stream
            pushToRedisStream(campaign.getId(), savedBatch.getId(), provider, priority != null ? priority : "NORMAL");
        }

        campaign.setTotalRecipients(totalContacts);
    }

    private int getOptimalBatchSize(String provider) {
        if (provider == null) return 100;
        switch (provider.toUpperCase()) {
            case "SES":
                return 200;
            case "BREVO":
                return 100;
            case "SMTP":
                return 20;
            default:
                return 100;
        }
    }

    private void pushToRedisStream(Long campaignId, Long batchId, String provider, String priority) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("campaignId", String.valueOf(campaignId));
            payload.put("batchId", String.valueOf(batchId));
            payload.put("provider", provider);
            payload.put("priority", priority);

            MapRecord<String, String, String> record = MapRecord.create("campaign:queue:pending", payload);
            RecordId recordId = redisTemplate.opsForStream().add(record);

            log.info("BatchGenerator: Enqueued batchId={} to Redis Stream 'campaign:queue:pending' with recordId={}",
                    batchId, recordId);
        } catch (Exception e) {
            log.error("BatchGenerator: Failed to push batchId={} to Redis Stream: {}", batchId, e.getMessage(), e);
        }
    }
}
