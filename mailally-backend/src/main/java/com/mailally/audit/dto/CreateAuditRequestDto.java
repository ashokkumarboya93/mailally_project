package com.mailally.audit.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for creating an audit log entry.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAuditRequestDto {

    @NotBlank(message = "Action name is required")
    private String action;

    @NotBlank(message = "Module name is required")
    private String module;

    private String description;
    private String ipAddress;
    private String browser;
    private Boolean success;
    private String failureReason;
    private Long referenceId;

    public CreateAuditRequestDto() {}

    public CreateAuditRequestDto(String action, String module, String description, String ipAddress,
                                 String browser, Boolean success, String failureReason, Long referenceId) {
        this.action = action;
        this.module = module;
        this.description = description;
        this.ipAddress = ipAddress;
        this.browser = browser;
        this.success = success;
        this.failureReason = failureReason;
        this.referenceId = referenceId;
    }

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
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
}
