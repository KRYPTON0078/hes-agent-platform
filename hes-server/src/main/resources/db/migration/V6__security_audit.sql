-- V6: immutable security audit trail
CREATE TABLE security_audit_event (
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    event_type      VARCHAR(64)  NOT NULL,
    actor           VARCHAR(128) NULL,
    subject         VARCHAR(128) NULL,
    detail_json     CLOB         NULL,
    ip_address      VARCHAR(64)  NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_security_audit_type_created ON security_audit_event (event_type, created_at);
CREATE INDEX idx_security_audit_actor_created ON security_audit_event (actor, created_at);
