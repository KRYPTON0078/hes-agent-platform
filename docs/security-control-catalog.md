# Security control catalog

This repository encodes security definitions as executable Spring beans:

| Family | ID prefix | Package |
|---|---|---|
| Controls | SEC-xxx | `security.controls.generated` |
| Anomaly detectors | ANO-xxx | `security.anomaly.generated` |
| ABAC policies | POL-xxx | `security.abac.generated` |
| Incident playbooks | IR-xxx | `security.incident.generated` |
| Privacy rules | PRV-xxx | `security.privacy.generated` |

Each control includes: identifier, threat, mitigation, severity, and an `evaluate(...)` / `detect(...)` / `permits(...)` method so recruiters can see security-as-code rather than slideware.

See also:
- [threat-model.md](threat-model.md)
- [adr/0004-csrf-disabled-for-apis.md](adr/0004-csrf-disabled-for-apis.md)
- [adr/0005-bcrypt-agent-api-keys.md](adr/0005-bcrypt-agent-api-keys.md)
- [adr/0006-immutable-security-audit.md](adr/0006-immutable-security-audit.md)
