package com.mailally.settings.service.impl;

import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.settings.dto.ImportExportSettingsDto;
import com.mailally.settings.dto.SettingsResponseDto;
import com.mailally.settings.dto.UpdateSettingRequestDto;
import com.mailally.settings.dto.UpdateSettingsRequestDto;
import com.mailally.settings.entity.Settings;
import com.mailally.settings.mapper.SettingsMapper;
import com.mailally.settings.repository.SettingsRepository;
import com.mailally.settings.service.SettingsService;
import com.mailally.settings.validator.SettingsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for enterprise Settings management.
 * Handles lazy default seeding, category queries, bulk updates, encryption security, and JSON backup/restore.
 */
@Service
@Transactional
public class SettingsServiceImpl implements SettingsService {

    private static final Logger log = LoggerFactory.getLogger(SettingsServiceImpl.class);

    private final SettingsRepository settingsRepository;
    private final OrganizationRepository organizationRepository;
    private final SettingsValidator settingsValidator;
    private final SettingsMapper settingsMapper;

    public SettingsServiceImpl(SettingsRepository settingsRepository,
                               OrganizationRepository organizationRepository,
                               SettingsValidator settingsValidator,
                               SettingsMapper settingsMapper) {
        this.settingsRepository = settingsRepository;
        this.organizationRepository = organizationRepository;
        this.settingsValidator = settingsValidator;
        this.settingsMapper = settingsMapper;
    }

    @Override
    public List<SettingsResponseDto> getOrganizationSettings(CustomUserDetails currentUser) {
        settingsValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();

        List<Settings> settingsList = settingsRepository.findByOrganizationIdAndIsDeletedFalse(orgId);
        if (settingsList.isEmpty()) {
            Organization org = organizationRepository.findById(orgId)
                    .orElseThrow(() -> new CustomException("Organization not found"));
            settingsList = seedDefaultSettings(org);
        }

        return settingsList.stream().map(settingsMapper::toSettingsResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<SettingsResponseDto> getCategorySettings(CustomUserDetails currentUser, String category) {
        settingsValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();
        String cat = category != null ? category.trim().toUpperCase() : "GENERAL";

        List<Settings> categoryList = settingsRepository.findByOrganizationIdAndCategoryAndIsDeletedFalse(orgId, cat);
        if (categoryList.isEmpty()) {
            getOrganizationSettings(currentUser); // Trigger seed
            categoryList = settingsRepository.findByOrganizationIdAndCategoryAndIsDeletedFalse(orgId, cat);
        }

        return categoryList.stream().map(settingsMapper::toSettingsResponseDto).collect(Collectors.toList());
    }

    @Override
    public SettingsResponseDto getSettingByKey(CustomUserDetails currentUser, String category, String key) {
        settingsValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();
        String cat = category != null ? category.trim().toUpperCase() : "GENERAL";

        Settings setting = settingsRepository.findByOrganizationIdAndCategoryAndSettingKeyAndIsDeletedFalse(orgId, cat, key)
                .orElseGet(() -> {
                    getOrganizationSettings(currentUser); // Trigger seed
                    return settingsRepository.findByOrganizationIdAndCategoryAndSettingKeyAndIsDeletedFalse(orgId, cat, key)
                            .orElseThrow(() -> new CustomException("Setting not found for key: " + key));
                });

        return settingsMapper.toSettingsResponseDto(setting);
    }

    @Override
    public SettingsResponseDto updateSingleSetting(CustomUserDetails currentUser, UpdateSettingRequestDto dto) {
        settingsValidator.validateAdminOrManagerRole(currentUser);
        Long orgId = currentUser.getOrganizationId();
        String cat = dto.getCategory().trim().toUpperCase();
        String key = dto.getSettingKey().trim();

        Settings setting = settingsRepository.findByOrganizationIdAndCategoryAndSettingKeyAndIsDeletedFalse(orgId, cat, key)
                .orElseGet(() -> {
                    getOrganizationSettings(currentUser);
                    return settingsRepository.findByOrganizationIdAndCategoryAndSettingKeyAndIsDeletedFalse(orgId, cat, key)
                            .orElseThrow(() -> new CustomException("Setting not found for key: " + key));
                });

        if (Boolean.FALSE.equals(setting.getEditable())) {
            throw new CustomException("Setting '" + key + "' is read-only and cannot be modified.");
        }

        settingsValidator.validateValueDataType(dto.getSettingValue(), setting.getDataType());

        setting.setSettingValue(dto.getSettingValue());
        setting.setVersion(setting.getVersion() != null ? setting.getVersion() + 1 : 1);
        setting.setUpdatedBy(currentUser.getUserId());

        Settings updated = settingsRepository.save(setting);
        log.info("Updated setting key '{}' for Organization ID {}", key, orgId);
        return settingsMapper.toSettingsResponseDto(updated);
    }

    @Override
    public List<SettingsResponseDto> updateMultipleSettings(CustomUserDetails currentUser, UpdateSettingsRequestDto dto) {
        settingsValidator.validateAdminOrManagerRole(currentUser);
        List<SettingsResponseDto> results = new ArrayList<>();
        for (UpdateSettingRequestDto single : dto.getSettings()) {
            results.add(updateSingleSetting(currentUser, single));
        }
        return results;
    }

    @Override
    public List<SettingsResponseDto> resetCategorySettings(CustomUserDetails currentUser, String category) {
        settingsValidator.validateAdminRole(currentUser);
        Long orgId = currentUser.getOrganizationId();
        String cat = category != null ? category.trim().toUpperCase() : "GENERAL";

        List<Settings> existingCategory = settingsRepository.findByOrganizationIdAndCategoryAndIsDeletedFalse(orgId, cat);
        for (Settings s : existingCategory) {
            s.setIsDeleted(true);
            settingsRepository.save(s);
        }

        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new CustomException("Organization not found"));
        seedCategoryDefaults(org, cat);

        List<Settings> resetList = settingsRepository.findByOrganizationIdAndCategoryAndIsDeletedFalse(orgId, cat);
        return resetList.stream().map(settingsMapper::toSettingsResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<SettingsResponseDto> resetOrganizationSettings(CustomUserDetails currentUser) {
        settingsValidator.validateAdminRole(currentUser);
        Long orgId = currentUser.getOrganizationId();

        List<Settings> existingAll = settingsRepository.findByOrganizationIdAndIsDeletedFalse(orgId);
        for (Settings s : existingAll) {
            s.setIsDeleted(true);
            settingsRepository.save(s);
        }

        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new CustomException("Organization not found"));

        List<Settings> seeded = seedDefaultSettings(org);
        return seeded.stream().map(settingsMapper::toSettingsResponseDto).collect(Collectors.toList());
    }

    @Override
    public ImportExportSettingsDto exportSettingsJson(CustomUserDetails currentUser) {
        settingsValidator.validateAdminRole(currentUser);
        Long orgId = currentUser.getOrganizationId();

        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new CustomException("Organization not found"));

        List<SettingsResponseDto> settings = getOrganizationSettings(currentUser);

        return new ImportExportSettingsDto(org.getName(), LocalDateTime.now(), "1.0", settings);
    }

    @Override
    public List<SettingsResponseDto> importSettingsJson(CustomUserDetails currentUser, ImportExportSettingsDto dto) {
        settingsValidator.validateAdminRole(currentUser);
        if (dto == null || dto.getSettings() == null || dto.getSettings().isEmpty()) {
            throw new CustomException("Import payload cannot be empty");
        }

        List<UpdateSettingRequestDto> updateDtos = dto.getSettings().stream()
                .map(s -> new UpdateSettingRequestDto(s.getCategory(), s.getSettingKey(), s.getSettingValue()))
                .collect(Collectors.toList());

        return updateMultipleSettings(currentUser, new UpdateSettingsRequestDto(updateDtos));
    }

    private List<Settings> seedDefaultSettings(Organization org) {
        List<Settings> defaults = new ArrayList<>();
        defaults.addAll(seedCategoryDefaults(org, "GENERAL"));
        defaults.addAll(seedCategoryDefaults(org, "BRAND"));
        defaults.addAll(seedCategoryDefaults(org, "SECURITY"));
        defaults.addAll(seedCategoryDefaults(org, "NOTIFICATIONS"));
        defaults.addAll(seedCategoryDefaults(org, "DASHBOARD"));
        defaults.addAll(seedCategoryDefaults(org, "CAMPAIGN"));
        defaults.addAll(seedCategoryDefaults(org, "EMAIL_ENGINE"));
        defaults.addAll(seedCategoryDefaults(org, "SCHEDULER"));
        defaults.addAll(seedCategoryDefaults(org, "ANALYTICS"));
        defaults.addAll(seedCategoryDefaults(org, "API"));
        return defaults;
    }

    private List<Settings> seedCategoryDefaults(Organization org, String category) {
        List<Settings> seedList = new ArrayList<>();

        switch (category.toUpperCase()) {
            case "GENERAL":
                seedList.add(createSetting(org, "GENERAL", "mailally.general.timezone", "UTC", "STRING", "Default timezone"));
                seedList.add(createSetting(org, "GENERAL", "mailally.general.language", "en_US", "STRING", "Default language"));
                seedList.add(createSetting(org, "GENERAL", "mailally.general.currency", "USD", "STRING", "Default currency"));
                seedList.add(createSetting(org, "GENERAL", "mailally.general.date_format", "YYYY-MM-DD", "STRING", "Date format"));
                break;
            case "BRAND":
                seedList.add(createSetting(org, "BRAND", "mailally.theme.logo_url", "", "STRING", "Organization logo URL"));
                seedList.add(createSetting(org, "BRAND", "mailally.theme.primary_color", "#4F46E5", "STRING", "Primary brand color"));
                seedList.add(createSetting(org, "BRAND", "mailally.theme.secondary_color", "#0EA5E9", "STRING", "Secondary brand color"));
                break;
            case "SECURITY":
                seedList.add(createSetting(org, "SECURITY", "mailally.security.password_min_length", "8", "INTEGER", "Minimum password length"));
                seedList.add(createSetting(org, "SECURITY", "mailally.security.session_timeout_minutes", "60", "INTEGER", "Session timeout in minutes"));
                seedList.add(createSetting(org, "SECURITY", "mailally.security.enable_two_factor", "false", "BOOLEAN", "Enable 2FA policy"));
                break;
            case "NOTIFICATIONS":
                seedList.add(createSetting(org, "NOTIFICATIONS", "mailally.notifications.email_alerts", "true", "BOOLEAN", "Enable email notifications"));
                seedList.add(createSetting(org, "NOTIFICATIONS", "mailally.notifications.system_alerts", "true", "BOOLEAN", "Enable system alerts"));
                break;
            case "DASHBOARD":
                seedList.add(createSetting(org, "DASHBOARD", "mailally.dashboard.default_view", "EXECUTIVE", "STRING", "Default dashboard view"));
                seedList.add(createSetting(org, "DASHBOARD", "mailally.dashboard.default_date_range", "30_DAYS", "STRING", "Default date range"));
                break;
            case "CAMPAIGN":
                seedList.add(createSetting(org, "CAMPAIGN", "mailally.campaign.default_sender_name", "MailAlly Admin", "STRING", "Default campaign sender name"));
                seedList.add(createSetting(org, "CAMPAIGN", "mailally.campaign.default_sender_email", "info@marcamor.com", "STRING", "Default sender email"));
                break;
            case "EMAIL_ENGINE":
                seedList.add(createSetting(org, "EMAIL_ENGINE", "mailally.email.default_provider", "SMTP", "STRING", "Active email provider"));
                seedList.add(createSetting(org, "EMAIL_ENGINE", "mailally.email.max_retries", "3", "INTEGER", "Maximum retry attempts"));
                seedList.add(createSetting(org, "EMAIL_ENGINE", "mailally.email.batch_size", "500", "INTEGER", "Email dispatch batch size"));
                break;
            case "SCHEDULER":
                seedList.add(createSetting(org, "SCHEDULER", "mailally.scheduler.poll_rate_seconds", "30", "INTEGER", "Scheduler poll interval"));
                seedList.add(createSetting(org, "SCHEDULER", "mailally.scheduler.auto_retry_failed", "true", "BOOLEAN", "Auto retry failed scheduled jobs"));
                break;
            case "ANALYTICS":
                seedList.add(createSetting(org, "ANALYTICS", "mailally.analytics.default_chart", "BAR", "STRING", "Default analytics chart type"));
                seedList.add(createSetting(org, "ANALYTICS", "mailally.analytics.export_format", "CSV", "STRING", "Default export format"));
                break;
            case "API":
                seedList.add(createSetting(org, "API", "mailally.api.enabled", "true", "BOOLEAN", "API access enabled"));
                seedList.add(createSetting(org, "API", "mailally.api.webhook_enabled", "false", "BOOLEAN", "Webhook dispatch enabled"));
                seedList.add(createEncryptedSetting(org, "API", "mailally.api.webhook_secret", "secret_key_123", "STRING", "Webhook HMAC secret"));
                break;
        }

        List<Settings> savedList = new ArrayList<>();
        for (Settings s : seedList) {
            savedList.add(settingsRepository.save(s));
        }
        return savedList;
    }

    private Settings createSetting(Organization org, String category, String key, String value, String dataType, String desc) {
        return Settings.builder()
                .organization(org)
                .category(category)
                .settingKey(key)
                .settingValue(value)
                .dataType(dataType)
                .description(desc)
                .editable(true)
                .encrypted(false)
                .version(1)
                .build();
    }

    private Settings createEncryptedSetting(Organization org, String category, String key, String value, String dataType, String desc) {
        return Settings.builder()
                .organization(org)
                .category(category)
                .settingKey(key)
                .settingValue(value)
                .dataType(dataType)
                .description(desc)
                .editable(true)
                .encrypted(true)
                .version(1)
                .build();
    }
}
