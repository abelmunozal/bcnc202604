# 1. Hexagonal (Ports & Adapters) architecture

- Status: Accepted
- Date: 2026-06-09

## Context

The service must expose a pricing query while keeping the business rules independent of the
delivery mechanism (REST) and the storage technology (JPA/H2). The role explicitly values
**DDD and hexagonal architecture**, testability and the ability to swap adapters.

## Decision

Adopt Ports & Adapters with three layers:

- **domain** — the model (`Price`), the outbound port (`PriceRepository`) and domain
  exceptions. No framework imports.
- **application** — the inbound port (`GetApplicablePriceQuery`) and its use-case
  implementation (`PriceQueryService`). Depends only on the domain.
- **infrastructure** — inbound REST adapter, outbound JPA adapter and Spring configuration.
  Depends inwards only.

Dependencies always point inwards; the domain defines interfaces that infrastructure implements.

## Consequences

- The domain is unit-testable without Spring and the persistence/web technologies are
  replaceable behind ports.
- The boundary is enforced automatically (see [ADR-0010](0010-enforce-architecture-with-archunit.md)).
- More indirection than a layered CRUD app — an accepted trade-off for isolation and clarity.
