# 8. Dev-only JWT resource server with stable keys

- Status: Accepted
- Date: 2026-06-09

## Context

Authentication is **not** part of the statement. The project nonetheless includes a JWT
resource server (the target role lists security among its concerns). The original
implementation generated a new RSA key pair on every boot, so tokens did not survive a restart
and could not be validated across instances, and it left the H2 console open to remote clients.

## Decision

Keep a **minimal** JWT resource server as an optional extra, but:

- load a **fixed** RSA key pair from PEM resources (`certs/`, clearly dev-only);
- restrict the H2 console to local access.

Token issuance (`/auth/token`) remains a mock with no credential check, explicitly scoped as a
development aid.

## Consequences

- Tokens are stable and verifiable across restarts and instances; the security posture is
  honestly documented.
- The bundled keys are for local use only and must never reach production; a real deployment
  delegates to an external IdP and a secret store. If the exercise is judged strictly by its
  statement, the whole auth layer can be removed without touching the pricing core.
