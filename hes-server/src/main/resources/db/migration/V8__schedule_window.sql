-- V8: Schedule windows (TOU / SOC constraints)
CREATE TABLE schedule_window (
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    schedule_id     BIGINT       NOT NULL,
    window_type     VARCHAR(32)  NOT NULL,
    day_mask        INT          NOT NULL DEFAULT 127,
    start_minute    INT          NOT NULL,
    end_minute      INT          NOT NULL,
    target_mode     VARCHAR(32)  NOT NULL,
    soc_min         DECIMAL(5,2) NULL,
    soc_max         DECIMAL(5,2) NULL,
    power_watts     DECIMAL(12,2) NULL,
    priority        INT          NOT NULL DEFAULT 100,
    CONSTRAINT fk_schedule_window_schedule FOREIGN KEY (schedule_id) REFERENCES charge_schedule (id)
);

CREATE INDEX idx_schedule_window_sched_prio ON schedule_window (schedule_id, priority);
