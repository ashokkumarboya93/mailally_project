package com.mailally.billing.dto;

import java.time.LocalDateTime;

/**
 * Payment transaction record DTO.
 */
public class PaymentDto {

    private String invoiceNumber;
    private double paidAmount;
    private String paymentMethod;
    private String transactionReference;
    private LocalDateTime paymentDate;

    public PaymentDto() {}

    public PaymentDto(String invoiceNumber, double paidAmount, String paymentMethod, String transactionReference, LocalDateTime paymentDate) {
        this.invoiceNumber = invoiceNumber;
        this.paidAmount = paidAmount;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.paymentDate = paymentDate;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
