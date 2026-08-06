package com.mailally.billing.dto;

import java.util.Map;

/**
 * Financial metrics statistics DTO including payment method breakdown and status distribution.
 */
public class BillingStatisticsDto {

    private String currency;
    private double totalRevenue;
    private double averageInvoiceValue;
    private Map<String, Long> statusBreakdown;
    private Map<String, Double> paymentMethodRevenue;

    public BillingStatisticsDto() {}

    public BillingStatisticsDto(String currency, double totalRevenue, double averageInvoiceValue,
                                Map<String, Long> statusBreakdown, Map<String, Double> paymentMethodRevenue) {
        this.currency = currency;
        this.totalRevenue = totalRevenue;
        this.averageInvoiceValue = averageInvoiceValue;
        this.statusBreakdown = statusBreakdown;
        this.paymentMethodRevenue = paymentMethodRevenue;
    }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public double getAverageInvoiceValue() { return averageInvoiceValue; }
    public void setAverageInvoiceValue(double averageInvoiceValue) { this.averageInvoiceValue = averageInvoiceValue; }
    public Map<String, Long> getStatusBreakdown() { return statusBreakdown; }
    public void setStatusBreakdown(Map<String, Long> statusBreakdown) { this.statusBreakdown = statusBreakdown; }
    public Map<String, Double> getPaymentMethodRevenue() { return paymentMethodRevenue; }
    public void setPaymentMethodRevenue(Map<String, Double> paymentMethodRevenue) { this.paymentMethodRevenue = paymentMethodRevenue; }

    public static BillingStatisticsDtoBuilder builder() { return new BillingStatisticsDtoBuilder(); }

    public static class BillingStatisticsDtoBuilder {
        private String currency;
        private double totalRevenue;
        private double averageInvoiceValue;
        private Map<String, Long> statusBreakdown;
        private Map<String, Double> paymentMethodRevenue;

        BillingStatisticsDtoBuilder() {}

        public BillingStatisticsDtoBuilder currency(String currency) { this.currency = currency; return this; }
        public BillingStatisticsDtoBuilder totalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public BillingStatisticsDtoBuilder averageInvoiceValue(double averageInvoiceValue) { this.averageInvoiceValue = averageInvoiceValue; return this; }
        public BillingStatisticsDtoBuilder statusBreakdown(Map<String, Long> statusBreakdown) { this.statusBreakdown = statusBreakdown; return this; }
        public BillingStatisticsDtoBuilder paymentMethodRevenue(Map<String, Double> paymentMethodRevenue) { this.paymentMethodRevenue = paymentMethodRevenue; return this; }

        public BillingStatisticsDto build() {
            return new BillingStatisticsDto(currency, totalRevenue, averageInvoiceValue, statusBreakdown, paymentMethodRevenue);
        }
    }
}
