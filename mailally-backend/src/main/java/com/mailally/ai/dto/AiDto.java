package com.mailally.ai.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for AI.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class AiDto {

    private Long id;
    private Long organizationId;
    private String prompt;
    private String promptType;
    private String responseContent;
    private String provider;
    private Integer tokensUsed;
    private LocalDateTime createdAt;

    public AiDto() {}

    public AiDto(Long id, Long organizationId, String prompt, String promptType, String responseContent, String provider, Integer tokensUsed, LocalDateTime createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.prompt = prompt;
        this.promptType = promptType;
        this.responseContent = responseContent;
        this.provider = provider;
        this.tokensUsed = tokensUsed;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getPromptType() { return promptType; }
    public void setPromptType(String promptType) { this.promptType = promptType; }
    public String getResponseContent() { return responseContent; }
    public void setResponseContent(String responseContent) { this.responseContent = responseContent; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public Integer getTokensUsed() { return tokensUsed; }
    public void setTokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static AiDtoBuilder builder() { return new AiDtoBuilder(); }

    public static class AiDtoBuilder {
        private Long id;
        private Long organizationId;
        private String prompt;
        private String promptType;
        private String responseContent;
        private String provider;
        private Integer tokensUsed;
        private LocalDateTime createdAt;

        AiDtoBuilder() {}

        public AiDtoBuilder id(Long id) { this.id = id; return this; }
        public AiDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public AiDtoBuilder prompt(String prompt) { this.prompt = prompt; return this; }
        public AiDtoBuilder promptType(String promptType) { this.promptType = promptType; return this; }
        public AiDtoBuilder responseContent(String responseContent) { this.responseContent = responseContent; return this; }
        public AiDtoBuilder provider(String provider) { this.provider = provider; return this; }
        public AiDtoBuilder tokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; return this; }
        public AiDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AiDto build() {
            return new AiDto(id, organizationId, prompt, promptType, responseContent, provider, tokensUsed, createdAt);
        }
    }
}
