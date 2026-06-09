# 3. Deterministic tie-break for the single result

- Status: Accepted
- Date: 2026-06-09

## Context

The statement requires the endpoint to return a **single** price. Ordering only by
`priority DESC` is ambiguous if two rows share the same priority: the database could return
either, making the response non-deterministic and the tests potentially flaky.

## Decision

Order by `priority DESC, startDate DESC, priceList DESC`. The highest priority wins; ties are
broken by the most recently started rate and finally by the price list, which is unique.

## Consequences

- The "single result" guarantee is deterministic and reproducible across runs and engines.
- The tie-break beyond `priority` is a deliberate convention; if the business defines a
  different rule, only the `Sort` in the persistence adapter changes.
