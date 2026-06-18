# Product Intent: RealWorld Auth and User API

## Workflow Kind

`feature`

## Parent Workflow-Set

`realworld-quarkus-sldd-workspace`

## Origin

This Step 01 draft was scaffolded from:

- Parent journal: `../realworld-quarkus-sldd-workspace/_spec-journal.json`
- Parent artifact: `../realworld-quarkus-sldd-workspace/01-workflow-set-plan.md`

## Scope

Included:

- Implement registration, login, current user, user update, token behavior, validation, and HTTP system tests.

Excluded:

- Profile follow behavior.
- Articles, tags, favorites, and comments behavior.
- Frontend authentication flows.

## Workflow Precedence

Required predecessors:

- `../realworld-api-application-shell/_spec-journal.json`
- `../realworld-api-st-shell/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Deliver the first RealWorld business API slice for authentication and current-user behavior with corresponding standalone HTTP system tests.

## Acceptance Criteria Draft

- The workflow implements registration and login behavior according to the approved API contract.
- The workflow implements current-user retrieval and update behavior.
- The workflow defines and verifies token behavior and validation failures.
- HTTP system tests cover the approved auth and user scenarios through `realworld-api-st`.
- Registration, login, and update request attributes are accepted only inside the RealWorld `user` JSON envelope.

## Resolved Questions

### Token format and signing approach

Follow the architecture baseline direction: JWT bearer tokens via `quarkus-smallrye-jwt`.

- **Format:** JWT (JSON Web Token) as defined by the contract baseline's `Authorization: Bearer <token>` convention.
- **Signing:** Asymmetric JWT signing through SmallRye JWT Build. The app signs tokens with `smallrye.jwt.sign.key.location=privateKey.pem` and verifies bearer tokens with `mp.jwt.verify.publickey.location=publicKey.pem` where explicitly configured.
- **Claims:** `TokenService` sets only `sub` from the username. `iss` (`realworld-api`), `iat`, and `exp` are supplied by SmallRye JWT Build from token-generation defaults and `application.properties`. No roles or fine-grained permissions required for the demo scope.
- **Expiration:** 5-minute token lifetime is configured via `smallrye.jwt.new-token.lifespan=300` for issued tokens and `mp.jwt.verify.token.age=300` for accepted bearer tokens. Production expiration tuning is out of scope.

### Password storage and validation constraints

Demo-scope constraints sufficient for system-test verification:

- **Storage:** PBKDF2 hash. The User entity stores `passwordHash`, `passwordSalt`, and `passwordAlgorithm` separately. PBKDF2 with HMAC-SHA256, 65536 iterations, 256-bit key length.
- **Validation:** Password must be at least 8 characters. Email must be present and well-formed. Username must be present and contain only alphanumeric characters.
- **Demo scope:** No password complexity rules beyond minimum length. No account lockout, rate limiting, or password reset.

### Persistence

Follow the architecture baseline direction: MongoDB with `quarkus-jnosql-mongodb`.

- **User document:** Store users in a `users` collection with `username` as the natural key. Fields: `username`, `email` (unique), `passwordHash`, `passwordSalt`, `passwordAlgorithm`, `bio`, `image`, plus JNoSQL entity annotations.
- **Unique constraints:** Enforce unique `email` via MongoDB unique index. Enforce unique `username` via document ID.
- **Demo scope:** No schema migration tooling. Dev services provide the MongoDB instance. Indexes created at application startup.

### Request and response envelope contract

Follow the RealWorld API contract for both request and response payloads:

- **Registration request:** `{ "user": { "username": "...", "email": "...", "password": "..." } }`.
- **Login request:** `{ "user": { "email": "...", "password": "..." } }`.
- **Current-user update request:** `{ "user": { "email": "...", "bio": "...", "image": "...", "password": "..." } }` with all update fields optional.
- **User responses:** `{ "user": { "email": "...", "token": "...", "username": "...", "bio": "...", "image": "..." } }`.
- **Error responses:** `{ "errors": { "body": ["..."] } }`.

## Approval Status

Approved.
