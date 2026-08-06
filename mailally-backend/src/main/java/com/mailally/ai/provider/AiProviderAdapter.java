package com.mailally.ai.provider;

import com.mailally.ai.dto.AiGenerateRequestDto;
import com.mailally.ai.dto.AiResponseDto;

/**
 * Strategy interface for provider-agnostic AI adapters (MOCK, OPENAI, GEMINI, CLAUDE, GROQ, OLLAMA).
 */
public interface AiProviderAdapter {

    /**
     * Checks if this adapter supports the specified AI provider identifier.
     */
    boolean supportsProvider(String providerName);

    /**
     * Generates AI response for the target prompt type.
     */
    AiResponseDto generate(String promptType, AiGenerateRequestDto request);
}
