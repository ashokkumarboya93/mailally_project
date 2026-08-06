package com.mailally.contact.validator;

import com.mailally.contact.dto.CreateContactRequestDto;
import com.mailally.contact.repository.ContactRepository;
import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Validator enforcing domain logic and security role permissions for Contact operations.
 */
@Component
public class ContactValidator {

    private static final Set<String> ALLOWED_STATUSES = Set.of("SUBSCRIBED", "UNSUBSCRIBED", "BOUNCED", "CLEANED", "PENDING");
    private final ContactRepository contactRepository;

    public ContactValidator(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void validateCreate(CreateContactRequestDto dto, Long organizationId) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new CustomException("Contact email address is required");
        }

        String email = dto.getEmail().trim().toLowerCase();
        if (contactRepository.existsByOrganizationIdAndEmailAndIsDeletedFalse(organizationId, email)) {
            throw new CustomException("Contact with email '" + email + "' already exists in this organization");
        }

        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            validateStatus(dto.getStatus());
        }
    }

    public void validateStatus(String status) {
        if (status == null || !ALLOWED_STATUSES.contains(status.trim().toUpperCase())) {
            throw new CustomException("Invalid contact status specified. Allowed statuses: " + ALLOWED_STATUSES);
        }
    }

    public void validateAdminOrManager(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access");
        }
        String role = currentUser.getRole();
        if (!"ADMIN".equalsIgnoreCase(role) && !"MANAGER".equalsIgnoreCase(role)) {
            throw new CustomException("Access denied. Only ADMIN or MANAGER roles can perform this action.");
        }
    }

    public void validateAdmin(CustomUserDetails currentUser) {
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new CustomException("Access denied. Only organization ADMIN can perform this action.");
        }
    }
}
