# ADR 0005: BCrypt for Agent API keys

## Status
Accepted

## Context
SHA-256 of API keys is fast to brute-force offline if the DB leaks.

## Decision
Store Agent API keys as `bcrypt:` + BCrypt hash (cost 12 in production encoder). Accept legacy bare SHA-256 during migration.

## Consequences
Verification is slower (intentional). Rotation endpoint issues new bcrypt-hashed keys.
