-- ============================================================
-- MailAlly Support Modules Schema Script
-- (Subscription, Audit Logs, AI Logs)
-- Run this script in MySQL database `mailally`
-- ============================================================

USE mailally;

-- 1. Subscriptions Table
CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL DEFAULT 'FREE',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    max_contacts INT NOT NULL DEFAULT 1000,
    max_emails_per_month INT NOT NULL DEFAULT 5000,
    max_users INT NOT NULL DEFAULT 2,
    max_campaigns INT NOT NULL DEFAULT 10,
    storage_limit_mb INT NOT NULL DEFAULT 100,
    api_limit INT NOT NULL DEFAULT 1000,
    ai_limit INT NOT NULL DEFAULT 50,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    trial_ends_at DATETIME,
    grace_period_ends_at DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_sub_code (code),
    INDEX idx_sub_org (organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Audit Logs Table
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    module VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    description TEXT,
    ip_address VARCHAR(50),
    browser VARCHAR(255),
    timestamp DATETIME NOT NULL,
    success BOOLEAN DEFAULT TRUE,
    failure_reason TEXT,
    reference_id BIGINT,
    created_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_audit_org_user (organization_id, user_id),
    INDEX idx_audit_module (module),
    INDEX idx_audit_action (action)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. AI Logs Table
CREATE TABLE IF NOT EXISTS ai_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    prompt TEXT NOT NULL,
    prompt_type VARCHAR(50) NOT NULL DEFAULT 'SUBJECT',
    response_content LONGTEXT,
    provider VARCHAR(50) NOT NULL DEFAULT 'MOCK',
    tokens_used INT DEFAULT 0,
    created_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_ai_org_user (organization_id, user_id),
    INDEX idx_ai_type (prompt_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
