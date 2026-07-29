# Security notes

## Authentication
- Ops: JWT Bearer (`/api/v1/auth/login`) with VIEWER / OPERATOR / ADMIN roles
- Agents: `X-Api-Key` hashed with BCrypt (`bcrypt:` prefix); legacy SHA-256 accepted during migration
- Failed login / Agent auth lockouts are enforced

## Authorization
- Spring Security deny-by-default for `/api/v1/ops/**`
- Method security on audit (`ADMIN`) and key rotation (`OPERATOR`/`ADMIN`)
- ABAC policy beans (POL-xxx) for finer resource/action checks

## Audit & detection
- Append-only `security_audit_event`
- Anomaly detectors (ANO-xxx) for SOC drops, command floods, auth fail rates, etc.
- Incident playbooks (IR-xxx) for response steps

## Edge & supply chain
- Nginx security headers + TLS stub (`docker/nginx/tls.conf`)
- Payload size limits and Redis-backed rate limiting
- Gitleaks + Trivy workflows in `.github/workflows/security.yml`

## Secrets
- Never commit real secrets; use `.env.example` and `HES_JWT_SECRET`
- Prod profile hides actuator details
