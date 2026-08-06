package com.mailally.billing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for recording a payment transaction against an invoice.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordPaymentRequestDto {

    @NotNull(message = "Billing ID is required")
    private Long billingId;

    @NotNull(message = "Payment amount is required")
    @Min(value = 0, message = "Payment amount must be positive")
    private Double amount;

    private String paymentMethod; // CARD, BANK_TRANSFER, UPI, NET_BANKING, WALLET, OFFLINE, OTHER
    private String transactionReference;
    private String notes;

    public RecordPaymentRequestDto() {}

    public RecordPaymentRequestDto(Long billingId, Double amount, String paymentMethod,
                                  String transactionReference, String notes) {
        this.billingId = billingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.notes = notes;
    }

    public Long getBillingId() { return billingId; }
    public void setBillingId(Long billingId) { this.billingId = billingId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
