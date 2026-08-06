package com.mailally.email.provider;

/**
 * Result of an email dispatch operation by an {@link EmailProvider}.
 */
public class EmailSendResult {

    private final boolean success;
    private final String responseId;
    private final String errorMessage;
    private final String providerName;
    private final String failureCategory;
    private final String smtpResponseCode;

    public EmailSendResult(boolean success, String responseId, String errorMessage, String providerName) {
        this(success, responseId, errorMessage, providerName, success ? null : "PROVIDER_REJECTED", success ? "250 OK" : "500");
    }

    public EmailSendResult(boolean success, String responseId, String errorMessage, String providerName, String failureCategory, String smtpResponseCode) {
        this.success = success;
        this.responseId = responseId;
        this.errorMessage = errorMessage;
        this.providerName = providerName;
        this.failureCategory = failureCategory;
        this.smtpResponseCode = smtpResponseCode;
    }

    public boolean isSuccess() { return success; }
    public String getResponseId() { return responseId; }
    public String getErrorMessage() { return errorMessage; }
    public String getProviderName() { return providerName; }
    public String getFailureCategory() { return failureCategory; }
    public String getSmtpResponseCode() { return smtpResponseCode; }

    public static EmailSendResult ok(String responseId, String providerName) {
        return new EmailSendResult(true, responseId, null, providerName, null, "250 OK");
    }

    public static EmailSendResult ok(String responseId, String providerName, String smtpCode) {
        return new EmailSendResult(true, responseId, null, providerName, null, smtpCode);
    }

    public static EmailSendResult fail(String errorMessage, String providerName) {
        return new EmailSendResult(false, null, errorMessage, providerName, "PROVIDER_REJECTED", "500");
    }

    public static EmailSendResult fail(String errorMessage, String providerName, String failureCategory, String smtpCode) {
        return new EmailSendResult(false, null, errorMessage, providerName, failureCategory, smtpCode);
    }
}
