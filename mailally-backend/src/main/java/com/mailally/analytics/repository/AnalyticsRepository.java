package com.mailally.analytics.repository;

import com.mailally.analytics.entity.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Analytics} entity.
 */
@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
}
