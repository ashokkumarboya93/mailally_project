package com.mailally.integration.repository;

import com.mailally.integration.entity.GoogleIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleIntegrationRepository extends JpaRepository<GoogleIntegration, Long> {

    Optional<GoogleIntegration> findByOrganizationIdAndProvider(Long organizationId, String provider);
}
