# ADR 0003: Command idempotency keys

## Status
Accepted

## Context
Ops clients retry under network failures and must not double-dispatch charge commands.

## Decision
Require idempotencyKey on command issuance; unique DB constraint plus optional Redis cache.

## Consequences
Safe retries; duplicate keys return the original command record.
