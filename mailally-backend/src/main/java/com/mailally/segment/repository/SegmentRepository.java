package com.mailally.segment.repository;

import com.mailally.segment.entity.Segment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Segment} entity database operations.
 */
@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {

    Optional<Segment> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    Page<Segment> findByOrganizationIdAndIsDeletedFalse(Long organizationId, Pageable pageable);

    boolean existsByOrganizationIdAndNameAndIsDeletedFalse(Long organizationId, String name);

    @Query("SELECT s FROM Segment s WHERE s.organization.id = :organizationId " +
           "AND s.isDeleted = false " +
           "AND (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:type IS NULL OR s.type = :type) " +
           "AND (:status IS NULL OR s.status = :status)")
    Page<Segment> searchSegments(
            @Param("organizationId") Long organizationId,
            @Param("name") String name,
            @Param("type") String type,
            @Param("status") String status,
            Pageable pageable
    );
}
