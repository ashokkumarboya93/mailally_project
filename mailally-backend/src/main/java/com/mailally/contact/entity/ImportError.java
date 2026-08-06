package com.mailally.contact.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_errors")
public class ImportError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "import_batch_id", nullable = false)
    private Long importBatchId;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "row_num", nullable = false)
    private Integer rowNumber;

    @Column(name = "raw_email", length = 255)
    private String rawEmail;

    @Column(name = "raw_record", columnDefinition = "TEXT")
    private String rawRecord;

    @Column(name = "error_reason", nullable = false, length = 255)
    private String errorReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ImportError() {
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

    public Long getImportBatchId() {
        return importBatchId;
    }

    public void setImportBatchId(Long importBatchId) {
        this.importBatchId = importBatchId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getRawEmail() {
        return rawEmail;
    }

    public void setRawEmail(String rawEmail) {
        this.rawEmail = rawEmail;
    }

    public String getRawRecord() {
        return rawRecord;
    }

    public void setRawRecord(String rawRecord) {
        this.rawRecord = rawRecord;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
