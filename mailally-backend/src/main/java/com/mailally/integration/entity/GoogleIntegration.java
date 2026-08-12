package com.mailally.integration.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "google_integrations", indexes = {
        @Index(name = "idx_google_org_provider", columnList = "organization_id, provider")
})
@Getter
@Setter
@NoArgsConstructor
public class GoogleIntegration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "connected_by_user_id")
    private Long connectedByUserId;

    @Column(name = "provider", nullable = false)
    private String provider = "GOOGLE";

    @Column(name = "account_email")
    private String accountEmail;

    @Column(name = "status", nullable = false)
    private String status = "NOT_CONNECTED"; // NOT_CONNECTED, CONNECTED, REVOKED, ERROR

    @Column(name = "encrypted_access_token", columnDefinition = "TEXT")
    private String encryptedAccessToken;

    @Column(name = "encrypted_refresh_token", columnDefinition = "TEXT")
    private String encryptedRefreshToken;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    @Column(name = "scopes", columnDefinition = "TEXT")
    private String scopes;

    @Column(name = "connected_at")
    private LocalDateTime connectedAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "disconnected_at")
    private LocalDateTime disconnectedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
