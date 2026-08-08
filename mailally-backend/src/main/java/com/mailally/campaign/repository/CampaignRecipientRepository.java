package com.mailally.campaign.repository;

import com.mailally.campaign.entity.CampaignRecipient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRecipientRepository extends JpaRepository<CampaignRecipient, Long> {

    List<CampaignRecipient> findByCampaignId(Long campaignId);

    Page<CampaignRecipient> findByCampaignId(Long campaignId, Pageable pageable);

    Page<CampaignRecipient> findByCampaignIdAndStatus(Long campaignId, String status, Pageable pageable);

    Optional<CampaignRecipient> findByCampaignIdAndContactId(Long campaignId, Long contactId);

    boolean existsByCampaignIdAndContactId(Long campaignId, Long contactId);

    long countByCampaignId(Long campaignId);

    long countByCampaignIdAndStatus(Long campaignId, String status);

    Optional<CampaignRecipient> findFirstByResponseId(String responseId);

    Optional<CampaignRecipient> findFirstByResponseIdContaining(String responseIdPart);

    Optional<CampaignRecipient> findFirstByContactEmailIgnoreCaseOrderByQueuedAtDesc(String email);

    @Query("SELECT cr FROM CampaignRecipient cr JOIN FETCH cr.contact JOIN FETCH cr.campaign LEFT JOIN FETCH cr.campaign.organization LEFT JOIN FETCH cr.campaign.template WHERE cr.campaign.id = :campaignId AND cr.status = 'QUEUED'")
    List<CampaignRecipient> findQueuedRecipients(@Param("campaignId") Long campaignId, Pageable pageable);
}
