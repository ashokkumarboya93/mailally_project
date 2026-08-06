package com.mailally.settings.entity;

import com.mailally.organization.entity.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity representing an organization configuration setting entry.
 */
@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Organization organization;

    @Column(name = "category", nullable = false, length = 50)
    private String category; // GENERAL, BRAND, SECURITY, NOTIFICATIONS, DASHBOARD, CAMPAIGN, EMAIL_ENGINE, SCHEDULER, ANALYTICS, API

    @Column(name = "setting_key", nullable = false, length = 100)
    private String settingKey;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String settingValue;

    @Column(name = "data_type", nullable = false, length = 20)
    private String dataType; // STRING, BOOLEAN, INTEGER, DOUBLE, JSON

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "editable")
    private Boolean editable;

    @Column(name = "encrypted")
    private Boolean encrypted;

    @Column(name = "version")
    private Integer version;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Settings() {}

    public Settings(Long id, Organization organization, String category, String settingKey, String settingValue,
                    String dataType, String description, Boolean editable, Boolean encrypted, Integer version,
                    Long createdBy, Long updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted) {
        this.id = id;
        this.organization = organization;
        this.category = category;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.dataType = dataType;
        this.description = description;
        this.editable = editable;
        this.encrypted = encrypted;
        this.version = version;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }
    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getEditable() { return editable; }
    public void setEditable(Boolean editable) { this.editable = editable; }
    public Boolean getEncrypted() { return encrypted; }
    public void setEncrypted(Boolean encrypted) { this.encrypted = encrypted; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
        if (this.category == null) this.category = "GENERAL";
        if (this.dataType == null) this.dataType = "STRING";
        if (this.editable == null) this.editable = true;
        if (this.encrypted == null) this.encrypted = false;
        if (this.version == null) this.version = 1;
        if (this.isDeleted == null) this.isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static SettingsBuilder builder() { return new SettingsBuilder(); }

    public static class SettingsBuilder {
        private Long id;
        private Organization organization;
        private String category;
        private String settingKey;
        private String settingValue;
        private String dataType;
        private String description;
        private Boolean editable;
        private Boolean encrypted;
        private Integer version;
        private Long createdBy;
        private Long updatedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean isDeleted;

        SettingsBuilder() {}

        public SettingsBuilder id(Long id) { this.id = id; return this; }
        public SettingsBuilder organization(Organization organization) { this.organization = organization; return this; }
        public SettingsBuilder category(String category) { this.category = category; return this; }
        public SettingsBuilder settingKey(String settingKey) { this.settingKey = settingKey; return this; }
        public SettingsBuilder settingValue(String settingValue) { this.settingValue = settingValue; return this; }
        public SettingsBuilder dataType(String dataType) { this.dataType = dataType; return this; }
        public SettingsBuilder description(String description) { this.description = description; return this; }
        public SettingsBuilder editable(Boolean editable) { this.editable = editable; return this; }
        public SettingsBuilder encrypted(Boolean encrypted) { this.encrypted = encrypted; return this; }
        public SettingsBuilder version(Integer version) { this.version = version; return this; }
        public SettingsBuilder createdBy(Long createdBy) { this.createdBy = createdBy; return this; }
        public SettingsBuilder updatedBy(Long updatedBy) { this.updatedBy = updatedBy; return this; }
        public SettingsBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SettingsBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public SettingsBuilder isDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Settings build() {
            return new Settings(id, organization, category, settingKey, settingValue, dataType, description,
                    editable, encrypted, version, createdBy, updatedBy, createdAt, updatedAt, isDeleted);
        }
    }
}
