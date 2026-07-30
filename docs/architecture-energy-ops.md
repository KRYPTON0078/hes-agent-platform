# Architecture: Energy ops, VPP, observability, protocol v1.1

## Scheduling
ScheduleEngine + matchers evaluate TOU/SOC/DR windows; executions audited.

## Analytics
Hourly rollups + deterministic forecast stub + FleetKpi formulas for O&M dashboards.

## VPP dispatch
DispatchEngine applies export/SOC/DR policies; DispatchMqBridge publishes outbox events to RocketMQ.

## Observability
Micrometer path metrics, coded SLOs, bulkhead/retry policies, MDC trace ids.

## Protocol
v1.1 capability/seq/nonce validators, enrollment tokens, duplex long-poll commands, OTA job phases.