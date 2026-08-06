package com.mailally.auth.validator;

import com.mailally.auth.dto.RegisterRequestDto;
import com.mailally.exception.CustomException;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.user.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Custom validator for authentication domain checks.
 */
@Component
public class AuthValidator {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public AuthValidator(UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    public void validateRegistration(RegisterRequestDto dto) {
        if (dto == null || dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new CustomException("Email address is required");
        }

        String cleanEmail = dto.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(cleanEmail) || userRepository.existsByEmailAndIsDeletedFalse(cleanEmail)) {
            throw new CustomException("Email address '" + cleanEmail + "' is already registered");
        }

        if (dto.getOrganizationSlug() != null && !dto.getOrganizationSlug().isBlank()
                && organizationRepository.existsBySlug(dto.getOrganizationSlug().trim().toLowerCase())) {
            throw new CustomException("Organization slug '" + dto.getOrganizationSlug() + "' is already taken");
        }
    }

}
