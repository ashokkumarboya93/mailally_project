package com.mailally.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO payload for requesting a password reset.
 */
public class ForgotPasswordRequestDto {

    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email format")
    private String email;

    public ForgotPasswordRequestDto() {
    }

    public ForgotPasswordRequestDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static ForgotPasswordRequestDtoBuilder builder() {
        return new ForgotPasswordRequestDtoBuilder();
    }

    public static class ForgotPasswordRequestDtoBuilder {
        private String email;

        ForgotPasswordRequestDtoBuilder() {
        }

        public ForgotPasswordRequestDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ForgotPasswordRequestDto build() {
            return new ForgotPasswordRequestDto(email);
        }
    }
}
