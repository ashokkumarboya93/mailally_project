package com.mailally.billing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Request DTO for issuing a new invoice.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateInvoiceRequestDto {

    @NotNull(message = "Subtotal amount is required")
    @Min(value = 0, message = "Subtotal cannot be negative")
    private Double subtotal;

    private Double taxAmount;
    private Double discountAmount;
    private String currency; // USD, EUR, INR, etc.
    private LocalDateTime dueDate;

    @NotBlank(message = "Billing email is required")
    private String billingEmail;

    private String billingAddress;
    private String paymentMethod; // CARD, BANK_TRANSFER, UPI, NET_BANKING, WALLET, OFFLINE, OTHER
    private String notes;

    public CreateInvoiceRequestDto() {}

    public CreateInvoiceRequestDto(Double subtotal, Double taxAmount, Double discountAmount, String currency,
                                   LocalDateTime dueDate, String billingEmail, String billingAddress,
                                   String paymentMethod, String notes) {
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.currency = currency;
        this.dueDate = dueDate;
        this.billingEmail = billingEmail;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
    }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }
    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public String getBillingEmail() { return billingEmail; }
    public void setBillingEmail(String billingEmail) { this.billingEmail = billingEmail; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
