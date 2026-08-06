package com.mailally.auth.entity;

import com.mailally.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity representing security credentials for a user in auth_credentials table.
 */
@Entity
@Table(name = "auth_credentials")
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "mfa_secret", length = 255)
    private String mfaSecret;

    @Column(name = "mfa_enabled", nullable = false)
    private Boolean mfaEnabled;

    @Column(name = "password_reset_token", length = 255)
    private String passwordResetToken;

    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Auth() {
    }

    public Auth(Long id, User user, String passwordHash, String mfaSecret, Boolean mfaEnabled,
                String passwordResetToken, LocalDateTime tokenExpiresAt, LocalDateTime lastLoginAt,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.passwordHash = passwordHash;
        this.mfaSecret = mfaSecret;
        this.mfaEnabled = mfaEnabled;
        this.passwordResetToken = passwordResetToken;
        this.tokenExpiresAt = tokenExpiresAt;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getResetToken() {
        return passwordResetToken;
    }

    public void setResetToken(String resetToken) {
        this.passwordResetToken = resetToken;
    }

    public LocalDateTime getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    public void setTokenExpiresAt(LocalDateTime tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }

    public LocalDateTime getResetTokenExpiry() {
        return tokenExpiresAt;
    }

    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
        this.tokenExpiresAt = resetTokenExpiry;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.mfaEnabled == null) {
            this.mfaEnabled = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static AuthBuilder builder() {
        return new AuthBuilder();
    }

    public static class AuthBuilder {
        private Long id;
        private User user;
        private String passwordHash;
        private String mfaSecret;
        private Boolean mfaEnabled;
        private String passwordResetToken;
        private LocalDateTime tokenExpiresAt;
        private LocalDateTime lastLoginAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        AuthBuilder() {
        }

        public AuthBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AuthBuilder user(User user) {
            this.user = user;
            return this;
        }

        public AuthBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public AuthBuilder mfaSecret(String mfaSecret) {
            this.mfaSecret = mfaSecret;
            return this;
        }

        public AuthBuilder mfaEnabled(Boolean mfaEnabled) {
            this.mfaEnabled = mfaEnabled;
            return this;
        }

        public AuthBuilder passwordResetToken(String passwordResetToken) {
            this.passwordResetToken = passwordResetToken;
            return this;
        }

        public AuthBuilder tokenExpiresAt(LocalDateTime tokenExpiresAt) {
            this.tokenExpiresAt = tokenExpiresAt;
            return this;
        }

        public AuthBuilder lastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public AuthBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AuthBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Auth build() {
            return new Auth(id, user, passwordHash, mfaSecret, mfaEnabled, passwordResetToken, tokenExpiresAt, lastLoginAt, createdAt, updatedAt);
        }
    }
}
