# Prices Service

[![CI](https://github.com/abelmunozal/bcnc202604/actions/workflows/ci.yml/badge.svg)](https://github.com/abelmunozal/bcnc202604/actions/workflows/ci.yml)

A REST microservice (Spring Boot 3.4 / Java 21) that returns the **applicable price** of a
product for a brand at a given instant. Built with **Domain-Driven Design** and a **Hexagonal
(Ports & Adapters)** architecture.

For the design rationale see **[ARCHITECTURE.md](ARCHITECTURE.md)** and the
**[Architecture Decision Records](docs/adr)**.

## Requirements

- Any **JDK 17+** on the `PATH` (only to bootstrap Gradle).
- The compile/run JDK (Temurin 21) is pinned by the **Gradle Java Toolchain** and
  auto-provisioned by the foojay resolver — no manual JDK setup (ADR-0009).

## Build & run

```bash
./gradlew clean build      # compile + test
./gradlew bootRun          # start on http://localhost:8080
```

With Docker:

```bash
docker compose up --build
```

## API

### `GET /api/v1/prices`

| Parameter | Type | Example |
|-----------|------|---------|
| `applicationDate` | ISO-8601 local date-time | `2020-06-14T10:00:00` |
| `productId` | positive integer | `35455` |
| `brandId` | positive integer | `1` |

```bash
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1" \
  -H "Authorization: Bearer <token>"
```

**200** returns the applicable price:

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50,
  "currency": "EUR"
}
```

When several rates overlap, the one with the **highest `priority`** wins, with a deterministic
tie-break (ADR-0002, ADR-0003). No match returns **404** as an RFC 7807
`application/problem+json` body (ADR-0007).

Interactive docs once running: Swagger UI at `/swagger-ui.html`, OpenAPI at `/api-docs`.

### Authentication (development only)

The protected endpoint requires a JWT. This is an **optional dev-only** layer, not part of the
exercise's scope (ADR-0008): `POST /auth/token` with `{"username":"..."}` issues an RSA-signed
token valid for one hour, with no credential validation.

## Data

An in-memory H2 database is initialised on startup from `schema.sql` and `data.sql` with the
four reference rows for product `35455`, brand `1`. The five mandated scenarios are covered by
`PriceControllerIntegrationTest`.

## Testing

```bash
./gradlew test
```

Includes domain unit tests, a `@DataJpaTest` persistence slice, end-to-end web tests (the five
scenarios, the real JWT flow, validation and error bodies), a real-HTTP smoke test, a PostgreSQL
**Testcontainers** showcase (skipped without Docker) and **ArchUnit** rules that enforce the
hexagonal dependency boundary (ADR-0010). See [ARCHITECTURE.md §5](ARCHITECTURE.md#5-testing-strategy).

**CI:** GitHub Actions builds and tests on every push and validates the Helm chart
(`.github/workflows/ci.yml`).

## Tech stack

Java 21 (virtual threads) · Spring Boot 3.4 (Web, Data JPA, Validation, Security/OAuth2 Resource
Server, Actuator) · H2 · springdoc-openapi · JUnit 5 / AssertJ / ArchUnit · Gradle (Kotlin DSL).

## Deployment

A multi-stage `Dockerfile` and `docker-compose.yml` are provided, plus Helm charts under
`charts/prices-service` for Kubernetes.
