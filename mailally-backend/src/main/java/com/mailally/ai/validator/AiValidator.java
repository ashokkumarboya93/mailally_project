package com.mailally.ai.validator;

import com.mailally.exception.CustomException;
import com.mailally.security.CustomUserDetails;
import org.springframework.stereotype.Component;

/**
 * Validator for AI authentication and prompt input verification.
 */
@Component
public class AiValidator {

    public void validateAuthenticatedUser(CustomUserDetails currentUser) {
        if (currentUser == null) {
            throw new CustomException("Unauthenticated user access to AI Module");
        }
        if (currentUser.getOrganizationId() == null) {
            throw new CustomException("User has no associated organization for AI generation");
        }
    }

    public void validatePromptInput(String prompt) {
        if (prompt == null || prompt.trim().isBlank()) {
            throw new CustomException("AI prompt text cannot be empty");
        }
    }
}
