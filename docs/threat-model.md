# Threat model (STRIDE) — HES Agent Platform

## Assets
- Agent API keys / device control plane
- Ops JWT credentials and RBAC roles
- Telemetry integrity and command authenticity
- Audit trail integrity

## STRIDE summary

| Threat | Register | Telemetry | Command | Ops API |
|---|---|---|---|---|
| Spoofing | Mitigated by post-register API key; register still bootstraps trust | API key + lockout | API key + JWT for issue | JWT + RBAC |
| Tampering | Payload validation | Schema validation + MQ consumer authz | Idempotency + audit | Method security |
| Repudiation | Audit on rotate/login | Telemetry history | command_event + security audit | security_audit_event |
| Information disclosure | Keys returned once at register | Ops auth required | Ops auth required | Deny-by-default |
| Denial of service | Rate limit + auth lockout | Rate limit | Command timeout | Login lockout |
| Elevation of privilege | Default VIEWER role | N/A | OPERATOR required for issue | ADMIN for audit/actuator |

## Residual risks
- First register is unauthenticated (device onboarding tradeoff) — mitigate with future enrollment tokens / mTLS.
- Local JWT secret default must never ship to prod without `HES_JWT_SECRET`.
