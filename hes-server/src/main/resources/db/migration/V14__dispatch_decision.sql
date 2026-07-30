CREATE TABLE IF NOT EXISTS dispatch_decision (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    policy_id BIGINT NOT NULL,
    device_id VARCHAR(64) NOT NULL,
    decided_action VARCHAR(32) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    signal_snapshot_json TEXT NULL,
    decided_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_dispatch_device_time (device_id, decided_at),
    KEY idx_dispatch_policy (policy_id)
);