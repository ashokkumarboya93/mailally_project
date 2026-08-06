package com.mailally.contact.dto;

import java.time.LocalDateTime;

public class ContactAuditHistoryDto {
    private Long id;
    private Long contactId;
    private Long organizationId;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private Long editedBy;
    private LocalDateTime editedAt;

    public ContactAuditHistoryDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getContactId() { return contactId; }
    public void setContactId(Long contactId) { this.contactId = contactId; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }

    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }

    public Long getEditedBy() { return editedBy; }
    public void setEditedBy(Long editedBy) { this.editedBy = editedBy; }

    public LocalDateTime getEditedAt() { return editedAt; }
    public void setEditedAt(LocalDateTime editedAt) { this.editedAt = editedAt; }
}
