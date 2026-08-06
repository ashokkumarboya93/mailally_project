package com.mailally.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO payload for updating an existing User's details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserRequestDto {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Status is required")
    private String status;

    public UpdateUserRequestDto() {
    }

    public UpdateUserRequestDto(String firstName, String lastName, String role, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.status = status;
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

    public static UpdateUserRequestDtoBuilder builder() {
        return new UpdateUserRequestDtoBuilder();
    }

    public static class UpdateUserRequestDtoBuilder {
        private String firstName;
        private String lastName;
        private String role;
        private String status;

        UpdateUserRequestDtoBuilder() {
        }

        public UpdateUserRequestDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UpdateUserRequestDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UpdateUserRequestDtoBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UpdateUserRequestDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public UpdateUserRequestDto build() {
            return new UpdateUserRequestDto(firstName, lastName, role, status);
        }
    }
}
