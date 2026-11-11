# ledger

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
| Flyway | 12.4.0 | Yes (`flyway.version`) | Planned. Use `spring-boot-starter-flyway` and `flyway-database-postgresql` |
| Testcontainers | 2.0.5 | Yes (`testcontainers.version`) | Planned. Use `spring-boot-testcontainers`. Testcontainers 2.x module names: `testcontainers-postgresql`, `testcontainers-kafka` |
| Spring for Apache Kafka | 4.1.2 (kafka-clients 4.2.2) | Yes (`spring-kafka.version`, `kafka.version`) | Planned. Use `spring-boot-starter-kafka` |
| springdoc-openapi | 3.2.0 | **No**, declare the version explicitly | Planned. The [springdoc compatibility matrix](https://springdoc.org/faq.html) maps Spring Boot 4.1.x to springdoc 3.1.x–3.2.x |

"Planned" libraries are not dependencies yet. Each one is added by the task that introduces it.
Versions managed by the Spring Boot BOM are tested together with Spring Boot and must not be overridden without a reason.

## Running locally

Prerequisites: JDK 21.

Start the application:

```bash
./mvnw spring-boot:run
```

Check that it is up:

```bash
curl -s http://localhost:8080/actuator/health
```

The response must contain `"status":"UP"`.

Build and run all tests:

```bash
./mvnw verify
```

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
