-- ============================================================
-- MailAlly Enterprise Workflow Schema Migration Script
-- Database: MySQL `mailally`
-- ============================================================

USE mailally;

-- 1. Contact Collections Table
CREATE TABLE IF NOT EXISTS contact_collections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL DEFAULT 'IMPORT', -- IMPORT, MANUAL, SMART, SAVED_SEARCH
    rules_json TEXT,
    color_code VARCHAR(20) DEFAULT '#3B82F6',
    tag VARCHAR(100),
    contact_count INT DEFAULT 0,
    subscribed_count INT DEFAULT 0,
    unsubscribed_count INT DEFAULT 0,
    invalid_count INT DEFAULT 0,
    duplicate_count INT DEFAULT 0,
    source_type VARCHAR(50) DEFAULT 'EXCEL',
    created_by BIGINT,
    updated_by BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_collections_org (organization_id),
    INDEX idx_collections_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Dynamic Field Registry Table
CREATE TABLE IF NOT EXISTS contact_field_registry (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    field_key VARCHAR(100) NOT NULL,
    display_name VARCHAR(150) NOT NULL,
    data_type VARCHAR(50) NOT NULL DEFAULT 'TEXT', -- TEXT, NUMBER, CURRENCY, DATE, EMAIL, URL
    is_filterable BOOLEAN DEFAULT TRUE,
    is_sortable BOOLEAN DEFAULT TRUE,
    is_visible BOOLEAN DEFAULT TRUE,
    default_visible BOOLEAN DEFAULT TRUE,
    order_index INT DEFAULT 0,
    sample_value VARCHAR(255),
    source_batch_id BIGINT,
    created_at DATETIME NOT NULL,
    INDEX idx_field_reg_org (organization_id),
    UNIQUE KEY uk_org_field_key (organization_id, field_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Contact Audit History Table
CREATE TABLE IF NOT EXISTS contact_audit_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contact_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    field_name VARCHAR(100) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    edited_by BIGINT,
    edited_at DATETIME NOT NULL,
    INDEX idx_audit_contact (contact_id),
    INDEX idx_audit_org (organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Campaign Recipients Join Table
CREATE TABLE IF NOT EXISTS campaign_recipients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    contact_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'QUEUED', -- QUEUED, SENDING, DELIVERED, OPENED, CLICKED, SPAM, BOUNCE, COMPLAINT, UNSUBSCRIBED
    queued_at DATETIME NOT NULL,
    sent_at DATETIME,
    delivered_at DATETIME,
    opened_at DATETIME,
    clicked_at DATETIME,
    bounced_at DATETIME,
    complaint_at DATETIME,
    unsubscribed_at DATETIME,
    response_id VARCHAR(255),
    retry_count INT DEFAULT 0,
    failure_reason TEXT,
    INDEX idx_recipients_campaign (campaign_id),
    INDEX idx_recipients_contact (contact_id),
    INDEX idx_recipients_status (status),
    INDEX idx_recipients_org (organization_id),
    UNIQUE KEY uk_campaign_contact (campaign_id, contact_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Campaign Activity Logs Table
CREATE TABLE IF NOT EXISTS campaign_activity_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    severity VARCHAR(20) DEFAULT 'INFO', -- INFO, SUCCESS, WARNING, ERROR
    created_at DATETIME NOT NULL,
    INDEX idx_activity_campaign (campaign_id),
    INDEX idx_activity_org (organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Contacts Table Alterations
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS custom_fields LONGTEXT NULL;
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS collection_id BIGINT NULL;

-- 7. Additional Indexes for Performance
CREATE INDEX idx_contacts_org_collection ON contacts (organization_id, collection_id);
