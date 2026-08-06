package com.mailally.settings.dto;

/**
 * Security policy settings DTO.
 */
public class SecuritySettingsDto {

    private int minimumPasswordLength;
    private boolean requireSpecialCharacters;
    private int sessionTimeoutMinutes;
    private boolean enableTwoFactorAuth;
    private int maxLoginAttempts;

    public SecuritySettingsDto() {}

    public SecuritySettingsDto(int minimumPasswordLength, boolean requireSpecialCharacters,
                               int sessionTimeoutMinutes, boolean enableTwoFactorAuth, int maxLoginAttempts) {
        this.minimumPasswordLength = minimumPasswordLength;
        this.requireSpecialCharacters = requireSpecialCharacters;
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
        this.enableTwoFactorAuth = enableTwoFactorAuth;
        this.maxLoginAttempts = maxLoginAttempts;
    }

    public int getMinimumPasswordLength() { return minimumPasswordLength; }
    public void setMinimumPasswordLength(int minimumPasswordLength) { this.minimumPasswordLength = minimumPasswordLength; }
    public boolean isRequireSpecialCharacters() { return requireSpecialCharacters; }
    public void setRequireSpecialCharacters(boolean requireSpecialCharacters) { this.requireSpecialCharacters = requireSpecialCharacters; }
    public int getSessionTimeoutMinutes() { return sessionTimeoutMinutes; }
    public void setSessionTimeoutMinutes(int sessionTimeoutMinutes) { this.sessionTimeoutMinutes = sessionTimeoutMinutes; }
    public boolean isEnableTwoFactorAuth() { return enableTwoFactorAuth; }
    public void setEnableTwoFactorAuth(boolean enableTwoFactorAuth) { this.enableTwoFactorAuth = enableTwoFactorAuth; }
    public int getMaxLoginAttempts() { return maxLoginAttempts; }
    public void setMaxLoginAttempts(int maxLoginAttempts) { this.maxLoginAttempts = maxLoginAttempts; }
}
