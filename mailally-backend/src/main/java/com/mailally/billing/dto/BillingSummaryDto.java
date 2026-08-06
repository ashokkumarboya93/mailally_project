package com.mailally.billing.dto;

/**
 * Summary breakdown of organization financial ledger.
 */
public class BillingSummaryDto {

    private String currency;
    private double totalBilled;
    private double totalPaid;
    private double totalOutstanding;
    private double totalRefunded;
    private long totalInvoicesCount;
    private long paidInvoicesCount;
    private long pendingInvoicesCount;

    public BillingSummaryDto() {}

    public BillingSummaryDto(String currency, double totalBilled, double totalPaid, double totalOutstanding,
                             double totalRefunded, long totalInvoicesCount, long paidInvoicesCount, long pendingInvoicesCount) {
        this.currency = currency;
        this.totalBilled = totalBilled;
        this.totalPaid = totalPaid;
        this.totalOutstanding = totalOutstanding;
        this.totalRefunded = totalRefunded;
        this.totalInvoicesCount = totalInvoicesCount;
        this.paidInvoicesCount = paidInvoicesCount;
        this.pendingInvoicesCount = pendingInvoicesCount;
    }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public double getTotalBilled() { return totalBilled; }
    public void setTotalBilled(double totalBilled) { this.totalBilled = totalBilled; }
    public double getTotalPaid() { return totalPaid; }
    public void setTotalPaid(double totalPaid) { this.totalPaid = totalPaid; }
    public double getTotalOutstanding() { return totalOutstanding; }
    public void setTotalOutstanding(double totalOutstanding) { this.totalOutstanding = totalOutstanding; }
    public double getTotalRefunded() { return totalRefunded; }
    public void setTotalRefunded(double totalRefunded) { this.totalRefunded = totalRefunded; }
    public long getTotalInvoicesCount() { return totalInvoicesCount; }
    public void setTotalInvoicesCount(long totalInvoicesCount) { this.totalInvoicesCount = totalInvoicesCount; }
    public long getPaidInvoicesCount() { return paidInvoicesCount; }
    public void setPaidInvoicesCount(long paidInvoicesCount) { this.paidInvoicesCount = paidInvoicesCount; }
    public long getPendingInvoicesCount() { return pendingInvoicesCount; }
    public void setPendingInvoicesCount(long pendingInvoicesCount) { this.pendingInvoicesCount = pendingInvoicesCount; }

    public static BillingSummaryDtoBuilder builder() { return new BillingSummaryDtoBuilder(); }

    public static class BillingSummaryDtoBuilder {
        private String currency;
        private double totalBilled;
        private double totalPaid;
        private double totalOutstanding;
        private double totalRefunded;
        private long totalInvoicesCount;
        private long paidInvoicesCount;
        private long pendingInvoicesCount;

        BillingSummaryDtoBuilder() {}

        public BillingSummaryDtoBuilder currency(String currency) { this.currency = currency; return this; }
        public BillingSummaryDtoBuilder totalBilled(double totalBilled) { this.totalBilled = totalBilled; return this; }
        public BillingSummaryDtoBuilder totalPaid(double totalPaid) { this.totalPaid = totalPaid; return this; }
        public BillingSummaryDtoBuilder totalOutstanding(double totalOutstanding) { this.totalOutstanding = totalOutstanding; return this; }
        public BillingSummaryDtoBuilder totalRefunded(double totalRefunded) { this.totalRefunded = totalRefunded; return this; }
        public BillingSummaryDtoBuilder totalInvoicesCount(long totalInvoicesCount) { this.totalInvoicesCount = totalInvoicesCount; return this; }
        public BillingSummaryDtoBuilder paidInvoicesCount(long paidInvoicesCount) { this.paidInvoicesCount = paidInvoicesCount; return this; }
        public BillingSummaryDtoBuilder pendingInvoicesCount(long pendingInvoicesCount) { this.pendingInvoicesCount = pendingInvoicesCount; return this; }

        public BillingSummaryDto build() {
            return new BillingSummaryDto(currency, totalBilled, totalPaid, totalOutstanding, totalRefunded,
                    totalInvoicesCount, paidInvoicesCount, pendingInvoicesCount);
        }
    }
}
