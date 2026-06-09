# 5. Location of the price-selection rule

- Status: Accepted
- Date: 2026-06-09

## Context

"When several rates overlap, the highest priority wins" is a **domain rule**. Strict DDD would
place it in a domain service operating on candidate prices. However, [ADR-0002](0002-resolve-applicable-price-in-database.md)
resolves the winner in the database for efficiency. There is a genuine tension between domain
purity and read performance.

## Decision

Treat the persistence query as a **read-optimised projection** of the domain rule (a CQRS-style
read model). The rule is preserved as ubiquitous language and as the deterministic ordering of
[ADR-0003](0003-deterministic-tie-break.md). We deliberately do **not** add an in-memory
`PriceSelectionPolicy`/comparator in the domain, because with DB-side selection it would be
unused production code — exactly the kind of low-value artefact the review criticised.

## Consequences

- Best read efficiency with no dead code; the rule is covered by repository and integration
  tests rather than an isolated domain unit test.
- If pricing grows multi-factor (promotions, customer segments, currency conversion), the
  decision is revisited: fetch the candidate set through the port and apply an explicit domain
  `PriceSelectionPolicy`. This ADR would then be superseded.
