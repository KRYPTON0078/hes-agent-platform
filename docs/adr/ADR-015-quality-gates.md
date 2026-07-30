# ADR-015: Quality gates for energy platform waves

## Decision
Each wave ships Flyway schema, service/API behavior, unit tests that fail if the rule is removed, and an ADR/runbook section. Testcontainers ITs cover authz matrices where infrastructure allows.

## Consequences
Commit history maps 1:1 to reviewable behavior; clone-catalog inflation is out of scope.