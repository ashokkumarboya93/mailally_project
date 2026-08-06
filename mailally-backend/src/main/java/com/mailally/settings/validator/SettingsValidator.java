package com.mailally.settings.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

/**
 * Validator for Settings permissions and data types.
 */
@Component
public class SettingsValidator {

    public void validateAuthenticatedUser(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access to Settings");
        }
        if (currentUser.getOrganizationId() == null) {
            throw new CustomException("User has no associated organization for configuration settings");
        }
    }

    public void validateAdminRole(CustomUserDetails currentUser) {
        validateAuthenticatedUser(currentUser);
        String role = currentUser.getRole();
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new CustomException("Access denied. Only ADMIN role can modify organization settings.");
        }
    }

    public void validateAdminOrManagerRole(CustomUserDetails currentUser) {
        validateAuthenticatedUser(currentUser);
        String role = currentUser.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"MANAGER".equalsIgnoreCase(role)) {
            throw new CustomException("Access denied. Only ADMIN or MANAGER roles can perform this action.");
        }
    }

    public void validateValueDataType(String value, String dataType) {
        if (value == null || dataType == null) return;
        if ("INTEGER".equalsIgnoreCase(dataType)) {
            try { Integer.parseInt(value); } catch (NumberFormatException e) {
                throw new CustomException("Invalid setting value '" + value + "' for INTEGER data type");
            }
        } else if ("BOOLEAN".equalsIgnoreCase(dataType)) {
            if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
                throw new CustomException("Invalid setting value '" + value + "' for BOOLEAN data type (must be true or false)");
            }
        } else if ("DOUBLE".equalsIgnoreCase(dataType)) {
            try { Double.parseDouble(value); } catch (NumberFormatException e) {
                throw new CustomException("Invalid setting value '" + value + "' for DOUBLE data type");
            }
        }
    }
}
