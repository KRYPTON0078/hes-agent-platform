# ADR-010: Charge schedules as first-class energy control

## Context
HES Agents need deterministic charge/discharge windows for TOU and SOC constraints.

## Decision
Persist charge_schedule, schedule_window, and schedule_execution. Evaluate via pluggable ScheduleWindowMatcher beans ordered by priority.

## Consequences
Ops can CRUD schedules under RBAC OPERATOR+. Simulator mirrors local TOU evaluation for offline demos.