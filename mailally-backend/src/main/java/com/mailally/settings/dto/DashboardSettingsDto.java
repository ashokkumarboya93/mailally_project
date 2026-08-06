package com.mailally.settings.dto;

/**
 * Dashboard preference settings DTO.
 */
public class DashboardSettingsDto {

    private String defaultView;
    private String defaultDateRange;
    private String defaultChartType;

    public DashboardSettingsDto() {}

    public DashboardSettingsDto(String defaultView, String defaultDateRange, String defaultChartType) {
        this.defaultView = defaultView;
        this.defaultDateRange = defaultDateRange;
        this.defaultChartType = defaultChartType;
    }

    public String getDefaultView() { return defaultView; }
    public void setDefaultView(String defaultView) { this.defaultView = defaultView; }
    public String getDefaultDateRange() { return defaultDateRange; }
    public void setDefaultDateRange(String defaultDateRange) { this.defaultDateRange = defaultDateRange; }
    public String getDefaultChartType() { return defaultChartType; }
    public void setDefaultChartType(String defaultChartType) { this.defaultChartType = defaultChartType; }
}
