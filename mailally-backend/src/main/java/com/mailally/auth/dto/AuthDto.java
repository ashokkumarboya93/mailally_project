package com.mailally.auth.dto;

/**
 * Data Transfer Object for Auth.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class AuthDto {

    private String token;
    private String email;
    private String role;
    private Long organizationId;

    public AuthDto() {}

    public AuthDto(String token, String email, String role, Long organizationId) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.organizationId = organizationId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public static AuthDtoBuilder builder() { return new AuthDtoBuilder(); }

    public static class AuthDtoBuilder {
        private String token;
        private String email;
        private String role;
        private Long organizationId;

        AuthDtoBuilder() {}

        public AuthDtoBuilder token(String token) { this.token = token; return this; }
        public AuthDtoBuilder email(String email) { this.email = email; return this; }
        public AuthDtoBuilder role(String role) { this.role = role; return this; }
        public AuthDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }

        public AuthDto build() {
            return new AuthDto(token, email, role, organizationId);
        }
    }
}
