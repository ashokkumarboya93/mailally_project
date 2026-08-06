package com.mailally.billing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Billing.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class BillingDto {

    private Long id;
    private Long organizationId;
    private String invoiceNumber;
    private BigDecimal amount;
    private String status;
    private LocalDateTime dueDate;

    public BillingDto() {}

    public BillingDto(Long id, Long organizationId, String invoiceNumber, BigDecimal amount, String status, LocalDateTime dueDate) {
        this.id = id;
        this.organizationId = organizationId;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.status = status;
        this.dueDate = dueDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public static BillingDtoBuilder builder() { return new BillingDtoBuilder(); }

    public static class BillingDtoBuilder {
        private Long id;
        private Long organizationId;
        private String invoiceNumber;
        private BigDecimal amount;
        private String status;
        private LocalDateTime dueDate;

        BillingDtoBuilder() {}

        public BillingDtoBuilder id(Long id) { this.id = id; return this; }
        public BillingDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public BillingDtoBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public BillingDtoBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public BillingDtoBuilder status(String status) { this.status = status; return this; }
        public BillingDtoBuilder dueDate(LocalDateTime dueDate) { this.dueDate = dueDate; return this; }

        public BillingDto build() {
            return new BillingDto(id, organizationId, invoiceNumber, amount, status, dueDate);
        }
    }
}
