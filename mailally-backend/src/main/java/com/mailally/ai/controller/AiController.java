package com.mailally.ai.controller;

import com.mailally.ai.dto.AiGenerateRequestDto;
import com.mailally.ai.dto.AiResponseDto;
import com.mailally.ai.dto.AiUsageSummaryDto;
import com.mailally.ai.service.AiService;
import com.mailally.common.response.ApiResponse;
import com.mailally.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST Controller for MailAlly AI Content Generation & Analysis APIs.
 */
@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/generate-subject")
    public ResponseEntity<ApiResponse<AiResponseDto>> generateSubjectLines(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AiGenerateRequestDto dto) {
        AiResponseDto result = aiService.generateSubjectLines(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<AiResponseDto>builder()
                .success(true).message("Email subject lines generated").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/generate-content")
    public ResponseEntity<ApiResponse<AiResponseDto>> generateEmailContent(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AiGenerateRequestDto dto) {
        AiResponseDto result = aiService.generateEmailContent(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<AiResponseDto>builder()
                .success(true).message("Email template content generated").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/rewrite")
    public ResponseEntity<ApiResponse<AiResponseDto>> rewriteEmail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AiGenerateRequestDto dto) {
        AiResponseDto result = aiService.rewriteEmail(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<AiResponseDto>builder()
                .success(true).message("Email content rewritten").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/grammar-fix")
    public ResponseEntity<ApiResponse<AiResponseDto>> fixGrammar(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AiGenerateRequestDto dto) {
        AiResponseDto result = aiService.fixGrammar(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<AiResponseDto>builder()
                .success(true).message("Grammar and tone polished").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/spam-score")
    public ResponseEntity<ApiResponse<AiResponseDto>> analyzeSpamScore(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AiGenerateRequestDto dto) {
        AiResponseDto result = aiService.analyzeSpamScore(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<AiResponseDto>builder()
                .success(true).message("Spam score analysis completed").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/campaign-ideas")
    public ResponseEntity<ApiResponse<AiResponseDto>> generateCampaignIdeas(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AiGenerateRequestDto dto) {
        AiResponseDto result = aiService.generateCampaignIdeas(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<AiResponseDto>builder()
                .success(true).message("Campaign ideas generated").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/usage")
    public ResponseEntity<ApiResponse<AiUsageSummaryDto>> getAiUsageSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        AiUsageSummaryDto summary = aiService.getAiUsageSummary(userDetails);
        return ResponseEntity.ok(ApiResponse.<AiUsageSummaryDto>builder()
                .success(true).message("AI usage summary retrieved").data(summary).timestamp(LocalDateTime.now()).build());
    }
}
