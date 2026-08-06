package com.mailally.email.repository;

import com.mailally.email.entity.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Email} entity operations.
 */
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    Optional<Email> findByIdAndOrganizationId(Long id, Long organizationId);

    Page<Email> findByOrganizationId(Long organizationId, Pageable pageable);

    Page<Email> findByOrganizationIdAndCampaignId(Long organizationId, Long campaignId, Pageable pageable);

    List<Email> findByOrganizationIdAndCampaignIdAndStatus(Long organizationId, Long campaignId, String status);

    long countByOrganizationIdAndCampaignId(Long organizationId, Long campaignId);

    long countByOrganizationIdAndCampaignIdAndStatus(Long organizationId, Long campaignId, String status);

    @Query("SELECT e FROM Email e WHERE e.organization.id = :organizationId " +
           "AND (:campaignId IS NULL OR e.campaign.id = :campaignId) " +
           "AND (:status IS NULL OR e.status = :status) " +
           "AND (:recipientEmail IS NULL OR LOWER(e.recipientEmail) LIKE LOWER(CONCAT('%', :recipientEmail, '%')))")
    Page<Email> searchEmailLogs(
            @Param("organizationId") Long organizationId,
            @Param("campaignId") Long campaignId,
            @Param("status") String status,
            @Param("recipientEmail") String recipientEmail,
            Pageable pageable
    );
}
