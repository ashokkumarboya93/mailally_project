package com.mailally.user.service;

import com.mailally.security.CustomUserDetails;
import com.mailally.user.dto.CreateUserRequestDto;
import com.mailally.user.dto.UpdateUserRequestDto;
import com.mailally.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;

/**
 * Service interface defining operations for tenant-scoped User Management.
 */
public interface UserService {

    UserResponseDto createUser(CustomUserDetails currentUser, CreateUserRequestDto dto);

    UserResponseDto getUserById(CustomUserDetails currentUser, Long id);

    Page<UserResponseDto> getAllUsers(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir);

    UserResponseDto updateUser(CustomUserDetails currentUser, Long id, UpdateUserRequestDto dto);

    UserResponseDto deactivateUser(CustomUserDetails currentUser, Long id);

    UserResponseDto activateUser(CustomUserDetails currentUser, Long id);

    void softDeleteUser(CustomUserDetails currentUser, Long id);

    Page<UserResponseDto> searchUsers(
            CustomUserDetails currentUser,
            String email,
            String name,
            String role,
            String status,
            int page,
            int size,
            String sortBy,
            String sortDir
    );
}
