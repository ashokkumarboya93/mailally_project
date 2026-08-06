package com.mailally.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO payload for creating a new User within an Organization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequestDto {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    public CreateUserRequestDto() {
    }

    public CreateUserRequestDto(String firstName, String lastName, String email, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static CreateUserRequestDtoBuilder builder() {
        return new CreateUserRequestDtoBuilder();
    }

    public static class CreateUserRequestDtoBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String role;

        CreateUserRequestDtoBuilder() {
        }

        public CreateUserRequestDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CreateUserRequestDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CreateUserRequestDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CreateUserRequestDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CreateUserRequestDtoBuilder role(String role) {
            this.role = role;
            return this;
        }

        public CreateUserRequestDto build() {
            return new CreateUserRequestDto(firstName, lastName, email, password, role);
        }
    }
}
