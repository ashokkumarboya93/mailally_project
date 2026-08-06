package com.mailally.billing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for recording a refund transaction against a paid invoice.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordRefundRequestDto {

    @NotNull(message = "Billing ID is required")
    private Long billingId;

    @NotNull(message = "Refund amount is required")
    @Min(value = 0, message = "Refund amount must be positive")
    private Double refundAmount;

    private String reason;
    private String transactionReference;

    public RecordRefundRequestDto() {}

    public RecordRefundRequestDto(Long billingId, Double refundAmount, String reason, String transactionReference) {
        this.billingId = billingId;
        this.refundAmount = refundAmount;
        this.reason = reason;
        this.transactionReference = transactionReference;
    }

    public Long getBillingId() { return billingId; }
    public void setBillingId(Long billingId) { this.billingId = billingId; }
    public Double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(Double refundAmount) { this.refundAmount = refundAmount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
}
