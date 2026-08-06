package com.mailally.notification.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Notification.
 * Provides explicit getters, setters, constructors, and builder pattern.
 */
public class NotificationDto {

    private Long id;
    private Long organizationId;
    private String title;
    private String message;
    private String type;
    private String priority;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDto() {}

    public NotificationDto(Long id, Long organizationId, String title, String message, String type, String priority, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.priority = priority;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static NotificationDtoBuilder builder() { return new NotificationDtoBuilder(); }

    public static class NotificationDtoBuilder {
        private Long id;
        private Long organizationId;
        private String title;
        private String message;
        private String type;
        private String priority;
        private Boolean isRead;
        private LocalDateTime createdAt;

        NotificationDtoBuilder() {}

        public NotificationDtoBuilder id(Long id) { this.id = id; return this; }
        public NotificationDtoBuilder organizationId(Long organizationId) { this.organizationId = organizationId; return this; }
        public NotificationDtoBuilder title(String title) { this.title = title; return this; }
        public NotificationDtoBuilder message(String message) { this.message = message; return this; }
        public NotificationDtoBuilder type(String type) { this.type = type; return this; }
        public NotificationDtoBuilder priority(String priority) { this.priority = priority; return this; }
        public NotificationDtoBuilder isRead(Boolean isRead) { this.isRead = isRead; return this; }
        public NotificationDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public NotificationDto build() {
            return new NotificationDto(id, organizationId, title, message, type, priority, isRead, createdAt);
        }
    }
}
