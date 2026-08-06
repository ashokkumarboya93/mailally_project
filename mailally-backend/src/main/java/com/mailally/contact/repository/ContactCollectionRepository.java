package com.mailally.contact.repository;

import com.mailally.contact.entity.ContactCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactCollectionRepository extends JpaRepository<ContactCollection, Long> {

    List<ContactCollection> findByOrganizationIdAndIsDeletedFalseOrderByCreatedAtDesc(Long organizationId);

    Page<ContactCollection> findByOrganizationIdAndIsDeletedFalse(Long organizationId, Pageable pageable);

    Optional<ContactCollection> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    @Query("SELECT c FROM ContactCollection c WHERE c.organization.id = :orgId AND c.isDeleted = false AND LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<ContactCollection> searchCollections(@Param("orgId") Long orgId, @Param("query") String query);
}
