CREATE TABLE IF NOT EXISTS dispatch_event (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id VARCHAR(64) NOT NULL UNIQUE,
    device_id VARCHAR(64) NOT NULL,
    intent VARCHAR(64) NOT NULL,
    payload_json TEXT NOT NULL,
    published TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_dispatch_event_device (device_id, created_at)
);