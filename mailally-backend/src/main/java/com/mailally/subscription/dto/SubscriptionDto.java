package com.mailally.subscription.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Subscription.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class SubscriptionDto {

    private Long id;
    private Long organizationId;
    private String name;
    private String code;
    private BigDecimal price;
    private String currency;
    private Integer maxContacts;
    private Integer maxEmailsPerMonth;
    private Integer maxUsers;
    private String status;

    public SubscriptionDto() {}

    public SubscriptionDto(Long id, Long organizationId, String name, String code, BigDecimal price, String currency, Integer maxContacts, Integer maxEmailsPerMonth, Integer maxUsers, String status) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
        this.code = code;
        this.price = price;
        this.currency = currency;
        this.maxContacts = maxContacts;
        this.maxEmailsPerMonth = maxEmailsPerMonth;
        this.maxUsers = maxUsers;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static SubscriptionDtoBuilder builder() { return new SubscriptionDtoBuilder(); }

    public static class SubscriptionDtoBuilder {
        private Long id;
        private Long organizationId;
        private String name;
        private String code;
        private BigDecimal price;
        private String currency;
        private Integer maxContacts;
        private Integer maxEmailsPerMonth;
        private Integer maxUsers;
        private String status;

        SubscriptionDtoBuilder() {}

        public SubscriptionDtoBuilder id(Long id) { this.id = id; return this; }
        public SubscriptionDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public SubscriptionDtoBuilder name(String name) { this.name = name; return this; }
        public SubscriptionDtoBuilder code(String code) { this.code = code; return this; }
        public SubscriptionDtoBuilder price(BigDecimal price) { this.price = price; return this; }
        public SubscriptionDtoBuilder currency(String currency) { this.currency = currency; return this; }
        public SubscriptionDtoBuilder maxContacts(Integer maxContacts) { this.maxContacts = maxContacts; return this; }
        public SubscriptionDtoBuilder maxEmailsPerMonth(Integer maxEmailsPerMonth) { this.maxEmailsPerMonth = maxEmailsPerMonth; return this; }
        public SubscriptionDtoBuilder maxUsers(Integer maxUsers) { this.maxUsers = maxUsers; return this; }
        public SubscriptionDtoBuilder status(String status) { this.status = status; return this; }

        public SubscriptionDto build() {
            return new SubscriptionDto(id, organizationId, name, code, price, currency, maxContacts, maxEmailsPerMonth, maxUsers, status);
        }
    }
}
