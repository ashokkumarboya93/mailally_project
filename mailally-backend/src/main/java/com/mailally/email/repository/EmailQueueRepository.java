package com.mailally.email.repository;

import com.mailally.email.entity.EmailQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link EmailQueue} entity operations.
 */
@Repository
public interface EmailQueueRepository extends JpaRepository<EmailQueue, Long> {

    Optional<EmailQueue> findByIdAndOrganizationId(Long id, Long organizationId);

    Page<EmailQueue> findByOrganizationIdAndCampaignId(Long organizationId, Long campaignId, Pageable pageable);

    List<EmailQueue> findByOrganizationIdAndCampaignIdAndStatus(Long organizationId, Long campaignId, String status);

    long countByOrganizationIdAndCampaignId(Long organizationId, Long campaignId);

    long countByOrganizationIdAndCampaignIdAndStatus(Long organizationId, Long campaignId, String status);

    List<EmailQueue> findByOrganizationIdAndCampaignIdAndStatusIn(Long organizationId, Long campaignId, List<String> statuses);
}
