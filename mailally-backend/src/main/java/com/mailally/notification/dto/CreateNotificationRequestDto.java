package com.mailally.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Request DTO for creating or dispatching a notification.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateNotificationRequestDto {

    @NotNull(message = "Recipient user ID is required")
    private Long userId;

    @NotBlank(message = "Notification title is required")
    private String title;

    @NotBlank(message = "Notification message is required")
    private String message;

    private String type; // e.g. CAMPAIGN_COMPLETED, EMAIL_FAILED, etc.
    private String priority; // LOW, NORMAL, HIGH, CRITICAL
    private String channel; // IN_APP, EMAIL, SMS, PUSH, SLACK, TEAMS, WHATSAPP, WEBSOCKET
    private String sourceModule; // AUTH, CAMPAIGN, SCHEDULER, etc.
    private Long referenceId;
    private String actionUrl;
    private String icon;
    private String color;
    private LocalDateTime expiresAt;

    public CreateNotificationRequestDto() {}

    public CreateNotificationRequestDto(Long userId, String title, String message, String type, String priority,
                                        String channel, String sourceModule, Long referenceId, String actionUrl,
                                        String icon, String color, LocalDateTime expiresAt) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.priority = priority;
        this.channel = channel;
        this.sourceModule = sourceModule;
        this.referenceId = referenceId;
        this.actionUrl = actionUrl;
        this.icon = icon;
        this.color = color;
        this.expiresAt = expiresAt;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public String getSourceModule() { return sourceModule; }
    public void setSourceModule(String sourceModule) { this.sourceModule = sourceModule; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getActionUrl() { return actionUrl; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
