package com.mailally.auth.dto;

import java.time.LocalDateTime;

/**
 * DTO representing current logged-in user profile response.
 */
public class UserProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private Long organizationId;
    private String organizationName;
    private String organizationSlug;
    private LocalDateTime createdAt;

    public UserProfileDto() {
    }

    public UserProfileDto(Long id, String firstName, String lastName, String email, String role, String status,
                          Long organizationId, String organizationName, String organizationSlug, LocalDateTime createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.status = status;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.organizationSlug = organizationSlug;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationSlug() {
        return organizationSlug;
    }

    public void setOrganizationSlug(String organizationSlug) {
        this.organizationSlug = organizationSlug;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static UserProfileDtoBuilder builder() {
        return new UserProfileDtoBuilder();
    }

    public static class UserProfileDtoBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String role;
        private String status;
        private Long organizationId;
        private String organizationName;
        private String organizationSlug;
        private LocalDateTime createdAt;

        UserProfileDtoBuilder() {
        }

        public UserProfileDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserProfileDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserProfileDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserProfileDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserProfileDtoBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserProfileDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public UserProfileDtoBuilder organizationId(Long organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public UserProfileDtoBuilder organizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public UserProfileDtoBuilder organizationSlug(String organizationSlug) {
            this.organizationSlug = organizationSlug;
            return this;
        }

        public UserProfileDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserProfileDto build() {
            return new UserProfileDto(id, firstName, lastName, email, role, status, organizationId, organizationName, organizationSlug, createdAt);
        }
    }
}
