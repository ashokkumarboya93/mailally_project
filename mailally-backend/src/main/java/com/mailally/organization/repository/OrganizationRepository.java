package com.mailally.organization.repository;

import com.mailally.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Organization} entity operations.
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByIdAndIsDeletedFalse(Long id);

    Optional<Organization> findBySlugAndIsDeletedFalse(String slug);

    boolean existsBySlug(String slug);

    List<Organization> findAllByIsDeletedFalse();
}

