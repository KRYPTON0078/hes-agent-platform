-- V1: core schema for HES Agent Platform
-- Designed for MySQL 8; H2 (MODE=MySQL) is used for local smoke runs.

CREATE TABLE site (
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    site_code       VARCHAR(64)  NOT NULL,
    name            VARCHAR(128) NOT NULL,
    timezone        VARCHAR(64)  NOT NULL DEFAULT 'UTC',
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_site_code UNIQUE (site_code)
);

CREATE TABLE device (
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    device_id       VARCHAR(64)  NOT NULL,
    site_id         BIGINT       NULL,
    model           VARCHAR(64)  NOT NULL,
    firmware_version VARCHAR(32) NULL,
    status          VARCHAR(32)  NOT NULL DEFAULT 'REGISTERED',
    last_seen_at    TIMESTAMP    NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_device_id UNIQUE (device_id),
    CONSTRAINT fk_device_site FOREIGN KEY (site_id) REFERENCES site (id)
);

CREATE INDEX idx_device_status_updated ON device (status, updated_at);

CREATE TABLE device_credential (
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    device_id       BIGINT       NOT NULL,
    api_key_hash    VARCHAR(128) NOT NULL,
    active          BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_device_credential_device UNIQUE (device_id),
    CONSTRAINT fk_credential_device FOREIGN KEY (device_id) REFERENCES device (id)
);

CREATE TABLE telemetry_latest (
    device_id           BIGINT         NOT NULL PRIMARY KEY,
    soc_percent         DECIMAL(5, 2)  NULL,
    battery_kwh         DECIMAL(10, 3) NULL,
    inverter_watts      DECIMAL(12, 2) NULL,
    grid_watts          DECIMAL(12, 2) NULL,
    home_load_watts     DECIMAL(12, 2) NULL,
    battery_voltage     DECIMAL(10, 3) NULL,
    battery_current     DECIMAL(10, 3) NULL,
    fault_code          INT            NULL,
    fault_message       VARCHAR(256)   NULL,
    operating_mode      VARCHAR(32)    NULL,
    reported_at         TIMESTAMP      NOT NULL,
    updated_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_telemetry_latest_device FOREIGN KEY (device_id) REFERENCES device (id)
);

CREATE TABLE telemetry_history (
    id                  BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    device_id           BIGINT         NOT NULL,
    soc_percent         DECIMAL(5, 2)  NULL,
    battery_kwh         DECIMAL(10, 3) NULL,
    inverter_watts      DECIMAL(12, 2) NULL,
    grid_watts          DECIMAL(12, 2) NULL,
    home_load_watts     DECIMAL(12, 2) NULL,
    battery_voltage     DECIMAL(10, 3) NULL,
    battery_current     DECIMAL(10, 3) NULL,
    fault_code          INT            NULL,
    fault_message       VARCHAR(256)   NULL,
    operating_mode      VARCHAR(32)    NULL,
    reported_at         TIMESTAMP      NOT NULL,
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_telemetry_history_device FOREIGN KEY (device_id) REFERENCES device (id)
);

-- Hot path for time-range queries per device (ops charts / investigation)
CREATE INDEX idx_telemetry_device_reported ON telemetry_history (device_id, reported_at);

CREATE TABLE command_record (
    id                  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    command_id          VARCHAR(64)  NOT NULL,
    device_id           BIGINT       NOT NULL,
    command_type        VARCHAR(64)  NOT NULL,
    payload_json        TEXT         NULL,
    status              VARCHAR(32)  NOT NULL DEFAULT 'PENDING',
    idempotency_key     VARCHAR(128) NOT NULL,
    requested_by        VARCHAR(64)  NULL,
    timeout_at          TIMESTAMP    NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_command_id UNIQUE (command_id),
    CONSTRAINT uk_command_idempotency UNIQUE (idempotency_key),
    CONSTRAINT fk_command_device FOREIGN KEY (device_id) REFERENCES device (id)
);

CREATE INDEX idx_command_status_created ON command_record (status, created_at);

CREATE TABLE command_event (
    id                  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    command_id          BIGINT       NOT NULL,
    event_type          VARCHAR(32)  NOT NULL,
    detail_json         TEXT         NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_command_event_command FOREIGN KEY (command_id) REFERENCES command_record (id)
);

CREATE INDEX idx_command_event_command ON command_event (command_id, created_at);

CREATE TABLE alert_record (
    id                  BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    device_id           BIGINT       NOT NULL,
    alert_type          VARCHAR(64)  NOT NULL,
    severity            VARCHAR(16)  NOT NULL,
    message             VARCHAR(512) NOT NULL,
    status              VARCHAR(32)  NOT NULL DEFAULT 'OPEN',
    opened_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at         TIMESTAMP    NULL,
    CONSTRAINT fk_alert_device FOREIGN KEY (device_id) REFERENCES device (id)
);

CREATE INDEX idx_alert_status_opened ON alert_record (status, opened_at);
CREATE INDEX idx_alert_device_type_status ON alert_record (device_id, alert_type, status);
