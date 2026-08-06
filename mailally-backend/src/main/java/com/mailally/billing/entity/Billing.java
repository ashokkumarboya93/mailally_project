package com.mailally.billing.entity;

import com.mailally.organization.entity.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity representing an organization billing invoice and financial transaction record.
 */
@Entity
@Table(name = "billings")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "invoice_number", nullable = false, length = 100)
    private String invoiceNumber;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency; // USD, EUR, INR, GBP, etc.

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "balance_amount")
    private Double balanceAmount;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod; // CARD, BANK_TRANSFER, UPI, NET_BANKING, WALLET, OFFLINE, OTHER

    @Column(name = "payment_status", nullable = false, length = 30)
    private String paymentStatus; // PENDING, PAID, FAILED, REFUNDED, PARTIALLY_PAID, CANCELLED

    @Column(name = "transaction_reference", length = 255)
    private String transactionReference;

    @Column(name = "billing_address", length = 500)
    private String billingAddress;

    @Column(name = "billing_email", length = 255)
    private String billingEmail;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Billing() {}

    public Billing(Long id, Organization organization, String invoiceNumber, LocalDateTime invoiceDate,
                   LocalDateTime dueDate, LocalDateTime paymentDate, String currency, Double subtotal,
                   Double taxAmount, Double discountAmount, Double totalAmount, Double paidAmount,
                   Double balanceAmount, String paymentMethod, String paymentStatus, String transactionReference,
                   String billingAddress, String billingEmail, String notes, Long createdBy, Long updatedBy,
                   LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
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
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
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
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
        if (this.invoiceDate == null) this.invoiceDate = LocalDateTime.now();
        if (this.currency == null) this.currency = "USD";
        if (this.subtotal == null) this.subtotal = 0.0;
        if (this.taxAmount == null) this.taxAmount = 0.0;
        if (this.discountAmount == null) this.discountAmount = 0.0;
        if (this.totalAmount == null) this.totalAmount = 0.0;
        if (this.paidAmount == null) this.paidAmount = 0.0;
        if (this.balanceAmount == null) this.balanceAmount = this.totalAmount;
        if (this.paymentMethod == null) this.paymentMethod = "OFFLINE";
        if (this.paymentStatus == null) this.paymentStatus = "PENDING";
        if (this.isDeleted == null) this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static BillingBuilder builder() { return new BillingBuilder(); }

    public static class BillingBuilder {
        private Long id;
        private Organization organization;
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
        private Long createdBy;
        private Long updatedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean isDeleted;

        BillingBuilder() {}

        public BillingBuilder id(Long id) { this.id = id; return this; }
        public BillingBuilder organization(Organization organization) { this.organization = organization; return this; }
        public BillingBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public BillingBuilder invoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; return this; }
        public BillingBuilder dueDate(LocalDateTime dueDate) { this.dueDate = dueDate; return this; }
        public BillingBuilder paymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; return this; }
        public BillingBuilder currency(String currency) { this.currency = currency; return this; }
        public BillingBuilder subtotal(Double subtotal) { this.subtotal = subtotal; return this; }
        public BillingBuilder taxAmount(Double taxAmount) { this.taxAmount = taxAmount; return this; }
        public BillingBuilder discountAmount(Double discountAmount) { this.discountAmount = discountAmount; return this; }
        public BillingBuilder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public BillingBuilder paidAmount(Double paidAmount) { this.paidAmount = paidAmount; return this; }
        public BillingBuilder balanceAmount(Double balanceAmount) { this.balanceAmount = balanceAmount; return this; }
        public BillingBuilder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public BillingBuilder paymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public BillingBuilder transactionReference(String transactionReference) { this.transactionReference = transactionReference; return this; }
        public BillingBuilder billingAddress(String billingAddress) { this.billingAddress = billingAddress; return this; }
        public BillingBuilder billingEmail(String billingEmail) { this.billingEmail = billingEmail; return this; }
        public BillingBuilder notes(String notes) { this.notes = notes; return this; }
        public BillingBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public BillingBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public BillingBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public BillingBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public BillingBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Billing build() {
            return new Billing(id, organization, invoiceNumber, invoiceDate, dueDate, paymentDate, currency,
                    subtotal, taxAmount, discountAmount, totalAmount, paidAmount, balanceAmount, paymentMethod,
                    paymentStatus, transactionReference, billingAddress, billingEmail, notes, createdBy, updatedBy,
                    createdAt, updatedAt, isDeleted);
        }
    }
}
