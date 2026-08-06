package com.mailally.security;

import com.mailally.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom UserDetails implementation holding authenticated User principal data.
 */
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String email;
    private final String passwordHash;
    private final Long organizationId;
    private final String role;
    private final boolean active;

    public CustomUserDetails(User user, String passwordHash) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.passwordHash = passwordHash;
        this.organizationId = user.getOrganization() != null ? user.getOrganization().getId() : null;
        this.role = user.getRole();
        this.active = "ACTIVE".equalsIgnoreCase(user.getStatus()) && !Boolean.TRUE.equals(user.getIsDeleted());
    }

    public CustomUserDetails(Long userId, String email, String passwordHash, Long organizationId, String role, boolean active) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.organizationId = organizationId;
        this.role = role != null ? role : "ADMIN";
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
