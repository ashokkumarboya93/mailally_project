package com.mailally.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO payload for resetting password using token.
 */
public class ResetPasswordRequestDto {

    @NotBlank(message = "Reset token is required")
    private String token;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;

    public ResetPasswordRequestDto() {
    }

    public ResetPasswordRequestDto(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public static ResetPasswordRequestDtoBuilder builder() {
        return new ResetPasswordRequestDtoBuilder();
    }

    public static class ResetPasswordRequestDtoBuilder {
        private String token;
        private String newPassword;

        ResetPasswordRequestDtoBuilder() {
        }

        public ResetPasswordRequestDtoBuilder token(String token) {
            this.token = token;
            return this;
        }

        public ResetPasswordRequestDtoBuilder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public ResetPasswordRequestDto build() {
            return new ResetPasswordRequestDto(token, newPassword);
        }
    }
}
