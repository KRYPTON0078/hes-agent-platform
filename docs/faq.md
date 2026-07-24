# FAQ

## Why H2 in local profile?
So recruiters can run the API without Docker while still validating Flyway migrations.

## Why both Redis and in-memory presence?
Local demos stay zero-deps; docker profile proves TTL eviction with Redis.

## Can I point a real inverter Agent at this?
Protocol is intentionally simplified JSON v1. Adapt codecs before production hardware use.
