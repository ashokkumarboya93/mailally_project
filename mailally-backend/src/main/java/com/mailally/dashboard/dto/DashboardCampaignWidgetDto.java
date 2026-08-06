package com.mailally.dashboard.dto;

import com.mailally.analytics.dto.CampaignAnalyticsDto;

import java.util.List;

/**
 * Executive widgets for campaign performance ranking and metrics.
 */
public class DashboardCampaignWidgetDto {

    private List<CampaignAnalyticsDto> topPerformingCampaigns;
    private List<CampaignAnalyticsDto> lowestPerformingCampaigns;
    private CampaignAnalyticsDto mostOpenedCampaign;
    private CampaignAnalyticsDto highestDeliveryCampaign;
    private CampaignAnalyticsDto highestBounceCampaign;

    public DashboardCampaignWidgetDto() {}

    public DashboardCampaignWidgetDto(List<CampaignAnalyticsDto> topPerformingCampaigns,
                                      List<CampaignAnalyticsDto> lowestPerformingCampaigns,
                                      CampaignAnalyticsDto mostOpenedCampaign,
                                      CampaignAnalyticsDto highestDeliveryCampaign,
                                      CampaignAnalyticsDto highestBounceCampaign) {
        this.topPerformingCampaigns = topPerformingCampaigns;
        this.lowestPerformingCampaigns = lowestPerformingCampaigns;
        this.mostOpenedCampaign = mostOpenedCampaign;
        this.highestDeliveryCampaign = highestDeliveryCampaign;
        this.highestBounceCampaign = highestBounceCampaign;
    }

    public List<CampaignAnalyticsDto> getTopPerformingCampaigns() { return topPerformingCampaigns; }
    public void setTopPerformingCampaigns(List<CampaignAnalyticsDto> topPerformingCampaigns) { this.topPerformingCampaigns = topPerformingCampaigns; }
    public List<CampaignAnalyticsDto> getLowestPerformingCampaigns() { return lowestPerformingCampaigns; }
    public void setLowestPerformingCampaigns(List<CampaignAnalyticsDto> lowestPerformingCampaigns) { this.lowestPerformingCampaigns = lowestPerformingCampaigns; }
    public CampaignAnalyticsDto getMostOpenedCampaign() { return mostOpenedCampaign; }
    public void setMostOpenedCampaign(CampaignAnalyticsDto mostOpenedCampaign) { this.mostOpenedCampaign = mostOpenedCampaign; }
    public CampaignAnalyticsDto getHighestDeliveryCampaign() { return highestDeliveryCampaign; }
    public void setHighestDeliveryCampaign(CampaignAnalyticsDto highestDeliveryCampaign) { this.highestDeliveryCampaign = highestDeliveryCampaign; }
    public CampaignAnalyticsDto getHighestBounceCampaign() { return highestBounceCampaign; }
    public void setHighestBounceCampaign(CampaignAnalyticsDto highestBounceCampaign) { this.highestBounceCampaign = highestBounceCampaign; }

    public static DashboardCampaignWidgetDtoBuilder builder() { return new DashboardCampaignWidgetDtoBuilder(); }

    public static class DashboardCampaignWidgetDtoBuilder {
        private List<CampaignAnalyticsDto> topPerformingCampaigns;
        private List<CampaignAnalyticsDto> lowestPerformingCampaigns;
        private CampaignAnalyticsDto mostOpenedCampaign;
        private CampaignAnalyticsDto highestDeliveryCampaign;
        private CampaignAnalyticsDto highestBounceCampaign;

        DashboardCampaignWidgetDtoBuilder() {}

        public DashboardCampaignWidgetDtoBuilder topPerformingCampaigns(List<CampaignAnalyticsDto> topPerformingCampaigns) { this.topPerformingCampaigns = topPerformingCampaigns; return this; }
        public DashboardCampaignWidgetDtoBuilder lowestPerformingCampaigns(List<CampaignAnalyticsDto> lowestPerformingCampaigns) { this.lowestPerformingCampaigns = lowestPerformingCampaigns; return this; }
        public DashboardCampaignWidgetDtoBuilder mostOpenedCampaign(CampaignAnalyticsDto mostOpenedCampaign) { this.mostOpenedCampaign = mostOpenedCampaign; return this; }
        public DashboardCampaignWidgetDtoBuilder highestDeliveryCampaign(CampaignAnalyticsDto highestDeliveryCampaign) { this.highestDeliveryCampaign = highestDeliveryCampaign; return this; }
        public DashboardCampaignWidgetDtoBuilder highestBounceCampaign(CampaignAnalyticsDto highestBounceCampaign) { this.highestBounceCampaign = highestBounceCampaign; return this; }

        public DashboardCampaignWidgetDto build() {
            return new DashboardCampaignWidgetDto(topPerformingCampaigns, lowestPerformingCampaigns,
                    mostOpenedCampaign, highestDeliveryCampaign, highestBounceCampaign);
        }
    }
}
