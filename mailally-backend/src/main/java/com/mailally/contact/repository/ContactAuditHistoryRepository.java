package com.mailally.contact.repository;

import com.mailally.contact.entity.ContactAuditHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactAuditHistoryRepository extends JpaRepository<ContactAuditHistory, Long> {

    List<ContactAuditHistory> findByContactIdAndOrganizationIdOrderByEditedAtDesc(Long contactId, Long organizationId);
}
