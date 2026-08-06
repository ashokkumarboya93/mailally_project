package com.mailally.email.dto;

/**
 * Aggregated delivery metrics and statistics for a campaign or organization.
 */
public class DeliveryStatsDto {

    private long totalSent;
    private long totalDelivered;
    private long totalBounced;
    private long totalFailed;
    private long totalOpened;
    private long totalClicked;
    private double deliveryRate;
    private double bounceRate;
    private double openRate;
    private double clickRate;

    public DeliveryStatsDto() {}

    public DeliveryStatsDto(long totalSent, long totalDelivered, long totalBounced, long totalFailed,
                            long totalOpened, long totalClicked, double deliveryRate, double bounceRate,
                            double openRate, double clickRate) {
        this.totalSent = totalSent;
        this.totalDelivered = totalDelivered;
        this.totalBounced = totalBounced;
        this.totalFailed = totalFailed;
        this.totalOpened = totalOpened;
        this.totalClicked = totalClicked;
        this.deliveryRate = deliveryRate;
        this.bounceRate = bounceRate;
        this.openRate = openRate;
        this.clickRate = clickRate;
    }

    public long getTotalSent() { return totalSent; }
    public void setTotalSent(long totalSent) { this.totalSent = totalSent; }
    public long getTotalDelivered() { return totalDelivered; }
    public void setTotalDelivered(long totalDelivered) { this.totalDelivered = totalDelivered; }
    public long getTotalBounced() { return totalBounced; }
    public void setTotalBounced(long totalBounced) { this.totalBounced = totalBounced; }
    public long getTotalFailed() { return totalFailed; }
    public void setTotalFailed(long totalFailed) { this.totalFailed = totalFailed; }
    public long getTotalOpened() { return totalOpened; }
    public void setTotalOpened(long totalOpened) { this.totalOpened = totalOpened; }
    public long getTotalClicked() { return totalClicked; }
    public void setTotalClicked(long totalClicked) { this.totalClicked = totalClicked; }
    public double getDeliveryRate() { return deliveryRate; }
    public void setDeliveryRate(double deliveryRate) { this.deliveryRate = deliveryRate; }
    public double getBounceRate() { return bounceRate; }
    public void setBounceRate(double bounceRate) { this.bounceRate = bounceRate; }
    public double getOpenRate() { return openRate; }
    public void setOpenRate(double openRate) { this.openRate = openRate; }
    public double getClickRate() { return clickRate; }
    public void setClickRate(double clickRate) { this.clickRate = clickRate; }

    public static DeliveryStatsDtoBuilder builder() { return new DeliveryStatsDtoBuilder(); }

    public static class DeliveryStatsDtoBuilder {
        private long totalSent;
        private long totalDelivered;
        private long totalBounced;
        private long totalFailed;
        private long totalOpened;
        private long totalClicked;
        private double deliveryRate;
        private double bounceRate;
        private double openRate;
        private double clickRate;

        DeliveryStatsDtoBuilder() {}

        public DeliveryStatsDtoBuilder totalSent(long totalSent) { this.totalSent = totalSent; return this; }
        public DeliveryStatsDtoBuilder totalDelivered(long totalDelivered) { this.totalDelivered = totalDelivered; return this; }
        public DeliveryStatsDtoBuilder totalBounced(long totalBounced) { this.totalBounced = totalBounced; return this; }
        public DeliveryStatsDtoBuilder totalFailed(long totalFailed) { this.totalFailed = totalFailed; return this; }
        public DeliveryStatsDtoBuilder totalOpened(long totalOpened) { this.totalOpened = totalOpened; return this; }
        public DeliveryStatsDtoBuilder totalClicked(long totalClicked) { this.totalClicked = totalClicked; return this; }
        public DeliveryStatsDtoBuilder deliveryRate(double deliveryRate) { this.deliveryRate = deliveryRate; return this; }
        public DeliveryStatsDtoBuilder bounceRate(double bounceRate) { this.bounceRate = bounceRate; return this; }
        public DeliveryStatsDtoBuilder openRate(double openRate) { this.openRate = openRate; return this; }
        public DeliveryStatsDtoBuilder clickRate(double clickRate) { this.clickRate = clickRate; return this; }

        public DeliveryStatsDto build() {
            return new DeliveryStatsDto(totalSent, totalDelivered, totalBounced, totalFailed, totalOpened,
                    totalClicked, deliveryRate, bounceRate, openRate, clickRate);
        }
    }
}
