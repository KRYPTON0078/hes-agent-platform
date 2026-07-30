-- V9: Schedule execution audit
CREATE TABLE schedule_execution (
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    schedule_id     BIGINT       NOT NULL,
    window_id       BIGINT       NULL,
    device_id       VARCHAR(64)  NOT NULL,
    decided_mode    VARCHAR(32)  NOT NULL,
    reason          VARCHAR(256) NOT NULL,
    executed_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedule_exec_schedule FOREIGN KEY (schedule_id) REFERENCES charge_schedule (id)
);

CREATE INDEX idx_schedule_exec_device_time ON schedule_execution (device_id, executed_at);
