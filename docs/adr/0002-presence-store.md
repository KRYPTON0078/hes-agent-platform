# ADR 0002: Presence store abstraction

## Status
Accepted

## Context
Online TTL must work without Redis during local H2 demos.

## Decision
Introduce OnlinePresenceStore with Redis and in-memory implementations selected by Spring conditions.

## Consequences
Local profile boots without middleware; docker profile proves Redis TTL behavior.
