package com.mailally.dashboard.dto;

import java.time.LocalDateTime;

/**
 * DTO representing an individual item in the real-time Dashboard Activity stream.
 */
public class DashboardActivityDto {

    private String activityType; // CAMPAIGN, CONTACT, TEMPLATE, SCHEDULER, SYSTEM
    private String title;
    private String description;
    private String status;
    private LocalDateTime timestamp;

    public DashboardActivityDto() {}

    public DashboardActivityDto(String activityType, String title, String description, String status, LocalDateTime timestamp) {
        this.activityType = activityType;
        this.title = title;
        this.description = description;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
