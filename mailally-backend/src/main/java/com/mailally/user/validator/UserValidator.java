package com.mailally.user.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import com.mailally.user.dto.CreateUserRequestDto;
import com.mailally.user.dto.UpdateUserRequestDto;
import com.mailally.user.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Validator component enforcing domain rules for User creation, modification, and access control.
 */
@Component
public class UserValidator {

    private static final Set<String> ALLOWED_ROLES = Set.of("ADMIN", "MEMBER", "USER", "MANAGER");
    private static final Set<String> ALLOWED_STATUSES = Set.of("ACTIVE", "INACTIVE", "SUSPENDED");

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateCreateUser(CreateUserRequestDto dto) {
        if (userRepository.existsByEmailAndIsDeletedFalse(dto.getEmail())) {
            throw new CustomException("Email address '" + dto.getEmail() + "' is already registered");
        }
        validateRole(dto.getRole());
    }

    public void validateUpdateUser(UpdateUserRequestDto dto) {
        validateRole(dto.getRole());
        validateStatus(dto.getStatus());
    }

    public void validateRole(String role) {
        if (role == null || !ALLOWED_ROLES.contains(role.toUpperCase())) {
            throw new CustomException("Invalid role specified. Allowed roles: " + ALLOWED_ROLES);
        }
    }

    public void validateStatus(String status) {
        if (status == null || !ALLOWED_STATUSES.contains(status.toUpperCase())) {
            throw new CustomException("Invalid status specified. Allowed statuses: " + ALLOWED_STATUSES);
        }
    }

    public void validateAdminRole(CustomUserDetails currentUser) {
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new CustomException("Access denied. Only organization administrators can perform this operation.");
        }
    }
}
