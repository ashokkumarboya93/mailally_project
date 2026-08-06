package com.mailally.billing.dto;

import java.time.LocalDateTime;

/**
 * Detailed invoice DTO for printable/exportable invoice representation.
 */
public class InvoiceDto {

    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
    private String billingEmail;
    private String billingAddress;
    private String currency;
    private double subtotal;
    private double taxAmount;
    private double discountAmount;
    private double totalAmount;
    private double balanceAmount;
    private String paymentStatus;

    public InvoiceDto() {}

    public InvoiceDto(String invoiceNumber, LocalDateTime invoiceDate, LocalDateTime dueDate, String billingEmail,
                      String billingAddress, String currency, double subtotal, double taxAmount,
                      double discountAmount, double totalAmount, double balanceAmount, String paymentStatus) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.billingEmail = billingEmail;
        this.billingAddress = billingAddress;
        this.currency = currency;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.balanceAmount = balanceAmount;
        this.paymentStatus = paymentStatus;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public String getBillingEmail() { return billingEmail; }
    public void setBillingEmail(String billingEmail) { this.billingEmail = billingEmail; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(double taxAmount) { this.taxAmount = taxAmount; }
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public double getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(double balanceAmount) { this.balanceAmount = balanceAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
