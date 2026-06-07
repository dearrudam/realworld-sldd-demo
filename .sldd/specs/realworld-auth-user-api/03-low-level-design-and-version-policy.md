# Low-Level Design and Version Policy: RealWorld Auth and User API

## Requirement-to-Design Traceability

- Registration, login, current-user retrieval, and current-user update are implemented by a `users` BCE component with `UsersResource`, account operations, token operations, password operations, and user persistence.
- JWT Bearer HS256 with a 2-minute TTL is implemented through configuration-driven issuer, shared secret, and TTL settings. JWT `sub` uses the internal stable user id.
- PBKDF2 salt/hash password storage is implemented with an owned `PasswordCredential` value on `UserAccount`. Plaintext passwords exist only in request DTOs and method parameters during hashing/verification.
- Boundary validation uses Jakarta Bean Validation on request DTOs. This workflow explicitly accepts `quarkus-hibernate-validator` because approved Step 01 requires it, overriding the generic local convention conflict for this feature.
- RealWorld JSON envelopes are modeled as boundary DTOs. Domain entities do not model transport envelopes.
- HTTP system tests are added in `realworld-api-st` as black-box REST-client scenarios and do not depend on production Java classes.

## API Contracts

### `POST /api/users`

Consumes:

```json
{
  "user": {
    "email": "ada@example.com",
    "username": "ada",
    "password": "lovelace"
  }
}
```

Returns `201` with:

```json
{
  "user": {
    "email": "ada@example.com",
    "token": "<jwt>",
    "username": "ada",
    "bio": null,
    "image": null
  }
}
```

### `POST /api/users/login`

Consumes:

```json
{
  "user": {
    "email": "ada@example.com",
    "password": "lovelace"
  }
}
```

Returns `200` with the same `user` envelope shape.

### `GET /api/user`

Requires `Authorization: Bearer <token>`. Returns `200` with the current `user` envelope.

### `PUT /api/user`

Requires `Authorization: Bearer <token>`.

Consumes optional update fields:

```json
{
  "user": {
    "email": "ada-updated@example.com",
    "username": "ada-updated",
    "password": "babbage",
    "bio": "first programmer",
    "image": "https://example.com/ada.png"
  }
}
```

Returns `200` with the updated `user` envelope.

The `token` field is response-only. It is generated for register/login/current-user/update responses and is never persisted on the user entity.

## Data Models

### Domain entity: `UserAccount`

Package: `dev.realworld.users.entity`.

Fields:

- `String id` — internal generated stable id; used as JWT subject and future cross-component reference.
- `String email` — unique login identity.
- `String username` — unique public handle.
- `String bio` — nullable/blank allowed until profile workflows refine it.
- `String image` — nullable/blank allowed until profile workflows refine it.
- `PasswordCredential credential` — owned credential value.
- `Instant createdAt` — account creation timestamp.
- `Instant updatedAt` — last account update timestamp.

Rules:

- `UserAccount` has no `token` field.
- `UserAccount` has no plaintext `password` field.
- Email and username require unique indexes or persistence-level constraints.
- Entity behavior owns safe update decisions: changed email, username, password, bio, or image update `updatedAt`; credential changes only when a new password is supplied.

### Owned value: `PasswordCredential`

Package: `dev.realworld.users.entity`.

Fields:

- `String salt` — Base64-encoded per-password salt.
- `String passwordHash` — Base64-encoded PBKDF2 hash.
- `String algorithm` — `PBKDF2WithHmacSHA256`.
- `int iterations` — PBKDF2 iteration count.
- `int keyLength` — derived-key length.

Rules:

- Created only from password hashing control.
- Password verification compares derived hash in constant time where possible.
- Stored algorithm, iteration count, and key length make future credential migration explicit.

### Boundary DTOs

Package: `dev.realworld.users.boundary`.

- `UserRegistrationEnvelope(UserRegistration user)`
- `UserLoginEnvelope(UserLogin user)`
- `UserUpdateEnvelope(UserUpdate user)`
- `UserEnvelope(UserRepresentation user)`
- `ErrorsEnvelope(...)` using a RealWorld-compatible `errors` object.

Validation:

- Registration: email, username, and password required; password minimum length `5`.
- Login: email and password required.
- Update: all fields optional; supplied password minimum length `5`; supplied email/username must not be blank.

## Error Model

- `422` for validation failures, duplicate email, and duplicate username, using the RealWorld errors envelope.
- `401` for missing, malformed, invalid, or expired token.
- `401` for invalid login credentials without revealing whether email or password failed.
- A valid token referencing a deleted or missing user is treated as `401` in this workflow.
- `403` is reserved for future authorization workflows and is not expected in this auth/current-user slice.

## Test Strategy

- Step 04 writes tests before implementation.
- Unit/control tests cover password hash/verify, duplicate identity rules, token claims/expiry, and user update behavior.
- `realworld-api` Quarkus tests cover HTTP contracts, envelopes, validation/status mapping, health regression, and `/hello` regression.
- `realworld-api-st` tests cover black-box register/login/current-user/update and failure scenarios through REST Client interfaces.

## Test Scenario Catalog

1. Register valid user returns user envelope with token.
2. Register duplicate email or username returns `422`.
3. Register password shorter than 5 characters returns `422`.
4. Login valid credentials returns a new token.
5. Login invalid credentials returns `401`.
6. `GET /api/user` with valid token returns user envelope.
7. `PUT /api/user` changes allowed fields and returns updated user envelope.
8. Missing, invalid, and expired tokens on `/api/user` return `401`.
9. Password storage behavior never returns plaintext and authenticates through PBKDF2 salt/hash verification.
10. Existing shell health endpoints remain `UP` and `/hello` remains `404`.

## Dependency and Version Policy

The current `realworld-api` dependency set is insufficient for this workflow.

Add to `realworld-api` only after Step 03 approval and before Step 04/05 work:

- `quarkus-rest-jsonb` — JSON-B request/response body mapping for RealWorld envelopes.
- `quarkus-smallrye-jwt` — Bearer JWT verification and protected endpoint integration.
- `quarkus-smallrye-jwt-build` — application-side JWT generation/signing for register/login responses.
- `quarkus-hibernate-validator` — Jakarta Bean Validation at REST boundaries; explicitly approved for this workflow despite the generic local convention conflict.
- `quarkus-mongodb-client` — MongoDB extension selected after `quarkus-jnosql-mongodb` resolved but failed Quarkus augmentation without JNoSQL entity metadata in this slice. The current implementation keeps a CDI repository seam and uses in-memory storage for the demo auth/user slice; future persistence hardening can replace that repository with MongoDB-backed persistence.

Do not pin direct extension versions. Use the Quarkus BOM/platform-managed versions from the current `3.36.1` project configuration.

Runtime and maintenance impact:

- JSON-B support changes REST body serialization/deserialization for RealWorld envelopes.
- JWT support adds bearer authentication behavior and token generation/verification configuration.
- Hibernate Validator adds boundary DTO validation and validation exception handling requirements.
- MongoDB client adds MongoDB configuration, database availability concerns, and dev-service behavior where supported.

`realworld-api-st` dependency set is sufficient for REST-client system tests unless JSON-B client support becomes necessary. Its current Jackson REST-client dependency can consume black-box JSON and does not affect the production JSON-B direction.

## Ordered Implementation Plan

1. Add approved Quarkus extensions to `realworld-api` using Quarkus tooling rather than manual dependency invention.
2. Add profile-scoped configuration keys for JWT issuer, HS256 secret, TTL, and Mongo/JNoSQL connection/database.
3. Write Step 04 tests first: unit/control tests, Quarkus HTTP tests, and standalone HTTP system tests.
4. Implement `users.entity` with `UserAccount` and `PasswordCredential`.
5. Implement boundary request/response/error DTOs.
6. Implement password hashing, token issue/verify, persistence access, and account operations.
7. Implement `UsersResource` and error mapping.
8. Update `realworld-api-st` REST clients and black-box scenarios.
9. Update README files after structural endpoint and extension changes.
10. Verify all tests and baseline scripts.

## User Entity Review Note

The approved low-level model intentionally keeps the user entity focused on persisted account state. `UserAccount` stores stable identity, public profile fields, timestamps, and the owned password credential. It does not store generated JWT strings or plaintext passwords. If later review changes this structure, rerun Step 03 or mark downstream completed steps as requiring rerun according to the SLDD gate rules.

## Evidence

- Uses approved Step 01 product intent, Step 99 codebase context, and Step 02 high-level design.
- Consulted Quarkus documentation for SmallRye JWT, Quarkus REST JSON-B, Hibernate Validator/Jakarta Bean Validation, and MongoDB-related persistence options.
- Checked Quarkus extension catalog output for JWT, JSON-B, Hibernate Validator, MongoDB, and JNoSQL extension names.

## Approval Status

Approved and saved on 2026-06-07.
