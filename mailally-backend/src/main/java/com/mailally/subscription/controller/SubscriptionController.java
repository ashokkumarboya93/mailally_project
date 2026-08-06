package com.mailally.subscription.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.security.CustomUserDetails;
import com.mailally.subscription.dto.PlanUpgradeRequestDto;
import com.mailally.subscription.dto.QuotaCheckResponseDto;
import com.mailally.subscription.dto.SubscriptionResponseDto;
import com.mailally.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST Controller for MailAlly Subscription and Quota Management APIs.
 */
@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<SubscriptionResponseDto>> getOrganizationSubscription(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        SubscriptionResponseDto result = subscriptionService.getOrganizationSubscription(userDetails);
        return ResponseEntity.ok(ApiResponse.<SubscriptionResponseDto>builder()
                .success(true).message("Subscription plan details retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/upgrade")
    public ResponseEntity<ApiResponse<SubscriptionResponseDto>> upgradePlan(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PlanUpgradeRequestDto dto) {
        SubscriptionResponseDto result = subscriptionService.upgradePlan(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<SubscriptionResponseDto>builder()
                .success(true).message("Subscription plan upgraded successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PostMapping("/downgrade")
    public ResponseEntity<ApiResponse<SubscriptionResponseDto>> downgradePlan(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PlanUpgradeRequestDto dto) {
        SubscriptionResponseDto result = subscriptionService.downgradePlan(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<SubscriptionResponseDto>builder()
                .success(true).message("Subscription plan downgraded successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/quota-check")
    public ResponseEntity<ApiResponse<QuotaCheckResponseDto>> checkQuota(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "EMAILS") String feature) {
        QuotaCheckResponseDto result = subscriptionService.checkQuota(userDetails, feature);
        return ResponseEntity.ok(ApiResponse.<QuotaCheckResponseDto>builder()
                .success(true).message("Quota check completed").data(result).timestamp(LocalDateTime.now()).build());
    }
}
