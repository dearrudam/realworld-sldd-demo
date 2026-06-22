# Low-Level Design and Version Policy — RealWorld Auth and User API

## Requirement-to-Design Traceability

| Step 01 AC | Step 02 Decision | Step 03 Coverage |
|---|---|---|
| 1. Registration | JAX-RS resource → control → repository | `UsersResource`, `UserRegistry`, `User`, `Users` |
| 2. Duplicate email | Repository uniqueness + control check | `Users.findByEmail()` → `UserRegistry` returns 422 |
| 3. Validation | Bean Validation + control rules | `@Valid` on DTO, password policy in `UserRegistry` |
| 4. Login | Resource → control → hasher → token | `UsersResource` → `UserRegistry` → `PasswordHasher` → `TokenIssuer` |
| 5. Invalid credentials | Control returns 401 | `UserRegistry` throws mapped exception |
| 6. Current user | JWT filter → control → repository | `CurrentUserResource.getCurrentUser()` → `UserRegistry` |
| 7. No token | JWT filter → 401 | `quarkus-smallrye-jwt` filter |
| 8. Expired token | JWT filter validates `exp` | `quarkus-smallrye-jwt` filter |
| 9. Update user | Resource → control → repository | `CurrentUserResource` → `UserRegistry.update()` |
| 10. RS256 / MP-JWT | `quarkus-smallrye-jwt` config | `application.properties` dev/test profiles |
| 11. Configurable expiry | `mp.jwt.token.expiry` property | `application.properties` |
| 12. PBKDF2 hashing | JDK `SecretKeyFactory` | `PasswordHasher` with `PBKDF2WithHmacSHA256` |
| 13. MongoDB storage | Jakarta Data + JNoSQL | `User` record + `Users` repository interface |
| 14. Email uniqueness | Query + control check | `Users.findByEmail()` + `UserRegistry` |

## API Contracts

### Request / Response Shapes

All payloads use RealWorld JSON envelope conventions. Serialization uses **JSON-B** via `quarkus-rest-jsonb` — records use `@JsonbProperty` annotations. JSON-B chosen over JSON-P for OpenAPI/Swagger extension compatibility (annotation-based binding enables automatic schema generation).

#### `POST /api/users` — Register

**Request:**
```json
{
  "user": {
    "username": "Jacob",
    "email": "jake@jake.jake",
    "password": "JakeJake1"
  }
}
```

**Response (201):**
```json
{
  "user": {
    "email": "jake@jake.jake",
    "token": "<rs256-signed-jwt>",
    "username": "Jacob",
    "bio": null,
    "image": null
  }
}
```

**Validation:** `username` required non-blank, `email` required valid format, `password` min 8 chars + uppercase + digit.

**Error (422):**
```json
{
  "errors": {
    "email": ["is already taken"],
    "password": ["must be at least 8 characters", "must contain an uppercase letter", "must contain a digit"]
  }
}
```

#### `POST /api/users/login` — Login

**Request:**
```json
{
  "user": {
    "email": "jake@jake.jake",
    "password": "JakeJake1"
  }
}
```

**Response (200):**
```json
{
  "user": {
    "email": "jake@jake.jake",
    "token": "<rs256-signed-jwt>",
    "username": "Jacob",
    "bio": null,
    "image": null
  }
}
```

**Error (401):**
```json
{
  "errors": {
    "email or password": ["is invalid"]
  }
}
```

#### `GET /api/user` — Get Current User

**Headers:** `Authorization: Bearer <rs256-signed-jwt>`

**Response (200):**
```json
{
  "user": {
    "email": "jake@jake.jake",
    "token": "<rs256-signed-jwt>",
    "username": "Jacob",
    "bio": "I work at statefarm",
    "image": null
  }
}
```

**Error (401):**
```json
{
  "errors": {
    "token": ["is missing", "is expired", "is invalid"]
  }
}
```

#### `PUT /api/user` — Update User

**Headers:** `Authorization: Bearer <rs256-signed-jwt>`

**Request:** (all fields optional)
```json
{
  "user": {
    "email": "jake@newdomain.io",
    "username": "JacobNew",
    "password": "NewPass123",
    "bio": "I work at State Farm",
    "image": "https://example.com/avatar.jpg"
  }
}
```

**Response (200):**
```json
{
  "user": {
    "email": "jake@newdomain.io",
    "token": "<new-rs256-signed-jwt>",
    "username": "JacobNew",
    "bio": "I work at State Farm",
    "image": "https://example.com/avatar.jpg"
  }
}
```

## Data Models

### Naming Convention (Confirmed)

| Layer | Package | Name | Rationale |
|---|---|---|---|
| **Boundary** — JAX-RS | `auth.boundary` | `UsersResource` | Plural per microprofile-server; `Resource` suffix fulfills JAX-RS protocol role |
| **Boundary** — JAX-RS | `auth.boundary` | `CurrentUserResource` | `/api/user` is singular per RealWorld spec |
| **Control** | `auth.control` | `UserRegistry` | Named after domain responsibility; avoids banned `*Service` suffix |
| **Control** | `auth.control` | `PasswordHasher` | PBKDF2 hashing — communicates responsibility |
| **Control** | `auth.control` | `TokenIssuer` | JWT creation — communicates responsibility |
| **Entity** | `auth.entity` | `User` | Record — named after domain concept, no suffix |
| **Entity** | `auth.entity` | `Users` | Jakarta Data repository — concise, plural of entity |
| **ST Client** | `systemtest.boundary` | `UsersResourceClient` | `Client` suffix per microprofile-server |

Banned suffixes (not used): `*Service`, `*Impl`, `*Manager`, `*Creator`.

### MongoDB Document — `User` (`dev.realworld.auth.entity`)

Jakarta NoSQL entity mapped as a MongoDB document. Use the `quarkus-jnosql` skill to determine correct entity annotations and document mappings.

Required fields: `id` (unique identifier), `email` (unique), `username`, `passwordHash` (PBKDF2), `salt` (per-user PBKDF2 salt), `bio`, `image`, `createdAt`, `updatedAt`.

### Jakarta Data Repository — `Users` (`dev.realworld.auth.entity`)

Jakarta Data repository interface. Use the `quarkus-jnosql` skill to determine the correct repository base type and query method patterns.

Required methods: `save`, `findByEmail` (for uniqueness check and login lookup).

### Boundary DTO Conventions

All DTOs must be concrete Java records with `@JsonbProperty` annotations so OpenAPI/Swagger extensions can inspect them for schema generation.

- Request DTOs follow the RealWorld wrapper format: a top-level `user` property containing operation-specific fields as a nested record
- Response uses a shared `UserResponse` record with a `user` property containing `email`, `token`, `username`, `bio`, `image` fields
- Each operation has its own request record tailored to its required fields (see API Contracts above for exact JSON shapes)
- All records live in `dev.realworld.auth.boundary`

### Boundary Resources

Two JAX-RS resources in `dev.realworld.auth.boundary`:

- **`UsersResource`** at `/api/users` — handles `POST /api/users` (register) and `POST /api/users/login` (login). Both endpoints are `@Consumes`/`@Produces` `application/json`. Login is nested at `/api/users/login`.

- **`CurrentUserResource`** at `/api/user` — handles `GET /api/user` (get current user) and `PUT /api/user` (update user). Both require a valid Bearer JWT token via `quarkus-smallrye-jwt` filter.

No business logic in resources — delegate to `UserRegistry` and return JAX-RS `Response` per microprofile-server convention.

### System Test — REST Client Interface (`realworld-api-st`)

Extend the existing `TargetApiClient` or create a new `UsersResourceClient` in `dev.realworld.systemtest.boundary` with `@RegisterRestClient(configKey = "service_uri")`. Must expose methods for all four auth/user endpoints (register, login, getCurrentUser, updateUser).

System test methods requiring auth must accept an `Authorization` header parameter per microprofile-rest-client conventions.

## Validation Approach

`quarkus-hibernate-validator` is not used (per microprofile-server convention). Validation is implemented directly in the control layer (`UserRegistry`) with manual field checks:

- **Password**: min 8 chars, at least one uppercase letter, at least one digit
- **Email**: basic format validation (contains `@`, non-empty)
- **Username**: non-blank
- **Email uniqueness**: checked via `Users.findByEmail()` before registration

Validation errors are collected as a `Map<String, List<String>>` (field → error messages) and thrown through the exception mapper as a 422 `errors` envelope.

## Error Model

### Exception Hierarchy

| Exception | HTTP Status | When |
|---|---|---|
| `RegistrationException` | 422 | Duplicate email, validation failure |
| `AuthenticationException` | 401 | Invalid credentials, missing/expired token |
| `UserNotFoundException` | 404 | User email not found (internal, maps to 401 on login) |

Standard JAX-RS `WebApplicationException` subclasses used where applicable per microprofile-server convention.

### RealWorldExceptionMapper

Maps exceptions to `errors` JSON envelope:

```json
{
  "errors": {
    "field": ["message1", "message2"]
  }
}
```

## Test Strategy

### Layer Isolation

| Layer | Test Type | What It Tests |
|---|---|---|
| Control | Unit (plain JUnit 5) | `UserRegistry`, `PasswordHasher`, `TokenIssuer` in isolation. Mock `Users` repository. |
| Boundary | Integration (`@QuarkusTest`, `IT` suffix) | `UsersResource` + `CurrentUserResource` with Dev Services MongoDB. Full HTTP roundtrip. |
| System | System test (`IT` suffix, realworld-api-st) | End-to-end HTTP via REST Client against running realworld-api. |

### Unit Tests (at most 3 per class per java-conventions)

| Test Class | Scenarios |
|---|---|
| `UserRegistryTest` | Register new user, reject duplicate email, login with valid/invalid credentials, update user fields |
| `PasswordHasherTest` | Hash/verify roundtrip, different salts for same password, reject wrong password |
| `TokenIssuerTest` | Token contains required claims, expiry is configurable |

### Integration Tests (`@QuarkusTest`, `IT` suffix)

| Test Class | Scenarios |
|---|---|
| `UsersResourceIT` | Full HTTP roundtrip for all 4 endpoints. Validates JSON envelope structure, HTTP status codes, error responses. |

### System Tests (`IT` suffix, realworld-api-st)

| Test Class | Scenarios |
|---|---|
| `AuthScenarioIT` | Register → login → get current user → update user → verify all changes |
| `AuthErrorsIT` | Invalid login, missing token, expired token, duplicate email, validation failures |

## Dependency and Version Policy

### Current Dependencies (realworld-api)

Already present: `quarkus-rest`, `quarkus-arc`, `quarkus-smallrye-health`, `quarkus-junit`, `rest-assured` (test).

### New Dependencies Required

| Extension | Purpose | Version Source |
|---|---|---|
| `quarkus-jnosql-mongodb` | MongoDB persistence + Jakarta Data repositories | Managed by Quarkus BOM (`3.36.1`) |
| `quarkus-smallrye-jwt` | JWT token issuance and validation | Managed by Quarkus BOM |
| `quarkus-rest-jsonb` | JSON-B serialization for RealWorld envelopes | Managed by Quarkus BOM |

Note: annotation processor configuration and exact entity/repository patterns will follow the `quarkus-jnosql` skill's documented approach during implementation.

### System-Test Module (realworld-api-st)

No new dependencies — already has `quarkus-rest-client`, `quarkus-rest-client-jackson`, `quarkus-rest`.

## Ordered Implementation Plan

### Phase 1: Foundation
1. Add `quarkus-jnosql-mongodb`, `quarkus-smallrye-jwt`, `quarkus-rest-jsonb` to `realworld-api/pom.xml`
2. Configure JNoSQL annotation processor in maven-compiler-plugin
3. Add JWT config to `application.properties` (dev/test profiles: key location, issuer, expiry)
4. Create `User` record entity (MongoDB document with Jakarta NoSQL `@Entity`)
5. Create `Users` repository interface (Jakarta Data, extends `DataRepository`)

### Phase 2: Control Layer
6. Create `PasswordHasher` — PBKDF2WithHmacSHA256 with per-user salt
7. Create `TokenIssuer` — RS256 JWT via `quarkus-smallrye-jwt`
8. Create `UserRegistry` — register, login, findByEmail, update

### Phase 3: Boundary Layer
9. Create `UsersResource` — POST /api/users, POST /api/users/login
10. Create `CurrentUserResource` — GET /api/user, PUT /api/user
11. Create `RealWorldExceptionMapper` — maps exceptions to `errors` envelope

### Phase 4: Tests
12. Unit tests: `UserRegistryTest`, `PasswordHasherTest`, `TokenIssuerTest`
13. Integration tests: `UsersResourceIT` (@QuarkusTest)
14. System tests: Add `BEARER_TOKEN` to `AuthenticationMode`, extend `UsersResourceClient`, create `AuthScenarioIT` + `AuthErrorsIT` in realworld-api-st
