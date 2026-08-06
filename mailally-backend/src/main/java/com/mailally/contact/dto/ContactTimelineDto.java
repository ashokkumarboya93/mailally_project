package com.mailally.contact.dto;

import java.time.LocalDateTime;

public class ContactTimelineDto {
    private Long id;
    private Long contactId;
    private String eventType;
    private String description;
    private Long performedBy;
    private LocalDateTime createdAt;

    public ContactTimelineDto() {
    }

    public ContactTimelineDto(Long id, Long contactId, String eventType, String description, Long performedBy, LocalDateTime createdAt) {
        this.id = id;
        this.contactId = contactId;
        this.eventType = eventType;
        this.description = description;
        this.performedBy = performedBy;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getContactId() { return contactId; }
    public void setContactId(Long contactId) { this.contactId = contactId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getPerformedBy() { return performedBy; }
    public void setPerformedBy(Long performedBy) { this.performedBy = performedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
