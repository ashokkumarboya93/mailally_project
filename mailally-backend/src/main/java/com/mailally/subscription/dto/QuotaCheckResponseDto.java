package com.mailally.subscription.dto;

/**
 * Quota check response DTO indicating feature availability and remaining allocation limits.
 */
public class QuotaCheckResponseDto {

    private boolean allowed;
    private String feature;
    private long currentUsage;
    private long limit;
    private long remaining;

    public QuotaCheckResponseDto() {}

    public QuotaCheckResponseDto(boolean allowed, String feature, long currentUsage, long limit, long remaining) {
        this.allowed = allowed;
        this.feature = feature;
        this.currentUsage = currentUsage;
        this.limit = limit;
        this.remaining = remaining;
    }

    public boolean isAllowed() { return allowed; }
    public void setAllowed(boolean allowed) { this.allowed = allowed; }
    public String getFeature() { return feature; }
    public void setFeature(String feature) { this.feature = feature; }
    public long getCurrentUsage() { return currentUsage; }
    public void setCurrentUsage(long currentUsage) { this.currentUsage = currentUsage; }
    public long getLimit() { return limit; }
    public void setLimit(long limit) { this.limit = limit; }
    public long getRemaining() { return remaining; }
    public void setRemaining(long remaining) { this.remaining = remaining; }
}
