package com.mailally.audit.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

/**
 * Validator for Audit permissions and search parameters.
 */
@Component
public class AuditValidator {

    public void validateAuthenticatedUser(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access to Audit Logs");
        }
        if (currentUser.getOrganizationId() == null) {
            throw new CustomException("User has no associated organization for audit viewing");
        }
    }

    public void validateSearchQuery(String query) {
        if (query == null || query.trim().length() < 2) {
            throw new CustomException("Audit log search query must be at least 2 characters long");
        }
    }
}
