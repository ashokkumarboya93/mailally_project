package com.mailally.security;

import com.mailally.auth.entity.Auth;
import com.mailally.auth.repository.AuthRepository;
import com.mailally.user.entity.User;
import com.mailally.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom UserDetailsService implementation for Spring Security authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    public CustomUserDetailsService(UserRepository userRepository, AuthRepository authRepository) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Auth auth = authRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Auth credentials not found for user: " + email));

        return new CustomUserDetails(user, auth.getPasswordHash());
    }
}
