package com.mailally.email.repository;

import com.mailally.email.entity.CampaignRecipientLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    /**
     * Atomic conditional state transition: only updates if current status is in allowedFromStatuses.
     * Prevents race conditions where e.g. DELIVERED -> PROCESSING could occur.
     * Returns number of rows affected (0 = transition blocked, 1 = success).
     */
    @Modifying
    @Query("UPDATE CampaignRecipientLog r SET r.status = :newStatus, r.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE r.id = :id AND r.status IN :allowedFromStatuses")
    int atomicStatusTransition(@Param("id") Long id,
                               @Param("newStatus") String newStatus,
                               @Param("allowedFromStatuses") Collection<String> allowedFromStatuses);
}
