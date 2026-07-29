-- V4: Ops IAM tables for JWT/RBAC
CREATE TABLE ops_role (
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role_code       VARCHAR(64)  NOT NULL,
    description     VARCHAR(256) NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_ops_role_code UNIQUE (role_code)
);

CREATE TABLE ops_user (
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(64)  NOT NULL,
    password_hash   VARCHAR(128) NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
    locked_until    TIMESTAMP    NULL,
    failed_logins   INT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_ops_user_username UNIQUE (username)
);

CREATE TABLE ops_user_role (
    user_id         BIGINT       NOT NULL,
    role_id         BIGINT       NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ops_user_role_user FOREIGN KEY (user_id) REFERENCES ops_user (id),
    CONSTRAINT fk_ops_user_role_role FOREIGN KEY (role_id) REFERENCES ops_role (id)
);

CREATE INDEX idx_ops_user_enabled ON ops_user (enabled);
