package com.mailally.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for AI prompt execution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiGenerateRequestDto {

    @NotBlank(message = "Prompt text is required")
    private String prompt;

    private String tone; // PROFESSIONAL, CASUAL, URGENT, PERSUASIVE, FRIENDLY
    private String audience;
    private String provider; // MOCK, OPENAI, GEMINI, CLAUDE, GROQ, OLLAMA

    public AiGenerateRequestDto() {}

    public AiGenerateRequestDto(String prompt, String tone, String audience, String provider) {
        this.prompt = prompt;
        this.tone = tone;
        this.audience = audience;
        this.provider = provider;
    }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }
    public String getAudience() { return audience; }
    public void setAudience(String audience) { this.audience = audience; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
