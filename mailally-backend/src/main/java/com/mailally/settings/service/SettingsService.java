package com.mailally.settings.service;

import com.mailally.security.CustomUserDetails;
import com.mailally.settings.dto.ImportExportSettingsDto;
import com.mailally.settings.dto.SettingsResponseDto;
import com.mailally.settings.dto.UpdateSettingRequestDto;
import com.mailally.settings.dto.UpdateSettingsRequestDto;

import java.util.List;

/**
 * Service interface for organization settings management, category grouping, bulk updates, default resets, and backup/restore.
 */
public interface SettingsService {

    List<SettingsResponseDto> getOrganizationSettings(CustomUserDetails currentUser);

    List<SettingsResponseDto> getCategorySettings(CustomUserDetails currentUser, String category);

    SettingsResponseDto getSettingByKey(CustomUserDetails currentUser, String category, String key);

    SettingsResponseDto updateSingleSetting(CustomUserDetails currentUser, UpdateSettingRequestDto dto);

    List<SettingsResponseDto> updateMultipleSettings(CustomUserDetails currentUser, UpdateSettingsRequestDto dto);

    List<SettingsResponseDto> resetCategorySettings(CustomUserDetails currentUser, String category);

    List<SettingsResponseDto> resetOrganizationSettings(CustomUserDetails currentUser);

    ImportExportSettingsDto exportSettingsJson(CustomUserDetails currentUser);

    List<SettingsResponseDto> importSettingsJson(CustomUserDetails currentUser, ImportExportSettingsDto dto);
}
