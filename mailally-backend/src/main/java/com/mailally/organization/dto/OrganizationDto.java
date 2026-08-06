package com.mailally.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Organization data transfer across layers.
 * Provides explicit getters, setters, constructors, and builder pattern to guarantee IDE compilation without Lombok plugin dependencies.
 */
public class OrganizationDto {

    private Long id;

    @NotNull(message = "Subscription ID is required")
    private Long subscriptionId;

    @NotBlank(message = "Organization name is required")
    @Size(max = 150, message = "Organization name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Organization slug is required")
    @Size(max = 100, message = "Organization slug must not exceed 100 characters")
    private String slug;

    @Size(max = 255, message = "Domain must not exceed 255 characters")
    private String domain;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    public OrganizationDto() {
    }

    public OrganizationDto(Long id, Long subscriptionId, String name, String slug, String domain, String status,
                           LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.slug = slug;
        this.domain = domain;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public static OrganizationDtoBuilder builder() {
        return new OrganizationDtoBuilder();
    }

    public static class OrganizationDtoBuilder {
        private Long id;
        private Long subscriptionId;
        private String name;
        private String slug;
        private String domain;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean isDeleted;

        OrganizationDtoBuilder() {
        }

        public OrganizationDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrganizationDtoBuilder subscriptionId(Long subscriptionId) {
            this.subscriptionId = subscriptionId;
            return this;
        }

        public OrganizationDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OrganizationDtoBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public OrganizationDtoBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public OrganizationDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public OrganizationDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrganizationDtoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public OrganizationDtoBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public OrganizationDto build() {
            return new OrganizationDto(id, subscriptionId, name, slug, domain, status, createdAt, updatedAt, isDeleted);
        }
    }
}
