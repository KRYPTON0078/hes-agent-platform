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
| `AGENT_REGISTER_ACK` | Cloud → Agent | Accept + session hints |
| `HEARTBEAT` | Agent → Cloud | Liveness; drives Redis online TTL |
| `HEARTBEAT_ACK` | Cloud → Agent | Optional clock skew / config |
| `TELEMETRY_REPORT` | Agent → Cloud | SOC, power, faults |
| `TELEMETRY_ACK` | Cloud → Agent | Persist accepted |
| `COMMAND_DISPATCH` | Cloud → Agent | Charge / discharge / limits |
| `COMMAND_ACK` | Agent → Cloud | Success / failure / progress |
| `ERROR` | either | Protocol or business error |

## Telemetry fields (energy domain)

- `socPercent` — state of charge (0–100)
- `batteryKwh` — remaining energy
- `inverterWatts` — inverter active power
- `gridWatts` — grid import/export
- `homeLoadWatts` — home load
- `batteryVoltage` / `batteryCurrent`
- `faultCode` / `faultMessage`
- `operatingMode` — e.g. `IDLE`, `CHARGING`, `DISCHARGING`, `STANDBY`

## Transport

- **HTTP** (local demo / fallback): `POST /api/v1/agent/messages`
- **MQTT / MQ path** (docker profile): ingest topic → RocketMQ → consumers

Exact HTTP routes and MQTT topics are wired in subsequent commits.
