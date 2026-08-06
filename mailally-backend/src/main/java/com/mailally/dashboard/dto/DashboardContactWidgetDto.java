package com.mailally.dashboard.dto;

import com.mailally.analytics.dto.AudienceAnalyticsDto;
import com.mailally.contact.dto.ContactResponseDto;

import java.util.List;

/**
 * Dashboard widgets for contact growth and audience demographics.
 */
public class DashboardContactWidgetDto {

    private AudienceAnalyticsDto summary;
    private List<ContactResponseDto> latestContacts;
    private List<String> topCities;
    private List<String> topCountries;
    private String fastestGrowingSegment;

    public DashboardContactWidgetDto() {}

    public DashboardContactWidgetDto(AudienceAnalyticsDto summary, List<ContactResponseDto> latestContacts,
                                     List<String> topCities, List<String> topCountries, String fastestGrowingSegment) {
        this.summary = summary;
        this.latestContacts = latestContacts;
        this.topCities = topCities;
        this.topCountries = topCountries;
        this.fastestGrowingSegment = fastestGrowingSegment;
    }

    public AudienceAnalyticsDto getSummary() { return summary; }
    public void setSummary(AudienceAnalyticsDto summary) { this.summary = summary; }
    public List<ContactResponseDto> getLatestContacts() { return latestContacts; }
    public void setLatestContacts(List<ContactResponseDto> latestContacts) { this.latestContacts = latestContacts; }
    public List<String> getTopCities() { return topCities; }
    public void setTopCities(List<String> topCities) { this.topCities = topCities; }
    public List<String> getTopCountries() { return topCountries; }
    public void setTopCountries(List<String> topCountries) { this.topCountries = topCountries; }
    public String getFastestGrowingSegment() { return fastestGrowingSegment; }
    public void setFastestGrowingSegment(String fastestGrowingSegment) { this.fastestGrowingSegment = fastestGrowingSegment; }

    public static DashboardContactWidgetDtoBuilder builder() { return new DashboardContactWidgetDtoBuilder(); }

    public static class DashboardContactWidgetDtoBuilder {
        private AudienceAnalyticsDto summary;
        private List<ContactResponseDto> latestContacts;
        private List<String> topCities;
        private List<String> topCountries;
        private String fastestGrowingSegment;

        DashboardContactWidgetDtoBuilder() {}

        public DashboardContactWidgetDtoBuilder summary(AudienceAnalyticsDto summary) { this.summary = summary; return this; }
        public DashboardContactWidgetDtoBuilder latestContacts(List<ContactResponseDto> latestContacts) { this.latestContacts = latestContacts; return this; }
        public DashboardContactWidgetDtoBuilder topCities(List<String> topCities) { this.topCities = topCities; return this; }
        public DashboardContactWidgetDtoBuilder topCountries(List<String> topCountries) { this.topCountries = topCountries; return this; }
        public DashboardContactWidgetDtoBuilder fastestGrowingSegment(String fastestGrowingSegment) { this.fastestGrowingSegment = fastestGrowingSegment; return this; }

        public DashboardContactWidgetDto build() {
            return new DashboardContactWidgetDto(summary, latestContacts, topCities, topCountries, fastestGrowingSegment);
        }
    }
}
