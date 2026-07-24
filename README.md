# HES Agent Platform

**Home Energy Storage Agent Platform** — a production-style Java backend that manages residential battery/inverter **Agents**: registry, heartbeat, telemetry ingest, remote command/control, and ops/alerting.

Built as a portfolio project aligned with Java backend + Agent docking + middleware roles in home energy storage.

## Why this repo exists

Recruiters scanning this GitHub should see end-to-end ownership of:

| Skill (JD-aligned) | Where it lives |
|---|---|
| Spring Boot backend | `hes-server` |
| MySQL schema + indexes | `hes-server/.../db/migration` |
| Redis (online status / idempotency) | planned in device registry phase |
| RocketMQ (async telemetry / commands) | Docker Compose + consumers (next phases) |
| Nginx reverse proxy | `docker/nginx` |
| Agent protocol collect / report / control | `hes-common` + `hes-agent-simulator` |
| Unit / integration tests | expanding with Testcontainers |
| English docs + architecture | `docs/` |

## Architecture

```text
Agent Simulator  --protocol v1-->  Nginx  -->  Spring Boot API
                                              |-- MySQL (Flyway)
                                              |-- Redis
                                              |-- RocketMQ --> consumers
```

## Modules

- `hes-common` — protocol DTOs, message types, error codes
- `hes-server` — Spring Boot 3 / Java 21 API, Flyway, Actuator, OpenAPI
- `hes-agent-simulator` — simulated energy-storage Agent client

## Quick start (local, no Docker)

Requirements: **JDK 21**, **Maven 3.9+**

```bash
mvn -pl hes-server -am spring-boot:run
```

Smoke check:

```bash
curl http://localhost:8080/api/v1/ping
```

Swagger UI: http://localhost:8080/swagger-ui.html

## Full stack (Docker Compose)

```bash
docker compose up --build
```

Then hit the API via Nginx on port 80 or directly on 8080.

## Protocol (v1)

Message types: `AGENT_REGISTER`, `HEARTBEAT`, `TELEMETRY_REPORT`, `COMMAND_DISPATCH`, `COMMAND_ACK`  
See [docs/protocol.md](docs/protocol.md).

## Roadmap (committed in phases)

1. Scaffold + Docker + docs (this commit)
2. Domain entities + richer Flyway notes
3. Agent registry + Redis online status
4. Telemetry pipeline + RocketMQ
5. Command control loop + audit
6. Ops APIs + alerts
7. Full Agent simulator loop
8. Testcontainers + polish

## License

MIT — see [LICENSE](LICENSE).
