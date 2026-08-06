package com.mailally.analytics.controller;

import com.mailally.analytics.dto.AudienceAnalyticsDto;
import com.mailally.analytics.dto.CampaignAnalyticsDto;
import com.mailally.analytics.dto.ChartDataDto;
import com.mailally.analytics.dto.DashboardOverviewDto;
import com.mailally.analytics.dto.PdfReportDto;
import com.mailally.analytics.dto.ProviderAnalyticsDto;
import com.mailally.analytics.dto.SchedulerAnalyticsDto;
import com.mailally.analytics.dto.SegmentAnalyticsDto;
import com.mailally.analytics.dto.TemplateAnalyticsDto;
import com.mailally.analytics.dto.TimeSeriesDataPointDto;
import com.mailally.analytics.service.AnalyticsService;
import com.mailally.common.response.ApiResponse;
import com.mailally.email.dto.DeliveryStatsDto;
import com.mailally.security.CustomUserDetails;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for MailAlly Analytics, Reports, Chart Data, and File Exports.
 */
@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardOverviewDto>> getDashboardOverview(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DashboardOverviewDto dto = analyticsService.getDashboardOverview(userDetails);
        return ResponseEntity.ok(ApiResponse.<DashboardOverviewDto>builder()
                .success(true).message("Dashboard overview retrieved").data(dto).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/campaigns")
    public ResponseEntity<ApiResponse<List<CampaignAnalyticsDto>>> getCampaignAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        List<CampaignAnalyticsDto> list = analyticsService.getCampaignAnalytics(userDetails, dateFrom, dateTo);
        return ResponseEntity.ok(ApiResponse.<List<CampaignAnalyticsDto>>builder()
                .success(true).message("Campaign analytics retrieved").data(list).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/campaign/{id}")
    public ResponseEntity<ApiResponse<CampaignAnalyticsDto>> getCampaignAnalyticsById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        CampaignAnalyticsDto dto = analyticsService.getCampaignAnalyticsById(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<CampaignAnalyticsDto>builder()
                .success(true).message("Campaign details analytics retrieved").data(dto).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<List<TemplateAnalyticsDto>>> getTemplateAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TemplateAnalyticsDto> list = analyticsService.getTemplateAnalytics(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<TemplateAnalyticsDto>>builder()
                .success(true).message("Template analytics retrieved").data(list).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/segments")
    public ResponseEntity<ApiResponse<SegmentAnalyticsDto>> getSegmentAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        SegmentAnalyticsDto dto = analyticsService.getSegmentAnalytics(userDetails);
        return ResponseEntity.ok(ApiResponse.<SegmentAnalyticsDto>builder()
                .success(true).message("Segment analytics retrieved").data(dto).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/providers")
    public ResponseEntity<ApiResponse<List<ProviderAnalyticsDto>>> getProviderAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ProviderAnalyticsDto> list = analyticsService.getProviderAnalytics(userDetails);
        return ResponseEntity.ok(ApiResponse.<List<ProviderAnalyticsDto>>builder()
                .success(true).message("Provider analytics retrieved").data(list).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/emails")
    public ResponseEntity<ApiResponse<DeliveryStatsDto>> getEmailEngineAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        DeliveryStatsDto dto = analyticsService.getEmailEngineAnalytics(userDetails, dateFrom, dateTo);
        return ResponseEntity.ok(ApiResponse.<DeliveryStatsDto>builder()
                .success(true).message("Email engine delivery stats retrieved").data(dto).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/scheduler")
    public ResponseEntity<ApiResponse<SchedulerAnalyticsDto>> getSchedulerAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        SchedulerAnalyticsDto dto = analyticsService.getSchedulerAnalytics(userDetails);
        return ResponseEntity.ok(ApiResponse.<SchedulerAnalyticsDto>builder()
                .success(true).message("Scheduler analytics retrieved").data(dto).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/organizations")
    public ResponseEntity<ApiResponse<AudienceAnalyticsDto>> getOrganizationAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        AudienceAnalyticsDto dto = analyticsService.getOrganizationAnalytics(userDetails);
        return ResponseEntity.ok(ApiResponse.<AudienceAnalyticsDto>builder()
                .success(true).message("Organization audience analytics retrieved").data(dto).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/daily")
    public ResponseEntity<ApiResponse<List<TimeSeriesDataPointDto>>> getDailyAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "7") int days) {
        List<TimeSeriesDataPointDto> list = analyticsService.getDailyAnalytics(userDetails, days);
        return ResponseEntity.ok(ApiResponse.<List<TimeSeriesDataPointDto>>builder()
                .success(true).message("Daily analytics time series retrieved").data(list).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/weekly")
    public ResponseEntity<ApiResponse<List<TimeSeriesDataPointDto>>> getWeeklyAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "4") int weeks) {
        List<TimeSeriesDataPointDto> list = analyticsService.getWeeklyAnalytics(userDetails, weeks);
        return ResponseEntity.ok(ApiResponse.<List<TimeSeriesDataPointDto>>builder()
                .success(true).message("Weekly analytics time series retrieved").data(list).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<List<TimeSeriesDataPointDto>>> getMonthlyAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "12") int months) {
        List<TimeSeriesDataPointDto> list = analyticsService.getMonthlyAnalytics(userDetails, months);
        return ResponseEntity.ok(ApiResponse.<List<TimeSeriesDataPointDto>>builder()
                .success(true).message("Monthly analytics time series retrieved").data(list).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/yearly")
    public ResponseEntity<ApiResponse<List<TimeSeriesDataPointDto>>> getYearlyAnalytics(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "5") int years) {
        List<TimeSeriesDataPointDto> list = analyticsService.getYearlyAnalytics(userDetails, years);
        return ResponseEntity.ok(ApiResponse.<List<TimeSeriesDataPointDto>>builder()
                .success(true).message("Yearly analytics time series retrieved").data(list).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/charts")
    public ResponseEntity<ApiResponse<ChartDataDto>> getChartData(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "BAR") String chartType,
            @RequestParam(defaultValue = "EMAILS_SENT") String metric) {
        ChartDataDto chartData = analyticsService.getChartData(userDetails, chartType, metric);
        return ResponseEntity.ok(ApiResponse.<ChartDataDto>builder()
                .success(true).message("Chart data payload generated").data(chartData).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsvReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "EXECUTIVE_SUMMARY") String reportType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        byte[] csvBytes = analyticsService.exportCsvReport(userDetails, reportType, dateFrom, dateTo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=MailAlly_Analytics_Report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcelReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "EXECUTIVE_SUMMARY") String reportType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        byte[] excelBytes = analyticsService.exportExcelReport(userDetails, reportType, dateFrom, dateTo);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=MailAlly_Analytics_Report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<ApiResponse<PdfReportDto>> exportPdfReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "EXECUTIVE_SUMMARY") String reportType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        PdfReportDto pdfDto = analyticsService.exportPdfReport(userDetails, reportType, dateFrom, dateTo);
        return ResponseEntity.ok(ApiResponse.<PdfReportDto>builder()
                .success(true).message("PDF-ready report DTO generated").data(pdfDto).timestamp(LocalDateTime.now()).build());
    }
}
