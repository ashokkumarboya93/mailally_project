package com.mailally.email.repository;

import com.mailally.email.entity.CampaignRecipientLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for V2 CampaignRecipientLog entity database operations.
 */
@Repository
public interface CampaignRecipientLogRepository extends JpaRepository<CampaignRecipientLog, Long> {
    List<CampaignRecipientLog> findByCampaignId(Long campaignId);
    List<CampaignRecipientLog> findByCampaignIdAndStatus(Long campaignId, String status);
    long countByCampaignId(Long campaignId);
    long countByCampaignIdAndStatus(Long campaignId, String status);
}
