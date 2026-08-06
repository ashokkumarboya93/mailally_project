package com.mailally.ai.repository;

import com.mailally.ai.entity.Ai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Ai} prompt logs database operations.
 */
@Repository
public interface AiRepository extends JpaRepository<Ai, Long> {

    Page<Ai> findByOrganizationIdAndUserIdAndIsDeletedFalse(Long organizationId, Long userId, Pageable pageable);

    List<Ai> findByOrganizationIdAndIsDeletedFalse(Long organizationId);

    long countByOrganizationIdAndIsDeletedFalse(Long organizationId);

    @Query("SELECT COALESCE(SUM(a.tokensUsed), 0) FROM Ai a WHERE a.organization.id = :organizationId AND a.isDeleted = false")
    Long sumTokensUsedByOrganizationId(@Param("organizationId") Long organizationId);
}
