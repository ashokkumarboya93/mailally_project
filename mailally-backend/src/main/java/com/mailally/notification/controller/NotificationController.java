package com.mailally.notification.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.notification.dto.CreateNotificationRequestDto;
import com.mailally.notification.dto.NotificationCountDto;
import com.mailally.notification.dto.NotificationResponseDto;
import com.mailally.notification.dto.NotificationSummaryDto;
import com.mailally.notification.service.NotificationService;
import com.mailally.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST Controller for MailAlly Notification Management APIs.
 */
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponseDto>> createNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateNotificationRequestDto dto) {
        NotificationResponseDto result = notificationService.createNotification(userDetails, dto);
        return new ResponseEntity<>(ApiResponse.<NotificationResponseDto>builder()
                .success(true).message("Notification dispatched successfully").data(result).timestamp(LocalDateTime.now()).build(),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponseDto>>> getUserNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        Page<NotificationResponseDto> pageResult = notificationService.getUserNotifications(userDetails, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.<Page<NotificationResponseDto>>builder()
                .success(true).message("Notifications retrieved successfully").data(pageResult).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> getNotificationById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        NotificationResponseDto result = notificationService.getNotificationById(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<NotificationResponseDto>builder()
                .success(true).message("Notification details retrieved").data(result).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<Page<NotificationResponseDto>>> getUnreadNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationResponseDto> unread = notificationService.getUnreadNotifications(userDetails, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<NotificationResponseDto>>builder()
                .success(true).message("Unread notifications retrieved").data(unread).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<NotificationCountDto>> countUnreadNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        NotificationCountDto countDto = notificationService.countUnreadNotifications(userDetails);
        return ResponseEntity.ok(ApiResponse.<NotificationCountDto>builder()
                .success(true).message("Notification counts retrieved").data(countDto).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<NotificationSummaryDto>> getNotificationSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        NotificationSummaryDto summary = notificationService.getNotificationSummary(userDetails);
        return ResponseEntity.ok(ApiResponse.<NotificationSummaryDto>builder()
                .success(true).message("Notification summary retrieved").data(summary).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<NotificationResponseDto>>> searchNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("query") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationResponseDto> results = notificationService.searchNotifications(userDetails, query, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<NotificationResponseDto>>builder()
                .success(true).message("Notification search completed").data(results).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<NotificationResponseDto>>> filterNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sourceModule,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationResponseDto> filtered = notificationService.filterNotifications(userDetails, type, priority, status, sourceModule, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<NotificationResponseDto>>builder()
                .success(true).message("Filtered notifications retrieved").data(filtered).timestamp(LocalDateTime.now()).build());
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> markAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        NotificationResponseDto result = notificationService.markAsRead(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<NotificationResponseDto>builder()
                .success(true).message("Notification marked as read").data(result).timestamp(LocalDateTime.now()).build());
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<NotificationSummaryDto>> markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        NotificationSummaryDto summary = notificationService.markAllAsRead(userDetails);
        return ResponseEntity.ok(ApiResponse.<NotificationSummaryDto>builder()
                .success(true).message("All unread notifications marked as read").data(summary).timestamp(LocalDateTime.now()).build());
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> archiveNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        NotificationResponseDto result = notificationService.archiveNotification(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<NotificationResponseDto>builder()
                .success(true).message("Notification archived successfully").data(result).timestamp(LocalDateTime.now()).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        notificationService.deleteNotification(userDetails, id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Notification soft deleted successfully").data(null).timestamp(LocalDateTime.now()).build());
    }
}
