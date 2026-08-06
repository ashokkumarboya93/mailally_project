package com.mailally.auth.service.impl;

import com.mailally.auth.dto.AuthResponseDto;
import com.mailally.auth.dto.ChangePasswordRequestDto;
import com.mailally.auth.dto.ForgotPasswordRequestDto;
import com.mailally.auth.dto.LoginRequestDto;
import com.mailally.auth.dto.RegisterRequestDto;
import com.mailally.auth.dto.ResetPasswordRequestDto;
import com.mailally.auth.dto.UserProfileDto;
import com.mailally.auth.entity.Auth;
import com.mailally.auth.mapper.AuthMapper;
import com.mailally.auth.repository.AuthRepository;
import com.mailally.auth.service.AuthService;
import com.mailally.auth.validator.AuthValidator;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.security.JwtService;
import com.mailally.subscription.entity.Subscription;
import com.mailally.subscription.repository.SubscriptionRepository;
import com.mailally.user.entity.User;
import com.mailally.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service implementation managing authentication, registration, JWT issuance,
 * and password management.
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final OrganizationRepository organizationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AuthValidator authValidator;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
            AuthRepository authRepository,
            OrganizationRepository organizationRepository,
            SubscriptionRepository subscriptionRepository,
            AuthValidator authValidator,
            AuthMapper authMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.organizationRepository = organizationRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.authValidator = authValidator;
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserProfileDto register(RegisterRequestDto dto) {
        if (dto == null || dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new CustomException("Email address is required");
        }

        String email = dto.getEmail().trim().toLowerCase();

        // Check if user already exists - if so, update credentials and return profile seamlessly
        User existingUser = userRepository.findByEmailAndIsDeletedFalse(email).orElse(null);
        if (existingUser != null) {
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                Auth auth = authRepository.findByUser(existingUser).orElse(null);
                if (auth != null) {
                    auth.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
                    authRepository.save(auth);
                } else {
                    Auth newAuth = Auth.builder()
                            .user(existingUser)
                            .passwordHash(passwordEncoder.encode(dto.getPassword()))
                            .mfaEnabled(false)
                            .build();
                    authRepository.save(newAuth);
                }
            }
            existingUser.setStatus("ACTIVE");
            userRepository.save(existingUser);
            return authMapper.toUserProfileDto(existingUser);
        }

        Long requestedSubId = dto.getSubscriptionId() != null ? dto.getSubscriptionId() : 1L;
        Subscription subscription = subscriptionRepository.findById(requestedSubId)
                .orElseGet(() -> subscriptionRepository.findAll().stream().findFirst()
                        .orElseGet(() -> subscriptionRepository.save(Subscription.builder()
                                .name("Default Plan")
                                .code("DEFAULT")
                                .price(new java.math.BigDecimal("0.00"))
                                .currency("USD")
                                .maxContacts(1000)
                                .maxEmailsPerMonth(5000)
                                .maxUsers(2)
                                .status("ACTIVE")
                                .isDeleted(false)
                                .build())));

        String firstName = (dto.getFirstName() != null && !dto.getFirstName().isBlank())
                ? dto.getFirstName().trim()
                : email.split("@")[0];
        String lastName = (dto.getLastName() != null && !dto.getLastName().isBlank())
                ? dto.getLastName().trim()
                : "User";

        String orgName = (dto.getOrganizationName() != null && !dto.getOrganizationName().isBlank())
                ? dto.getOrganizationName().trim()
                : (firstName + "'s Organization");

        String baseSlug = (dto.getOrganizationSlug() != null && !dto.getOrganizationSlug().isBlank())
                ? dto.getOrganizationSlug().trim().toLowerCase()
                : email.split("@")[0].replaceAll("[^a-z0-9]", "");

        if (baseSlug.isBlank()) {
            baseSlug = "org";
        }

        String finalSlug = baseSlug;
        int counter = 1;
        while (organizationRepository.existsBySlug(finalSlug)) {
            finalSlug = baseSlug + "-" + counter;
            counter++;
        }

        Organization organization = Organization.builder()
                .subscription(subscription)
                .name(orgName)
                .slug(finalSlug)
                .status("ACTIVE")
                .isDeleted(false)
                .build();
        Organization savedOrg = organizationRepository.save(organization);

        User user = User.builder()
                .organization(savedOrg)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role("ADMIN")
                .status("ACTIVE")
                .isDeleted(false)
                .build();
        User savedUser = userRepository.save(user);

        Auth auth = Auth.builder()
                .user(savedUser)
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .mfaEnabled(false)
                .build();
        authRepository.save(auth);

        return authMapper.toUserProfileDto(savedUser);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto dto) {
        if (dto == null || dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new CustomException("Invalid email or password");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new CustomException("Invalid email or password");
        }

        String cleanEmail = dto.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmailAndIsDeletedFalse(cleanEmail)
                .orElseThrow(() -> new CustomException("Invalid email or password"));

        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Authentication credentials not configured"));

        if (!passwordEncoder.matches(dto.getPassword(), auth.getPasswordHash())) {
            throw new CustomException("Invalid email or password");
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new CustomException("User account is inactive or suspended");
        }

        auth.setLastLoginAt(LocalDateTime.now());
        authRepository.save(auth);

        CustomUserDetails userDetails = new CustomUserDetails(user, auth.getPasswordHash());
        String token = jwtService.generateToken(userDetails);

        return authMapper.toAuthResponseDto(user, token);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequestDto dto) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Authentication credentials not configured"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), auth.getPasswordHash())) {
            throw new CustomException("Current password is incorrect");
        }

        auth.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        authRepository.save(auth);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequestDto dto) {
        User user = userRepository.findByEmailAndIsDeletedFalse(dto.getEmail())
                .orElseThrow(() -> new CustomException("User with specified email address not found"));

        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Authentication credentials not configured"));

        String resetToken = UUID.randomUUID().toString();
        auth.setResetToken(resetToken);
        auth.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
        authRepository.save(auth);
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto dto) {
        Auth auth = authRepository.findAll().stream()
                .filter(a -> dto.getToken().equals(a.getResetToken()))
                .findFirst()
                .orElseThrow(() -> new CustomException("Invalid or expired password reset token"));

        if (auth.getResetTokenExpiry() != null && auth.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new CustomException("Password reset token has expired");
        }

        auth.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        auth.setResetToken(null);
        auth.setResetTokenExpiry(null);
        authRepository.save(auth);
    }

    @Override
    public UserProfileDto getProfile(Long userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException("User profile not found"));
        return authMapper.toUserProfileDto(user);
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
