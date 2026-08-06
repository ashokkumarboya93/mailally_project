package com.mailally.subscription.entity;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a Subscription plan tier and organization allocation.
 */
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", nullable = false, length = 50)
    private String code; // FREE, STARTER, PRO, BUSINESS, ENTERPRISE

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Column(name = "max_contacts", nullable = false)
    private Integer maxContacts;

    @Column(name = "max_emails_per_month", nullable = false)
    private Integer maxEmailsPerMonth;

    @Column(name = "max_users", nullable = false)
    private Integer maxUsers;

    @Column(name = "max_campaigns", nullable = false)
    private Integer maxCampaigns;

    @Column(name = "storage_limit_mb", nullable = false)
    private Integer storageLimitMb;

    @Column(name = "api_limit", nullable = false)
    private Integer apiLimit;

    @Column(name = "ai_limit", nullable = false)
    private Integer aiLimit;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // ACTIVE, TRIAL, EXPIRED, GRACE_PERIOD, CANCELLED

    @Column(name = "trial_ends_at")
    private LocalDateTime trialEndsAt;

    @Column(name = "grace_period_ends_at")
    private LocalDateTime gracePeriodEndsAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Subscription() {}

    public Subscription(Long id, Organization organization, String name, String code, BigDecimal price,
                        String currency, Integer maxContacts, Integer maxEmailsPerMonth, Integer maxUsers,
                        Integer maxCampaigns, Integer storageLimitMb, Integer apiLimit, Integer aiLimit,
                        String status, LocalDateTime trialEndsAt, LocalDateTime gracePeriodEndsAt,
                        LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
        this.name = name;
        this.code = code;
        this.price = price;
        this.currency = currency;
        this.maxContacts = maxContacts;
        this.maxEmailsPerMonth = maxEmailsPerMonth;
        this.maxUsers = maxUsers;
        this.maxCampaigns = maxCampaigns;
        this.storageLimitMb = storageLimitMb;
        this.apiLimit = apiLimit;
        this.aiLimit = aiLimit;
        this.status = status;
        this.trialEndsAt = trialEndsAt;
        this.gracePeriodEndsAt = gracePeriodEndsAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Integer getMaxContacts() { return maxContacts; }
    public void setMaxContacts(Integer maxContacts) { this.maxContacts = maxContacts; }
    public Integer getMaxEmailsPerMonth() { return maxEmailsPerMonth; }
    public void setMaxEmailsPerMonth(Integer maxEmailsPerMonth) { this.maxEmailsPerMonth = maxEmailsPerMonth; }
    public Integer getMaxUsers() { return maxUsers; }
    public void setMaxUsers(Integer maxUsers) { this.maxUsers = maxUsers; }
    public Integer getMaxCampaigns() { return maxCampaigns; }
    public void setMaxCampaigns(Integer maxCampaigns) { this.maxCampaigns = maxCampaigns; }
    public Integer getStorageLimitMb() { return storageLimitMb; }
    public void setStorageLimitMb(Integer storageLimitMb) { this.storageLimitMb = storageLimitMb; }
    public Integer getApiLimit() { return apiLimit; }
    public void setApiLimit(Integer apiLimit) { this.apiLimit = apiLimit; }
    public Integer getAiLimit() { return aiLimit; }
    public void setAiLimit(Integer aiLimit) { this.aiLimit = aiLimit; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getTrialEndsAt() { return trialEndsAt; }
    public void setTrialEndsAt(LocalDateTime trialEndsAt) { this.trialEndsAt = trialEndsAt; }
    public LocalDateTime getGracePeriodEndsAt() { return gracePeriodEndsAt; }
    public void setGracePeriodEndsAt(LocalDateTime gracePeriodEndsAt) { this.gracePeriodEndsAt = gracePeriodEndsAt; }
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
        if (this.price == null) this.price = BigDecimal.ZERO;
        if (this.currency == null) this.currency = "USD";
        if (this.code == null) this.code = "FREE";
        if (this.name == null) this.name = "Free Tier";
        if (this.maxContacts == null) this.maxContacts = 1000;
        if (this.maxEmailsPerMonth == null) this.maxEmailsPerMonth = 5000;
        if (this.maxUsers == null) this.maxUsers = 2;
        if (this.maxCampaigns == null) this.maxCampaigns = 10;
        if (this.storageLimitMb == null) this.storageLimitMb = 100;
        if (this.apiLimit == null) this.apiLimit = 1000;
        if (this.aiLimit == null) this.aiLimit = 50;
        if (this.status == null) this.status = "ACTIVE";
        if (this.isDeleted == null) this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static SubscriptionBuilder builder() { return new SubscriptionBuilder(); }

    public static class SubscriptionBuilder {
        private Long id;
        private Organization organization;
        private String name;
        private String code;
        private BigDecimal price;
        private String currency;
        private Integer maxContacts;
        private Integer maxEmailsPerMonth;
        private Integer maxUsers;
        private Integer maxCampaigns;
        private Integer storageLimitMb;
        private Integer apiLimit;
        private Integer aiLimit;
        private String status;
        private LocalDateTime trialEndsAt;
        private LocalDateTime gracePeriodEndsAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean isDeleted;

        SubscriptionBuilder() {}

        public SubscriptionBuilder id(Long id) { this.id = id; return this; }
        public SubscriptionBuilder organization(Organization organization) { this.organization = organization; return this; }
        public SubscriptionBuilder name(String name) { this.name = name; return this; }
        public SubscriptionBuilder code(String code) { this.code = code; return this; }
        public SubscriptionBuilder price(BigDecimal price) { this.price = price; return this; }
        public SubscriptionBuilder currency(String currency) { this.currency = currency; return this; }
        public SubscriptionBuilder maxContacts(Integer maxContacts) { this.maxContacts = maxContacts; return this; }
        public SubscriptionBuilder maxEmailsPerMonth(Integer maxEmailsPerMonth) { this.maxEmailsPerMonth = maxEmailsPerMonth; return this; }
        public SubscriptionBuilder maxUsers(Integer maxUsers) { this.maxUsers = maxUsers; return this; }
        public SubscriptionBuilder maxCampaigns(Integer maxCampaigns) { this.maxCampaigns = maxCampaigns; return this; }
        public SubscriptionBuilder storageLimitMb(Integer storageLimitMb) { this.storageLimitMb = storageLimitMb; return this; }
        public SubscriptionBuilder apiLimit(Integer apiLimit) { this.apiLimit = apiLimit; return this; }
        public SubscriptionBuilder aiLimit(Integer aiLimit) { this.aiLimit = aiLimit; return this; }
        public SubscriptionBuilder status(String status) { this.status = status; return this; }
        public SubscriptionBuilder trialEndsAt(LocalDateTime trialEndsAt) { this.trialEndsAt = trialEndsAt; return this; }
        public SubscriptionBuilder gracePeriodEndsAt(LocalDateTime gracePeriodEndsAt) { this.gracePeriodEndsAt = gracePeriodEndsAt; return this; }
        public SubscriptionBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SubscriptionBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public SubscriptionBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Subscription build() {
            return new Subscription(id, organization, name, code, price, currency, maxContacts, maxEmailsPerMonth,
                    maxUsers, maxCampaigns, storageLimitMb, apiLimit, aiLimit, status, trialEndsAt, gracePeriodEndsAt,
                    createdAt, updatedAt, isDeleted);
        }
    }
}
