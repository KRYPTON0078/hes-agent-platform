# Performance notes

## SQL
- Prefer telemetry_latest for dashboards
- Use idx_telemetry_device_reported for history windows
- Avoid SELECT * on telemetry_history without time bounds

## Redis
- Online keys use TTL equal to heartbeat window
- Snapshot cache reduces MySQL hits for latest SOC reads

## RocketMQ
- Telemetry publish is async; ACK to Agent is immediate after accept
- Consumer persists history + latest in one transaction
