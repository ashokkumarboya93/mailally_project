package com.mailally.settings.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating a single setting entry.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateSettingRequestDto {

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Setting key is required")
    private String settingKey;

    @NotNull(message = "Setting value is required")
    private String settingValue;

    public UpdateSettingRequestDto() {}

    public UpdateSettingRequestDto(String category, String settingKey, String settingValue) {
        this.category = category;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }

    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
}
