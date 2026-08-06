package com.mailally.ai.dto;

import java.time.LocalDateTime;

/**
 * Response DTO returning generated content, token usage, and metadata.
 */
public class AiResponseDto {

    private String promptType;
    private String generatedContent;
    private String provider;
    private int tokensUsed;
    private LocalDateTime generatedAt;

    public AiResponseDto() {}

    public AiResponseDto(String promptType, String generatedContent, String provider, int tokensUsed, LocalDateTime generatedAt) {
        this.promptType = promptType;
        this.generatedContent = generatedContent;
        this.provider = provider;
        this.tokensUsed = tokensUsed;
        this.generatedAt = generatedAt;
    }

    public String getPromptType() { return promptType; }
    public void setPromptType(String promptType) { this.promptType = promptType; }
    public String getGeneratedContent() { return generatedContent; }
    public void setGeneratedContent(String generatedContent) { this.generatedContent = generatedContent; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public int getTokensUsed() { return tokensUsed; }
    public void setTokensUsed(int tokensUsed) { this.tokensUsed = tokensUsed; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public static AiResponseDtoBuilder builder() { return new AiResponseDtoBuilder(); }

    public static class AiResponseDtoBuilder {
        private String promptType;
        private String generatedContent;
        private String provider;
        private int tokensUsed;
        private LocalDateTime generatedAt;

        AiResponseDtoBuilder() {}

        public AiResponseDtoBuilder promptType(String promptType) { this.promptType = promptType; return this; }
        public AiResponseDtoBuilder generatedContent(String generatedContent) { this.generatedContent = generatedContent; return this; }
        public AiResponseDtoBuilder provider(String provider) { this.provider = provider; return this; }
        public AiResponseDtoBuilder tokensUsed(int tokensUsed) { this.tokensUsed = tokensUsed; return this; }
        public AiResponseDtoBuilder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }

        public AiResponseDto build() {
            return new AiResponseDto(promptType, generatedContent, provider, tokensUsed, generatedAt);
        }
    }
}
