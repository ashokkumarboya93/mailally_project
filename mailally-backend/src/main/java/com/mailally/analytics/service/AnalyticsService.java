package com.mailally.analytics.service;

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
import com.mailally.email.dto.DeliveryStatsDto;
import com.mailally.security.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Analytics metrics generation, data aggregation, time-series, and exports.
 */
public interface AnalyticsService {

    com.mailally.analytics.dto.AnalyticsV1Dto getAnalyticsV1(CustomUserDetails currentUser, Long campaignId, LocalDateTime dateFrom, LocalDateTime dateTo);

    DashboardOverviewDto getDashboardOverview(CustomUserDetails currentUser);

    List<CampaignAnalyticsDto> getCampaignAnalytics(CustomUserDetails currentUser, LocalDateTime dateFrom, LocalDateTime dateTo);

    CampaignAnalyticsDto getCampaignAnalyticsById(CustomUserDetails currentUser, Long campaignId);

    List<TemplateAnalyticsDto> getTemplateAnalytics(CustomUserDetails currentUser);

    SegmentAnalyticsDto getSegmentAnalytics(CustomUserDetails currentUser);

    List<ProviderAnalyticsDto> getProviderAnalytics(CustomUserDetails currentUser);

    DeliveryStatsDto getEmailEngineAnalytics(CustomUserDetails currentUser, LocalDateTime dateFrom, LocalDateTime dateTo);

    SchedulerAnalyticsDto getSchedulerAnalytics(CustomUserDetails currentUser);

    AudienceAnalyticsDto getOrganizationAnalytics(CustomUserDetails currentUser);

    List<TimeSeriesDataPointDto> getDailyAnalytics(CustomUserDetails currentUser, int days);

    List<TimeSeriesDataPointDto> getWeeklyAnalytics(CustomUserDetails currentUser, int weeks);

    List<TimeSeriesDataPointDto> getMonthlyAnalytics(CustomUserDetails currentUser, int months);

    List<TimeSeriesDataPointDto> getYearlyAnalytics(CustomUserDetails currentUser, int years);

    ChartDataDto getChartData(CustomUserDetails currentUser, String chartType, String metric);

    byte[] exportCsvReport(CustomUserDetails currentUser, String reportType, LocalDateTime dateFrom, LocalDateTime dateTo);

    byte[] exportExcelReport(CustomUserDetails currentUser, String reportType, LocalDateTime dateFrom, LocalDateTime dateTo);

    PdfReportDto exportPdfReport(CustomUserDetails currentUser, String reportType, LocalDateTime dateFrom, LocalDateTime dateTo);
}
