package com.mailally.email.repository;

import com.mailally.email.entity.CampaignRecipientLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for V2 CampaignRecipientLog entity database operations.
 */
@Repository
public interface CampaignRecipientLogRepository extends JpaRepository<CampaignRecipientLog, Long> {
    List<CampaignRecipientLog> findByCampaignId(Long campaignId);
    List<CampaignRecipientLog> findByCampaignIdAndStatus(Long campaignId, String status);
    long countByCampaignId(Long campaignId);
    long countByCampaignIdAndStatus(Long campaignId, String status);

    Optional<CampaignRecipientLog> findFirstByProviderMessageId(String providerMessageId);
    Optional<CampaignRecipientLog> findFirstByProviderMessageIdContaining(String providerMessageIdPart);
    Optional<CampaignRecipientLog> findFirstByEmailOrderByCreatedAtDesc(String email);
    Optional<CampaignRecipientLog> findFirstByEmailIgnoreCaseOrderByCreatedAtDesc(String email);
    Optional<CampaignRecipientLog> findFirstByCampaignOrganizationIdAndEmailIgnoreCaseOrderByCreatedAtDesc(Long organizationId, String email);
    List<CampaignRecipientLog> findByCampaignOrganizationId(Long organizationId);
}
