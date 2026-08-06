package com.mailally.scheduler.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.scheduler.dto.LaunchNowRequestDto;
import com.mailally.scheduler.dto.RescheduleCampaignRequestDto;
import com.mailally.scheduler.dto.ScheduleCampaignRequestDto;
import com.mailally.scheduler.dto.SchedulerResponseDto;
import com.mailally.scheduler.dto.SchedulerStatsDto;
import com.mailally.scheduler.service.SchedulerService;
import com.mailally.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST Controller for Scheduler Management APIs.
 */
@RestController
@RequestMapping("/api/v1/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<ApiResponse<SchedulerResponseDto>> scheduleCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ScheduleCampaignRequestDto dto) {
        SchedulerResponseDto result = schedulerService.scheduleCampaign(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<SchedulerResponseDto>builder()
                .success(true).message("Campaign scheduled successfully").data(result).timestamp(LocalDateTime.now()).build(),
                HttpStatus.CREATED);
    }

    @PostMapping("/launch-now")
    public ResponseEntity<ApiResponse<SchedulerResponseDto>> launchImmediately(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody LaunchNowRequestDto dto) {
        SchedulerResponseDto result = schedulerService.launchImmediately(userDetails, dto);
        return ResponseEntity.ok(ApiResponse.<SchedulerResponseDto>builder()
                .success(true).message("Campaign immediate launch initiated").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PutMapping("/reschedule/{id}")
    public ResponseEntity<ApiResponse<SchedulerResponseDto>> rescheduleCampaign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody RescheduleCampaignRequestDto dto) {
        SchedulerResponseDto result = schedulerService.rescheduleCampaign(userDetails, id, dto);
        return ResponseEntity.ok(ApiResponse.<SchedulerResponseDto>builder()
                .success(true).message("Campaign rescheduled successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PatchMapping("/pause/{id}")
    public ResponseEntity<ApiResponse<SchedulerResponseDto>> pauseScheduler(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        SchedulerResponseDto result = schedulerService.pauseScheduler(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<SchedulerResponseDto>builder()
                .success(true).message("Scheduler paused successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PatchMapping("/resume/{id}")
    public ResponseEntity<ApiResponse<SchedulerResponseDto>> resumeScheduler(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        SchedulerResponseDto result = schedulerService.resumeScheduler(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<SchedulerResponseDto>builder()
                .success(true).message("Scheduler resumed successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<SchedulerResponseDto>> cancelScheduler(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        SchedulerResponseDto result = schedulerService.cancelScheduler(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<SchedulerResponseDto>builder()
                .success(true).message("Scheduler cancelled successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SchedulerResponseDto>> getSchedulerById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        SchedulerResponseDto result = schedulerService.getSchedulerById(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<SchedulerResponseDto>builder()
                .success(true).message("Scheduler details retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SchedulerResponseDto>>> listAllSchedules(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<SchedulerResponseDto> result = schedulerService.listAllSchedules(userDetails, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<SchedulerResponseDto>>builder()
                .success(true).message("Schedules retrieved successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<Page<SchedulerResponseDto>>> getUpcomingSchedules(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SchedulerResponseDto> result = schedulerService.getUpcomingSchedules(userDetails, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<SchedulerResponseDto>>builder()
                .success(true).message("Upcoming schedules retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Page<SchedulerResponseDto>>> getExecutionHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SchedulerResponseDto> result = schedulerService.getExecutionHistory(userDetails, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<SchedulerResponseDto>>builder()
                .success(true).message("Execution history retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<SchedulerStatsDto>> getSchedulerStatistics(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        SchedulerStatsDto stats = schedulerService.getSchedulerStatistics(userDetails);
        return ResponseEntity.ok(ApiResponse.<SchedulerStatsDto>builder()
                .success(true).message("Scheduler statistics calculated").data(stats).timestamp(LocalDateTime.now()).build());
    }
}
