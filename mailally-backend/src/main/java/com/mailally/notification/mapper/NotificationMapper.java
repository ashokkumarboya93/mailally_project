package com.mailally.notification.mapper;

import com.mailally.notification.dto.NotificationResponseDto;
import com.mailally.notification.entity.Notification;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Notification entity and NotificationResponseDto.
 */
@Component
public class NotificationMapper {

    public NotificationResponseDto toNotificationResponseDto(Notification notification) {
        if (notification == null) return null;
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .organizationId(notification.getOrganization() != null ? notification.getOrganization().getId() : null)
                .userId(notification.getUser() != null ? notification.getUser().getId() : null)
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .priority(notification.getPriority())
                .status(notification.getStatus())
                .channel(notification.getChannel())
                .sourceModule(notification.getSourceModule())
                .referenceId(notification.getReferenceId())
                .actionUrl(notification.getActionUrl())
                .icon(notification.getIcon())
                .color(notification.getColor())
                .isRead(notification.getIsRead())
                .readAt(notification.getReadAt())
                .expiresAt(notification.getExpiresAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
