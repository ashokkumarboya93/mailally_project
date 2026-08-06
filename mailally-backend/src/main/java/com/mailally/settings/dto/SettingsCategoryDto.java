package com.mailally.settings.dto;

/**
 * Category metadata DTO.
 */
public class SettingsCategoryDto {

    private String categoryName;
    private String description;
    private int totalSettingsCount;

    public SettingsCategoryDto() {}

    public SettingsCategoryDto(String categoryName, String description, int totalSettingsCount) {
        this.categoryName = categoryName;
        this.description = description;
        this.totalSettingsCount = totalSettingsCount;
    }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getTotalSettingsCount() { return totalSettingsCount; }
    public void setTotalSettingsCount(int totalSettingsCount) { this.totalSettingsCount = totalSettingsCount; }
}
