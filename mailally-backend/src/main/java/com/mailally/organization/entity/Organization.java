package com.mailally.organization.entity;

import com.mailally.subscription.entity.Subscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
 * Entity representing an Organization in the MailAlly platform.
 * Serves as the primary multi-tenant boundary.
 */
@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "domain", length = 255)
    private String domain;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Organization() {
    }

    public Organization(Long id, Subscription subscription, String name, String slug, String domain,
                        String status, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted) {
        this.id = id;
        this.subscription = subscription;
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

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
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

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "ACTIVE";
        }
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static OrganizationBuilder builder() {
        return new OrganizationBuilder();
    }

    public static class OrganizationBuilder {
        private Long id;
        private Subscription subscription;
        private String name;
        private String slug;
        private String domain;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean isDeleted;

        OrganizationBuilder() {
        }

        public OrganizationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrganizationBuilder subscription(Subscription subscription) {
            this.subscription = subscription;
            return this;
        }

        public OrganizationBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OrganizationBuilder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public OrganizationBuilder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public OrganizationBuilder status(String status) {
            this.status = status;
            return this;
        }

        public OrganizationBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrganizationBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public OrganizationBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Organization build() {
            return new Organization(id, subscription, name, slug, domain, status, createdAt, updatedAt, isDeleted);
        }
    }
}
