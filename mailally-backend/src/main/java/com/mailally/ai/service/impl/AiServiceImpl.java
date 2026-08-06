package com.mailally.ai.service.impl;

import com.mailally.ai.dto.AiGenerateRequestDto;
import com.mailally.ai.dto.AiResponseDto;
import com.mailally.ai.dto.AiUsageSummaryDto;
import com.mailally.ai.entity.Ai;
import com.mailally.ai.mapper.AiMapper;
import com.mailally.ai.validator.AiValidator;
import com.mailally.ai.provider.AiProviderAdapter;
import com.mailally.ai.repository.AiRepository;
import com.mailally.ai.service.AiService;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.subscription.service.SubscriptionService;
import com.mailally.user.entity.User;
import com.mailally.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for AI prompt execution, provider routing, logging, and token usage tracking.
 */
@Service
@Transactional
public class AiServiceImpl implements AiService {

    private static final Logger log = LoggerFactory.getLogger(AiServiceImpl.class);

    private final AiRepository aiRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final List<AiProviderAdapter> providerAdapters;
    private final AiValidator aiValidator;
    private final AiMapper aiMapper;

    public AiServiceImpl(AiRepository aiRepository,
                         OrganizationRepository organizationRepository,
                         UserRepository userRepository,
                         SubscriptionService subscriptionService,
                         List<AiProviderAdapter> providerAdapters,
                         AiValidator aiValidator,
                         AiMapper aiMapper) {
        this.aiRepository = aiRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
        this.providerAdapters = providerAdapters;
        this.aiValidator = aiValidator;
        this.aiMapper = aiMapper;
    }

    @Override
    public AiResponseDto generateSubjectLines(CustomUserDetails currentUser, AiGenerateRequestDto dto) {
        return executeAiPrompt(currentUser, "SUBJECT", dto);
    }

    @Override
    public AiResponseDto generateEmailContent(CustomUserDetails currentUser, AiGenerateRequestDto dto) {
        return executeAiPrompt(currentUser, "CONTENT", dto);
    }

    @Override
    public AiResponseDto rewriteEmail(CustomUserDetails currentUser, AiGenerateRequestDto dto) {
        return executeAiPrompt(currentUser, "REWRITE", dto);
    }

    @Override
    public AiResponseDto fixGrammar(CustomUserDetails currentUser, AiGenerateRequestDto dto) {
        return executeAiPrompt(currentUser, "GRAMMAR", dto);
    }

    @Override
    public AiResponseDto analyzeSpamScore(CustomUserDetails currentUser, AiGenerateRequestDto dto) {
        return executeAiPrompt(currentUser, "SPAM_SCORE", dto);
    }

    @Override
    public AiResponseDto generateCampaignIdeas(CustomUserDetails currentUser, AiGenerateRequestDto dto) {
        return executeAiPrompt(currentUser, "CAMPAIGN_IDEA", dto);
    }

    private AiResponseDto executeAiPrompt(CustomUserDetails currentUser, String promptType, AiGenerateRequestDto dto) {
        aiValidator.validateAuthenticatedUser(currentUser);
        aiValidator.validatePromptInput(dto.getPrompt());

        if (!subscriptionService.canUseAI(currentUser)) {
            throw new CustomException("AI generation limit reached for your subscription plan. Please upgrade to continue.");
        }

        Long orgId = currentUser.getOrganizationId();
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new CustomException("Organization not found"));
        User user = userRepository.findById(currentUser.getUserId()).orElse(null);

        String targetProvider = dto.getProvider() != null ? dto.getProvider().toUpperCase() : "MOCK";
        AiProviderAdapter adapter = providerAdapters.stream()
                .filter(a -> a.supportsProvider(targetProvider))
                .findFirst()
                .orElseGet(() -> providerAdapters.get(0));

        AiResponseDto response = adapter.generate(promptType, dto);

        Ai logEntry = Ai.builder()
                .organization(org)
                .user(user)
                .prompt(dto.getPrompt())
                .promptType(promptType)
                .responseContent(response.getGeneratedContent())
                .provider(response.getProvider())
                .tokensUsed(response.getTokensUsed())
                .build();

        aiRepository.save(logEntry);
        log.info("Executed AI Prompt [{}] for User ID {} using provider {}", promptType, currentUser.getUserId(), response.getProvider());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public AiUsageSummaryDto getAiUsageSummary(CustomUserDetails currentUser) {
        aiValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();

        long totalPrompts = aiRepository.countByOrganizationIdAndIsDeletedFalse(orgId);
        long totalTokens = aiRepository.sumTokensUsedByOrganizationId(orgId);

        return new AiUsageSummaryDto(totalPrompts, totalTokens, 1000 - totalPrompts, "MOCK");
    }
}
