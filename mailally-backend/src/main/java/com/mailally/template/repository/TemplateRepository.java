package com.mailally.template.repository;

import com.mailally.template.entity.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Template} entity database operations.
 */
@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    Page<Template> findByOrganizationIdAndIsDeletedFalse(Long organizationId, Pageable pageable);

    boolean existsByOrganizationIdAndNameAndIsDeletedFalse(Long organizationId, String name);

    @Query("SELECT t FROM Template t WHERE t.organization.id = :organizationId " +
           "AND t.isDeleted = false " +
           "AND (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:status IS NULL OR t.status = :status)")
    Page<Template> searchTemplates(
            @Param("organizationId") Long organizationId,
            @Param("name") String name,
            @Param("status") String status,
            Pageable pageable
    );
}
