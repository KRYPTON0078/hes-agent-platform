# Interview talking points

1. Designed Agent collect/report/control as a versioned protocol envelope
2. Split hot telemetry_latest from append-only history with composite indexes
3. Used Redis TTL for online presence and idempotency helpers
4. Decoupled ingest with RocketMQ while keeping HTTP simulator DX
5. Built O&M loop: alerts, command audit, timeout sweeper
6. Kept profiles honest: local boots without middleware, docker shows full stack
