# ADR 0010: Security scanning in CI

## Status
Accepted

## Decision
Run Gitleaks and Trivy on every PR/push and weekly schedule.

## Consequences
Secrets and critical/high vulns fail the pipeline; documented exceptions require ADRs.
