CREATE TABLE IF NOT EXISTS energy_forecast (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id VARCHAR(64) NOT NULL,
    forecast_hour TIMESTAMP NOT NULL,
    predicted_soc DECIMAL(5,2) NOT NULL,
    predicted_load_kw DECIMAL(10,3) NOT NULL,
    model_version VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_forecast (device_id, forecast_hour, model_version),
    KEY idx_forecast_hour (forecast_hour)
);