package com.mailally.dashboard.dto;

import com.mailally.analytics.dto.ChartDataDto;

import java.util.List;

/**
 * Chart collection DTO for rendering executive dashboard charts.
 */
public class DashboardChartDto {

    private ChartDataDto emailDispatchTrend;
    private ChartDataDto campaignStatusDistribution;
    private ChartDataDto subscriberGrowth;
    private ChartDataDto providerPerformance;

    public DashboardChartDto() {}

    public DashboardChartDto(ChartDataDto emailDispatchTrend, ChartDataDto campaignStatusDistribution,
                             ChartDataDto subscriberGrowth, ChartDataDto providerPerformance) {
        this.emailDispatchTrend = emailDispatchTrend;
        this.campaignStatusDistribution = campaignStatusDistribution;
        this.subscriberGrowth = subscriberGrowth;
        this.providerPerformance = providerPerformance;
    }

    public ChartDataDto getEmailDispatchTrend() { return emailDispatchTrend; }
    public void setEmailDispatchTrend(ChartDataDto emailDispatchTrend) { this.emailDispatchTrend = emailDispatchTrend; }
    public ChartDataDto getCampaignStatusDistribution() { return campaignStatusDistribution; }
    public void setCampaignStatusDistribution(ChartDataDto campaignStatusDistribution) { this.campaignStatusDistribution = campaignStatusDistribution; }
    public ChartDataDto getSubscriberGrowth() { return subscriberGrowth; }
    public void setSubscriberGrowth(ChartDataDto subscriberGrowth) { this.subscriberGrowth = subscriberGrowth; }
    public ChartDataDto getProviderPerformance() { return providerPerformance; }
    public void setProviderPerformance(ChartDataDto providerPerformance) { this.providerPerformance = providerPerformance; }

    public static DashboardChartDtoBuilder builder() { return new DashboardChartDtoBuilder(); }

    public static class DashboardChartDtoBuilder {
        private ChartDataDto emailDispatchTrend;
        private ChartDataDto campaignStatusDistribution;
        private ChartDataDto subscriberGrowth;
        private ChartDataDto providerPerformance;

        DashboardChartDtoBuilder() {}

        public DashboardChartDtoBuilder emailDispatchTrend(ChartDataDto emailDispatchTrend) { this.emailDispatchTrend = emailDispatchTrend; return this; }
        public DashboardChartDtoBuilder campaignStatusDistribution(ChartDataDto campaignStatusDistribution) { this.campaignStatusDistribution = campaignStatusDistribution; return this; }
        public DashboardChartDtoBuilder subscriberGrowth(ChartDataDto subscriberGrowth) { this.subscriberGrowth = subscriberGrowth; return this; }
        public DashboardChartDtoBuilder providerPerformance(ChartDataDto providerPerformance) { this.providerPerformance = providerPerformance; return this; }

        public DashboardChartDto build() {
            return new DashboardChartDto(emailDispatchTrend, campaignStatusDistribution, subscriberGrowth, providerPerformance);
        }
    }
}
