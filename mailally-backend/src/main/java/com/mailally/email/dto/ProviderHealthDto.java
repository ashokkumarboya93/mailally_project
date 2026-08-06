package com.mailally.email.dto;

/**
 * Health and availability status of an email provider (SMTP, Brevo, SES).
 */
public class ProviderHealthDto {

    private String providerName;
    private boolean available;
    private boolean active;
    private String statusMessage;

    public ProviderHealthDto() {}

    public ProviderHealthDto(String providerName, boolean available, boolean active, String statusMessage) {
        this.providerName = providerName;
        this.available = available;
        this.active = active;
        this.statusMessage = statusMessage;
    }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }

    public static ProviderHealthDtoBuilder builder() { return new ProviderHealthDtoBuilder(); }

    public static class ProviderHealthDtoBuilder {
        private String providerName;
        private boolean available;
        private boolean active;
        private String statusMessage;

        ProviderHealthDtoBuilder() {}

        public ProviderHealthDtoBuilder providerName(String providerName) { this.providerName = providerName; return this; }
        public ProviderHealthDtoBuilder available(boolean available) { this.available = available; return this; }
        public ProviderHealthDtoBuilder active(boolean active) { this.active = active; return this; }
        public ProviderHealthDtoBuilder statusMessage(String statusMessage) { this.statusMessage = statusMessage; return this; }

        public ProviderHealthDto build() {
            return new ProviderHealthDto(providerName, available, active, statusMessage);
        }
    }
}
