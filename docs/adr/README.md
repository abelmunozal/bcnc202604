# Architecture Decision Records

This directory captures the significant architectural decisions for the Prices Service,
using a lightweight [MADR](https://adr.github.io/madr/) format (Context / Decision /
Consequences). Each record is immutable once accepted; a superseding decision gets a new file.

| ADR | Title | Status |
|-----|-------|--------|
| [0001](0001-hexagonal-architecture.md) | Hexagonal (Ports & Adapters) architecture | Accepted |
| [0002](0002-resolve-applicable-price-in-database.md) | Resolve the applicable price in the database | Accepted |
| [0003](0003-deterministic-tie-break.md) | Deterministic tie-break for the single result | Accepted |
| [0004](0004-price-as-value-object.md) | Model `Price` as a Value Object without persistence identity | Accepted |
| [0005](0005-location-of-price-selection-rule.md) | Location of the price-selection rule | Accepted |
| [0006](0006-local-datetime-without-timezone.md) | Use `LocalDateTime` without timezone | Accepted |
| [0007](0007-rfc7807-problem-details.md) | RFC 7807 Problem Details for error responses | Accepted |
| [0008](0008-dev-only-jwt-with-stable-keys.md) | Dev-only JWT resource server with stable keys | Accepted |
| [0009](0009-gradle-toolchains.md) | Gradle Toolchains instead of an in-repo JDK | Accepted |
| [0010](0010-enforce-architecture-with-archunit.md) | Enforce the dependency rule with ArchUnit | Accepted |
