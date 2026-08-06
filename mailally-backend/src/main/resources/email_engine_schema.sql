-- ============================================================
-- MailAlly Email Engine Database Schema Script
-- Run this script in MySQL database `mailally`
-- ============================================================

USE mailally;

-- 1. Emails Table (Email Logs)
CREATE TABLE IF NOT EXISTS emails (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    campaign_id BIGINT,
    recipient_email VARCHAR(255) NOT NULL,
    recipient_name VARCHAR(200),
    subject VARCHAR(500),
    provider VARCHAR(50),
    status VARCHAR(30) NOT NULL DEFAULT 'QUEUED',
    response_id VARCHAR(255),
    error_message TEXT,
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    sent_at DATETIME,
    delivered_at DATETIME,
    opened_at DATETIME,
    clicked_at DATETIME,
    bounced_at DATETIME,
    failed_at DATETIME,
    created_by BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_email_org_campaign (organization_id, campaign_id),
    INDEX idx_email_status (status),
    INDEX idx_email_recipient (recipient_email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Email Queue Table
CREATE TABLE IF NOT EXISTS email_queue (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    campaign_id BIGINT,
    recipient_email VARCHAR(255) NOT NULL,
    recipient_name VARCHAR(200),
    personalized_subject VARCHAR(500),
    personalized_html LONGTEXT,
    provider VARCHAR(50),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    retry_count INT DEFAULT 0,
    max_retries INT DEFAULT 3,
    failure_reason TEXT,
    batch_number INT DEFAULT 0,
    scheduled_at DATETIME,
    processed_at DATETIME,
    created_by BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_queue_org_campaign (organization_id, campaign_id),
    INDEX idx_queue_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Campaign Batches Table (V2)
CREATE TABLE IF NOT EXISTS campaign_batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    worker_node_id VARCHAR(100),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    optimal_size INT,
    retry_count INT DEFAULT 0,
    started_at DATETIME,
    completed_at DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_batch_campaign (campaign_id),
    INDEX idx_batch_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Campaign Recipients Table (V2)
CREATE TABLE IF NOT EXISTS campaign_recipient_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    contact_id BIGINT,
    email VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'QUEUED',
    provider VARCHAR(50),
    attempts INT DEFAULT 0,
    last_error TEXT,
    smtp_response_code VARCHAR(50),
    worker_thread_id VARCHAR(100),
    duration_ms INT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_recipient_campaign (campaign_id),
    INDEX idx_recipient_status (status),
    INDEX idx_recipient_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Email Events Table (V2 Append-Only)
CREATE TABLE IF NOT EXISTS email_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT,
    recipient_id BIGINT,
    event_type VARCHAR(30) NOT NULL,
    provider_message_id VARCHAR(255),
    user_agent VARCHAR(500),
    ip_address VARCHAR(45),
    timestamp DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    INDEX idx_event_campaign (campaign_id),
    INDEX idx_event_type (event_type),
    INDEX idx_event_recipient (recipient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

