package com.mailally.dashboard.service;

import com.mailally.analytics.dto.ProviderAnalyticsDto;
import com.mailally.analytics.dto.TemplateAnalyticsDto;
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
import com.mailally.security.CustomUserDetails;

import java.util.List;

/**
 * Service interface for executive Dashboard widget orchestration, KPIs, live status, and global search.
 */
public interface DashboardService {

    DashboardOverviewDto getDashboardOverview(CustomUserDetails currentUser);

    DashboardKpiDto getDashboardKpis(CustomUserDetails currentUser);

    DashboardChartDto getDashboardCharts(CustomUserDetails currentUser);

    List<DashboardActivityDto> getRecentActivity(CustomUserDetails currentUser);

    DashboardHealthDto getSystemHealth(CustomUserDetails currentUser);

    DashboardLiveStatusDto getLiveStatus(CustomUserDetails currentUser);

    DashboardCampaignWidgetDto getCampaignWidgets(CustomUserDetails currentUser);

    DashboardContactWidgetDto getContactWidgets(CustomUserDetails currentUser);

    List<TemplateAnalyticsDto> getTemplateWidgets(CustomUserDetails currentUser);

    DashboardSchedulerWidgetDto getSchedulerWidgets(CustomUserDetails currentUser);

    DashboardEmailWidgetDto getEmailEngineWidgets(CustomUserDetails currentUser);

    List<ProviderAnalyticsDto> getProviderHealthWidgets(CustomUserDetails currentUser);

    List<DashboardQuickActionDto> getQuickActions(CustomUserDetails currentUser);

    DashboardSearchResultDto globalSearch(CustomUserDetails currentUser, String query);
}
