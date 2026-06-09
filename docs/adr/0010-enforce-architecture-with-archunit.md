# 10. Enforce the dependency rule with ArchUnit

- Status: Accepted
- Date: 2026-06-09

## Context

The hexagonal boundary ([ADR-0001](0001-hexagonal-architecture.md)) was only a convention.
Conventions erode: a single careless import of a Spring or JPA type into the domain would go
unnoticed in review and silently couple the core to a framework.

## Decision

Add ArchUnit tests (`HexagonalArchitectureTest`) that assert, on every build, that the domain
depends on no framework or outer layer, that the application layer never reaches into
infrastructure, and that the layering is respected.

## Consequences

- The architecture is a verified, executable contract; violations fail the build immediately.
- A negligible test-time cost and one more test dependency, well worth the guarantee.
