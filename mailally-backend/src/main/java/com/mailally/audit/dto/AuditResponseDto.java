package com.mailally.audit.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing an audit log entry.
 */
public class AuditResponseDto {

    private Long id;
    private Long organizationId;
    private Long userId;
    private String userEmail;
    private String action;
    private String module;
    private String description;
    private String ipAddress;
    private String browser;
    private LocalDateTime timestamp;
    private Boolean success;
    private String failureReason;
    private Long referenceId;

    public AuditResponseDto() {}

    public AuditResponseDto(Long id, Long organizationId, Long userId, String userEmail, String action,
                            String module, String description, String ipAddress, String browser,
                            LocalDateTime timestamp, Boolean success, String failureReason, Long referenceId) {
        this.id = id;
        this.organizationId = organizationId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.action = action;
        this.module = module;
        this.description = description;
        this.ipAddress = ipAddress;
        this.browser = browser;
        this.timestamp = timestamp;
        this.success = success;
        this.failureReason = failureReason;
        this.referenceId = referenceId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    public static AuditResponseDtoBuilder builder() { return new AuditResponseDtoBuilder(); }

    public static class AuditResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private Long userId;
        private String userEmail;
        private String action;
        private String module;
        private String description;
        private String ipAddress;
        private String browser;
        private LocalDateTime timestamp;
        private Boolean success;
        private String failureReason;
        private Long referenceId;

        AuditResponseDtoBuilder() {}

        public AuditResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public AuditResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public AuditResponseDtoBuilder userId(Long userId) { this.userId = userId; return this; }
        public AuditResponseDtoBuilder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public AuditResponseDtoBuilder action(String action) { this.action = action; return this; }
        public AuditResponseDtoBuilder module(String module) { this.module = module; return this; }
        public AuditResponseDtoBuilder description(String description) { this.description = description; return this; }
        public AuditResponseDtoBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AuditResponseDtoBuilder browser(String browser) { this.browser = browser; return this; }
        public AuditResponseDtoBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AuditResponseDtoBuilder success(Boolean success) { this.success = success; return this; }
        public AuditResponseDtoBuilder failureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public AuditResponseDtoBuilder referenceId(Long referenceId) { this.referenceId = referenceId; return this; }

        public AuditResponseDto build() {
            return new AuditResponseDto(id, organizationId, userId, userEmail, action, module, description,
                    ipAddress, browser, timestamp, success, failureReason, referenceId);
        }
    }
}
