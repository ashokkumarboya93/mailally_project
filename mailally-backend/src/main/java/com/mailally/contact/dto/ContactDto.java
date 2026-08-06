package com.mailally.contact.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Contact.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class ContactDto {

    private Long id;
    private Long organizationId;
    private String email;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ContactDto() {}

    public ContactDto(Long id, Long organizationId, String email, String firstName, String lastName, String city, String country, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static ContactDtoBuilder builder() { return new ContactDtoBuilder(); }

    public static class ContactDtoBuilder {
        private Long id;
        private Long organizationId;
        private String email;
        private String firstName;
        private String lastName;
        private String city;
        private String country;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        ContactDtoBuilder() {}

        public ContactDtoBuilder id(Long id) { this.id = id; return this; }
        public ContactDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public ContactDtoBuilder email(String email) { this.email = email; return this; }
        public ContactDtoBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public ContactDtoBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public ContactDtoBuilder city(String city) { this.city = city; return this; }
        public ContactDtoBuilder country(String country) { this.country = country; return this; }
        public ContactDtoBuilder status(String status) { this.status = status; return this; }
        public ContactDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ContactDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ContactDto build() {
            return new ContactDto(id, organizationId, email, firstName, lastName, city, country, status, createdAt, updatedAt);
        }
    }
}
