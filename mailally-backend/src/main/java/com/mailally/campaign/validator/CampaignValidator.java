package com.mailally.campaign.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Validator for Campaign domain rules and role authorization.
 */
@Component
public class CampaignValidator {

    private static final Set<String> ALLOWED_STATUSES = Set.of("DRAFT", "SCHEDULED", "RUNNING", "COMPLETED", "FAILED", "CANCELLED");

    public void validateStatus(String status) {
        if (status != null && !status.isBlank() && !ALLOWED_STATUSES.contains(status.trim().toUpperCase())) {
            throw new CustomException("Invalid campaign status. Allowed statuses: " + ALLOWED_STATUSES);
        }
    }

    public void validateCampaignEditable(String currentStatus) {
        if (!"DRAFT".equalsIgnoreCase(currentStatus)) {
            throw new CustomException("Only campaigns in DRAFT status can be edited. Current status: " + currentStatus);
        }
    }

    public void validateCampaignSchedulable(String currentStatus) {
        if (!"DRAFT".equalsIgnoreCase(currentStatus)) {
            throw new CustomException("Only DRAFT campaigns can be scheduled. Current status: " + currentStatus);
        }
    }

    public void validateCampaignCancellable(String currentStatus) {
        if (!"SCHEDULED".equalsIgnoreCase(currentStatus) && !"RUNNING".equalsIgnoreCase(currentStatus)) {
            throw new CustomException("Only SCHEDULED or RUNNING campaigns can be cancelled. Current status: " + currentStatus);
        }
    }

    public void validateAdminOrManager(CustomUserDetails currentUser) {
        if (currentUser == null) throw new CustomException("Unauthenticated user access");
        String role = currentUser.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"MANAGER".equalsIgnoreCase(role)) {
            throw new CustomException("Access denied. Only ADMIN or MANAGER roles can perform this action.");
        }
    }
}
