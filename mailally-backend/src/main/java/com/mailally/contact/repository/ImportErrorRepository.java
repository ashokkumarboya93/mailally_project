package com.mailally.contact.repository;

import com.mailally.contact.entity.ImportError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportErrorRepository extends JpaRepository<ImportError, Long> {

    List<ImportError> findByImportBatchIdAndOrganizationId(Long importBatchId, Long organizationId);

    void deleteByImportBatchIdAndOrganizationId(Long importBatchId, Long organizationId);
}
