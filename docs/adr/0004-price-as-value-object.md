# 4. Model `Price` as a Value Object without persistence identity

- Status: Accepted
- Date: 2026-06-09

## Context

The original domain `Price` record carried the database surrogate key (`id`) and an
abbreviated field name (`curr`). A database identity in the domain model couples the core to a
persistence concern, and the review flagged inconsistencies in the domain layer.

## Decision

`Price` is an immutable **Value Object**: no identity, defined entirely by its attributes. It
validates its own invariants in the compact constructor (mandatory fields, `endDate` not before
`startDate`) and uses ubiquitous language (`currency`, not `curr`). Persistence identity lives
only in the JPA `PriceEntity`, translated by `PriceMapper`.

## Consequences

- The domain is free of persistence concerns and its invariants are centralised.
- A separate entity and a mapper are required (already present), which is the standard
  hexagonal cost of keeping the model pure.
