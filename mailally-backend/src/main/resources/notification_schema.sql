-- ============================================================
-- MailAlly Notification Module Database Schema Script
-- Run this script in MySQL database `mailally`
-- ============================================================

USE mailally;

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL DEFAULT 'CUSTOM',
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    status VARCHAR(20) NOT NULL DEFAULT 'UNREAD',
    channel VARCHAR(30) NOT NULL DEFAULT 'IN_APP',
    source_module VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    reference_id BIGINT,
    action_url VARCHAR(500),
    icon VARCHAR(100),
    color VARCHAR(30),
    is_read BOOLEAN DEFAULT FALSE,
    read_at DATETIME,
    expires_at DATETIME,
    created_by BIGINT,
    updated_by BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_notif_org_user (organization_id, user_id),
    INDEX idx_notif_status (status),
    INDEX idx_notif_type (type),
    INDEX idx_notif_read (is_read),
    INDEX idx_notif_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
