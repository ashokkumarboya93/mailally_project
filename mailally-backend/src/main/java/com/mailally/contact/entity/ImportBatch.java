package com.mailally.contact.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_batches")
public class ImportBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_code", nullable = false, length = 100)
    private String batchCode;

    @Column(name = "batch_name", length = 150)
    private String batchName;

    @Column(name = "original_file_name", length = 255)
    private String originalFileName;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "uploaded_by", nullable = false)
    private Long uploadedBy;

    @Column(name = "import_date", nullable = false)
    private LocalDateTime importDate;

    @Column(name = "source_type", nullable = false, length = 50)
    private String sourceType;

    @Column(name = "duplicate_strategy", nullable = false, length = 50)
    private String duplicateStrategy;

    @Column(name = "total_rows", nullable = false)
    private Integer totalRows = 0;

    @Column(name = "imported_count", nullable = false)
    private Integer importedCount = 0;

    @Column(name = "skipped_count", nullable = false)
    private Integer skippedCount = 0;

    @Column(name = "invalid_count", nullable = false)
    private Integer invalidCount = 0;

    @Column(name = "duplicate_count", nullable = false)
    private Integer duplicateCount = 0;

    @Column(name = "status", nullable = false, length = 50)
    private String status = "PROCESSING";

    @Column(name = "duration_ms")
    private Long durationMs = 0L;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ImportBatch() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.importDate == null) {
            this.importDate = LocalDateTime.now();
        }
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

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getDuplicateStrategy() {
        return duplicateStrategy;
    }

    public void setDuplicateStrategy(String duplicateStrategy) {
        this.duplicateStrategy = duplicateStrategy;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(Integer importedCount) {
        this.importedCount = importedCount;
    }

    public Integer getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(Integer skippedCount) {
        this.skippedCount = skippedCount;
    }

    public Integer getInvalidCount() {
        return invalidCount;
    }

    public void setInvalidCount(Integer invalidCount) {
        this.invalidCount = invalidCount;
    }

    public Integer getDuplicateCount() {
        return duplicateCount;
    }

    public void setDuplicateCount(Integer duplicateCount) {
        this.duplicateCount = duplicateCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
