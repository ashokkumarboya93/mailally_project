package com.mailally.dashboard.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

/**
 * Validator for Dashboard access levels, organization boundary checks, and search parameters.
 */
@Component
public class DashboardValidator {

    public void validateAuthenticatedUser(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access to Dashboard");
        }
        if (currentUser.getOrganizationId() == null) {
            throw new CustomException("User has no associated organization for dashboard viewing");
        }
    }

    public void validateSearchQuery(String query) {
        if (query == null || query.trim().length() < 2) {
            throw new CustomException("Global dashboard search query must be at least 2 characters long");
        }
    }
}
