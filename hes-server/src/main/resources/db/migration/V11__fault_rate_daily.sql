CREATE TABLE IF NOT EXISTS fault_rate_daily (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id VARCHAR(64) NOT NULL,
    day_bucket DATE NOT NULL,
    fault_count INT NOT NULL DEFAULT 0,
    telemetry_count INT NOT NULL DEFAULT 0,
    fault_rate DECIMAL(8,6) NOT NULL DEFAULT 0,
    UNIQUE KEY uk_device_day (device_id, day_bucket),
    KEY idx_fault_rate (fault_rate)
);