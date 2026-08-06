package com.mailally.scheduler.validator;

import com.mailally.campaign.entity.Campaign;
import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Validator for Scheduler domain rules, state transitions, and role authorization.
 */
@Component
public class SchedulerValidator {

    public void validateAdminOrManager(CustomUserDetails currentUser) {
        if (currentUser == null) throw new CustomException("Unauthenticated user access");
        String role = currentUser.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"MANAGER".equalsIgnoreCase(role)) {
            throw new CustomException("Access denied. Only ADMIN or MANAGER roles can perform this action.");
        }
    }

    public void validateScheduledTimeInFuture(LocalDateTime scheduledTime) {
        if (scheduledTime == null) {
            throw new CustomException("Scheduled time is required");
        }
        if (scheduledTime.isBefore(LocalDateTime.now())) {
            throw new CustomException("Cannot schedule campaign execution in the past");
        }
    }

    public void validateCampaignForExecution(Campaign campaign) {
        if (campaign == null) {
            throw new CustomException("Campaign is required for execution");
        }
        if (Boolean.TRUE.equals(campaign.getIsDeleted())) {
            throw new CustomException("Cannot schedule a deleted campaign");
        }
        if (campaign.getTemplate() == null) {
            throw new CustomException("Campaign has no template attached. Attach a template before scheduling.");
        }
        if (campaign.getSegment() == null) {
            throw new CustomException("Campaign has no segment attached. Attach a segment before scheduling.");
        }
        if (campaign.getSegment().getContactCount() == null || campaign.getSegment().getContactCount() <= 0) {
            throw new CustomException("Campaign target segment has no contacts to dispatch");
        }
        if ("RUNNING".equalsIgnoreCase(campaign.getStatus())) {
            throw new CustomException("Campaign is currently running");
        }
        if ("COMPLETED".equalsIgnoreCase(campaign.getStatus())) {
            throw new CustomException("Campaign has already completed execution");
        }
        if ("CANCELLED".equalsIgnoreCase(campaign.getStatus())) {
            throw new CustomException("Campaign has been cancelled and cannot be executed");
        }
    }

    public void validatePauseState(String status) {
        if (!"SCHEDULED".equalsIgnoreCase(status) && !"WAITING".equalsIgnoreCase(status) && !"RUNNING".equalsIgnoreCase(status)) {
            throw new CustomException("Only SCHEDULED, WAITING, or RUNNING executions can be paused. Current state: " + status);
        }
    }

    public void validateResumeState(String status) {
        if (!"PAUSED".equalsIgnoreCase(status)) {
            throw new CustomException("Only PAUSED executions can be resumed. Current state: " + status);
        }
    }

    public void validateCancelState(String status) {
        if ("COMPLETED".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status)) {
            throw new CustomException("Cannot cancel an execution that is already " + status);
        }
    }

    public void validateRescheduleState(String status) {
        if (!"SCHEDULED".equalsIgnoreCase(status) && !"WAITING".equalsIgnoreCase(status) && !"PAUSED".equalsIgnoreCase(status)) {
            throw new CustomException("Only SCHEDULED, WAITING, or PAUSED executions can be rescheduled. Current state: " + status);
        }
    }
}
