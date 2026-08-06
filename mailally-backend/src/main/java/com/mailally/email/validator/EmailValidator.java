package com.mailally.email.validator;

import com.mailally.campaign.entity.Campaign;
import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

/**
 * Validator component for Email domain operations and access control.
 */
@Component
public class EmailValidator {

    public void validateAdminOrManager(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access");
        }
        String role = currentUser.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"MANAGER".equalsIgnoreCase(role)) {
            throw new CustomException("Access denied. Only ADMIN or MANAGER roles can perform this action.");
        }
    }

    public void validateCampaignReadyForLaunch(Campaign campaign) {
        if (campaign == null) {
            throw new CustomException("Campaign is required");
        }
        if (campaign.getTemplate() == null) {
            throw new CustomException("Campaign has no template attached. Attach a template before launching.");
        }
        if (campaign.getSegment() == null) {
            throw new CustomException("Campaign has no segment attached. Attach a segment before launching.");
        }
        if ("RUNNING".equalsIgnoreCase(campaign.getStatus())) {
            throw new CustomException("Campaign is already currently running");
        }
        if ("COMPLETED".equalsIgnoreCase(campaign.getStatus())) {
            throw new CustomException("Campaign has already completed execution");
        }
        if ("CANCELLED".equalsIgnoreCase(campaign.getStatus())) {
            throw new CustomException("Campaign has been cancelled and cannot be launched");
        }
    }
}
