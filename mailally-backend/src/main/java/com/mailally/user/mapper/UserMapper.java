package com.mailally.user.mapper;

import com.mailally.user.dto.UserResponseDto;
import com.mailally.user.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper component for User domain objects providing manual object transformation.
 */
@Component
public class UserMapper {

    public UserResponseDto toUserResponseDto(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .organizationId(user.getOrganization() != null ? user.getOrganization().getId() : null)
                .organizationName(user.getOrganization() != null ? user.getOrganization().getName() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
