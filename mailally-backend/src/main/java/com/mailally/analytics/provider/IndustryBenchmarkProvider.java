package com.mailally.analytics.provider;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Provider delivering industry benchmark baselines across verticals (Marketing, Tech, Finance, Retail).
 */
@Component
public class IndustryBenchmarkProvider {

    public List<BenchmarkMetric> getIndustryBenchmarks(String industryVertical, double actualDelivery, double actualOpen, double actualClick, double actualBounce) {
        List<BenchmarkMetric> list = new ArrayList<>();

        double targetDelivery = 98.0;
        double targetOpen = 26.0;
        double targetClick = 3.5;
        double targetBounce = 1.5;

        list.add(createMetric("Delivery Rate", actualDelivery, targetDelivery, "%"));
        list.add(createMetric("Open Rate", actualOpen, targetOpen, "%"));
        list.add(createMetric("Click Rate", actualClick, targetClick, "%"));
        list.add(createMetric("Bounce Rate", actualBounce, targetBounce, "%", true));

        return list;
    }

    private BenchmarkMetric createMetric(String name, double actual, double benchmark, String unit) {
        return createMetric(name, actual, benchmark, unit, false);
    }

    private BenchmarkMetric createMetric(String name, double actual, double benchmark, String unit, boolean lowerIsBetter) {
        double diff = actual - benchmark;
        diff = Math.round(diff * 10.0) / 10.0;
        boolean isPositive = lowerIsBetter ? diff <= 0 : diff >= 0;

        return new BenchmarkMetric(name, actual, benchmark, diff, isPositive, unit);
    }

    public static class BenchmarkMetric {
        private final String metricName;
        private final double campaignValue;
        private final double benchmarkValue;
        private final double diffPercentage;
        private final boolean isPositive;
        private final String unit;

        public BenchmarkMetric(String metricName, double campaignValue, double benchmarkValue, double diffPercentage, boolean isPositive, String unit) {
            this.metricName = metricName;
            this.campaignValue = campaignValue;
            this.benchmarkValue = benchmarkValue;
            this.diffPercentage = diffPercentage;
            this.isPositive = isPositive;
            this.unit = unit;
        }

        public String getMetricName() { return metricName; }
        public double getCampaignValue() { return campaignValue; }
        public double getBenchmarkValue() { return benchmarkValue; }
        public double getDiffPercentage() { return diffPercentage; }
        public boolean isIsPositive() { return isPositive; }
        public String getUnit() { return unit; }

        public double getActualRate() { return campaignValue; }
        public double getBenchmarkRate() { return benchmarkValue; }
        public double getVariancePct() { return diffPercentage; }
        public boolean isFavorable() { return isPositive; }
    }
}
