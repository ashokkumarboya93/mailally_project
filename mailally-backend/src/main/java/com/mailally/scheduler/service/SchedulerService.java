package com.mailally.scheduler.service;

import com.mailally.scheduler.dto.LaunchNowRequestDto;
import com.mailally.scheduler.dto.RescheduleCampaignRequestDto;
import com.mailally.scheduler.dto.ScheduleCampaignRequestDto;
import com.mailally.scheduler.dto.SchedulerResponseDto;
import com.mailally.scheduler.dto.SchedulerStatsDto;
import com.mailally.security.CustomUserDetails;
import org.springframework.data.domain.Page;

/**
 * Service interface for Scheduler orchestration and campaign execution coordination.
 */
public interface SchedulerService {

    SchedulerResponseDto scheduleCampaign(CustomUserDetails currentUser, ScheduleCampaignRequestDto dto);

    SchedulerResponseDto launchImmediately(CustomUserDetails currentUser, LaunchNowRequestDto dto);

    SchedulerResponseDto rescheduleCampaign(CustomUserDetails currentUser, Long id, RescheduleCampaignRequestDto dto);

    SchedulerResponseDto pauseScheduler(CustomUserDetails currentUser, Long id);

    SchedulerResponseDto resumeScheduler(CustomUserDetails currentUser, Long id);

    SchedulerResponseDto cancelScheduler(CustomUserDetails currentUser, Long id);

    SchedulerResponseDto getSchedulerById(CustomUserDetails currentUser, Long id);

    Page<SchedulerResponseDto> listAllSchedules(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir);

    Page<SchedulerResponseDto> getUpcomingSchedules(CustomUserDetails currentUser, int page, int size);

    Page<SchedulerResponseDto> getExecutionHistory(CustomUserDetails currentUser, int page, int size);

    SchedulerStatsDto getSchedulerStatistics(CustomUserDetails currentUser);

    void processDueSchedules();
}
