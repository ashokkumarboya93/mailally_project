package com.mailally.analytics.calculator;

import org.springframework.stereotype.Component;

/**
 * Service calculating Campaign Health Score (0-100) using configurable weight strategies.
 */
@Component
public class CampaignHealthCalculator {

    // Configurable weight factors
    private double deliveryWeight = 0.40;
    private double openWeight = 0.30;
    private double clickWeight = 0.20;
    private double bouncePenalty = 1.50;
    private double complaintPenalty = 5.00;

    public HealthResult calculateHealthScore(double deliveryRate, double openRate, double clickRate, double bounceRate, double complaintRate) {
        if (deliveryRate == 0 && openRate == 0 && clickRate == 0 && bounceRate == 0 && complaintRate == 0) {
            return new HealthResult(0.0, "NO_DATA", "No campaign dispatch events recorded yet.");
        }

        double positiveScore = (deliveryRate * deliveryWeight) + (openRate * openWeight) + (clickRate * clickWeight);
        double penalties = (bounceRate * bouncePenalty) + (complaintRate * complaintPenalty);

        double finalScore = Math.max(0.0, Math.min(100.0, positiveScore - penalties + 20.0)); // Adjusted offset
        finalScore = Math.round(finalScore * 10.0) / 10.0;

        String rating = "EXCELLENT";
        String description = "Deliverability and audience engagement are operating at peak SLA standards.";

        if (finalScore < 50.0) {
            rating = "NEEDS_ATTENTION";
            description = "High bounce or complaint rates detected. Inspect sender reputation.";
        } else if (finalScore < 80.0) {
            rating = "GOOD";
            description = "Campaign metrics are performing stably within normal operational parameters.";
        }

        return new HealthResult(finalScore, rating, description);
    }

    public static class HealthResult {
        private final double score;
        private final String rating;
        private final String summary;

        public HealthResult(double score, String rating, String summary) {
            this.score = score;
            this.rating = rating;
            this.summary = summary;
        }

        public double getScore() { return score; }
        public String getRating() { return rating; }
        public String getSummary() { return summary; }
    }
}
