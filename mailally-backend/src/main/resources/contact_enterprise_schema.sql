-- ============================================================
-- MailAlly Enterprise Contact Module Schema Script
-- Run this script in MySQL database `mailally`
-- ============================================================

USE mailally;

-- 1. Import Batches Table
CREATE TABLE IF NOT EXISTS import_batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_code VARCHAR(100) NOT NULL,
    batch_name VARCHAR(150),
    original_file_name VARCHAR(255),
    organization_id BIGINT NOT NULL,
    uploaded_by BIGINT,
    import_date DATETIME NOT NULL,
    source_type VARCHAR(50) NOT NULL DEFAULT 'CSV',
    duplicate_strategy VARCHAR(50) NOT NULL DEFAULT 'SKIP',
    total_rows INT DEFAULT 0,
    imported_count INT DEFAULT 0,
    skipped_count INT DEFAULT 0,
    invalid_count INT DEFAULT 0,
    duplicate_count INT DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'COMPLETED',
    duration_ms BIGINT DEFAULT 0,
    created_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_import_batch_org (organization_id),
    INDEX idx_import_batch_code (batch_code),
    INDEX idx_import_batch_date (import_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Import Errors Table (for downloadable invalid_contacts.csv)
CREATE TABLE IF NOT EXISTS import_errors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    import_batch_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    row_num INT NOT NULL,
    raw_email VARCHAR(255),
    raw_record TEXT,
    error_reason VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    INDEX idx_import_error_batch (import_batch_id),
    INDEX idx_import_error_org (organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Normalized Tags Table
CREATE TABLE IF NOT EXISTS tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    color_code VARCHAR(20) DEFAULT '#059669',
    usage_count INT DEFAULT 0,
    created_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_tags_org (organization_id),
    INDEX idx_tags_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Contact Tags Join Table
CREATE TABLE IF NOT EXISTS contact_tags (
    contact_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (contact_id, tag_id),
    INDEX idx_contact_tags_tag (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Contact Timeline Table
CREATE TABLE IF NOT EXISTS contact_timeline (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contact_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    description TEXT,
    performed_by BIGINT,
    created_at DATETIME NOT NULL,
    INDEX idx_timeline_contact (contact_id),
    INDEX idx_timeline_org (organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Saved Filters Table
CREATE TABLE IF NOT EXISTS saved_filters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    filter_json TEXT NOT NULL,
    created_by BIGINT,
    created_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_saved_filters_org (organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Contact Table Column Additions & Indexes
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS import_batch_id BIGINT NULL;
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS import_batch_name VARCHAR(150) NULL;
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS import_date DATETIME NULL;
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS source_type VARCHAR(50) NULL DEFAULT 'MANUAL';
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS email_domain VARCHAR(150) NULL;

CREATE INDEX idx_contacts_email ON contacts (email);
CREATE INDEX idx_contacts_org_status ON contacts (organization_id, status);
CREATE INDEX idx_contacts_org_company ON contacts (organization_id, company);
CREATE INDEX idx_contacts_org_domain ON contacts (organization_id, email_domain);
CREATE INDEX idx_contacts_org_batch ON contacts (organization_id, import_batch_id);
CREATE INDEX idx_contacts_org_created ON contacts (organization_id, created_at);
