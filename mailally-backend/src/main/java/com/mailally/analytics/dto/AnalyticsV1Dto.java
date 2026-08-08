package com.mailally.analytics.dto;

import com.mailally.analytics.calculator.CampaignHealthCalculator;
import com.mailally.analytics.provider.IndustryBenchmarkProvider;

import java.util.List;

/**
 * Enterprise analytics DTO for Phase 1 Demo.
 * Strictly event-engine driven with zero fake data.
 */
public class AnalyticsV1Dto {

    private boolean hasData;
    private Long campaignId;
    private String campaignName;
    private String campaignStatus;

    // Health Score
    private double healthScore;
    private String healthRating;
    private String healthSummary;

    // Industry Benchmarks
    private List<IndustryBenchmarkProvider.BenchmarkMetric> benchmarks;

    // Section 1: Campaign Summary
    private CampaignSummaryDto campaignSummary;

    // Section 2: Delivery Funnel
    private DeliveryFunnelDto deliveryFunnel;

    // Section 3: Key Performance Indicators (KPIs)
    private KpiMetricsDto kpis;

    // Section 4: Timeline
    private List<TimeSeriesDataPointDto> timeline;

    // Section 5: Recipient Activity Table
    private List<RecipientActivityDto> recipientActivities;

    // Section 6: Live Activity Feed
    private List<LiveActivityFeedDto> liveActivityFeed;

    public AnalyticsV1Dto() {}

    public boolean isHasData() { return hasData; }
    public void setHasData(boolean hasData) { this.hasData = hasData; }

    public Long getCampaignId() { return campaignId; }
    public void setCampaignId(Long campaignId) { this.campaignId = campaignId; }

    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }

    public String getCampaignStatus() { return campaignStatus; }
    public void setCampaignStatus(String campaignStatus) { this.campaignStatus = campaignStatus; }

    public double getHealthScore() { return healthScore; }
    public void setHealthScore(double healthScore) { this.healthScore = healthScore; }

    public String getHealthRating() { return healthRating; }
    public void setHealthRating(String healthRating) { this.healthRating = healthRating; }

    public String getHealthSummary() { return healthSummary; }
    public void setHealthSummary(String healthSummary) { this.healthSummary = healthSummary; }

    public List<IndustryBenchmarkProvider.BenchmarkMetric> getBenchmarks() { return benchmarks; }
    public void setBenchmarks(List<IndustryBenchmarkProvider.BenchmarkMetric> benchmarks) { this.benchmarks = benchmarks; }

    public CampaignSummaryDto getCampaignSummary() { return campaignSummary; }
    public void setCampaignSummary(CampaignSummaryDto campaignSummary) { this.campaignSummary = campaignSummary; }

    public DeliveryFunnelDto getDeliveryFunnel() { return deliveryFunnel; }
    public void setDeliveryFunnel(DeliveryFunnelDto deliveryFunnel) { this.deliveryFunnel = deliveryFunnel; }

    public KpiMetricsDto getKpis() { return kpis; }
    public void setKpis(KpiMetricsDto kpis) { this.kpis = kpis; }

    public List<TimeSeriesDataPointDto> getTimeline() { return timeline; }
    public void setTimeline(List<TimeSeriesDataPointDto> timeline) { this.timeline = timeline; }

    public List<RecipientActivityDto> getRecipientActivities() { return recipientActivities; }
    public void setRecipientActivities(List<RecipientActivityDto> recipientActivities) { this.recipientActivities = recipientActivities; }

    public List<LiveActivityFeedDto> getLiveActivityFeed() { return liveActivityFeed; }
    public void setLiveActivityFeed(List<LiveActivityFeedDto> liveActivityFeed) { this.liveActivityFeed = liveActivityFeed; }

    // Nested DTO classes
    public static class CampaignSummaryDto {
        private long totalRecipients;
        private long sent;
        private long delivered;
        private long failed;
        private long queued;
        private long sending;

        public CampaignSummaryDto() {}
        public CampaignSummaryDto(long totalRecipients, long sent, long delivered, long failed, long queued, long sending) {
            this.totalRecipients = totalRecipients;
            this.sent = sent;
            this.delivered = delivered;
            this.failed = failed;
            this.queued = queued;
            this.sending = sending;
        }

        public long getTotalRecipients() { return totalRecipients; }
        public long getSent() { return sent; }
        public long getDelivered() { return delivered; }
        public long getFailed() { return failed; }
        public long getQueued() { return queued; }
        public long getSending() { return sending; }
    }

    public static class DeliveryFunnelDto {
        private long queued;
        private long sent;
        private long delivered;
        private long opened;
        private long clicked;

        private double sentPct;
        private double deliveredPct;
        private double openPct;
        private double clickPct;

        public DeliveryFunnelDto() {}
        public DeliveryFunnelDto(long queued, long sent, long delivered, long opened, long clicked,
                                 double sentPct, double deliveredPct, double openPct, double clickPct) {
            this.queued = queued;
            this.sent = sent;
            this.delivered = delivered;
            this.opened = opened;
            this.clicked = clicked;
            this.sentPct = sentPct;
            this.deliveredPct = deliveredPct;
            this.openPct = openPct;
            this.clickPct = clickPct;
        }

        public long getQueued() { return queued; }
        public long getSent() { return sent; }
        public long getDelivered() { return delivered; }
        public long getOpened() { return opened; }
        public long getClicked() { return clicked; }

        public double getSentPct() { return sentPct; }
        public double getDeliveredPct() { return deliveredPct; }
        public double getOpenPct() { return openPct; }
        public double getClickPct() { return clickPct; }
    }

    public static class KpiMetricsDto {
        private double deliveryRate;
        private double openRate;
        private double clickRate;
        private double bounceRate;
        private double complaintRate;
        private double unsubscribeRate;

        public KpiMetricsDto() {}
        public KpiMetricsDto(double deliveryRate, double openRate, double clickRate, double bounceRate, double complaintRate, double unsubscribeRate) {
            this.deliveryRate = deliveryRate;
            this.openRate = openRate;
            this.clickRate = clickRate;
            this.bounceRate = bounceRate;
            this.complaintRate = complaintRate;
            this.unsubscribeRate = unsubscribeRate;
        }

        public double getDeliveryRate() { return deliveryRate; }
        public double getOpenRate() { return openRate; }
        public double getClickRate() { return clickRate; }
        public double getBounceRate() { return bounceRate; }
        public double getComplaintRate() { return complaintRate; }
        public double getUnsubscribeRate() { return unsubscribeRate; }
    }

    public static class RecipientActivityDto {
        private Long recipientId;
        private String email;
        private String status;
        private String sentAt;
        private String deliveredAt;
        private String openedAt;
        private String clickedAt;
        private String bouncedAt;

        public RecipientActivityDto() {}
        public RecipientActivityDto(Long recipientId, String email, String status, String sentAt, String deliveredAt, String openedAt, String clickedAt, String bouncedAt) {
            this.recipientId = recipientId;
            this.email = email;
            this.status = status;
            this.sentAt = sentAt;
            this.deliveredAt = deliveredAt;
            this.openedAt = openedAt;
            this.clickedAt = clickedAt;
            this.bouncedAt = bouncedAt;
        }

        public Long getRecipientId() { return recipientId; }
        public String getEmail() { return email; }
        public String getStatus() { return status; }
        public String getSentAt() { return sentAt; }
        public String getDeliveredAt() { return deliveredAt; }
        public String getOpenedAt() { return openedAt; }
        public String getClickedAt() { return clickedAt; }
        public String getBouncedAt() { return bouncedAt; }
    }

    public static class LiveActivityFeedDto {
        private String timestamp;
        private String recipientEmail;
        private String campaignName;
        private String eventType;
        private String provider;
        private String formattedMessage;

        public LiveActivityFeedDto() {}
        public LiveActivityFeedDto(String timestamp, String recipientEmail, String campaignName, String eventType, String provider, String formattedMessage) {
            this.timestamp = timestamp;
            this.recipientEmail = recipientEmail;
            this.campaignName = campaignName;
            this.eventType = eventType;
            this.provider = provider;
            this.formattedMessage = formattedMessage;
        }

        public String getTimestamp() { return timestamp; }
        public String getRecipientEmail() { return recipientEmail; }
        public String getCampaignName() { return campaignName; }
        public String getEventType() { return eventType; }
        public String getProvider() { return provider; }
        public String getFormattedMessage() { return formattedMessage; }
    }
}
