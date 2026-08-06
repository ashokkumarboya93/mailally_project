package com.mailally.dashboard.dto;

/**
 * Metadata for UI Quick Action buttons (Create Campaign, Import Contacts, Schedule, etc.).
 */
public class DashboardQuickActionDto {

    private String id;
    private String title;
    private String description;
    private String targetRoute;
    private String icon;
    private String requiredRole;

    public DashboardQuickActionDto() {}

    public DashboardQuickActionDto(String id, String title, String description, String targetRoute, String icon, String requiredRole) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.targetRoute = targetRoute;
        this.icon = icon;
        this.requiredRole = requiredRole;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTargetRoute() { return targetRoute; }
    public void setTargetRoute(String targetRoute) { this.targetRoute = targetRoute; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getRequiredRole() { return requiredRole; }
    public void setRequiredRole(String requiredRole) { this.requiredRole = requiredRole; }
}
