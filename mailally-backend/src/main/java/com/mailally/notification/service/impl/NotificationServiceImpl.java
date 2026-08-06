package com.mailally.notification.service.impl;

import com.mailally.exception.CustomException;
import com.mailally.notification.channel.NotificationChannelHandler;
import com.mailally.notification.dto.CreateNotificationRequestDto;
import com.mailally.notification.dto.NotificationCountDto;
import com.mailally.notification.dto.NotificationResponseDto;
import com.mailally.notification.dto.NotificationSummaryDto;
import com.mailally.notification.entity.Notification;
import com.mailally.notification.mapper.NotificationMapper;
import com.mailally.notification.repository.NotificationRepository;
import com.mailally.notification.service.NotificationService;
import com.mailally.notification.validator.NotificationValidator;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.user.entity.User;
import com.mailally.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for enterprise Notification dispatch, channel routing, and lifecycle management.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final List<NotificationChannelHandler> channelHandlers;
    private final NotificationValidator notificationValidator;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   OrganizationRepository organizationRepository,
                                   UserRepository userRepository,
                                   List<NotificationChannelHandler> channelHandlers,
                                   NotificationValidator notificationValidator,
                                   NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.channelHandlers = channelHandlers;
        this.notificationValidator = notificationValidator;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public NotificationResponseDto createNotification(CustomUserDetails currentUser, CreateNotificationRequestDto dto) {
        notificationValidator.validateAuthenticatedUser(currentUser);

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        User targetUser = userRepository.findByIdAndOrganizationIdAndIsDeletedFalse(dto.getUserId(), currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Target user not found with ID: " + dto.getUserId()));

        Notification notification = Notification.builder()
                .organization(org)
                .user(targetUser)
                .title(dto.getTitle())
                .message(dto.getMessage())
                .type(dto.getType() != null ? dto.getType().toUpperCase() : "CUSTOM")
                .priority(dto.getPriority() != null ? dto.getPriority().toUpperCase() : "NORMAL")
                .channel(dto.getChannel() != null ? dto.getChannel().toUpperCase() : "IN_APP")
                .sourceModule(dto.getSourceModule() != null ? dto.getSourceModule().toUpperCase() : "SYSTEM")
                .referenceId(dto.getReferenceId())
                .actionUrl(dto.getActionUrl())
                .icon(dto.getIcon())
                .color(dto.getColor())
                .expiresAt(dto.getExpiresAt())
                .createdBy(currentUser.getUserId())
                .updatedBy(currentUser.getUserId())
                .build();

        Notification saved = notificationRepository.save(notification);
        dispatchToChannel(saved);

        return notificationMapper.toNotificationResponseDto(saved);
    }

    @Override
    public NotificationResponseDto sendNotification(Long organizationId, Long userId, String type, String title,
                                                     String message, String priority, String sourceModule,
                                                     Long referenceId, String actionUrl) {
        Organization org = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new CustomException("Organization not found for event notification"));

        User targetUser = userRepository.findByIdAndOrganizationIdAndIsDeletedFalse(userId, organizationId)
                .orElseThrow(() -> new CustomException("Target user not found with ID: " + userId));

        Notification notification = Notification.builder()
                .organization(org)
                .user(targetUser)
                .title(title)
                .message(message)
                .type(type != null ? type.toUpperCase() : "SYSTEM_EVENT")
                .priority(priority != null ? priority.toUpperCase() : "NORMAL")
                .channel("IN_APP")
                .sourceModule(sourceModule != null ? sourceModule.toUpperCase() : "SYSTEM")
                .referenceId(referenceId)
                .actionUrl(actionUrl)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        Notification saved = notificationRepository.save(notification);
        dispatchToChannel(saved);

        return notificationMapper.toNotificationResponseDto(saved);
    }

    private void dispatchToChannel(Notification notification) {
        String targetChannel = notification.getChannel();
        for (NotificationChannelHandler handler : channelHandlers) {
            if (handler.supportsChannel(targetChannel)) {
                handler.dispatch(notification);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponseDto getNotificationById(CustomUserDetails currentUser, Long id) {
        notificationValidator.validateAuthenticatedUser(currentUser);

        Notification notification = notificationRepository.findByIdAndOrganizationIdAndUserIdAndIsDeletedFalse(
                id, currentUser.getOrganizationId(), currentUser.getUserId())
                .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));

        notificationValidator.validateNotificationOwnership(currentUser, notification);
        return notificationMapper.toNotificationResponseDto(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponseDto> getUserNotifications(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir) {
        notificationValidator.validateAuthenticatedUser(currentUser);
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Notification> notifications = notificationRepository.findByOrganizationIdAndUserIdAndIsDeletedFalse(
                currentUser.getOrganizationId(), currentUser.getUserId(), pageable);
        return notifications.map(notificationMapper::toNotificationResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponseDto> getUnreadNotifications(CustomUserDetails currentUser, int page, int size) {
        notificationValidator.validateAuthenticatedUser(currentUser);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Notification> notifications = notificationRepository.findByOrganizationIdAndUserIdAndIsReadFalseAndIsDeletedFalse(
                currentUser.getOrganizationId(), currentUser.getUserId(), pageable);
        return notifications.map(notificationMapper::toNotificationResponseDto);
    }

    @Override
    public NotificationResponseDto markAsRead(CustomUserDetails currentUser, Long id) {
        notificationValidator.validateAuthenticatedUser(currentUser);

        Notification notification = notificationRepository.findByIdAndOrganizationIdAndUserIdAndIsDeletedFalse(
                id, currentUser.getOrganizationId(), currentUser.getUserId())
                .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));

        notificationValidator.validateNotificationOwnership(currentUser, notification);

        notification.setIsRead(true);
        notification.setStatus("READ");
        notification.setReadAt(LocalDateTime.now());
        notification.setUpdatedBy(currentUser.getUserId());

        Notification saved = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponseDto(saved);
    }

    @Override
    public NotificationSummaryDto markAllAsRead(CustomUserDetails currentUser) {
        notificationValidator.validateAuthenticatedUser(currentUser);

        Page<Notification> unreadPage = notificationRepository.findByOrganizationIdAndUserIdAndIsReadFalseAndIsDeletedFalse(
                currentUser.getOrganizationId(), currentUser.getUserId(), PageRequest.of(0, 1000));

        for (Notification n : unreadPage.getContent()) {
            n.setIsRead(true);
            n.setStatus("READ");
            n.setReadAt(LocalDateTime.now());
            n.setUpdatedBy(currentUser.getUserId());
            notificationRepository.save(n);
        }

        return getNotificationSummary(currentUser);
    }

    @Override
    public NotificationResponseDto archiveNotification(CustomUserDetails currentUser, Long id) {
        notificationValidator.validateAuthenticatedUser(currentUser);

        Notification notification = notificationRepository.findByIdAndOrganizationIdAndUserIdAndIsDeletedFalse(
                id, currentUser.getOrganizationId(), currentUser.getUserId())
                .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));

        notificationValidator.validateNotificationOwnership(currentUser, notification);

        notification.setStatus("ARCHIVED");
        notification.setUpdatedBy(currentUser.getUserId());

        Notification saved = notificationRepository.save(notification);
        return notificationMapper.toNotificationResponseDto(saved);
    }

    @Override
    public void deleteNotification(CustomUserDetails currentUser, Long id) {
        notificationValidator.validateAuthenticatedUser(currentUser);

        Notification notification = notificationRepository.findByIdAndOrganizationIdAndUserIdAndIsDeletedFalse(
                id, currentUser.getOrganizationId(), currentUser.getUserId())
                .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));

        notificationValidator.validateNotificationOwnership(currentUser, notification);

        notification.setIsDeleted(true);
        notification.setStatus("DELETED");
        notification.setUpdatedBy(currentUser.getUserId());
        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponseDto> searchNotifications(CustomUserDetails currentUser, String query, int page, int size) {
        notificationValidator.validateAuthenticatedUser(currentUser);
        notificationValidator.validateSearchQuery(query);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Notification> results = notificationRepository.searchNotifications(
                currentUser.getOrganizationId(), currentUser.getUserId(), query.trim(), pageable);
        return results.map(notificationMapper::toNotificationResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponseDto> filterNotifications(CustomUserDetails currentUser, String type, String priority,
                                                            String status, String sourceModule, int page, int size) {
        notificationValidator.validateAuthenticatedUser(currentUser);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Notification> results = notificationRepository.filterNotifications(
                currentUser.getOrganizationId(),
                currentUser.getUserId(),
                (type != null && !type.isBlank()) ? type.trim().toUpperCase() : null,
                (priority != null && !priority.isBlank()) ? priority.trim().toUpperCase() : null,
                (status != null && !status.isBlank()) ? status.trim().toUpperCase() : null,
                (sourceModule != null && !sourceModule.isBlank()) ? sourceModule.trim().toUpperCase() : null,
                pageable
        );
        return results.map(notificationMapper::toNotificationResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationCountDto countUnreadNotifications(CustomUserDetails currentUser) {
        notificationValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();
        Long userId = currentUser.getUserId();

        long unread = notificationRepository.countByOrganizationIdAndUserIdAndIsReadFalseAndIsDeletedFalse(orgId, userId);
        long total = notificationRepository.countByOrganizationIdAndUserIdAndIsDeletedFalse(orgId, userId);

        return new NotificationCountDto(unread, total);
    }

    @Override
    @Transactional(readOnly = true)
    public long countNotificationsByType(CustomUserDetails currentUser, String type) {
        notificationValidator.validateAuthenticatedUser(currentUser);
        return notificationRepository.countByOrganizationIdAndUserIdAndTypeAndIsDeletedFalse(
                currentUser.getOrganizationId(), currentUser.getUserId(), type.toUpperCase());
    }

    @Override
    @Transactional(readOnly = true)
    public long countNotificationsByPriority(CustomUserDetails currentUser, String priority) {
        notificationValidator.validateAuthenticatedUser(currentUser);
        return notificationRepository.countByOrganizationIdAndUserIdAndPriorityAndIsDeletedFalse(
                currentUser.getOrganizationId(), currentUser.getUserId(), priority.toUpperCase());
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationSummaryDto getNotificationSummary(CustomUserDetails currentUser) {
        notificationValidator.validateAuthenticatedUser(currentUser);
        Long orgId = currentUser.getOrganizationId();
        Long userId = currentUser.getUserId();

        long unread = notificationRepository.countByOrganizationIdAndUserIdAndIsReadFalseAndIsDeletedFalse(orgId, userId);
        long read = notificationRepository.countByOrganizationIdAndUserIdAndStatusAndIsDeletedFalse(orgId, userId, "READ");
        long archived = notificationRepository.countByOrganizationIdAndUserIdAndStatusAndIsDeletedFalse(orgId, userId, "ARCHIVED");
        long high = notificationRepository.countByOrganizationIdAndUserIdAndPriorityAndIsDeletedFalse(orgId, userId, "HIGH");
        long critical = notificationRepository.countByOrganizationIdAndUserIdAndPriorityAndIsDeletedFalse(orgId, userId, "CRITICAL");

        return NotificationSummaryDto.builder()
                .totalUnread(unread)
                .totalRead(read)
                .totalArchived(archived)
                .highPriorityCount(high)
                .criticalPriorityCount(critical)
                .build();
    }
}
