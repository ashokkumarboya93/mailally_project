package com.mailally.dashboard.controller;

import com.mailally.analytics.dto.ProviderAnalyticsDto;
import com.mailally.analytics.dto.TemplateAnalyticsDto;
import com.mailally.common.response.ApiResponse;
import com.mailally.dashboard.dto.DashboardActivityDto;
import com.mailally.dashboard.dto.DashboardCampaignWidgetDto;
import com.mailally.dashboard.dto.DashboardChartDto;
import com.mailally.dashboard.dto.DashboardContactWidgetDto;
import com.mailally.dashboard.dto.DashboardEmailWidgetDto;
import com.mailally.dashboard.dto.DashboardHealthDto;
import com.mailally.dashboard.dto.DashboardKpiDto;
import com.mailally.dashboard.dto.DashboardLiveStatusDto;
import com.mailally.dashboard.dto.DashboardOverviewDto;
import com.mailally.dashboard.dto.DashboardQuickActionDto;
import com.mailally.dashboard.dto.DashboardSchedulerWidgetDto;
import com.mailally.dashboard.dto.DashboardSearchResultDto;
import com.mailally.dashboard.service.DashboardService;
import com.mailally.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for MailAlly Executive Dashboard APIs.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<DashboardOverviewDto>> getDashboardOverview(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardOverviewDto result = dashboardService.getDashboardOverview(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardOverviewDto>builder()
                .success(true).message("Dashboard overview retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/kpis")
    public ResponseEntity<ApiResponse<DashboardKpiDto>> getDashboardKpis(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardKpiDto result = dashboardService.getDashboardKpis(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardKpiDto>builder()
                .success(true).message("Dashboard KPIs retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/charts")
    public ResponseEntity<ApiResponse<DashboardChartDto>> getDashboardCharts(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardChartDto result = dashboardService.getDashboardCharts(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardChartDto>builder()
                .success(true).message("Dashboard chart payloads retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<ApiResponse<List<DashboardActivityDto>>> getRecentActivity(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<DashboardActivityDto> result = dashboardService.getRecentActivity(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<DashboardActivityDto>>builder()
                .success(true).message("Recent activity feed retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/system-health")
    public ResponseEntity<ApiResponse<DashboardHealthDto>> getSystemHealth(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardHealthDto result = dashboardService.getSystemHealth(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardHealthDto>builder()
                .success(true).message("System health status retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/live-status")
    public ResponseEntity<ApiResponse<DashboardLiveStatusDto>> getLiveStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardLiveStatusDto result = dashboardService.getLiveStatus(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardLiveStatusDto>builder()
                .success(true).message("Live execution status retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/campaigns")
    public ResponseEntity<ApiResponse<DashboardCampaignWidgetDto>> getCampaignWidgets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardCampaignWidgetDto result = dashboardService.getCampaignWidgets(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardCampaignWidgetDto>builder()
                .success(true).message("Campaign widgets retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/contacts")
    public ResponseEntity<ApiResponse<DashboardContactWidgetDto>> getContactWidgets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardContactWidgetDto result = dashboardService.getContactWidgets(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardContactWidgetDto>builder()
                .success(true).message("Contact widgets retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<List<TemplateAnalyticsDto>>> getTemplateWidgets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TemplateAnalyticsDto> result = dashboardService.getTemplateWidgets(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<TemplateAnalyticsDto>>builder()
                .success(true).message("Template widgets retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/scheduler")
    public ResponseEntity<ApiResponse<DashboardSchedulerWidgetDto>> getSchedulerWidgets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardSchedulerWidgetDto result = dashboardService.getSchedulerWidgets(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardSchedulerWidgetDto>builder()
                .success(true).message("Scheduler widgets retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/email-engine")
    public ResponseEntity<ApiResponse<DashboardEmailWidgetDto>> getEmailEngineWidgets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardEmailWidgetDto result = dashboardService.getEmailEngineWidgets(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardEmailWidgetDto>builder()
                .success(true).message("Email engine widgets retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/provider-health")
    public ResponseEntity<ApiResponse<List<ProviderAnalyticsDto>>> getProviderHealthWidgets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ProviderAnalyticsDto> result = dashboardService.getProviderHealthWidgets(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<ProviderAnalyticsDto>>builder()
                .success(true).message("Provider health widgets retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/quick-actions")
    public ResponseEntity<ApiResponse<List<DashboardQuickActionDto>>> getQuickActions(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<DashboardQuickActionDto> result = dashboardService.getQuickActions(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<DashboardQuickActionDto>>builder()
                .success(true).message("Quick actions metadata retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<DashboardSearchResultDto>> globalSearch(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("q") String query) {
        DashboardSearchResultDto result = dashboardService.globalSearch(userDetails, query);
        return ResponseEntity.ok(ApiResponse.<DashboardSearchResultDto>builder()
                .success(true).message("Global dashboard search completed").data(result).timestamp(LocalDateTime.now()).build());
    }
}
