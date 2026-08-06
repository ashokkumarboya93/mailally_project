package com.mailally.contact.repository;

import com.mailally.contact.entity.DynamicFieldRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DynamicFieldRegistryRepository extends JpaRepository<DynamicFieldRegistry, Long> {

    List<DynamicFieldRegistry> findByOrganizationIdOrderByOrderIndexAscCreatedAtAsc(Long organizationId);

    Optional<DynamicFieldRegistry> findByOrganizationIdAndFieldKey(Long organizationId, String fieldKey);

    boolean existsByOrganizationIdAndFieldKey(Long organizationId, String fieldKey);
}
