package com.mailally.audit.repository;

import com.mailally.audit.entity.Audit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Audit} database operations.
 */
@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

    Page<Audit> findByOrganizationIdAndIsDeletedFalse(Long organizationId, Pageable pageable);

    List<Audit> findByOrganizationIdAndIsDeletedFalse(Long organizationId);

    @Query("SELECT a FROM Audit a WHERE a.organization.id = :organizationId AND a.isDeleted = false " +
           "AND (:module IS NULL OR a.module = :module) " +
           "AND (:action IS NULL OR a.action = :action) " +
           "AND (:success IS NULL OR a.success = :success)")
    Page<Audit> filterAuditLogs(
            @Param("organizationId") Long organizationId,
            @Param("module") String module,
            @Param("action") String action,
            @Param("success") Boolean success,
            Pageable pageable
    );

    @Query("SELECT a FROM Audit a WHERE a.organization.id = :organizationId AND a.isDeleted = false " +
           "AND (LOWER(a.action) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Audit> searchAuditLogs(
            @Param("organizationId") Long organizationId,
            @Param("query") String query,
            Pageable pageable
    );
}
