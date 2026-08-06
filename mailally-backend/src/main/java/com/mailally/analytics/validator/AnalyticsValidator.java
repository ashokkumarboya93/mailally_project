package com.mailally.analytics.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Validator for Analytics access permissions, date ranges, and organization scoping.
 */
@Component
public class AnalyticsValidator {

    public void validateAuthenticatedUser(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access to Analytics");
        }
        if (currentUser.getOrganizationId() == null) {
            throw new CustomException("User has no associated organization for analytics reporting");
        }
    }

    public void validateDateRange(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            throw new CustomException("Start date 'dateFrom' cannot be after end date 'dateTo'");
        }
    }
}
