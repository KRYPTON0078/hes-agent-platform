# ADR-013: Critical-path metrics and coded SLOs

## Decision
Instrument Agent register/telemetry/command ACK and ops auth with Micrometer timers/counters. Encode SLO targets in SloRegistry. Protect paths with timeout/bulkhead/retry policies.

## Consequences
Actuator/Prometheus scrapes hes.path.* metrics; operators can alert on SLO burn without external APM initially.