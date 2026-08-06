package com.mailally.user.dto;

import java.time.LocalDateTime;

/**
 * Response DTO returning user details.
 */
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private Long organizationId;
    private String organizationName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String firstName, String lastName, String email, String role,
                           String status, Long organizationId, String organizationName,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.status = status;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public static UserResponseDtoBuilder builder() {
        return new UserResponseDtoBuilder();
    }

    public static class UserResponseDtoBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String role;
        private String status;
        private Long organizationId;
        private String organizationName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        UserResponseDtoBuilder() {
        }

        public UserResponseDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserResponseDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserResponseDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserResponseDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserResponseDtoBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserResponseDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public UserResponseDtoBuilder organizationId(Long organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public UserResponseDtoBuilder organizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public UserResponseDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserResponseDtoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserResponseDto build() {
            return new UserResponseDto(id, firstName, lastName, email, role, status, organizationId, organizationName, createdAt, updatedAt);
        }
    }
}
