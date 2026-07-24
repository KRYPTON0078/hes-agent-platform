# ADR 0001: Dual transport for Agent protocol

## Status
Accepted

## Context
Field Agents may use MQTT/MQ paths in production, but recruiters and local demos need a simple HTTP loop.

## Decision
Support protocol v1 over HTTP for demo/fallback and RocketMQ for async ingest in the docker profile.

## Consequences
- Simulator stays simple (Java HttpClient)
- Production-like async path remains visible in code and Compose
