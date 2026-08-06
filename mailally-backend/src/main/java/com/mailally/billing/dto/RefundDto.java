package com.mailally.billing.dto;

import java.time.LocalDateTime;

/**
 * Refund transaction record DTO.
 */
public class RefundDto {

    private String invoiceNumber;
    private double refundAmount;
    private String reason;
    private String transactionReference;
    private LocalDateTime refundDate;

    public RefundDto() {}

    public RefundDto(String invoiceNumber, double refundAmount, String reason, String transactionReference, LocalDateTime refundDate) {
        this.invoiceNumber = invoiceNumber;
        this.refundAmount = refundAmount;
        this.reason = reason;
        this.transactionReference = transactionReference;
        this.refundDate = refundDate;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(double refundAmount) { this.refundAmount = refundAmount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    public LocalDateTime getRefundDate() { return refundDate; }
    public void setRefundDate(LocalDateTime refundDate) { this.refundDate = refundDate; }
}
