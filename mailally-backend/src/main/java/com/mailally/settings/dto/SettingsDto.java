package com.mailally.settings.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Settings.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class SettingsDto {

    private Long id;
    private Long organizationId;
    private String category;
    private String settingKey;
    private String settingValue;
    private Boolean encrypted;

    public SettingsDto() {}

    public SettingsDto(Long id, Long organizationId, String category, String settingKey, String settingValue, Boolean encrypted) {
        this.id = id;
        this.organizationId = organizationId;
        this.category = category;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.encrypted = encrypted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }
    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
    public Boolean getEncrypted() { return encrypted; }
    public void setEncrypted(Boolean encrypted) { this.encrypted = encrypted; }

    public static SettingsDtoBuilder builder() { return new SettingsDtoBuilder(); }

    public static class SettingsDtoBuilder {
        private Long id;
        private Long organizationId;
        private String category;
        private String settingKey;
        private String settingValue;
        private Boolean encrypted;

        SettingsDtoBuilder() {}

        public SettingsDtoBuilder id(Long id) { this.id = id; return this; }
        public SettingsDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public SettingsDtoBuilder category(String category) { this.category = category; return this; }
        public SettingsDtoBuilder settingKey(String settingKey) { this.settingKey = settingKey; return this; }
        public SettingsDtoBuilder settingValue(String settingValue) { this.settingValue = settingValue; return this; }
        public SettingsDtoBuilder encrypted(Boolean encrypted) { this.encrypted = encrypted; return this; }

        public SettingsDto build() {
            return new SettingsDto(id, organizationId, category, settingKey, settingValue, encrypted);
        }
    }
}
