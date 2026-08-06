package com.mailally.campaign.repository;

import com.mailally.campaign.entity.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repository interface for {@link Campaign} entity database operations.
 */
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Optional<Campaign> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    List<Campaign> findByStatus(String status);

    @Query("SELECT c FROM Campaign c LEFT JOIN FETCH c.template LEFT JOIN FETCH c.organization WHERE c.id = :id")
    Optional<Campaign> findByIdEager(@Param("id") Long id);

    long countByOrganizationIdAndIsDeletedFalse(Long organizationId);

    Page<Campaign> findByOrganizationIdAndIsDeletedFalse(Long organizationId, Pageable pageable);

    @Query("SELECT c FROM Campaign c WHERE c.organization.id = :organizationId " +
           "AND c.isDeleted = false " +
           "AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:status IS NULL OR c.status = :status)")
    Page<Campaign> searchCampaigns(
            @Param("organizationId") Long organizationId,
            @Param("name") String name,
            @Param("status") String status,
            Pageable pageable
    );
}
