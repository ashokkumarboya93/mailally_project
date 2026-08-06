-- ============================================================
-- MailAlly Billing Module Database Schema Script
-- Run this script in MySQL database `mailally`
-- ============================================================

USE mailally;

CREATE TABLE IF NOT EXISTS billings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    invoice_number VARCHAR(100) NOT NULL,
    invoice_date DATETIME NOT NULL,
    due_date DATETIME,
    payment_date DATETIME,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    subtotal DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    tax_amount DOUBLE PRECISION DEFAULT 0.0,
    discount_amount DOUBLE PRECISION DEFAULT 0.0,
    total_amount DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    paid_amount DOUBLE PRECISION DEFAULT 0.0,
    balance_amount DOUBLE PRECISION DEFAULT 0.0,
    payment_method VARCHAR(30) NOT NULL DEFAULT 'OFFLINE',
    payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    transaction_reference VARCHAR(255),
    billing_address VARCHAR(500),
    billing_email VARCHAR(255),
    notes TEXT,
    created_by BIGINT,
    updated_by BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    UNIQUE KEY uk_billing_org_invoice (organization_id, invoice_number),
    INDEX idx_billing_org_status (organization_id, payment_status),
    INDEX idx_billing_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
