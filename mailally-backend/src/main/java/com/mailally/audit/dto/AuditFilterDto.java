package com.mailally.audit.dto;

import java.time.LocalDateTime;

/**
 * Filter parameters DTO for audit log queries.
 */
public class AuditFilterDto {

    private String module;
    private String action;
    private Boolean success;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    public AuditFilterDto() {}

    public AuditFilterDto(String module, String action, Boolean success, LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.module = module;
        this.action = action;
        this.success = success;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public LocalDateTime getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDateTime dateFrom) { this.dateFrom = dateFrom; }
    public LocalDateTime getDateTo() { return dateTo; }
    public void setDateTo(LocalDateTime dateTo) { this.dateTo = dateTo; }
}
