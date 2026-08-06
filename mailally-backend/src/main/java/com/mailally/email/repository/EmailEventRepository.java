package com.mailally.email.repository;

import com.mailally.email.entity.EmailEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailEventRepository extends JpaRepository<EmailEvent, Long> {
    List<EmailEvent> findByCampaignId(Long campaignId);
    List<EmailEvent> findByRecipientId(Long recipientId);
}
