-- ============================================================
-- MailAlly Scheduler Module Database Schema Script
-- Run this script in MySQL database `mailally`
-- ============================================================

USE mailally;

CREATE TABLE IF NOT EXISTS schedulers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    campaign_id BIGINT NOT NULL,
    execution_type VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    scheduled_time DATETIME,
    executed_time DATETIME,
    error_message TEXT,
    created_by BIGINT,
    updated_by BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_scheduler_org (organization_id),
    INDEX idx_scheduler_campaign (campaign_id),
    INDEX idx_scheduler_status (status),
    INDEX idx_scheduler_scheduled_time (scheduled_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
