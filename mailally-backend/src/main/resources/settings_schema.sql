-- ============================================================
-- MailAlly Settings Module Database Schema Script
-- Run this script in MySQL database `mailally`
-- ============================================================

USE mailally;

CREATE TABLE IF NOT EXISTS settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL DEFAULT 'GENERAL',
    setting_key VARCHAR(100) NOT NULL,
    setting_value TEXT,
    data_type VARCHAR(20) NOT NULL DEFAULT 'STRING',
    description VARCHAR(255),
    editable BOOLEAN DEFAULT TRUE,
    encrypted BOOLEAN DEFAULT FALSE,
    version INT DEFAULT 1,
    created_by BIGINT,
    updated_by BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    UNIQUE KEY uk_setting_org_cat_key (organization_id, category, setting_key),
    INDEX idx_settings_org_cat (organization_id, category),
    INDEX idx_settings_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
