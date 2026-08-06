package com.mailally.subscription.repository;

import com.mailally.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Subscription} database operations.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByOrganizationIdAndIsDeletedFalse(Long organizationId);

    Optional<Subscription> findByCodeAndIsDeletedFalse(String code);
}
