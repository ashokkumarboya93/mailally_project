package com.mailally.analytics.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PDF-ready structured report DTO payload for frontend or PDF rendering services.
 */
public class PdfReportDto {

    private String reportTitle;
    private String organizationName;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private DashboardOverviewDto summary;
    private List<CampaignAnalyticsDto> topCampaigns;
    private AudienceAnalyticsDto audienceSummary;
    private List<ProviderAnalyticsDto> providerPerformance;

    public PdfReportDto() {}

    public PdfReportDto(String reportTitle, String organizationName, LocalDateTime generatedAt,
                        String generatedBy, DashboardOverviewDto summary,
                        List<CampaignAnalyticsDto> topCampaigns, AudienceAnalyticsDto audienceSummary,
                        List<ProviderAnalyticsDto> providerPerformance) {
        this.reportTitle = reportTitle;
        this.organizationName = organizationName;
        this.generatedAt = generatedAt;
        this.generatedBy = generatedBy;
        this.summary = summary;
        this.topCampaigns = topCampaigns;
        this.audienceSummary = audienceSummary;
        this.providerPerformance = providerPerformance;
    }

    public String getReportTitle() { return reportTitle; }
    public void setReportTitle(String reportTitle) { this.reportTitle = reportTitle; }
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public DashboardOverviewDto getSummary() { return summary; }
    public void setSummary(DashboardOverviewDto summary) { this.summary = summary; }
    public List<CampaignAnalyticsDto> getTopCampaigns() { return topCampaigns; }
    public void setTopCampaigns(List<CampaignAnalyticsDto> topCampaigns) { this.topCampaigns = topCampaigns; }
    public AudienceAnalyticsDto getAudienceSummary() { return audienceSummary; }
    public void setAudienceSummary(AudienceAnalyticsDto audienceSummary) { this.audienceSummary = audienceSummary; }
    public List<ProviderAnalyticsDto> getProviderPerformance() { return providerPerformance; }
    public void setProviderPerformance(List<ProviderAnalyticsDto> providerPerformance) { this.providerPerformance = providerPerformance; }

    public static PdfReportDtoBuilder builder() { return new PdfReportDtoBuilder(); }

    public static class PdfReportDtoBuilder {
        private String reportTitle;
        private String organizationName;
        private LocalDateTime generatedAt;
        private String generatedBy;
        private DashboardOverviewDto summary;
        private List<CampaignAnalyticsDto> topCampaigns;
        private AudienceAnalyticsDto audienceSummary;
        private List<ProviderAnalyticsDto> providerPerformance;

        PdfReportDtoBuilder() {}

        public PdfReportDtoBuilder reportTitle(String reportTitle) { this.reportTitle = reportTitle; return this; }
        public PdfReportDtoBuilder organizationName(String organizationName) { this.organizationName = organizationName; return this; }
        public PdfReportDtoBuilder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
        public PdfReportDtoBuilder generatedBy(String generatedBy) { this.generatedBy = generatedBy; return this; }
        public PdfReportDtoBuilder summary(DashboardOverviewDto summary) { this.summary = summary; return this; }
        public PdfReportDtoBuilder topCampaigns(List<CampaignAnalyticsDto> topCampaigns) { this.topCampaigns = topCampaigns; return this; }
        public PdfReportDtoBuilder audienceSummary(AudienceAnalyticsDto audienceSummary) { this.audienceSummary = audienceSummary; return this; }
        public PdfReportDtoBuilder providerPerformance(List<ProviderAnalyticsDto> providerPerformance) { this.providerPerformance = providerPerformance; return this; }

        public PdfReportDto build() {
            return new PdfReportDto(reportTitle, organizationName, generatedAt, generatedBy, summary,
                    topCampaigns, audienceSummary, providerPerformance);
        }
    }
}
