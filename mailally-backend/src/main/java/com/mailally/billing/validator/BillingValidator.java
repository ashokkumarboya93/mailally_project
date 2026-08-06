package com.mailally.billing.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

/**
 * Validator for Billing role-based authorization, multi-tenant boundaries, and invoice constraints.
 */
@Component
public class BillingValidator {

    public void validateAuthenticatedUser(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access to Billing");
        }
        if (currentUser.getOrganizationId() == null) {
            throw new CustomException("User has no associated organization for billing operations");
        }
        // MEMBER role is completely blocked from accessing Billing
        if ("MEMBER".equalsIgnoreCase(currentUser.getRole())) {
            throw new CustomException("Access denied. MEMBER role is not authorized to access Billing data.");
        }
    }

    public void validateAdminRole(CustomUserDetails currentUser) {
        validateAuthenticatedUser(currentUser);
        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new CustomException("Access denied. Only ADMIN role can create, update, or record billing transactions.");
        }
    }

    public void validateSearchQuery(String query) {
        if (query == null || query.trim().length() < 2) {
            throw new CustomException("Billing search query must be at least 2 characters long");
        }
    }
}
