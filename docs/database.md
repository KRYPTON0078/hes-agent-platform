# Database design

Flyway script: `hes-server/src/main/resources/db/migration/V1__init_schema.sql`

Compatible with **MySQL 8** (docker) and **H2 MODE=MySQL** (local).

## ER overview

```text
site 1──* device 1──1 device_credential
              │
              ├──1 telemetry_latest
              ├──* telemetry_history
              ├──* command_record 1──* command_event
              └──* alert_record
```

## Tables

| Table | Purpose |
|---|---|
| `site` | Household / installation grouping |
| `device` | Agent identity, model, firmware, status |
| `device_credential` | SHA-256 hashed API key for Agent auth |
| `telemetry_latest` | Latest snapshot per device (ops detail) |
| `telemetry_history` | Append-only time series |
| `command_record` | Control commands with idempotency key |
| `command_event` | Audit trail (CREATED / DISPATCHED / ACKED / FAILED / TIMEOUT) |
| `alert_record` | O&M alerts (LOW_SOC / DEVICE_FAULT / DEVICE_OFFLINE) |

## Index strategy

| Index | Why |
|---|---|
| `uk_device_id` | Stable Agent identity lookup |
| `idx_device_status_updated` | Fleet filters by status |
| `idx_telemetry_device_reported` | Time-range charts / investigation |
| `uk_command_idempotency` | Safe ops retries |
| `idx_command_status_created` | Timeout sweeper + ops queues |
| `idx_alert_device_type_status` | Open-alert de-duplication |
| `idx_alert_status_opened` | Ops alert inbox |

## Notes

- JSON-ish payloads use `TEXT` (MySQL-safe; avoids H2/MySQL `CLOB` mismatch).
- Redis mirrors online presence, command idempotency claims, and latest telemetry for hot reads — MySQL remains source of truth.
