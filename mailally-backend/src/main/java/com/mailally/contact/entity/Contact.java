package com.mailally.contact.entity;

import com.mailally.organization.entity.Organization;
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
 * Entity representing a Contact in the contacts table.
 */
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @jakarta.persistence.ForeignKey(jakarta.persistence.ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "company", length = 150)
    private String company;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "designation", length = 100)
    private String designation;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "import_batch_id")
    private Long importBatchId;

    @Column(name = "import_batch_name", length = 150)
    private String importBatchName;

    @Column(name = "import_date")
    private LocalDateTime importDate;

    @Column(name = "source_type", length = 50)
    private String sourceType;

    @Column(name = "email_domain", length = 150)
    private String emailDomain;

    @Column(name = "custom_fields", columnDefinition = "LONGTEXT")
    private String customFields;

    @Column(name = "collection_id")
    private Long collectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", insertable = false, updatable = false)
    private ContactCollection collection;

    public String getCustomFields() {
        return customFields;
    }

    public void setCustomFields(String customFields) {
        this.customFields = customFields;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public ContactCollection getCollection() {
        return collection;
    }

    public void setCollection(ContactCollection collection) {
        this.collection = collection;
    }

    public Contact() {
    }

    public Contact(Long id, Organization organization, String firstName, String lastName, String email, String phone,
                   String company, String department, String designation, String city, String state, String country,
                   String address, String postalCode, String website, String tags, String notes, String status,
                   Long createdBy, Long updatedBy, Long deletedBy, LocalDateTime createdAt, LocalDateTime updatedAt,
                   LocalDateTime deletedAt, Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.department = department;
        this.designation = designation;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.postalCode = postalCode;
        this.website = website;
        this.tags = tags;
        this.notes = notes;
        this.status = status;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.deletedBy = deletedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getImportBatchId() {
        return importBatchId;
    }

    public void setImportBatchId(Long importBatchId) {
        this.importBatchId = importBatchId;
    }

    public String getImportBatchName() {
        return importBatchName;
    }

    public void setImportBatchName(String importBatchName) {
        this.importBatchName = importBatchName;
    }

    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
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
            this.status = "SUBSCRIBED";
        }
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
        if (this.sourceType == null) {
            this.sourceType = "MANUAL";
        }
        if (this.email != null && this.email.contains("@")) {
            this.emailDomain = this.email.substring(this.email.indexOf("@") + 1).toLowerCase();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.email != null && this.email.contains("@")) {
            this.emailDomain = this.email.substring(this.email.indexOf("@") + 1).toLowerCase();
        }
    }

    public static ContactBuilder builder() {
        return new ContactBuilder();
    }

    public static class ContactBuilder {
        private Long id;
        private Organization organization;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String company;
        private String department;
        private String designation;
        private String city;
        private String state;
        private String country;
        private String address;
        private String postalCode;
        private String website;
        private String tags;
        private String notes;
        private String status;
        private Long createdBy;
        private Long updatedBy;
        private Long deletedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
        private Boolean isDeleted;
        private Long collectionId;

        ContactBuilder() {
        }

        public ContactBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ContactBuilder organization(Organization organization) {
            this.organization = organization;
            return this;
        }

        public ContactBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ContactBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ContactBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ContactBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public ContactBuilder company(String company) {
            this.company = company;
            return this;
        }

        public ContactBuilder department(String department) {
            this.department = department;
            return this;
        }

        public ContactBuilder designation(String designation) {
            this.designation = designation;
            return this;
        }

        public ContactBuilder city(String city) {
            this.city = city;
            return this;
        }

        public ContactBuilder state(String state) {
            this.state = state;
            return this;
        }

        public ContactBuilder country(String country) {
            this.country = country;
            return this;
        }

        public ContactBuilder address(String address) {
            this.address = address;
            return this;
        }

        public ContactBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public ContactBuilder website(String website) {
            this.website = website;
            return this;
        }

        public ContactBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }

        public ContactBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public ContactBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ContactBuilder createdBy(Long createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ContactBuilder updatedBy(Long updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public ContactBuilder deletedBy(Long deletedBy) {
            this.deletedBy = deletedBy;
            return this;
        }

        public ContactBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ContactBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ContactBuilder deletedAt(LocalDateTime deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public ContactBuilder isDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public ContactBuilder collectionId(Long collectionId) {
            this.collectionId = collectionId;
            return this;
        }

        public Contact build() {
            Contact c = new Contact(id, organization, firstName, lastName, email, phone, company, department, designation,
                    city, state, country, address, postalCode, website, tags, notes, status, createdBy, updatedBy,
                    deletedBy, createdAt, updatedAt, deletedAt, isDeleted);
            c.setCollectionId(collectionId);
            return c;
        }
    }
}
