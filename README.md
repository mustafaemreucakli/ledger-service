# ledger

[![CI](https://github.com/mustafaemreucakli/ledger-service/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/mustafaemreucakli/ledger-service/actions/workflows/ci.yml)

A ledger service built with Java and Spring Boot as a modular monolith.

## Status

Early development: There are no business features yet.

## Tech stack & compatibility

Compatibility checked on 2026-11-10 against Spring Boot 4.1.2.

| Component | Version | Managed by the Spring Boot 4.1.2 BOM | Notes |
|---|---|---|---|
| Java | 21 | – | |
| Spring Boot | 4.1.2 | – | Latest 4.1.x patch at the time of the check |
| Maven (wrapper) | 3.10.0 | – | Use `./mvnw` |
| PostgreSQL | 18.7 | – | Official `postgres:18.7` image (multi-arch, includes arm64). The same tag is used by `compose.yaml` and the tests; `PostgresImageConsistencyTests` fails if they drift apart |
| PostgreSQL JDBC driver | 42.7.14 | Yes (`postgresql.version`) | |
| Flyway | 12.4.0 | Yes (`flyway.version`) | Uses `spring-boot-starter-flyway` and `flyway-database-postgresql` |
| Testcontainers | 2.0.5 | Yes (`testcontainers.version`) | Uses `spring-boot-testcontainers` and `testcontainers-postgresql`. Testcontainers 2.x module names: `testcontainers-postgresql`, `testcontainers-kafka` |
| Spring for Apache Kafka | 4.1.2 (kafka-clients 4.2.2) | Yes (`spring-kafka.version`, `kafka.version`) | Planned. Use `spring-boot-starter-kafka` |
| springdoc-openapi | 3.2.0 | **No**, declare the version explicitly | Planned. The [springdoc compatibility matrix](https://springdoc.org/faq.html) maps Spring Boot 4.1.x to springdoc 3.1.x–3.2.x |

"Planned" libraries are not dependencies yet. Each one is added by the task that introduces it.
Versions managed by the Spring Boot BOM are tested together with Spring Boot and must not be overridden without a reason.

## Running locally

Prerequisites: JDK 21 and Docker Desktop. PostgreSQL is not installed on the machine; it runs in Docker.

Create your local environment file and set `POSTGRES_PASSWORD` in it (no quotes, no backslashes):

```bash
cp .env.example .env
```

`.env` is git-ignored. Docker Compose and the application's `local` profile both read it.

Start PostgreSQL (bound to `127.0.0.1:5432` only):

```bash
docker compose up -d
```

On the first start the database needs a few seconds to initialize. Check that it accepts connections:

```bash
docker compose exec postgres pg_isready
```

Start the application with the `local` profile. Flyway applies pending migrations on startup:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Without a profile the application does not start, because no datasource is configured.

Check that it is up:

```bash
curl -s http://localhost:8080/actuator/health
```

The response must contain `"status":"UP"`.

Check the applied migrations:

```bash
docker compose exec postgres sh -c 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "select version, description, success from flyway_schema_history"'
```

Stop PostgreSQL and keep its data:

```bash
docker compose stop
```

`docker compose down -v` deletes the `postgres-data` volume and all local data. It cannot be undone. A volume created by one PostgreSQL major version cannot be opened by another, so a major upgrade needs a fresh volume or a dump and restore.

Build and run all tests (Docker must be running):

```bash
./mvnw verify
```

Integration tests extend `AbstractIntegrationTest`. It starts one PostgreSQL container through Testcontainers per test run, shares it across all test classes, and removes it when the run ends, so every run starts with an empty database.

## Project structure

The application is a single deployable unit (modular monolith). Each module is a top-level package under `com.mustafaemreucakli.ledger`:

| Package | Responsibility |
|---|---|
| `account` | Account lifecycle and account queries |
| `ledger` | Ledger entries |
| `transfer` | Money movement between accounts |
| `common` | Shared, module-agnostic building blocks |
| `config` | Application-wide Spring configuration |

More modules are added by the tasks that need them.

## Architecture decisions

Architecture decisions are recorded as ADRs in [`docs/adr/`](docs/adr/). Start new ones from [`0000-template.md`](docs/adr/0000-template.md).

## License

Licensed under the [Apache License 2.0](LICENSE).
