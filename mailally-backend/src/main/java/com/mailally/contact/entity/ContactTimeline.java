package com.mailally.contact.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_timeline")
public class ContactTimeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact_id", nullable = false)
    private Long contactId;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "performed_by")
    private Long performedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ContactTimeline() {
    }

    public ContactTimeline(Long contactId, Long organizationId, String eventType, String description, Long performedBy) {
        this.contactId = contactId;
        this.organizationId = organizationId;
        this.eventType = eventType;
        this.description = description;
        this.performedBy = performedBy;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
