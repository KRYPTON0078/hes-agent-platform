# HES Agent Platform

**Home Energy Storage Agent Platform** — production-style Java backend for residential battery/inverter **Agents**: registry, heartbeat, telemetry ingest, remote command/control, and ops/alerting.

Repo: https://github.com/KRYPTON0078/hes-agent-platform

## JD skill matrix

| Skill | Evidence in this repo |
|---|---|
| Spring Boot / Java EE style backend | `hes-server` (Web, Validation, JPA, Actuator, OpenAPI) |
| Independent DB design + SQL indexes | Flyway `V1__init_schema.sql` + [docs/database.md](docs/database.md) |
| Redis | Online TTL, command idempotency, latest telemetry snapshot (docker) + in-memory fallbacks (local) |
| RocketMQ | `hes-telemetry` / `hes-command` producers + consumers (docker profile) |
| Nginx | `docker/nginx/default.conf` + Compose |
| Agent docking / protocol | `hes-common` protocol + `/api/v1/agent/**` + `X-Api-Key` auth |
| Collect / report / control | register, telemetry pipeline, command dispatch/ACK/timeout audit |
| Ops / O&M | `/api/v1/ops/**`, alerts (LOW_SOC / FAULT / OFFLINE), fleet overview |
| Tests | JUnit5 unit tests + optional Testcontainers MySQL schema IT + GitHub Actions CI |

## Architecture

```text
Agent Simulator --protocol v1 + X-Api-Key--> Nginx --> Spring Boot API
                                                     |- MySQL (Flyway)
                                                     |- Redis (online / idempotency / snapshot)
                                                     |- RocketMQ (async telemetry + commands)
```

Details: [docs/architecture.md](docs/architecture.md) · [docs/protocol.md](docs/protocol.md) · [docs/database.md](docs/database.md)

## Modules

- `hes-common` — protocol DTOs / error codes
- `hes-server` — Spring Boot 3 / Java 21 API
- `hes-agent-simulator` — simulated field Agent

## Quick start (local, H2, no Docker)

Requires **JDK 21** and **Maven 3.9+**.

```bash
mvn -pl hes-server -am spring-boot:run
```

```bash
curl http://localhost:8080/api/v1/ping
# Swagger: http://localhost:8080/swagger-ui.html
```

Smoke script (PowerShell): `scripts/smoke.ps1`

### Run Agent simulator

```bash
# terminal 1
mvn -pl hes-server -am spring-boot:run

# terminal 2
mvn -pl hes-agent-simulator -am exec:java -Dexec.args="HES-SIM-001 http://localhost:8080"
```

### Issue a command (ops)

```bash
curl -X POST http://localhost:8080/api/v1/ops/devices/HES-SIM-001/commands \
  -H "Content-Type: application/json" \
  -d "{\"commandType\":\"START_CHARGE\",\"params\":{\"watts\":2000},\"idempotencyKey\":\"demo-1\"}"
```

## Docker Compose (MySQL + Redis + RocketMQ + Nginx)

```bash
docker compose up --build
```

API via Nginx: `http://localhost/` · direct: `http://localhost:8080`

## Tests

```bash
mvn -B verify
```

MySQL Testcontainers IT runs only when Docker is available (`FlywaySchemaIT`).

## License

MIT — see [LICENSE](LICENSE).

## JD skill matrix (advanced)

| Skill | Evidence in repo |
| --- | --- |
| Charge scheduling / TOU | `energy.schedule` + tariff slots + matcher tests |
| Fleet analytics / forecast | rollups, `DeterministicForecastService`, FleetKpi APIs |
| VPP dispatch | `DispatchEngine` + RocketMQ outbox bridge |
| Observability / SLOs | Micrometer path metrics, `SloRegistry`, bulkhead/retry |
| Agent protocol / OTA | v1.1 validators, enrollment tokens, OTA job FSM |
