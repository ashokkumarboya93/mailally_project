package com.mailally.integration.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailally.audit.service.AuditService;
import com.mailally.exception.CustomException;
import com.mailally.integration.dto.GoogleIntegrationStatusDto;
import com.mailally.integration.entity.GoogleIntegration;
import com.mailally.integration.repository.GoogleIntegrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GoogleOAuthService {

    private static final Logger log = LoggerFactory.getLogger(GoogleOAuthService.class);

    private static final String GOOGLE_AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_ENDPOINT = "https://www.googleapis.com/oauth2/v2/userinfo";
    private static final String GOOGLE_REVOKE_ENDPOINT = "https://oauth2.googleapis.com/revoke";

    private static final List<String> SCOPES = List.of(
            "https://www.googleapis.com/auth/drive.readonly",
            "https://www.googleapis.com/auth/drive.file",
            "https://www.googleapis.com/auth/spreadsheets.readonly",
            "https://www.googleapis.com/auth/userinfo.email"
    );

    @Value("${google.oauth.client-id:}")
    private String clientId;

    @Value("${google.oauth.client-secret:}")
    private String clientSecret;

    @Value("${google.oauth.redirect-uri:http://localhost:8081/api/integrations/google/callback}")
    private String redirectUri;

    private final GoogleIntegrationRepository integrationRepository;
    private final TokenEncryptionService encryptionService;
    private final OAuthStateCache stateCache;
    private final AuditService auditService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleOAuthService(
            GoogleIntegrationRepository integrationRepository,
            TokenEncryptionService encryptionService,
            OAuthStateCache stateCache,
            AuditService auditService) {
        this.integrationRepository = integrationRepository;
        this.encryptionService = encryptionService;
        this.stateCache = stateCache;
        this.auditService = auditService;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String generateAuthorizationUrl(Long orgId, Long userId) {
        if (clientId == null || clientId.isBlank()) {
            throw new CustomException("Google Client ID is not configured in environment (GOOGLE_CLIENT_ID).");
        }

        String state = stateCache.generateState(userId, orgId);
        String scopeString = String.join(" ", SCOPES);

        return UriComponentsBuilder.fromUriString(GOOGLE_AUTH_ENDPOINT)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scopeString)
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .queryParam("include_granted_scopes", "true")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    public GoogleIntegration handleCallback(String code, String state) {
        OAuthStateCache.StateData stateData = stateCache.consumeState(state);
        if (stateData == null) {
            throw new CustomException("Invalid or expired OAuth state parameter. Please click Connect Google again.");
        }

        Long orgId = stateData.getOrganizationId();
        Long userId = stateData.getUserId();

        log.info("Processing Google OAuth callback for orgId: {}, userId: {}", orgId, userId);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_ENDPOINT, request, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());

            String accessToken = root.path("access_token").asText();
            String refreshToken = root.path("refresh_token").asText(null);
            int expiresIn = root.path("expires_in").asInt(3600);

            // Fetch Google account email
            String email = fetchGoogleUserEmail(accessToken);

            GoogleIntegration integration = integrationRepository.findByOrganizationIdAndProvider(orgId, "GOOGLE")
                    .orElseGet(() -> {
                        GoogleIntegration gi = new GoogleIntegration();
                        gi.setOrganizationId(orgId);
                        gi.setProvider("GOOGLE");
                        return gi;
                    });

            integration.setConnectedByUserId(userId);
            integration.setAccountEmail(email);
            integration.setStatus("CONNECTED");
            integration.setEncryptedAccessToken(encryptionService.encrypt(accessToken));
            if (refreshToken != null && !refreshToken.isBlank()) {
                integration.setEncryptedRefreshToken(encryptionService.encrypt(refreshToken));
            }
            integration.setTokenExpiry(LocalDateTime.now().plusSeconds(expiresIn));
            integration.setScopes(String.join(" ", SCOPES));
            integration.setConnectedAt(LocalDateTime.now());
            integration.setLastUsedAt(LocalDateTime.now());

            integration = integrationRepository.save(integration);
            auditService.logEventInternal(orgId, userId, "GOOGLE_CONNECTED", "INTEGRATION", "Connected Google Account: " + email, true);

            return integration;

        } catch (Exception e) {
            log.error("Google OAuth token exchange failed for orgId {}: {}", orgId, e.getMessage());
            throw new CustomException("Google OAuth authentication failed: " + e.getMessage());
        }
    }

    public String getValidAccessToken(GoogleIntegration integration) {
        if (!"CONNECTED".equalsIgnoreCase(integration.getStatus())) {
            throw new CustomException("Google integration is not connected for this organization. Status: " + integration.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        if (integration.getTokenExpiry() != null && now.isBefore(integration.getTokenExpiry().minusMinutes(2))) {
            return encryptionService.decrypt(integration.getEncryptedAccessToken());
        }

        // Access token expired, use refresh token
        log.info("Refreshing short-lived Google access token for organizationId: {}", integration.getOrganizationId());
        String refreshToken = encryptionService.decrypt(integration.getEncryptedRefreshToken());

        if (refreshToken == null || refreshToken.isBlank()) {
            integration.setStatus("REVOKED");
            integrationRepository.save(integration);
            throw new CustomException("Google authorization refresh token is missing. Please reconnect your Google account.");
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_ENDPOINT, new HttpEntity<>(body, headers), String.class);
            JsonNode root = objectMapper.readTree(response.getBody());

            String newAccessToken = root.path("access_token").asText();
            int expiresIn = root.path("expires_in").asInt(3600);

            integration.setEncryptedAccessToken(encryptionService.encrypt(newAccessToken));
            integration.setTokenExpiry(LocalDateTime.now().plusSeconds(expiresIn));
            integration.setLastUsedAt(LocalDateTime.now());
            integrationRepository.save(integration);

            return newAccessToken;

        } catch (Exception e) {
            log.warn("Google refresh token exchange failed for orgId {}. Marking REVOKED: {}", integration.getOrganizationId(), e.getMessage());
            integration.setStatus("REVOKED");
            integrationRepository.save(integration);
            auditService.logEventInternal(integration.getOrganizationId(), integration.getConnectedByUserId(), "GOOGLE_REVOKED", "INTEGRATION", "Google authorization was revoked or expired", false);
            throw new CustomException("Google authorization was revoked or expired. Please reconnect your Google account.");
        }
    }

    public void disconnect(Long orgId, Long userId) {
        Optional<GoogleIntegration> opt = integrationRepository.findByOrganizationIdAndProvider(orgId, "GOOGLE");
        if (opt.isPresent()) {
            GoogleIntegration integration = opt.get();
            try {
                String tokenToRevoke = encryptionService.decrypt(integration.getEncryptedRefreshToken());
                if (tokenToRevoke == null) {
                    tokenToRevoke = encryptionService.decrypt(integration.getEncryptedAccessToken());
                }
                if (tokenToRevoke != null) {
                    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                    body.add("token", tokenToRevoke);
                    restTemplate.postForEntity(GOOGLE_REVOKE_ENDPOINT, body, String.class);
                }
            } catch (Exception e) {
                log.warn("Failed to revoke token on Google server during disconnect: {}", e.getMessage());
            }

            integration.setStatus("NOT_CONNECTED");
            integration.setEncryptedAccessToken(null);
            integration.setEncryptedRefreshToken(null);
            integration.setDisconnectedAt(LocalDateTime.now());
            integrationRepository.save(integration);

            auditService.logEventInternal(orgId, userId, "GOOGLE_DISCONNECTED", "INTEGRATION", "Disconnected Google Account: " + integration.getAccountEmail(), true);
        }
    }

    public GoogleIntegrationStatusDto getStatus(Long orgId) {
        return integrationRepository.findByOrganizationIdAndProvider(orgId, "GOOGLE")
                .map(i -> GoogleIntegrationStatusDto.builder()
                        .status(i.getStatus())
                        .accountEmail(i.getAccountEmail())
                        .connectedByUserId(i.getConnectedByUserId())
                        .connectedAt(i.getConnectedAt())
                        .lastUsedAt(i.getLastUsedAt())
                        .build())
                .orElseGet(() -> GoogleIntegrationStatusDto.builder()
                        .status("NOT_CONNECTED")
                        .build());
    }

    private String fetchGoogleUserEmail(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("email").asText("connected-google-user@gmail.com");
        } catch (Exception e) {
            log.warn("Failed to fetch Google user email: {}", e.getMessage());
            return "connected-google-user@gmail.com";
        }
    }
}
