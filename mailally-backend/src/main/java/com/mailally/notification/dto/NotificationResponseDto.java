package com.mailally.notification.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a Notification entity.
 */
public class NotificationResponseDto {

    private Long id;
    private Long organizationId;
    private Long userId;
    private String title;
    private String message;
    private String type;
    private String priority;
    private String status;
    private String channel;
    private String sourceModule;
    private Long referenceId;
    private String actionUrl;
    private String icon;
    private String color;
    private Boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public NotificationResponseDto() {}

    public NotificationResponseDto(Long id, Long organizationId, Long userId, String title, String message,
                                   String type, String priority, String status, String channel, String sourceModule,
                                   Long referenceId, String actionUrl, String icon, String color, Boolean isRead,
                                   LocalDateTime readAt, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.priority = priority;
        this.status = status;
        this.channel = channel;
        this.sourceModule = sourceModule;
        this.referenceId = referenceId;
        this.actionUrl = actionUrl;
        this.icon = icon;
        this.color = color;
        this.isRead = isRead;
        this.readAt = readAt;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
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
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static NotificationResponseDtoBuilder builder() { return new NotificationResponseDtoBuilder(); }

    public static class NotificationResponseDtoBuilder {
        private Long id;
        private Long organizationId;
        private Long userId;
        private String title;
        private String message;
        private String type;
        private String priority;
        private String status;
        private String channel;
        private String sourceModule;
        private Long referenceId;
        private String actionUrl;
        private String icon;
        private String color;
        private Boolean isRead;
        private LocalDateTime readAt;
        private LocalDateTime expiresAt;
        private LocalDateTime createdAt;

        NotificationResponseDtoBuilder() {}

        public NotificationResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public NotificationResponseDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public NotificationResponseDtoBuilder userId(Long userId) { this.userId = userId; return this; }
        public NotificationResponseDtoBuilder title(String title) { this.title = title; return this; }
        public NotificationResponseDtoBuilder message(String message) { this.message = message; return this; }
        public NotificationResponseDtoBuilder type(String type) { this.type = type; return this; }
        public NotificationResponseDtoBuilder priority(String priority) { this.priority = priority; return this; }
        public NotificationResponseDtoBuilder status(String status) { this.status = status; return this; }
        public NotificationResponseDtoBuilder channel(String channel) { this.channel = channel; return this; }
        public NotificationResponseDtoBuilder sourceModule(String sourceModule) { this.sourceModule = sourceModule; return this; }
        public NotificationResponseDtoBuilder referenceId(Long referenceId) { this.referenceId = referenceId; return this; }
        public NotificationResponseDtoBuilder actionUrl(String actionUrl) { this.actionUrl = actionUrl; return this; }
        public NotificationResponseDtoBuilder icon(String icon) { this.icon = icon; return this; }
        public NotificationResponseDtoBuilder color(String color) { this.color = color; return this; }
        public NotificationResponseDtoBuilder isRead(Boolean isRead) { this.isRead = isRead; return this; }
        public NotificationResponseDtoBuilder readAt(LocalDateTime readAt) { this.readAt = readAt; return this; }
        public NotificationResponseDtoBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public NotificationResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public NotificationResponseDto build() {
            return new NotificationResponseDto(id, organizationId, userId, title, message, type, priority, status,
                    channel, sourceModule, referenceId, actionUrl, icon, color, isRead, readAt, expiresAt, createdAt);
        }
    }
}
