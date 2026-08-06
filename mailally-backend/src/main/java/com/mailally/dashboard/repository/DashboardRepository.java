package com.mailally.dashboard.repository;

import com.mailally.dashboard.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Dashboard} entity.
 */
@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
}
