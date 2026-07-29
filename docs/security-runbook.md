# Security runbook

## Suspected Agent key leak
1. Rotate key via `POST /api/v1/ops/devices/{id}/credentials/rotate`
2. Quarantine device (`POST /api/v1/ops/security/quarantine/{id}`)
3. Review `security_audit_event` for AUTH failures
4. Re-issue enrollment after forensics

## Ops account lockout
1. Wait lock window or ADMIN unlock (future endpoint)
2. Force password reset process
3. Audit `LOGIN` failures

## Command flood anomaly
1. Confirm ANO detector finding
2. Quarantine device
3. Inspect command_event and security audit
4. Raise IR playbook severity if coordinated across fleet
