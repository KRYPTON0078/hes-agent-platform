# Agent Protocol v1

Versioned JSON protocol between residential energy-storage **Agents** and the HES cloud backend.

## Envelope

```json
{
  "protocolVersion": "1.0",
  "type": "TELEMETRY_REPORT",
  "messageId": "uuid",
  "deviceId": "HES-SIM-001",
  "timestamp": "2026-07-23T10:00:00Z",
  "payload": {}
}
```

## Message types

| Type | Direction | Purpose |
|---|---|---|
| `AGENT_REGISTER` | Agent → Cloud | First-time / re-register device |
| `AGENT_REGISTER_ACK` | Cloud → Agent | Accept + `apiKey` + site hints |
| `HEARTBEAT` | Agent → Cloud | Liveness; drives online TTL |
| `HEARTBEAT_ACK` | Cloud → Agent | Clock / status |
| `TELEMETRY_REPORT` | Agent → Cloud | SOC, power, faults |
| `TELEMETRY_ACK` | Cloud → Agent | Persist accepted |
| `COMMAND_DISPATCH` | Cloud → Agent | Charge / discharge / limits |
| `COMMAND_ACK` | Agent → Cloud | Success / failure |
| `ERROR` | either | Protocol or business error |

## HTTP routes

| Method | Path | Auth | Notes |
|---|---|---|---|
| `POST` | `/api/v1/agent/messages` | none for `AGENT_REGISTER`; `X-Api-Key` otherwise | Single ingress for Agent envelopes |
| `GET` | `/api/v1/agent/{deviceId}/commands` | `X-Api-Key` | Long-poll style drain of pending `COMMAND_DISPATCH` |

## RocketMQ topics (docker profile)

| Topic | Producer | Consumer |
|---|---|---|
| `hes-telemetry` | `RocketMqAgentEventBus` | `TelemetryRocketMqConsumer` → persist |
| `hes-command` | `RocketMqAgentEventBus` | `CommandRocketMqConsumer` → poll buffer |

## Telemetry fields (energy domain)

- `socPercent` — state of charge (0–100)
- `batteryKwh` — remaining energy
- `inverterWatts` — inverter active power
- `gridWatts` — grid import/export
- `homeLoadWatts` — home load
- `batteryVoltage` / `batteryCurrent`
- `faultCode` / `faultMessage`
- `operatingMode` — e.g. `IDLE`, `CHARGING`, `DISCHARGING`, `STANDBY`

## Register payload extras

- `siteCode` / `siteName` / `timezone` — household site binding
- optional `apiKey` — if omitted, server generates one

## Command ACK payload

```json
{
  "commandId": "uuid",
  "success": true,
  "appliedMode": "CHARGING"
}
```

Terminal command states (`ACKED` / `FAILED` / `TIMEOUT`) reject duplicate ACKs with `ALREADY_TERMINAL`.
