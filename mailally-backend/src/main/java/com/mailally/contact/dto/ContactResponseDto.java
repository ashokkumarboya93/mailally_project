package com.mailally.contact.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing Contact details.
 */
public class ContactResponseDto {

    private Long id;
    private Long organizationId;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long importBatchId;
    private String importBatchName;
    private LocalDateTime importDate;
    private String sourceType;
    private String emailDomain;

    public ContactResponseDto() {
    }

    public ContactResponseDto(Long id, Long organizationId, String firstName, String lastName, String email,
                              String phone, String company, String department, String designation, String city,
                              String state, String country, String address, String postalCode, String website,
                              String tags, String notes, String status, Long createdBy, Long updatedBy,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public static ContactResponseDtoBuilder builder() {
        return new ContactResponseDtoBuilder();
    }

    public static class ContactResponseDtoBuilder {
        private Long id;
        private Long organizationId;
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
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        ContactResponseDtoBuilder() {
        }

        public ContactResponseDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ContactResponseDtoBuilder organizationId(Long organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public ContactResponseDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ContactResponseDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ContactResponseDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ContactResponseDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public ContactResponseDtoBuilder company(String company) {
            this.company = company;
            return this;
        }

        public ContactResponseDtoBuilder department(String department) {
            this.department = department;
            return this;
        }

        public ContactResponseDtoBuilder designation(String designation) {
            this.designation = designation;
            return this;
        }

        public ContactResponseDtoBuilder city(String city) {
            this.city = city;
            return this;
        }

        public ContactResponseDtoBuilder state(String state) {
            this.state = state;
            return this;
        }

        public ContactResponseDtoBuilder country(String country) {
            this.country = country;
            return this;
        }

        public ContactResponseDtoBuilder address(String address) {
            this.address = address;
            return this;
        }

        public ContactResponseDtoBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public ContactResponseDtoBuilder website(String website) {
            this.website = website;
            return this;
        }

        public ContactResponseDtoBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }

        public ContactResponseDtoBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public ContactResponseDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ContactResponseDtoBuilder createdBy(Long createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ContactResponseDtoBuilder updatedBy(Long updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public ContactResponseDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ContactResponseDtoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ContactResponseDto build() {
            return new ContactResponseDto(id, organizationId, firstName, lastName, email, phone, company, department,
                    designation, city, state, country, address, postalCode, website, tags, notes, status, createdBy,
                    updatedBy, createdAt, updatedAt);
        }
    }
}
