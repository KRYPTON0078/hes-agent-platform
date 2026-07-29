-- V5: seed default ops roles
INSERT INTO ops_role (role_code, description)
SELECT 'VIEWER', 'Read-only fleet and telemetry access'
WHERE NOT EXISTS (SELECT 1 FROM ops_role WHERE role_code = 'VIEWER');

INSERT INTO ops_role (role_code, description)
SELECT 'OPERATOR', 'Issue commands and manage devices'
WHERE NOT EXISTS (SELECT 1 FROM ops_role WHERE role_code = 'OPERATOR');

INSERT INTO ops_role (role_code, description)
SELECT 'ADMIN', 'Full IAM, audit, and actuator access'
WHERE NOT EXISTS (SELECT 1 FROM ops_role WHERE role_code = 'ADMIN');
