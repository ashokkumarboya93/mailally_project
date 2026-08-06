package com.mailally.analytics.dto;

/**
 * Audience and Contact analytics breakdown (status distribution, growth rates).
 */
public class AudienceAnalyticsDto {

    private long totalContacts;
    private long subscribedContacts;
    private long unsubscribedContacts;
    private long bouncedContacts;
    private long inactiveContacts;
    private long newContactsThisMonth;
    private double growthRatePercentage;

    public AudienceAnalyticsDto() {}

    public AudienceAnalyticsDto(long totalContacts, long subscribedContacts, long unsubscribedContacts,
                                long bouncedContacts, long inactiveContacts, long newContactsThisMonth,
                                double growthRatePercentage) {
        this.totalContacts = totalContacts;
        this.subscribedContacts = subscribedContacts;
        this.unsubscribedContacts = unsubscribedContacts;
        this.bouncedContacts = bouncedContacts;
        this.inactiveContacts = inactiveContacts;
        this.newContactsThisMonth = newContactsThisMonth;
        this.growthRatePercentage = growthRatePercentage;
    }

    public long getTotalContacts() { return totalContacts; }
    public void setTotalContacts(long totalContacts) { this.totalContacts = totalContacts; }
    public long getSubscribedContacts() { return subscribedContacts; }
    public void setSubscribedContacts(long subscribedContacts) { this.subscribedContacts = subscribedContacts; }
    public long getUnsubscribedContacts() { return unsubscribedContacts; }
    public void setUnsubscribedContacts(long unsubscribedContacts) { this.unsubscribedContacts = unsubscribedContacts; }
    public long getBouncedContacts() { return bouncedContacts; }
    public void setBouncedContacts(long bouncedContacts) { this.bouncedContacts = bouncedContacts; }
    public long getInactiveContacts() { return inactiveContacts; }
    public void setInactiveContacts(long inactiveContacts) { this.inactiveContacts = inactiveContacts; }
    public long getNewContactsThisMonth() { return newContactsThisMonth; }
    public void setNewContactsThisMonth(long newContactsThisMonth) { this.newContactsThisMonth = newContactsThisMonth; }
    public double getGrowthRatePercentage() { return growthRatePercentage; }
    public void setGrowthRatePercentage(double growthRatePercentage) { this.growthRatePercentage = growthRatePercentage; }

    public static AudienceAnalyticsDtoBuilder builder() { return new AudienceAnalyticsDtoBuilder(); }

    public static class AudienceAnalyticsDtoBuilder {
        private long totalContacts;
        private long subscribedContacts;
        private long unsubscribedContacts;
        private long bouncedContacts;
        private long inactiveContacts;
        private long newContactsThisMonth;
        private double growthRatePercentage;

        AudienceAnalyticsDtoBuilder() {}

        public AudienceAnalyticsDtoBuilder totalContacts(long totalContacts) { this.totalContacts = totalContacts; return this; }
        public AudienceAnalyticsDtoBuilder subscribedContacts(long subscribedContacts) { this.subscribedContacts = subscribedContacts; return this; }
        public AudienceAnalyticsDtoBuilder unsubscribedContacts(long unsubscribedContacts) { this.unsubscribedContacts = unsubscribedContacts; return this; }
        public AudienceAnalyticsDtoBuilder bouncedContacts(long bouncedContacts) { this.bouncedContacts = bouncedContacts; return this; }
        public AudienceAnalyticsDtoBuilder inactiveContacts(long inactiveContacts) { this.inactiveContacts = inactiveContacts; return this; }
        public AudienceAnalyticsDtoBuilder newContactsThisMonth(long newContactsThisMonth) { this.newContactsThisMonth = newContactsThisMonth; return this; }
        public AudienceAnalyticsDtoBuilder growthRatePercentage(double growthRatePercentage) { this.growthRatePercentage = growthRatePercentage; return this; }

        public AudienceAnalyticsDto build() {
            return new AudienceAnalyticsDto(totalContacts, subscribedContacts, unsubscribedContacts,
                    bouncedContacts, inactiveContacts, newContactsThisMonth, growthRatePercentage);
        }
    }
}
