package com.mailally.analytics.dto;

/**
 * Data point for time-series charts (daily, weekly, monthly, yearly).
 */
public class TimeSeriesDataPointDto {

    private String label; // e.g. "2026-08-01", "Week 31", "August 2026"
    private long primaryValue; // e.g., Sent emails
    private long secondaryValue; // e.g., Delivered / Opens
    private long tertiaryValue; // e.g., Clicks

    public TimeSeriesDataPointDto() {}

    public TimeSeriesDataPointDto(String label, long primaryValue, long secondaryValue) {
        this.label = label;
        this.primaryValue = primaryValue;
        this.secondaryValue = secondaryValue;
    }

    public TimeSeriesDataPointDto(String label, long primaryValue, long secondaryValue, long tertiaryValue) {
        this.label = label;
        this.primaryValue = primaryValue;
        this.secondaryValue = secondaryValue;
        this.tertiaryValue = tertiaryValue;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public long getPrimaryValue() { return primaryValue; }
    public void setPrimaryValue(long primaryValue) { this.primaryValue = primaryValue; }
    public long getSecondaryValue() { return secondaryValue; }
    public void setSecondaryValue(long secondaryValue) { this.secondaryValue = secondaryValue; }
    public long getTertiaryValue() { return tertiaryValue; }
    public void setTertiaryValue(long tertiaryValue) { this.tertiaryValue = tertiaryValue; }
}

