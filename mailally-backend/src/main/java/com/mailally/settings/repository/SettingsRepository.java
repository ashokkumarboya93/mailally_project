package com.mailally.settings.repository;

import com.mailally.settings.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Settings} database operations.
 */
@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {

    List<Settings> findByOrganizationIdAndIsDeletedFalse(Long organizationId);

    List<Settings> findByOrganizationIdAndCategoryAndIsDeletedFalse(Long organizationId, String category);

    Optional<Settings> findByOrganizationIdAndCategoryAndSettingKeyAndIsDeletedFalse(Long organizationId, String category, String settingKey);

    Optional<Settings> findByOrganizationIdAndSettingKeyAndIsDeletedFalse(Long organizationId, String settingKey);

    long countByOrganizationIdAndIsDeletedFalse(Long organizationId);
}
