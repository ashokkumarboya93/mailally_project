package com.mailally.user.controller;

import com.mailally.common.response.ApiResponse;
import com.mailally.security.CustomUserDetails;
import com.mailally.user.dto.CreateUserRequestDto;
import com.mailally.user.dto.UpdateUserRequestDto;
import com.mailally.user.dto.UserResponseDto;
import com.mailally.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST Controller exposing tenant-scoped User Management APIs.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateUserRequestDto dto
    ) {
        UserResponseDto responseDto = userService.createUser(userDetails, dto);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User created successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        UserResponseDto responseDto = userService.getUserById(userDetails, id);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User retrieved successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getAllUsers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Page<UserResponseDto> usersPage = userService.getAllUsers(userDetails, page, size, sortBy, sortDir);
        ApiResponse<Page<UserResponseDto>> response = ApiResponse.<Page<UserResponseDto>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(usersPage)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDto dto
    ) {
        UserResponseDto responseDto = userService.updateUser(userDetails, id, dto);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User updated successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponseDto>> deactivateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        UserResponseDto responseDto = userService.deactivateUser(userDetails, id);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User deactivated successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<UserResponseDto>> activateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        UserResponseDto responseDto = userService.activateUser(userDetails, id);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User activated successfully")
                .data(responseDto)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        userService.softDeleteUser(userDetails, id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("User soft deleted successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<UserResponseDto>>> searchUsers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        Page<UserResponseDto> usersPage = userService.searchUsers(
                userDetails, email, name, role, status, page, size, sortBy, sortDir
        );
        ApiResponse<Page<UserResponseDto>> response = ApiResponse.<Page<UserResponseDto>>builder()
                .success(true)
                .message("User search completed successfully")
                .data(usersPage)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
