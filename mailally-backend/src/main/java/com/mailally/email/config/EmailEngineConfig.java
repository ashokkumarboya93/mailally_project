package com.mailally.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the MailAlly Email Engine.
 * Binds to {@code mailally.email.*} properties in application.properties.
 */
@Configuration
@ConfigurationProperties(prefix = "mailally.email")
public class EmailEngineConfig {

    private String activeProvider;
    private String defaultSenderName;
    private String defaultSenderEmail;
    private int maxRetries;
    private BrevoConfig brevo = new BrevoConfig();
    private SesConfig ses = new SesConfig();

    public EmailEngineConfig() {}

    public String getActiveProvider() { return activeProvider; }
    public void setActiveProvider(String activeProvider) { this.activeProvider = activeProvider; }
    public String getDefaultSenderName() { return defaultSenderName; }
    public void setDefaultSenderName(String defaultSenderName) { this.defaultSenderName = defaultSenderName; }
    public String getDefaultSenderEmail() { return defaultSenderEmail; }
    public void setDefaultSenderEmail(String defaultSenderEmail) { this.defaultSenderEmail = defaultSenderEmail; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public BrevoConfig getBrevo() { return brevo; }
    public void setBrevo(BrevoConfig brevo) { this.brevo = brevo; }
    public SesConfig getSes() { return ses; }
    public void setSes(SesConfig ses) { this.ses = ses; }

    /**
     * Brevo (Sendinblue) provider configuration.
     */
    public static class BrevoConfig {
        private String apiKey;
        private String apiUrl;

        public BrevoConfig() {}

        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getApiUrl() { return apiUrl; }
        public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }

        public boolean isConfigured() {
            return apiKey != null && !apiKey.isBlank() && !"your-brevo-api-key".equals(apiKey);
        }
    }

    /**
     * Amazon SES provider configuration.
     */
    public static class SesConfig {
        private String accessKey;
        private String secretKey;
        private String region;

        public SesConfig() {}

        public String getAccessKey() { return accessKey; }
        public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
        public String getSecretKey() { return secretKey; }
        public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }

        public boolean isConfigured() {
            return accessKey != null && !accessKey.isBlank() && !"your-ses-access-key".equals(accessKey);
        }
    }
}
