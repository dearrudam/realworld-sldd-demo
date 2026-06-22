# High-Level Technical Design — RealWorld Auth and User API

## Requirements Traceability

| Step 01 AC | High-Level Coverage |
|---|---|
| 1. Successful registration | JAX-RS resource → control logic → repository (MongoDB) |
| 2. Duplicate email | Repository-level email uniqueness + control check |
| 3. Validation failures | Bean Validation on request DTO + control password rules |
| 4. Successful login | Resource → control → verify PBKDF2 hash → issue JWT |
| 5. Invalid credentials | Control returns error → 401 response |
| 6. Current user | JWT filter extracts identity → control → repository lookup |
| 7. No token | JWT filter rejects → 401 |
| 8. Expired token | JWT filter validates `exp` → 401 |
| 9. Update user | Resource → control → repository update |
| 10. RS256 / MP-JWT claims | `quarkus-smallrye-jwt` configuration |
| 11. Configurable expiry | `mp.jwt.token.expiry` property |
| 12. PBKDF2 hashing | `SecretKeyFactory` with `PBKDF2WithHmacSHA256` |
| 13. MongoDB storage | MongoDB document + Jakarta Data repository via `quarkus-jnosql-mongodb` |
| 14. Email uniqueness | Jakarta Data query method + control-level uniqueness check |

## Architecture Diagram

```text
realworld-api (port 8080)
┌──────────────────────────────────────────────────────────────────┐
│  dev.realworld.auth                                               │
│  ┌──────────────────────────────────────────────────┐             │
│  │ boundary                                         │             │
│  │  JAX-RS Resource  (plural, /api/users…, @Valid)  │             │
│  │  Request DTOs     (inbound JSON mapping)          │             │
│  │  Response DTO     (user envelope serialization)   │             │
│  └──────────┬───────────────────────────────────────┘             │
│             │                                                     │
│  ┌──────────▼───────────────────────────────────────┐             │
│  │ control                                           │             │
│  │  Business logic unit  (named after responsibility)│             │
│  │  Password hasher      (PBKDF2)                    │             │
│  │  Token issuer         (JWT creation)              │             │
│  └──────────┬───────────────────────────────────────┘             │
│             │                                                     │
│  ┌──────────▼───────────────────────────────────────┐             │
│  │ entity                                            │             │
│  │  MongoDB document      (persisted user data)      │             │
│  │  Jakarta Data repo     (CRUD + queries)           │             │
│  └──────────────────────────────────────────────────┘             │
│                                                                   │
│  Cross-cutting:                                                    │
│  ┌─────────────────────┐  ┌─────────────────────┐                │
│  │ JWT filter          │  │ Exception mapper    │                │
│  │ (quarkus-smallrye-  │  │ (RealWorld          │                │
│  │  jwt)               │  │  errors envelope)   │                │
│  └─────────────────────┘  └─────────────────────┘                │
└──────────────────────────────────────────────────────────────────┘
           ▲ HTTP
           │
realworld-api-st (port 8081)
┌──────────────────────────────────────────────────────────────────┐
│  dev.realworld.systemtest                                         │
│  ┌──────────────────────────────────────────────┐                │
│  │ boundary                                      │                │
│  │  REST Client interface  (Client suffix,      │                │
│  │   new auth endpoint methods)                  │                │
│  └──────────────────────────────────────────────┘                │
│  ┌──────────────────────────────────────────────┐                │
│  │ entity                                        │                │
│  │  AuthenticationMode (add BEARER_TOKEN)        │                │
│  └──────────────────────────────────────────────┘                │
└──────────────────────────────────────────────────────────────────┘
```

## Component Responsibilities

### realworld-api — Auth Business Component (`dev.realworld.auth`)

| Layer | Role | Responsibility |
|---|---|---|
| **Boundary** | JAX-RS Resource (plural) | Maps `POST /api/users`, `POST /api/users/login`, `GET /api/user`, `PUT /api/user`. Validates input via Bean Validation. Delegates to control. Returns RealWorld JSON envelopes. |
| **Boundary** | Request DTO | Deserializes incoming JSON request bodies (registration, login, update). |
| **Boundary** | Response DTO | Serializes `user` envelope responses (email, token, username, bio, image). |
| **Control** | Business logic unit | Orchestrates registration (uniqueness check), login (credential verification), current user retrieval, user update. Named after its domain responsibility — NOT suffixed `Service` per BCE naming rules. |
| **Control** | Password hasher | PBKDF2 password hashing via JDK `SecretKeyFactory` (PBKDF2WithHmacSHA256). |
| **Control** | Token issuer | JWT issuance via `quarkus-smallrye-jwt` — creates RS256-signed tokens with MP-JWT claims (sub, iss, exp, iat, upn). |
| **Entity** | MongoDB document | User data persisted in MongoDB. Fields: id, email, username, passwordHash, bio, image, createdAt, updatedAt. |
| **Entity** | Jakarta Data repository | Persistence operations: save, find by email, find by id. |

### Cross-Cutting

| Component | Responsibility |
|---|---|
| JWT filter | Validates Bearer token on protected endpoints via `quarkus-smallrye-jwt`. Returns 401 on missing/expired/invalid tokens. |
| Exception mapper | Maps validation (422), auth (401), not-found (404) to RealWorld `errors` JSON envelope. |

### realworld-api-st — System Test Layer

| Component | Change |
|---|---|
| REST Client interface | Add methods for each auth/user endpoint. Named after the JAX-RS resource with `Client` suffix (e.g., `UsersResource` → `UsersResourceClient`). Reuses existing `service_uri` config key. |
| AuthenticationMode | Add `BEARER_TOKEN` variant so system tests can pass tokens after login. |
| New test classes | Auth scenario tests: register-then-login flow, validation errors, token expiry. Named with `IT` suffix. |

## Data Flow

### Registration
```
Client → POST /api/users {user: {username, email, password}}
  → Resource.register()
    → Control.register()
      → Repository.findByEmail(email)  [check uniqueness]
        → (duplicate) → 422 errors envelope
      → Password hasher.hash(password)
      → Token issuer.createToken(user)
      → MongoDB document ← populate + hash
      → Repository.save(document)
    → Response DTO {email, token, username, bio, image}
```

### Login
```
Client → POST /api/users/login {user: {email, password}}
  → Resource.login()
    → Control.login()
      → Repository.findByEmail(email)
        → (not found) → 401
      → Password hasher.verify(password, storedHash)
        → (mismatch) → 401
      → Token issuer.createToken(user)
    → Response DTO {email, token, username, bio, image}
```

### Get Current User
```
Client → GET /api/user  [Authorization: Bearer <token>]
  → JWT filter validates token (exp, signature, issuer)
    → (invalid/expired) → 401
  → Resource.getCurrentUser()
    → Control.getCurrentUser(email)
      → Repository.findByEmail(email)
    → Response DTO {email, token, username, bio, image}
```

### Update User
```
Client → PUT /api/user  [Authorization: Bearer <token>]
  → JWT filter validates token
  → Resource.updateUser({user: {email?, username?, password?, bio?, image?}})
    → Control.updateUser(email, updates)
      → (if password) → Password hasher.hash(newPassword)
      → Repository.save(updatedDocument)
      → Token issuer.createToken(updatedUser)  [re-issue if email changed]
    → Response DTO {email, token, username, bio, image}
```

### System Test Flow
```
System test → Client.register() → HTTP → realworld-api
            → Client.login() → HTTP → realworld-api
            → (use received token) → Client.getCurrentUser() → HTTP → realworld-api
            → Client.updateUser() → HTTP → realworld-api
```

## Security and Observability Requirements

### Authentication

- JWT-based bearer authentication via `quarkus-smallrye-jwt`
- RS256 asymmetric key pair
  - Dev/test profile: auto-generated or bundled keys
  - Production: documented in `application.properties` comments for manual setup
- Token claims: `sub` (email), `iss` (application identifier), `exp` (configurable, default 5 min), `iat`, `upn` (email)
- Token validation on `GET /api/user` and `PUT /api/user`
- Returns 401 for missing, expired, or invalid tokens

### Password Security

- PBKDF2 via `javax.crypto.SecretKeyFactory` with `PBKDF2WithHmacSHA256`
- Salt generated per-user using `SecureRandom`
- Configurable iteration count with sensible default

### Observability

- Log auth failures at WARN level without leaking credential details
- Health endpoint (`/q/health`) already available via `quarkus-smallrye-health`
- No additional observability required for demo scope

## Trade-Offs and Alternatives

| Decision | Chosen Approach | Alternatives Considered | Rationale |
|---|---|---|---|
| Persistence | MongoDB via Jakarta Data (JNoSQL) | Hibernate ORM + Panache, plain driver | Architecture baseline decision. Jakarta Data provides repository abstraction. |
| Token auth | `quarkus-smallrye-jwt` | Custom JWT library, opaque tokens | Standard Quarkus extension, MP-JWT alignment. |
| Password hashing | PBKDF2 (JDK built-in) | BCrypt, Argon2 | Zero dependencies, FIPS-compliant, adequate for demo scope. |
| JSON binding | **TBD in Step 03** — JSON-P vs JSON-B | `quarkus-rest-jsonb` (arch baseline) vs JSON-P (microprofile-server skill preference) | Microprofile-server skill prefers JSON-P with record entities providing `toJSON()`/`fromJSON()`. Architecture baseline mentioned `quarkus-rest-jsonb`. Resolve in low-level design. |
| Validation | Bean Validation (`jakarta.validation`) | Manual validation | Declarative, integrates with JAX-RS `@Valid`. |
| System test auth | Add `BEARER_TOKEN` to `AuthenticationMode` enum | Separate auth config | Reuses existing system-test strategy pattern. |

### BCE Conventions Applied

- Package structure: `dev.realworld.auth.{boundary, control, entity}` ✅
- Boundary: coarse-grained facades (JAX-RS resource, DTOs, exception mapper) ✅
- Control: procedural business logic, stateless, named after responsibility (no `*Service` suffix) ✅
- Entity: domain objects with state + behavior ✅
- JAX-RS resource named in plural per microprofile-server convention
- REST Client interfaces in system-test module use `Client` suffix per microprofile-server convention
- System tests use `IT` suffix per microprofile-server convention

## High-Level Test Scenario Map

### Unit Tests (`realworld-api/src/test`)

| Test | Scope |
|---|---|
| Business logic unit test | Registration logic, duplicate email detection, login verification, update user |
| Password hasher test | Hash/verify roundtrip, salt uniqueness |
| Token issuer test | Token creation, claim structure, expiry enforcement |

### Integration Tests (`@QuarkusTest` in realworld-api)

| Test | Scope |
|---|---|
| Resource test | Full HTTP roundtrip: register, login, get current user, update user. Validates JSON envelopes, status codes, error responses. |
| Persistence test | MongoDB read/write via repository, email uniqueness, document structure |

### System Tests (`realworld-api-st`, `IT` suffix)

| Test | Scope |
|---|---|
| Auth scenario | End-to-end: register → login → get current user → update user → verify changes |
| Auth errors | Invalid login, missing token, expired token, validation errors, duplicate email |

### Required Extension Candidates

The following extensions may be added to `realworld-api/pom.xml` (exact set depends on Step 03 JSON binding decision):

| Extension | Purpose |
|---|---|
| `quarkus-jnosql-mongodb` | MongoDB persistence + Jakarta Data repositories |
| `quarkus-smallrye-jwt` | JWT token issuance and validation |
| `quarkus-rest-jsonb` or JSON-P equivalent | JSON serialization for RealWorld envelopes — to be decided in Step 03 |

Additional Maven config: JNoSQL Mapping Lite annotation processor for Java 25 compatibility.
