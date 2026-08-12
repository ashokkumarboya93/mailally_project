package com.mailally.email.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Result model holding status and per-recipient message IDs for provider batch send API calls.
 */
public class BatchSendResult {

    private final boolean success;
    private final String providerBatchId;
    private final Map<Long, String> recipientMessageIds;
    private final Map<Long, String> recipientFailures;
    private final String errorMessage;
    private final String providerName;
    private final String smtpResponseCode;
    private int retryAfterSeconds;

    public BatchSendResult(boolean success, String providerBatchId, Map<Long, String> recipientMessageIds,
                           Map<Long, String> recipientFailures, String errorMessage, String providerName, String smtpResponseCode) {
        this.success = success;
        this.providerBatchId = providerBatchId;
        this.recipientMessageIds = recipientMessageIds != null ? recipientMessageIds : new HashMap<>();
        this.recipientFailures = recipientFailures != null ? recipientFailures : new HashMap<>();
        this.errorMessage = errorMessage;
        this.providerName = providerName;
        this.smtpResponseCode = smtpResponseCode;
        this.retryAfterSeconds = 0;
    }

    public boolean isSuccess() { return success; }
    public String getProviderBatchId() { return providerBatchId; }
    public Map<Long, String> getRecipientMessageIds() { return recipientMessageIds; }
    public Map<Long, String> getRecipientFailures() { return recipientFailures; }
    public String getErrorMessage() { return errorMessage; }
    public String getProviderName() { return providerName; }
    public String getSmtpResponseCode() { return smtpResponseCode; }
    public int getRetryAfterSeconds() { return retryAfterSeconds; }
    public void setRetryAfterSeconds(int retryAfterSeconds) { this.retryAfterSeconds = retryAfterSeconds; }

    public static BatchSendResult ok(String providerBatchId, Map<Long, String> messageIds, String providerName) {
        return new BatchSendResult(true, providerBatchId, messageIds, null, null, providerName, "250 OK");
    }

    public static BatchSendResult fail(String errorMessage, String providerName, String smtpResponseCode) {
        return new BatchSendResult(false, null, null, null, errorMessage, providerName, smtpResponseCode);
    }
}
