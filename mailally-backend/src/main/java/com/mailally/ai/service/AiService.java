package com.mailally.ai.service;

import com.mailally.ai.dto.AiGenerateRequestDto;
import com.mailally.ai.dto.AiResponseDto;
import com.mailally.ai.dto.AiUsageSummaryDto;
import com.mailally.security.CustomUserDetails;

/**
 * Service interface for AI prompt execution, content generation, spam analysis, and usage tracking.
 */
public interface AiService {

    AiResponseDto generateSubjectLines(CustomUserDetails currentUser, AiGenerateRequestDto dto);

    AiResponseDto generateEmailContent(CustomUserDetails currentUser, AiGenerateRequestDto dto);

    AiResponseDto rewriteEmail(CustomUserDetails currentUser, AiGenerateRequestDto dto);

    AiResponseDto fixGrammar(CustomUserDetails currentUser, AiGenerateRequestDto dto);

    AiResponseDto analyzeSpamScore(CustomUserDetails currentUser, AiGenerateRequestDto dto);

    AiResponseDto generateCampaignIdeas(CustomUserDetails currentUser, AiGenerateRequestDto dto);

    AiUsageSummaryDto getAiUsageSummary(CustomUserDetails currentUser);
}
