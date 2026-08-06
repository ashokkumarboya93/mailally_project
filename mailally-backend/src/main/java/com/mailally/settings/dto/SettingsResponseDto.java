package com.mailally.settings.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing an organization setting entry.
 */
public class SettingsResponseDto {

    private Long id;
    private Long organizationId;
    private String category;
    private String settingKey;
    private String settingValue;
    private String dataType;
    private String description;
    private Boolean editable;
    private Boolean encrypted;
    private Integer version;
    private LocalDateTime updatedAt;

    public SettingsResponseDto() {}

    public SettingsResponseDto(Long id, Long organizationId, String category, String settingKey, String settingValue,
                               String dataType, String description, Boolean editable, Boolean encrypted,
                               Integer version, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.category = category;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.dataType = dataType;
        this.description = description;
        this.editable = editable;
        this.encrypted = encrypted;
        this.version = version;
        this.updatedAt = updatedAt;
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
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getEditable() { return editable; }
    public void setEditable(Boolean editable) { this.editable = editable; }
    public Boolean getEncrypted() { return encrypted; }
    public void setEncrypted(Boolean encrypted) { this.encrypted = encrypted; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static SettingsResponseDtoBuilder builder() { return new SettingsResponseDtoBuilder(); }

    public static class SettingsResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private String category;
        private String settingKey;
        private String settingValue;
        private String dataType;
        private String description;
        private Boolean editable;
        private Boolean encrypted;
        private Integer version;
        private LocalDateTime updatedAt;

        SettingsResponseDtoBuilder() {}

        public SettingsResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public SettingsResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public SettingsResponseDtoBuilder category(String category) { this.category = category; return this; }
        public SettingsResponseDtoBuilder settingKey(String settingKey) { this.settingKey = settingKey; return this; }
        public SettingsResponseDtoBuilder settingValue(String settingValue) { this.settingValue = settingValue; return this; }
        public SettingsResponseDtoBuilder dataType(String dataType) { this.dataType = dataType; return this; }
        public SettingsResponseDtoBuilder description(String description) { this.description = description; return this; }
        public SettingsResponseDtoBuilder editable(Boolean editable) { this.editable = editable; return this; }
        public SettingsResponseDtoBuilder encrypted(Boolean encrypted) { this.encrypted = encrypted; return this; }
        public SettingsResponseDtoBuilder version(Integer version) { this.version = version; return this; }
        public SettingsResponseDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public SettingsResponseDto build() {
            return new SettingsResponseDto(id, organizationId, category, settingKey, settingValue, dataType,
                    description, editable, encrypted, version, updatedAt);
        }
    }
}
