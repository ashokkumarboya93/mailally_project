package com.mailally.subscription.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

/**
 * Validator for Subscription permissions and plan codes.
 */
@Component
public class SubscriptionValidator {

    public void validateAuthenticatedUser(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access to Subscription");
        }
        if (currentUser.getOrganizationId() == null) {
            throw new CustomException("User has no associated organization for subscription management");
        }
    }

    public void validateAdminRole(CustomUserDetails currentUser) {
        validateAuthenticatedUser(currentUser);
        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new CustomException("Access denied. Only ADMIN role can change subscription plans.");
        }
    }

    public void validatePlanCode(String planCode) {
        if (planCode == null || planCode.isBlank()) {
            throw new CustomException("Plan code cannot be empty");
        }
        String code = planCode.trim().toUpperCase();
        if (!"FREE".equals(code) && !"STARTER".equals(code) && !"PRO".equals(code) && !"BUSINESS".equals(code) && !"ENTERPRISE".equals(code)) {
            throw new CustomException("Invalid plan code '" + planCode + "'. Allowed values: FREE, STARTER, PRO, BUSINESS, ENTERPRISE");
        }
    }
}
