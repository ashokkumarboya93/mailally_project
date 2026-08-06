package com.mailally.analytics.dto;

/**
 * Data Transfer Object for Analytics.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class AnalyticsDto {

    private Long totalEmailsSent;
    private Long totalDelivered;
    private Double openRate;
    private Double clickRate;

    public AnalyticsDto() {}

    public AnalyticsDto(Long totalEmailsSent, Long totalDelivered, Double openRate, Double clickRate) {
        this.totalEmailsSent = totalEmailsSent;
        this.totalDelivered = totalDelivered;
        this.openRate = openRate;
        this.clickRate = clickRate;
    }

    public Long getTotalEmailsSent() { return totalEmailsSent; }
    public void setTotalEmailsSent(Long totalEmailsSent) { this.totalEmailsSent = totalEmailsSent; }
    public Long getTotalDelivered() { return totalDelivered; }
    public void setTotalDelivered(Long totalDelivered) { this.totalDelivered = totalDelivered; }
    public Double getOpenRate() { return openRate; }
    public void setOpenRate(Double openRate) { this.openRate = openRate; }
    public Double getClickRate() { return clickRate; }
    public void setClickRate(Double clickRate) { this.clickRate = clickRate; }

    public static AnalyticsDtoBuilder builder() { return new AnalyticsDtoBuilder(); }

    public static class AnalyticsDtoBuilder {
        private Long totalEmailsSent;
        private Long totalDelivered;
        private Double openRate;
        private Double clickRate;

        AnalyticsDtoBuilder() {}

        public AnalyticsDtoBuilder totalEmailsSent(Long totalEmailsSent) { this.totalEmailsSent = totalEmailsSent; return this; }
        public AnalyticsDtoBuilder totalDelivered(Long totalDelivered) { this.totalDelivered = totalDelivered; return this; }
        public AnalyticsDtoBuilder openRate(Double openRate) { this.openRate = openRate; return this; }
        public AnalyticsDtoBuilder clickRate(Double clickRate) { this.clickRate = clickRate; return this; }

        public AnalyticsDto build() {
            return new AnalyticsDto(totalEmailsSent, totalDelivered, openRate, clickRate);
        }
    }
}
