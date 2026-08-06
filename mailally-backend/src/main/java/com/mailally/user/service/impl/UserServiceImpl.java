package com.mailally.user.service.impl;

import com.mailally.auth.entity.Auth;
import com.mailally.auth.repository.AuthRepository;
import com.mailally.exception.CustomException;
import com.mailally.organization.entity.Organization;
import com.mailally.organization.repository.OrganizationRepository;
import com.mailally.security.CustomUserDetails;
import com.mailally.user.dto.CreateUserRequestDto;
import com.mailally.user.dto.UpdateUserRequestDto;
import com.mailally.user.dto.UserResponseDto;
import com.mailally.user.entity.User;
import com.mailally.user.mapper.UserMapper;
import com.mailally.user.repository.UserRepository;
import com.mailally.user.service.UserService;
import com.mailally.user.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Production implementation of {@link UserService} enforcing tenant isolation, BCrypt credential management,
 * and role-based permissions.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final OrganizationRepository organizationRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           AuthRepository authRepository,
                           OrganizationRepository organizationRepository,
                           UserValidator userValidator,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.organizationRepository = organizationRepository;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto createUser(CustomUserDetails currentUser, CreateUserRequestDto dto) {
        userValidator.validateAdminRole(currentUser);
        userValidator.validateCreateUser(dto);

        Organization org = organizationRepository.findById(currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("Organization not found"));

        User user = User.builder()
                .organization(org)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail().toLowerCase().trim())
                .role(dto.getRole().toUpperCase())
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

        return userMapper.toUserResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(CustomUserDetails currentUser, Long id) {
        User user = userRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));
        return userMapper.toUserResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAllUsers(CustomUserDetails currentUser, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> usersPage = userRepository.findByOrganizationIdAndIsDeletedFalse(currentUser.getOrganizationId(), pageable);
        return usersPage.map(userMapper::toUserResponseDto);
    }

    @Override
    public UserResponseDto updateUser(CustomUserDetails currentUser, Long id, UpdateUserRequestDto dto) {
        userValidator.validateAdminRole(currentUser);
        userValidator.validateUpdateUser(dto);

        User user = userRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole().toUpperCase());
        user.setStatus(dto.getStatus().toUpperCase());

        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    public UserResponseDto deactivateUser(CustomUserDetails currentUser, Long id) {
        userValidator.validateAdminRole(currentUser);

        User user = userRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));

        user.setStatus("INACTIVE");
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    public UserResponseDto activateUser(CustomUserDetails currentUser, Long id) {
        userValidator.validateAdminRole(currentUser);

        User user = userRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));

        user.setStatus("ACTIVE");
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(updatedUser);
    }

    @Override
    public void softDeleteUser(CustomUserDetails currentUser, Long id) {
        userValidator.validateAdminRole(currentUser);

        User user = userRepository.findByIdAndOrganizationIdAndIsDeletedFalse(id, currentUser.getOrganizationId())
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));

        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsers(CustomUserDetails currentUser, String email, String name, String role, String status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> usersPage = userRepository.searchUsers(
                currentUser.getOrganizationId(),
                (email != null && !email.isBlank()) ? email.trim() : null,
                (name != null && !name.isBlank()) ? name.trim() : null,
                (role != null && !role.isBlank()) ? role.trim().toUpperCase() : null,
                (status != null && !status.isBlank()) ? status.trim().toUpperCase() : null,
                pageable
        );
        return usersPage.map(userMapper::toUserResponseDto);
    }
}
