-- V7: Energy charge schedules
CREATE TABLE charge_schedule (
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    schedule_code   VARCHAR(64)  NOT NULL,
    device_id       VARCHAR(64)  NOT NULL,
    name            VARCHAR(128) NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
    timezone        VARCHAR(64)  NOT NULL DEFAULT 'UTC',
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_charge_schedule_code UNIQUE (schedule_code)
);

CREATE INDEX idx_charge_schedule_device ON charge_schedule (device_id, enabled);
