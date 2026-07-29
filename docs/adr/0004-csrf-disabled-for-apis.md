# ADR 0004: CSRF disabled for machine APIs

## Status
Accepted

## Context
HES clients are Agent devices and ops tooling using Bearer JWT / API keys, not browser form posts.

## Decision
Disable Spring Security CSRF for `/api/**` and rely on:
- Bearer JWT for ops
- `X-Api-Key` for Agents
- Strict CORS allowlists in production

## Consequences
Browser cookie sessions must not be introduced without re-enabling CSRF.
