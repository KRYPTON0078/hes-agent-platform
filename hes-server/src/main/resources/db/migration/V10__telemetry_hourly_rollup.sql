CREATE TABLE IF NOT EXISTS telemetry_hourly_rollup (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id VARCHAR(64) NOT NULL,
    hour_bucket TIMESTAMP NOT NULL,
    avg_soc DECIMAL(5,2) NOT NULL,
    min_soc DECIMAL(5,2) NOT NULL,
    max_soc DECIMAL(5,2) NOT NULL,
    energy_in_kwh DECIMAL(12,4) NOT NULL DEFAULT 0,
    energy_out_kwh DECIMAL(12,4) NOT NULL DEFAULT 0,
    sample_count INT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_device_hour (device_id, hour_bucket),
    KEY idx_hour_bucket (hour_bucket)
);