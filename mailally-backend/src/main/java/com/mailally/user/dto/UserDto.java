package com.mailally.user.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for User.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class UserDto {

    private Long id;
    private Long organizationId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDto() {}

    public UserDto(Long id, Long organizationId, String email, String firstName, String lastName, String role, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
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
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static UserDtoBuilder builder() { return new UserDtoBuilder(); }

    public static class UserDtoBuilder {
        private Long id;
        private Long organizationId;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        UserDtoBuilder() {}

        public UserDtoBuilder id(Long id) { this.id = id; return this; }
        public UserDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public UserDtoBuilder email(String email) { this.email = email; return this; }
        public UserDtoBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public UserDtoBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public UserDtoBuilder role(String role) { this.role = role; return this; }
        public UserDtoBuilder status(String status) { this.status = status; return this; }
        public UserDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public UserDto build() {
            return new UserDto(id, organizationId, email, firstName, lastName, role, status, createdAt, updatedAt);
        }
    }
}
