# Ops runbook

## Device offline
1. Check /api/v1/ops/fleet
2. Inspect open alerts (DEVICE_OFFLINE)
3. Verify Agent heartbeat and API key
4. Review last telemetry on device detail

## Command stuck in DISPATCHED
1. GET /api/v1/ops/commands/{commandId}
2. Wait for timeout sweeper (15s)
3. Re-issue with a new idempotency key if needed

## Low SOC storm
1. Filter alerts by LOW_SOC
2. Issue START_CHARGE to affected devices
3. Confirm telemetry SOC trend recovers
