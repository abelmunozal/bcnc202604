# 7. RFC 7807 Problem Details for error responses

- Status: Accepted
- Date: 2026-06-09

## Context

Errors were returned with a bespoke `ErrorResponse` DTO, and a not-found result originally
produced an empty body — inconsistent and non-standard. The role values REST best practices.

## Decision

Use Spring Boot 3's native `ProblemDetail` (`application/problem+json`, RFC 7807) for every
error. A single `@RestControllerAdvice` maps domain and validation exceptions to a
`ProblemDetail` with `title`, `status`, `detail` and a `timestamp` extension; internal causes
are logged, not leaked.

## Consequences

- Standard, interoperable error contract with no custom DTO to maintain; OpenAPI documents
  `ProblemDetail` for the 4xx responses.
- Clients read `detail`/`status` instead of the previous `message` field — a one-time contract
  change, acceptable pre-release.
