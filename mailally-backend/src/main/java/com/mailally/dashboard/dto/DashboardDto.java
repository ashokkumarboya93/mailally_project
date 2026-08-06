package com.mailally.dashboard.dto;

/**
 * Data Transfer Object for Dashboard.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class DashboardDto {

    private Long totalCampaigns;
    private Long totalSent;
    private Long totalContacts;
    private Long totalTemplates;

    public DashboardDto() {}

    public DashboardDto(Long totalCampaigns, Long totalSent, Long totalContacts, Long totalTemplates) {
        this.totalCampaigns = totalCampaigns;
        this.totalSent = totalSent;
        this.totalContacts = totalContacts;
        this.totalTemplates = totalTemplates;
    }

    public Long getTotalCampaigns() { return totalCampaigns; }
    public void setTotalCampaigns(Long totalCampaigns) { this.totalCampaigns = totalCampaigns; }
    public Long getTotalSent() { return totalSent; }
    public void setTotalSent(Long totalSent) { this.totalSent = totalSent; }
    public Long getTotalContacts() { return totalContacts; }
    public void setTotalContacts(Long totalContacts) { this.totalContacts = totalContacts; }
    public Long getTotalTemplates() { return totalTemplates; }
    public void setTotalTemplates(Long totalTemplates) { this.totalTemplates = totalTemplates; }

    public static DashboardDtoBuilder builder() { return new DashboardDtoBuilder(); }

    public static class DashboardDtoBuilder {
        private Long totalCampaigns;
        private Long totalSent;
        private Long totalContacts;
        private Long totalTemplates;

        DashboardDtoBuilder() {}

        public DashboardDtoBuilder totalCampaigns(Long totalCampaigns) { this.totalCampaigns = totalCampaigns; return this; }
        public DashboardDtoBuilder totalSent(Long totalSent) { this.totalSent = totalSent; return this; }
        public DashboardDtoBuilder totalContacts(Long totalContacts) { this.totalContacts = totalContacts; return this; }
        public DashboardDtoBuilder totalTemplates(Long totalTemplates) { this.totalTemplates = totalTemplates; return this; }

        public DashboardDto build() {
            return new DashboardDto(totalCampaigns, totalSent, totalContacts, totalTemplates);
        }
    }
}
