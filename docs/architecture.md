# Architecture

## Context

HES Agent Platform is the cloud half of a home energy storage system. Each household battery/inverter pair runs an **Agent** that:

1. Registers with the cloud (receives `apiKey`)
2. Sends heartbeats (online presence)
3. Reports telemetry (SOC, power, faults)
4. Executes remote commands (charge/discharge/limits)

## Logical components

| Component | Responsibility |
|---|---|
| Nginx | Edge reverse proxy, `X-Request-Id` propagation |
| hes-server | REST + Agent protocol adaptation, business logic |
| MySQL | Durable device, telemetry history, commands, alerts |
| Redis | Online TTL, command idempotency keys, latest telemetry snapshot cache |
| RocketMQ | Decouple telemetry ingest and command dispatch (docker profile) |
| Agent simulator | Dev/demo stand-in for field devices |

## Profiles

| Profile | Persistence | Presence / cache | Messaging |
|---|---|---|---|
| `local` (default) | H2 (MySQL mode) + Flyway | In-memory | In-process bus |
| `docker` | MySQL 8 + Flyway | Redis | RocketMQ producers/consumers |

## Data paths

### Telemetry

Agent (`X-Api-Key`) → API validate → event bus → persist MySQL (`telemetry_history` + `telemetry_latest`) → Redis latest snapshot (docker) → alert rules

### Command

Ops API → `command_record` (unique `idempotency_key` + Redis claim) → RocketMQ `hes-command` (docker) / in-process buffer (local) → Agent poll + ACK → `command_event` audit → timeout sweeper

### Auth

- `AGENT_REGISTER` is unauthenticated and returns `apiKey`
- Subsequent Agent calls require header `X-Api-Key`
- Command poll `GET /api/v1/agent/{deviceId}/commands` also requires `X-Api-Key`

## Indexing highlights

- `telemetry_history (device_id, reported_at)` — ops time-series queries
- `command_record (status, created_at)` — timeout / ops queues
- Unique `idempotency_key` — safe retries from ops clients
- `alert_record (device_id, alert_type, status)` — open-alert lookups
