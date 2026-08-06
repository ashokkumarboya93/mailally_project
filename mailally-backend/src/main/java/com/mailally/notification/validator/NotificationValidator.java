package com.mailally.notification.validator;

import com.mailally.exception.CustomException;
import com.mailally.notification.entity.Notification;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

/**
 * Validator for Notification permissions, tenant boundaries, and payload verification.
 */
@Component
public class NotificationValidator {

    public void validateAuthenticatedUser(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access to Notifications");
        }
        if (currentUser.getOrganizationId() == null) {
            throw new CustomException("User has no associated organization for notification operations");
        }
    }

    public void validateNotificationOwnership(CustomUserDetails currentUser, Notification notification) {
        if (notification == null) {
            throw new CustomException("Notification record not found");
        }
        if (!notification.getOrganization().getId().equals(currentUser.getOrganizationId())) {
            throw new CustomException("Access denied. Notification belongs to a different organization.");
        }
        // MEMBER role can only manage their own notifications
        if ("MEMBER".equalsIgnoreCase(currentUser.getRole()) && !notification.getUser().getId().equals(currentUser.getUserId())) {
            throw new CustomException("Access denied. MEMBER role can only access their own notifications.");
        }
    }

    public void validateSearchQuery(String query) {
        if (query == null || query.trim().length() < 2) {
            throw new CustomException("Notification search query must be at least 2 characters long");
        }
    }
}
