# ADR 0007: Redis rate limiting for Agent ingest

## Status
Accepted

## Decision
Use Redis counters with 60s TTL in docker; in-memory fallback locally.

## Consequences
Multi-instance deployments share limits; local demos stay dependency-light.
