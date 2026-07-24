# Architecture

## Context

HES Agent Platform is the cloud half of a home energy storage system. Each household battery/inverter pair runs an **Agent** that:

1. Registers with the cloud
2. Sends heartbeats (online presence)
3. Reports telemetry (SOC, power, faults)
4. Executes remote commands (charge/discharge/limits)

## Logical components

| Component | Responsibility |
|---|---|
| Nginx | Edge reverse proxy, request id propagation |
| hes-server | REST + Agent protocol adaptation, business logic |
| MySQL | Durable device, telemetry history, commands, alerts |
| Redis | Online TTL, idempotency keys, latest snapshot cache |
| RocketMQ | Decouple telemetry ingest and command dispatch |
| Agent simulator | Dev/demo stand-in for field devices |

## Data paths

### Telemetry

Agent → API validate → RocketMQ → consumer batch write MySQL (`telemetry_history` + `telemetry_latest`) → Redis latest snapshot → alert rules

### Command

Ops API → `command_record` (idempotent) → RocketMQ dispatch → Agent ACK → `command_event` audit trail → timeout sweeper

## Indexing highlights

- `telemetry_history (device_id, reported_at)` — ops time-series queries
- `command_record (status, created_at)` — timeout / ops queues
- Unique `idempotency_key` — safe retries from ops clients
