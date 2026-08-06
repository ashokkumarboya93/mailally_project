package com.mailally.segment.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import com.mailally.segment.repository.SegmentRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Validator for Segment domain rules and role authorization.
 */
@Component
public class SegmentValidator {

    private static final Set<String> ALLOWED_TYPES = Set.of("STATIC", "DYNAMIC");
    private static final Set<String> ALLOWED_STATUSES = Set.of("ACTIVE", "INACTIVE", "ARCHIVED");

    private final SegmentRepository segmentRepository;

    public SegmentValidator(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }

    public void validateCreate(String name, String type, Long organizationId) {
        if (name == null || name.isBlank()) {
            throw new CustomException("Segment name is required");
        }
        validateType(type);
        if (segmentRepository.existsByOrganizationIdAndNameAndIsDeletedFalse(organizationId, name.trim())) {
            throw new CustomException("Segment with name '" + name.trim() + "' already exists in this organization");
        }
    }

    public void validateType(String type) {
        if (type == null || !ALLOWED_TYPES.contains(type.trim().toUpperCase())) {
            throw new CustomException("Invalid segment type. Allowed types: " + ALLOWED_TYPES);
        }
    }

    public void validateStatus(String status) {
        if (status != null && !status.isBlank() && !ALLOWED_STATUSES.contains(status.trim().toUpperCase())) {
            throw new CustomException("Invalid segment status. Allowed statuses: " + ALLOWED_STATUSES);
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
