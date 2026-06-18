# Verification and Feedback Report: RealWorld Auth and User API

## Compliance Matrix

| Area | Result | Evidence |
|---|---|---|
| Registration and login | Pass | `UsersResourceTest` and `AuthUserFlowIT` cover successful registration/login and invalid login. |
| Current user retrieval/update | Pass | `UsersResourceTest` and `AuthUserFlowIT` cover Bearer-token current user and update flows. |
| Token behavior | Pass | HS256 token issue/validation implemented with 2-minute TTL; tests cover valid, missing, and invalid tokens. |
| Password behavior | Pass | `PasswordHasherTest` verifies salted PBKDF2 hashing and no plaintext in stored credential fields. |
| Validation/error behavior | Pass | HTTP tests cover invalid registration, duplicate identity, invalid login, and invalid token statuses. |
| System-test boundary | Pass | `realworld-api-st` calls `realworld-api` over HTTP through REST Client only. |
| Shell regression | Pass | Health endpoints remain UP and `/hello` remains 404. |

## Version and Dependency Validation

- `realworld-api` remains on Quarkus platform `3.36.1`.
- Added production API extensions:
  - `quarkus-rest-jsonb`
  - `quarkus-smallrye-jwt`
  - `quarkus-smallrye-jwt-build`
  - `quarkus-hibernate-validator`
  - `quarkus-mongodb-client`
- `quarkus-jnosql-mongodb` was attempted per the low-level design direction but removed after it failed Quarkus augmentation without JNoSQL entity metadata for this slice. The selected MongoDB client extension keeps the architecture direction available while this workflow uses a CDI repository seam and in-memory demo storage.
- `realworld-api-st` dependency set remains unchanged and sufficient for REST-client system tests.

## Test Convention Compliance

- Unit/control and Quarkus HTTP tests live in `realworld-api`.
- Standalone HTTP system tests live in `realworld-api-st`, use MicroProfile REST Client, and do not depend on production Java classes.
- Verification commands passed:
  - `cd realworld-api && ./mvnw test`
  - `cd realworld-api && ./mvnw verify -DskipITs=false`
  - `cd realworld-api-st && ./mvnw test`
  - `cd realworld-api-st && ./mvnw verify -DskipITs=false`

## Risks by Severity

- Medium: user storage is currently in-memory behind `UserRepository`; a future persistence-hardening workflow should replace it with MongoDB-backed storage before multi-instance or durable deployments.
- Medium: SmallRye JWT extension is present, but this slice uses explicit HS256 token issue/validation to preserve the approved demo-token shape and avoid proactive extension authentication. Future work can migrate token verification to framework-managed JWT once HS256 JWK configuration is approved.
- Low: token TTL is 2 minutes; expiry tests should avoid wall-clock flakiness in future expansions.

## Remediation Steps

- Add a follow-up persistence workflow to implement durable MongoDB-backed `UserRepository` with unique indexes for email and username.
- Add a follow-up security-hardening workflow for production key rotation, framework-managed JWT verification, and secret management.
- Add profile/article workflows after this auth/current-user workflow is merged.

## Go/No-Go Decision and Rationale

Go. The auth/current-user API slice is implemented, tested at API and black-box system-test layers, and README/SLDD artifacts are updated. Remaining risks are documented follow-up hardening items and do not block the approved demo scope.
