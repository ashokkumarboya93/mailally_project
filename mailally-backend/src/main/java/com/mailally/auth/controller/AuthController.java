package com.mailally.auth.controller;

import com.mailally.auth.dto.AuthResponseDto;
import com.mailally.auth.dto.ChangePasswordRequestDto;
import com.mailally.auth.dto.ForgotPasswordRequestDto;
import com.mailally.auth.dto.LoginRequestDto;
import com.mailally.auth.dto.RegisterRequestDto;
import com.mailally.auth.dto.ResetPasswordRequestDto;
import com.mailally.auth.dto.UserProfileDto;
import com.mailally.auth.service.AuthService;
import com.mailally.common.response.ApiResponse;
import com.mailally.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST Controller exposing public and authenticated endpoints for authentication and user security.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserProfileDto>> register(@Valid @RequestBody RegisterRequestDto dto) {
        UserProfileDto profile = authService.register(dto);
        ApiResponse<UserProfileDto> response = ApiResponse.<UserProfileDto>builder()
                .success(true)
                .message("Organization and user registered successfully")
                .data(profile)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto dto) {
        AuthResponseDto authResponse = authService.login(dto);
        ApiResponse<AuthResponseDto> response = ApiResponse.<AuthResponseDto>builder()
                .success(true)
                .message("Login successful")
                .data(authResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Logout successful")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequestDto dto
    ) {
        authService.changePassword(userDetails.getUserId(), dto);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Password changed successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto dto) {
        authService.forgotPassword(dto);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset token generated successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto dto) {
        authService.resetPassword(dto);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Password reset successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileDto profile = authService.getProfile(userDetails.getUserId());
        ApiResponse<UserProfileDto> response = ApiResponse.<UserProfileDto>builder()
                .success(true)
                .message("User profile retrieved successfully")
                .data(profile)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}

