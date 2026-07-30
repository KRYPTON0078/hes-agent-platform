CREATE TABLE IF NOT EXISTS enrollment_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token_hash VARCHAR(128) NOT NULL UNIQUE,
    site_code VARCHAR(64) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    consumed_at TIMESTAMP NULL,
    consumed_by_device VARCHAR(64) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_enrollment_site (site_code),
    KEY idx_enrollment_expires (expires_at)
);