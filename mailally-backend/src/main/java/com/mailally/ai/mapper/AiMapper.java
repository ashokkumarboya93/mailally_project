package com.mailally.ai.mapper;

import com.mailally.ai.dto.AiResponseDto;
import com.mailally.ai.entity.Ai;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Ai entity and AiResponseDto.
 */
@Component
public class AiMapper {

    public AiResponseDto toAiResponseDto(Ai ai) {
        if (ai == null) return null;
        return AiResponseDto.builder()
                .promptType(ai.getPromptType())
                .generatedContent(ai.getResponseContent())
                .provider(ai.getProvider())
                .tokensUsed(ai.getTokensUsed() != null ? ai.getTokensUsed() : 0)
                .generatedAt(ai.getCreatedAt())
                .build();
    }
}
