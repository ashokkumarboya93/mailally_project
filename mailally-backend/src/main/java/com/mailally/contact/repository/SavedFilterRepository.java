package com.mailally.contact.repository;

import com.mailally.contact.entity.SavedFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedFilterRepository extends JpaRepository<SavedFilter, Long> {

    Optional<SavedFilter> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    List<SavedFilter> findByOrganizationIdAndIsDeletedFalse(Long organizationId);
}
