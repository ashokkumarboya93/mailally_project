package com.mailally.settings.dto;

/**
 * Brand and theme preferences DTO.
 */
public class ThemeSettingsDto {

    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private String themeMode; // LIGHT, DARK, SYSTEM
    private String emailFooterHtml;

    public ThemeSettingsDto() {}

    public ThemeSettingsDto(String logoUrl, String primaryColor, String secondaryColor, String themeMode, String emailFooterHtml) {
        this.logoUrl = logoUrl;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.themeMode = themeMode;
        this.emailFooterHtml = emailFooterHtml;
    }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }
    public String getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; }
    public String getThemeMode() { return themeMode; }
    public void setThemeMode(String themeMode) { this.themeMode = themeMode; }
    public String getEmailFooterHtml() { return emailFooterHtml; }
    public void setEmailFooterHtml(String emailFooterHtml) { this.emailFooterHtml = emailFooterHtml; }
}
