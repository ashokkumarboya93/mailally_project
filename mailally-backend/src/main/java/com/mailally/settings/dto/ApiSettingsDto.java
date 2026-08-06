package com.mailally.settings.dto;

/**
 * API & Webhook settings DTO.
 */
public class ApiSettingsDto {

    private boolean apiEnabled;
    private boolean webhookEnabled;
    private String webhookUrl;
    private String webhookSecretMasked;

    public ApiSettingsDto() {}

    public ApiSettingsDto(boolean apiEnabled, boolean webhookEnabled, String webhookUrl, String webhookSecretMasked) {
        this.apiEnabled = apiEnabled;
        this.webhookEnabled = webhookEnabled;
        this.webhookUrl = webhookUrl;
        this.webhookSecretMasked = webhookSecretMasked;
    }

    public boolean isApiEnabled() { return apiEnabled; }
    public void setApiEnabled(boolean apiEnabled) { this.apiEnabled = apiEnabled; }
    public boolean isWebhookEnabled() { return webhookEnabled; }
    public void setWebhookEnabled(boolean webhookEnabled) { this.webhookEnabled = webhookEnabled; }
    public String getWebhookUrl() { return webhookUrl; }
    public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }
    public String getWebhookSecretMasked() { return webhookSecretMasked; }
    public void setWebhookSecretMasked(String webhookSecretMasked) { this.webhookSecretMasked = webhookSecretMasked; }
}
