package com.mailally.scheduler.repository;

import com.mailally.scheduler.entity.Scheduler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Scheduler} entity database operations.
 */
@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    Optional<Scheduler> findByIdAndOrganizationId(Long id, Long organizationId);

    Page<Scheduler> findByOrganizationId(Long organizationId, Pageable pageable);

    Optional<Scheduler> findByCampaignIdAndOrganizationId(Long campaignId, Long organizationId);

    Page<Scheduler> findByOrganizationIdAndStatus(Long organizationId, String status, Pageable pageable);

    Page<Scheduler> findByOrganizationIdAndScheduledTimeAfterAndStatusIn(Long organizationId, LocalDateTime time, List<String> statuses, Pageable pageable);

    Page<Scheduler> findByOrganizationIdAndStatusIn(Long organizationId, List<String> statuses, Pageable pageable);

    long countByOrganizationId(Long organizationId);

    long countByOrganizationIdAndStatus(Long organizationId, String status);

    List<Scheduler> findByScheduledTimeBeforeAndStatusIn(LocalDateTime time, List<String> statuses);
}
