# ADR-012: VPP dispatch engine with RocketMQ outbox

## Decision
Evaluate export limits, SOC reserve, and demand response in DispatchEngine, persist decisions, and bridge intents via dispatch_event outbox to RocketMQ topic hes-dispatch-intent.

## Consequences
Command loop consumers can apply LIMIT_EXPORT / RESERVE_CHARGE / DR_DISCHARGE without coupling HTTP to MQ.