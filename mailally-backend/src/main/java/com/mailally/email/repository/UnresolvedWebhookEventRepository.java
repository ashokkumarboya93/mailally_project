package com.mailally.email.repository;

import com.mailally.email.entity.UnresolvedWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for UnresolvedWebhookEvent dead-letter database operations.
 */
@Repository
public interface UnresolvedWebhookEventRepository extends JpaRepository<UnresolvedWebhookEvent, Long> {
    List<UnresolvedWebhookEvent> findByStatus(String status);
    List<UnresolvedWebhookEvent> findByStatusAndRetryCountLessThan(String status, int maxRetries);
}
