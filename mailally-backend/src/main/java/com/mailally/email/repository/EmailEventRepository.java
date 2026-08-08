package com.mailally.email.repository;

import com.mailally.email.constant.EmailEventType;
import com.mailally.email.entity.EmailEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailEventRepository extends JpaRepository<EmailEvent, Long> {
    List<EmailEvent> findByCampaignId(Long campaignId);
    List<EmailEvent> findByRecipientId(Long recipientId);
    List<EmailEvent> findByOrganizationId(Long organizationId);

    long countByCampaignId(Long campaignId);
    long countByCampaignIdAndEventType(Long campaignId, EmailEventType eventType);

    long countByOrganizationId(Long organizationId);
    long countByOrganizationIdAndEventType(Long organizationId, EmailEventType eventType);

    List<EmailEvent> findTop20ByOrganizationIdOrderByTimestampDesc(Long organizationId);
    List<EmailEvent> findTop20ByCampaignIdOrderByTimestampDesc(Long campaignId);

    List<EmailEvent> findByOrganizationIdAndTimestampBetween(Long organizationId, LocalDateTime dateFrom, LocalDateTime dateTo);

    boolean existsByProviderMessageIdAndEventType(String providerMessageId, EmailEventType eventType);
}
