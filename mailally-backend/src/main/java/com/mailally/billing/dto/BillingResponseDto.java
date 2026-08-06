package com.mailally.billing.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a Billing transaction entity.
 */
public class BillingResponseDto {

    private Long id;
    private Long organizationId;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private LocalDateTime dueDate;
    private LocalDateTime paymentDate;
    private String currency;
    private Double subtotal;
    private Double taxAmount;
    private Double discountAmount;
    private Double totalAmount;
    private Double paidAmount;
    private Double balanceAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionReference;
    private String billingAddress;
    private String billingEmail;
    private String notes;
    private LocalDateTime createdAt;

    public BillingResponseDto() {}

    public BillingResponseDto(Long id, Long organizationId, String invoiceNumber, LocalDateTime invoiceDate,
                              LocalDateTime dueDate, LocalDateTime paymentDate, String currency, Double subtotal,
                              Double taxAmount, Double discountAmount, Double totalAmount, Double paidAmount,
                              Double balanceAmount, String paymentMethod, String paymentStatus,
                              String transactionReference, String billingAddress, String billingEmail,
                              String notes, LocalDateTime createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.currency = currency;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.balanceAmount = balanceAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionReference = transactionReference;
        this.billingAddress = billingAddress;
        this.billingEmail = billingEmail;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }
    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public Double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Double paidAmount) { this.paidAmount = paidAmount; }
    public Double getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(Double balanceAmount) { this.balanceAmount = balanceAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public String getBillingEmail() { return billingEmail; }
    public void setBillingEmail(String billingEmail) { this.billingEmail = billingEmail; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static BillingResponseDtoBuilder builder() { return new BillingResponseDtoBuilder(); }

    public static class BillingResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private String invoiceNumber;
        private LocalDateTime invoiceDate;
        private LocalDateTime dueDate;
        private LocalDateTime paymentDate;
        private String currency;
        private Double subtotal;
        private Double taxAmount;
        private Double discountAmount;
        private Double totalAmount;
        private Double paidAmount;
        private Double balanceAmount;
        private String paymentMethod;
        private String paymentStatus;
        private String transactionReference;
        private String billingAddress;
        private String billingEmail;
        private String notes;
        private LocalDateTime createdAt;

        BillingResponseDtoBuilder() {}

        public BillingResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public BillingResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public BillingResponseDtoBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public BillingResponseDtoBuilder invoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; return this; }
        public BillingResponseDtoBuilder dueDate(LocalDateTime dueDate) { this.dueDate = dueDate; return this; }
        public BillingResponseDtoBuilder paymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; return this; }
        public BillingResponseDtoBuilder currency(String currency) { this.currency = currency; return this; }
        public BillingResponseDtoBuilder subtotal(Double subtotal) { this.subtotal = subtotal; return this; }
        public BillingResponseDtoBuilder taxAmount(Double taxAmount) { this.taxAmount = taxAmount; return this; }
        public BillingResponseDtoBuilder discountAmount(Double discountAmount) { this.discountAmount = discountAmount; return this; }
        public BillingResponseDtoBuilder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public BillingResponseDtoBuilder paidAmount(Double paidAmount) { this.paidAmount = paidAmount; return this; }
        public BillingResponseDtoBuilder balanceAmount(Double balanceAmount) { this.balanceAmount = balanceAmount; return this; }
        public BillingResponseDtoBuilder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public BillingResponseDtoBuilder paymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public BillingResponseDtoBuilder transactionReference(String transactionReference) { this.transactionReference = transactionReference; return this; }
        public BillingResponseDtoBuilder billingAddress(String billingAddress) { this.billingAddress = billingAddress; return this; }
        public BillingResponseDtoBuilder billingEmail(String billingEmail) { this.billingEmail = billingEmail; return this; }
        public BillingResponseDtoBuilder notes(String notes) { this.notes = notes; return this; }
        public BillingResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public BillingResponseDto build() {
            return new BillingResponseDto(id, organizationId, invoiceNumber, invoiceDate, dueDate, paymentDate,
                    currency, subtotal, taxAmount, discountAmount, totalAmount, paidAmount, balanceAmount,
                    paymentMethod, paymentStatus, transactionReference, billingAddress, billingEmail, notes, createdAt);
        }
    }
}
