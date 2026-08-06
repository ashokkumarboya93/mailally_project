package com.mailally.settings.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JSON import/export wrapper DTO for organization backup and restore operations.
 */
public class ImportExportSettingsDto {

    private String organizationName;
    private LocalDateTime exportedAt;
    private String version;
    private List<SettingsResponseDto> settings;

    public ImportExportSettingsDto() {}

    public ImportExportSettingsDto(String organizationName, LocalDateTime exportedAt, String version, List<SettingsResponseDto> settings) {
        this.organizationName = organizationName;
        this.exportedAt = exportedAt;
        this.version = version;
        this.settings = settings;
    }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    public LocalDateTime getExportedAt() { return exportedAt; }
    public void setExportedAt(LocalDateTime exportedAt) { this.exportedAt = exportedAt; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public List<SettingsResponseDto> getSettings() { return settings; }
    public void setSettings(List<SettingsResponseDto> settings) { this.settings = settings; }
}
