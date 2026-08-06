package com.mailally.settings.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO for updating multiple settings in bulk.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateSettingsRequestDto {

    @NotNull(message = "Settings list is required")
    @NotEmpty(message = "Settings list cannot be empty")
    @Valid
    private List<UpdateSettingRequestDto> settings;

    public UpdateSettingsRequestDto() {}

    public UpdateSettingsRequestDto(List<UpdateSettingRequestDto> settings) {
        this.settings = settings;
    }

    public List<UpdateSettingRequestDto> getSettings() { return settings; }
    public void setSettings(List<UpdateSettingRequestDto> settings) { this.settings = settings; }
}
