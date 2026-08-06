package com.mailally.contact.entity;

import com.mailally.organization.entity.Organization;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_audit_history")
public class ContactAuditHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact_id", nullable = false)
    private Long contactId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "field_name", nullable = false, length = 100)
    private String fieldName;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "edited_by")
    private Long editedBy;

    @Column(name = "edited_at", nullable = false, updatable = false)
    private LocalDateTime editedAt;

    public ContactAuditHistory() {}

    @PrePersist
    protected void onCreate() {
        this.editedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getContactId() { return contactId; }
    public void setContactId(Long contactId) { this.contactId = contactId; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

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
