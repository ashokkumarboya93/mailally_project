package com.mailally.settings.mapper;

import com.mailally.settings.dto.SettingsResponseDto;
import com.mailally.settings.entity.Settings;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Settings entities and DTOs with security masking for encrypted credentials.
 */
@Component
public class SettingsMapper {

    public SettingsResponseDto toSettingsResponseDto(Settings settings) {
        if (settings == null) return null;

        String displayValue = settings.getSettingValue();
        if (Boolean.TRUE.equals(settings.getEncrypted()) && displayValue != null && !displayValue.isBlank()) {
            displayValue = "******"; // Security masking
        }

        return SettingsResponseDto.builder()
                .id(settings.getId())
                .organizationId(settings.getOrganization() != null ? settings.getOrganization().getId() : null)
                .category(settings.getCategory())
                .settingKey(settings.getSettingKey())
                .settingValue(displayValue)
                .dataType(settings.getDataType())
                .description(settings.getDescription())
                .editable(settings.getEditable())
                .encrypted(settings.getEncrypted())
                .version(settings.getVersion())
                .updatedAt(settings.getUpdatedAt())
                .build();
    }
}
