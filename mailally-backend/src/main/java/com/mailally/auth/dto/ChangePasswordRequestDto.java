package com.mailally.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO payload for changing password.
 */
public class ChangePasswordRequestDto {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;

    public ChangePasswordRequestDto() {
    }

    public ChangePasswordRequestDto(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public static ChangePasswordRequestDtoBuilder builder() {
        return new ChangePasswordRequestDtoBuilder();
    }

    public static class ChangePasswordRequestDtoBuilder {
        private String currentPassword;
        private String newPassword;

        ChangePasswordRequestDtoBuilder() {
        }

        public ChangePasswordRequestDtoBuilder currentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
            return this;
        }

        public ChangePasswordRequestDtoBuilder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public ChangePasswordRequestDto build() {
            return new ChangePasswordRequestDto(currentPassword, newPassword);
        }
    }
}
