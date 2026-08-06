package com.mailally.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO payload for Organization & User Registration.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequestDto {

    @Size(max = 150, message = "Organization name must not exceed 150 characters")
    private String organizationName;

    @Size(max = 100, message = "Organization slug must not exceed 100 characters")
    private String organizationSlug;

    private Long subscriptionId;

    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email address format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    public RegisterRequestDto() {
    }

    public RegisterRequestDto(String organizationName, String organizationSlug, Long subscriptionId,
                              String firstName, String lastName, String email, String password) {
        this.organizationName = organizationName;
        this.organizationSlug = organizationSlug;
        this.subscriptionId = subscriptionId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static RegisterRequestDtoBuilder builder() {
        return new RegisterRequestDtoBuilder();
    }

    public static class RegisterRequestDtoBuilder {
        private String organizationName;
        private String organizationSlug;
        private Long subscriptionId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        RegisterRequestDtoBuilder() {
        }

        public RegisterRequestDtoBuilder organizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public RegisterRequestDtoBuilder organizationSlug(String organizationSlug) {
            this.organizationSlug = organizationSlug;
            return this;
        }

        public RegisterRequestDtoBuilder subscriptionId(Long subscriptionId) {
            this.subscriptionId = subscriptionId;
            return this;
        }

        public RegisterRequestDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public RegisterRequestDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public RegisterRequestDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public RegisterRequestDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RegisterRequestDto build() {
            return new RegisterRequestDto(organizationName, organizationSlug, subscriptionId, firstName, lastName, email, password);
        }
    }
}


