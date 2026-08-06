package com.mailally.settings.dto;

/**
 * Email engine default configuration DTO.
 */
public class EmailSettingsDto {

    private String defaultProvider;
    private String defaultSenderName;
    private String defaultSenderEmail;
    private String defaultReplyTo;
    private int maxRetries;
    private int batchSize;

    public EmailSettingsDto() {}

    public EmailSettingsDto(String defaultProvider, String defaultSenderName, String defaultSenderEmail,
                            String defaultReplyTo, int maxRetries, int batchSize) {
        this.defaultProvider = defaultProvider;
        this.defaultSenderName = defaultSenderName;
        this.defaultSenderEmail = defaultSenderEmail;
        this.defaultReplyTo = defaultReplyTo;
        this.maxRetries = maxRetries;
        this.batchSize = batchSize;
    }

    public String getDefaultProvider() { return defaultProvider; }
    public void setDefaultProvider(String defaultProvider) { this.defaultProvider = defaultProvider; }
    public String getDefaultSenderName() { return defaultSenderName; }
    public void setDefaultSenderName(String defaultSenderName) { this.defaultSenderName = defaultSenderName; }
    public String getDefaultSenderEmail() { return defaultSenderEmail; }
    public void setDefaultSenderEmail(String defaultSenderEmail) { this.defaultSenderEmail = defaultSenderEmail; }
    public String getDefaultReplyTo() { return defaultReplyTo; }
    public void setDefaultReplyTo(String defaultReplyTo) { this.defaultReplyTo = defaultReplyTo; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }
}
