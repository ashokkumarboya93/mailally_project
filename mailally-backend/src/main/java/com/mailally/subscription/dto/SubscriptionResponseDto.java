package com.mailally.subscription.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO representing an organization's subscription plan details.
 */
public class SubscriptionResponseDto {

    private Long id;
    private Long organizationId;
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

    public SubscriptionResponseDto() {}

    public SubscriptionResponseDto(Long id, Long organizationId, String name, String code, BigDecimal price,
                                  String currency, Integer maxContacts, Integer maxEmailsPerMonth, Integer maxUsers,
                                  Integer maxCampaigns, Integer storageLimitMb, Integer apiLimit, Integer aiLimit,
                                  String status, LocalDateTime trialEndsAt, LocalDateTime gracePeriodEndsAt) {
        this.id = id;
        this.organizationId = organizationId;
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

    public static SubscriptionResponseDtoBuilder builder() { return new SubscriptionResponseDtoBuilder(); }

    public static class SubscriptionResponseDtoBuilder {
        private Long id;
        private Long organizationId;
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

        SubscriptionResponseDtoBuilder() {}

        public SubscriptionResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public SubscriptionResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public SubscriptionResponseDtoBuilder name(String name) { this.name = name; return this; }
        public SubscriptionResponseDtoBuilder code(String code) { this.code = code; return this; }
        public SubscriptionResponseDtoBuilder price(BigDecimal price) { this.price = price; return this; }
        public SubscriptionResponseDtoBuilder currency(String currency) { this.currency = currency; return this; }
        public SubscriptionResponseDtoBuilder maxContacts(Integer maxContacts) { this.maxContacts = maxContacts; return this; }
        public SubscriptionResponseDtoBuilder maxEmailsPerMonth(Integer maxEmailsPerMonth) { this.maxEmailsPerMonth = maxEmailsPerMonth; return this; }
        public SubscriptionResponseDtoBuilder maxUsers(Integer maxUsers) { this.maxUsers = maxUsers; return this; }
        public SubscriptionResponseDtoBuilder maxCampaigns(Integer maxCampaigns) { this.maxCampaigns = maxCampaigns; return this; }
        public SubscriptionResponseDtoBuilder storageLimitMb(Integer storageLimitMb) { this.storageLimitMb = storageLimitMb; return this; }
        public SubscriptionResponseDtoBuilder apiLimit(Integer apiLimit) { this.apiLimit = apiLimit; return this; }
        public SubscriptionResponseDtoBuilder aiLimit(Integer aiLimit) { this.aiLimit = aiLimit; return this; }
        public SubscriptionResponseDtoBuilder status(String status) { this.status = status; return this; }
        public SubscriptionResponseDtoBuilder trialEndsAt(LocalDateTime trialEndsAt) { this.trialEndsAt = trialEndsAt; return this; }
        public SubscriptionResponseDtoBuilder gracePeriodEndsAt(LocalDateTime gracePeriodEndsAt) { this.gracePeriodEndsAt = gracePeriodEndsAt; return this; }

        public SubscriptionResponseDto build() {
            return new SubscriptionResponseDto(id, organizationId, name, code, price, currency, maxContacts,
                    maxEmailsPerMonth, maxUsers, maxCampaigns, storageLimitMb, apiLimit, aiLimit, status, trialEndsAt, gracePeriodEndsAt);
        }
    }
}
