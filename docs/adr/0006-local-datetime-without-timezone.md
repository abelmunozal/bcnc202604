# 6. Use `LocalDateTime` without timezone

- Status: Accepted
- Date: 2026-06-09

## Context

The statement specifies an ISO-8601 local date-time format (`2020-06-14T10:00:00`) and the
sample data has no offset. The target domain (Inditex global logistics) is multi-timezone, so
the absence of a zone is a real modelling question.

## Decision

Model the application date as `LocalDateTime`, interpreting prices in the price catalogue's
reference time zone, to match the statement and its expected test results exactly.

## Consequences

- Conforms to the statement and keeps the mandated test scenarios green.
- Known limitation: a global deployment would standardise on `Instant`/`OffsetDateTime`, store
  UTC and convert at the edges. Called out as future work rather than silently assumed.
