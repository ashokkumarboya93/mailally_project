package com.mailally.auth.service;

import com.mailally.auth.dto.AuthResponseDto;
import com.mailally.auth.dto.ChangePasswordRequestDto;
import com.mailally.auth.dto.ForgotPasswordRequestDto;
import com.mailally.auth.dto.LoginRequestDto;
import com.mailally.auth.dto.RegisterRequestDto;
import com.mailally.auth.dto.ResetPasswordRequestDto;
import com.mailally.auth.dto.UserProfileDto;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {

    UserProfileDto register(RegisterRequestDto dto);

    AuthResponseDto login(LoginRequestDto dto);

    void changePassword(Long userId, ChangePasswordRequestDto dto);

    void forgotPassword(ForgotPasswordRequestDto dto);

    void resetPassword(ResetPasswordRequestDto dto);

    UserProfileDto getProfile(Long userId);

    void logout();
}

