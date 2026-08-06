package com.mailally.auth.mapper;

import com.mailally.auth.dto.AuthResponseDto;
import com.mailally.auth.dto.UserProfileDto;
import com.mailally.user.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper component for mapping Auth domain objects and User profiles.
 */
@Component
public class AuthMapper {

    public UserProfileDto toUserProfileDto(User user) {
        if (user == null) {
            return null;
        }
        return UserProfileDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                .organizationName(user.getOrganization() != null ? user.getOrganization().getName() : null)
                .organizationSlug(user.getOrganization() != null ? user.getOrganization().getSlug() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }

    public AuthResponseDto toAuthResponseDto(User user, String token) {
        if (user == null) {
            return null;
        }
        return AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                .organizationName(user.getOrganization() != null ? user.getOrganization().getName() : null)
                .build();
    }
}

