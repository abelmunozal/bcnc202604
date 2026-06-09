# 2. Resolve the applicable price in the database

- Status: Accepted
- Date: 2026-06-09

## Context

For a given product, brand and instant, several rates can overlap; only the one with the
highest `priority` must be returned. The first iteration fetched **all** matching rows and
selected the winner in memory (`stream().findFirst()`), which does not scale with data volume
and was flagged in the technical review ("efficiency of data extraction").

## Decision

Push the selection down to the database with a Spring Data derived query
(`findFirstBy...OrderByPriorityDesc...`), which the provider translates into a dialect-specific
`LIMIT 1`. A composite index on `(PRODUCT_ID, BRAND_ID, START_DATE, END_DATE)` supports the
filter.

## Consequences

- The application reads exactly one row regardless of how many rates overlap; the query is
  index-supported and scales with volume.
- The selection rule now lives in the persistence query rather than the domain — addressed
  explicitly in [ADR-0005](0005-location-of-price-selection-rule.md).
