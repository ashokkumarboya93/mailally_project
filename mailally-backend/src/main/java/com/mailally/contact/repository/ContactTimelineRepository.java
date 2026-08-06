package com.mailally.contact.repository;

import com.mailally.contact.entity.ContactTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactTimelineRepository extends JpaRepository<ContactTimeline, Long> {

    List<ContactTimeline> findByContactIdAndOrganizationIdOrderByCreatedAtDesc(Long contactId, Long organizationId);
}
