package com.mailally.billing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

/**
 * Request DTO for updating existing invoice details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateBillingRequestDto {

    private LocalDateTime dueDate;
    private String billingEmail;
    private String billingAddress;
    private String notes;

    public UpdateBillingRequestDto() {}

    public UpdateBillingRequestDto(LocalDateTime dueDate, String billingEmail, String billingAddress, String notes) {
        this.dueDate = dueDate;
        this.billingEmail = billingEmail;
        this.billingAddress = billingAddress;
        this.notes = notes;
    }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public String getBillingEmail() { return billingEmail; }
    public void setBillingEmail(String billingEmail) { this.billingEmail = billingEmail; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
