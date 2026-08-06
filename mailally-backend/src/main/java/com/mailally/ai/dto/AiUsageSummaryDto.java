package com.mailally.ai.dto;

/**
 * Summary breakdown of AI token and prompt usage.
 */
public class AiUsageSummaryDto {

    private long totalPromptsGenerated;
    private long totalTokensUsed;
    private long remainingAiLimit;
    private String activeProvider;

    public AiUsageSummaryDto() {}

    public AiUsageSummaryDto(long totalPromptsGenerated, long totalTokensUsed, long remainingAiLimit, String activeProvider) {
        this.totalPromptsGenerated = totalPromptsGenerated;
        this.totalTokensUsed = totalTokensUsed;
        this.remainingAiLimit = remainingAiLimit;
        this.activeProvider = activeProvider;
    }

    public long getTotalPromptsGenerated() { return totalPromptsGenerated; }
    public void setTotalPromptsGenerated(long totalPromptsGenerated) { this.totalPromptsGenerated = totalPromptsGenerated; }
    public long getTotalTokensUsed() { return totalTokensUsed; }
    public void setTotalTokensUsed(long totalTokensUsed) { this.totalTokensUsed = totalTokensUsed; }
    public long getRemainingAiLimit() { return remainingAiLimit; }
    public void setRemainingAiLimit(long remainingAiLimit) { this.remainingAiLimit = remainingAiLimit; }
    public String getActiveProvider() { return activeProvider; }
    public void setActiveProvider(String activeProvider) { this.activeProvider = activeProvider; }
}
