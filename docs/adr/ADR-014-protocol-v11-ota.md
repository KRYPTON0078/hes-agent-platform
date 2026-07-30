# ADR-014: Protocol v1.1, enrollment tokens, and OTA jobs

## Decision
Extend Agent envelopes with capabilityFlags/seq/nonce. Replace open bootstrap with one-time enrollment tokens. Model firmware updates as ota_job phases CREATEDâ†’DOWNLOADINGâ†’APPLYINGâ†’ACKED.

## Consequences
Simulator speaks v1.1 and can ACK OTA; duplex long-poll queues commands without requiring WebSocket infra in demos.