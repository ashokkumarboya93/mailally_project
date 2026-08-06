package com.mailally.template.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import com.mailally.template.repository.TemplateRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Validator for Template domain rules and role authorization.
 */
@Component
public class TemplateValidator {

    private static final Set<String> ALLOWED_STATUSES = Set.of("DRAFT", "ACTIVE", "INACTIVE", "ARCHIVED");

    private final TemplateRepository templateRepository;

    public TemplateValidator(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public void validateCreate(String name, Long organizationId) {
        if (name == null || name.isBlank()) {
            throw new CustomException("Template name is required");
        }
        if (templateRepository.existsByOrganizationIdAndNameAndIsDeletedFalse(organizationId, name.trim())) {
            throw new CustomException("Template with name '" + name.trim() + "' already exists in this organization");
        }
    }

    public void validateStatus(String status) {
        if (status != null && !status.isBlank() && !ALLOWED_STATUSES.contains(status.trim().toUpperCase())) {
            throw new CustomException("Invalid template status. Allowed statuses: " + ALLOWED_STATUSES);
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
