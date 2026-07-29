# ADR 0006: Immutable security audit log

## Status
Accepted

## Context
Investigations need a trustworthy trail of auth failures, key rotations, and privileged ops.

## Decision
`security_audit_event` is append-only. Application exposes no update/delete APIs. Admin-only read endpoints.

## Consequences
Retention jobs may archive later; they must not silently rewrite event bodies.
