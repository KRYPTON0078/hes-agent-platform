# ADR 0009: Payload size limits

## Status
Accepted

## Decision
Reject request bodies larger than 64 KiB at the filter and Nginx layers.

## Consequences
Firmware upload / bulk export must use dedicated endpoints later with higher limits and authz.
