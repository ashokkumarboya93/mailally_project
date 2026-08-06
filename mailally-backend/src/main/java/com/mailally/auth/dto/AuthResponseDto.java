package com.mailally.auth.dto;

/**
 * Response DTO returned upon successful login containing the JWT token.
 */
public class AuthResponseDto {

    private String token;
    private String tokenType;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Long organizationId;
    private String organizationName;

    public AuthResponseDto() {
    }

    public AuthResponseDto(String token, String tokenType, Long userId, String email, String firstName,
                           String lastName, String role, Long organizationId, String organizationName) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public static AuthResponseDtoBuilder builder() {
        return new AuthResponseDtoBuilder();
    }

    public static class AuthResponseDtoBuilder {
        private String token;
        private String tokenType;
        private Long userId;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private Long organizationId;
        private String organizationName;

        AuthResponseDtoBuilder() {
        }

        public AuthResponseDtoBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponseDtoBuilder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public AuthResponseDtoBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AuthResponseDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthResponseDtoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AuthResponseDtoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AuthResponseDtoBuilder role(String role) {
            this.role = role;
            return this;
        }

        public AuthResponseDtoBuilder organizationId(Long organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public AuthResponseDtoBuilder organizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public AuthResponseDto build() {
            return new AuthResponseDto(token, tokenType, userId, email, firstName, lastName, role, organizationId, organizationName);
        }
    }
}
