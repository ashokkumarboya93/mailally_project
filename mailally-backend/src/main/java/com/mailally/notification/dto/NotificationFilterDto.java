package com.mailally.notification.dto;

import java.time.LocalDateTime;

/**
 * Filter parameter wrapper DTO for notifications querying.
 */
public class NotificationFilterDto {

    private String type;
    private String priority;
    private String status;
    private String sourceModule;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    public NotificationFilterDto() {}

    public NotificationFilterDto(String type, String priority, String status, String sourceModule,
                                 LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.type = type;
        this.priority = priority;
        this.status = status;
        this.sourceModule = sourceModule;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSourceModule() { return sourceModule; }
    public void setSourceModule(String sourceModule) { this.sourceModule = sourceModule; }
    public LocalDateTime getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDateTime dateFrom) { this.dateFrom = dateFrom; }
    public LocalDateTime getDateTo() { return dateTo; }
    public void setDateTo(LocalDateTime dateTo) { this.dateTo = dateTo; }
}
