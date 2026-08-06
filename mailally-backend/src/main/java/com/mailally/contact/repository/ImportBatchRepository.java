package com.mailally.contact.repository;

import com.mailally.contact.entity.ImportBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportBatchRepository extends JpaRepository<ImportBatch, Long> {

    Optional<ImportBatch> findByIdAndOrganizationIdAndIsDeletedFalse(Long id, Long organizationId);

    Optional<ImportBatch> findByBatchCodeAndOrganizationIdAndIsDeletedFalse(String batchCode, Long organizationId);

    Page<ImportBatch> findByOrganizationIdAndIsDeletedFalse(Long organizationId, Pageable pageable);
}
