package com.mailally.notification.service;

import com.mailally.notification.dto.CreateNotificationRequestDto;
import com.mailally.notification.dto.NotificationCountDto;
import com.mailally.notification.dto.NotificationResponseDto;
import com.mailally.notification.dto.NotificationSummaryDto;
import com.mailally.security.CustomUserDetails;
import org.springframework.data.domain.Page;

/**
 * Service interface for enterprise Notification dispatch, lifecycle management, and querying.
 */
public interface NotificationService {

    NotificationResponseDto createNotification(CustomUserDetails currentUser, CreateNotificationRequestDto dto);

    NotificationResponseDto sendNotification(Long organizationId, Long userId, String type, String title,
                                             String message, String priority, String sourceModule,
                                             Long referenceId, String actionUrl);

    NotificationResponseDto getNotificationById(CustomUserDetails currentUser, Long id);

    Page<NotificationResponseDto> getUserNotifications(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir);

    Page<NotificationResponseDto> getUnreadNotifications(CustomUserDetails currentUser, int page, int size);

    NotificationResponseDto markAsRead(CustomUserDetails currentUser, Long id);

    NotificationSummaryDto markAllAsRead(CustomUserDetails currentUser);

    NotificationResponseDto archiveNotification(CustomUserDetails currentUser, Long id);

    void deleteNotification(CustomUserDetails currentUser, Long id);

    Page<NotificationResponseDto> searchNotifications(CustomUserDetails currentUser, String query, int page, int size);

    Page<NotificationResponseDto> filterNotifications(CustomUserDetails currentUser, String type, String priority,
                                                      String status, String sourceModule, int page, int size);

    NotificationCountDto countUnreadNotifications(CustomUserDetails currentUser);

    long countNotificationsByType(CustomUserDetails currentUser, String type);

    long countNotificationsByPriority(CustomUserDetails currentUser, String priority);

    NotificationSummaryDto getNotificationSummary(CustomUserDetails currentUser);
}
