# Requirements Traceability

| ID | Requirement (from Step 01) | Source |
|---|---|---|
| R01 | Implement `POST /api/users` — user registration | Contract baseline |
| R02 | Implement `POST /api/users/login` — user authentication | Contract baseline |
| R03 | Implement `GET /api/user` — current user retrieval (protected) | Contract baseline |
| R04 | Implement `PUT /api/user` — current user update (protected) | Contract baseline |
| R05 | JWT bearer token signed with SmallRye JWT Build using configured key material, 5-minute expiration, `sub` from code plus `iss`/`iat`/`exp` from SmallRye JWT configuration/defaults | Step 01 resolved question |
| R06 | PBKDF2 password hashing with separate salt, hash, and algorithm fields | Step 01 resolved question |
| R07 | Validation via `quarkus-hibernate-validator`: password ≥ 8 chars, email well-formed, username alphanumeric | Step 01 resolved question |
| R08 | MongoDB persistence with `quarkus-jnosql-mongodb`, `users` collection, `username` as document ID, unique index on `email` | Step 01 resolved question |
| R09 | HTTP system tests in `realworld-api-st` for all auth/user scenarios | Step 01 acceptance criteria |
| R10 | JSON `user` envelope for registration, login, current-user update, and all user responses | Contract baseline |
| R11 | Error responses: `422` validation, `401` auth failure, `404` missing resource | Contract baseline |
| R12 | `quarkus-rest-jsonb` for JSON serialization in `realworld-api` | Architecture baseline |
| R13 | `quarkus-smallrye-jwt` for JWT bearer authentication | Architecture baseline |
| R14 | BCE package convention: `dev.realworld.<component>.<boundary|control|entity>` | Architecture baseline |

# Architecture Diagram

```text
┌──────────────────────────────────────────────────────────────────────┐
│                        realworld-api (port 8080)                     │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────────┐ │
│  │                 authuser — Boundary Layer                        │ │
│  │  ┌──────────────────┐  ┌──────────────────┐                    │ │
│  │  │ UserResource     │  │ AuthResource      │                    │ │
│  │  │ GET/PUT /api/user│  │ POST /api/users   │                    │ │
│  │  │                  │  │ POST /api/users/  │                    │ │
│  │  │                  │  │      login         │                    │ │
│  │  └────────┬─────────┘  └────────┬─────────┘                    │ │
│  │           │                      │                               │ │
│  │           ▼                      ▼                               │ │
│  │  ┌─────────────────────────────────────────────────────────────┐│ │
│  │  │              authuser — Control Layer                       ││ │
│  │  │  ┌─────────────────┐  ┌─────────────────┐                  ││ │
│  │  │  │ UserControl     │  │ AuthControl      │                  ││ │
│  │  │  │ findCurrentUser │  │ register         │                  ││ │
│  │  │  │ updateCurrentUser│  │ login            │                  ││ │
│  │  │  └────────┬────────┘  └────────┬────────┘                  ││ │
│  │  │           │                    │                             ││ │
│  │  └───────────┼────────────────────┼─────────────────────────────┘│ │
│  │              │                    │                               │ │
│  │  ┌───────────▼────────────────────▼─────────────────────────────┐│ │
│  │  │              authuser — Entity Layer                         ││ │
│  │  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      ││ │
│  │  │  │ User          │  │ UserRepository│  │ TokenService │      ││ │
│  │  │  │ (JNoSQL doc)  │  │ (JNoSQL repo)│  │ (JWT issue) │      ││ │
│  │  │  └──────────────┘  └──────────────┘  └──────────────┘      ││ │
│  │  └──────────────────────────────────────────────────────────────┘│ │
│  └──────────────────────────────────────────────────────────────────┘ │
│                                                                      │
│  Extensions: quarkus-rest-jsonb, quarkus-smallrye-jwt,              │
│              quarkus-jnosql-mongodb, quarkus-hibernate-validator                                  │
└──────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────┐
│                    realworld-api-st (port 8081)                      │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────────┐  │
│  │  TargetApiClient (MicroProfile REST Client)                   │  │
│  │    register(NewUserRequest) → UserResponse                     │  │
│  │    login(LoginRequest) → UserResponse                          │  │
│  │    getCurrentUser(String token) → UserResponse                  │  │
│  │    updateCurrentUser(String token, UpdateUserRequest) → ...    │  │
│  └────────────────────────────────────────────────────────────────┘  │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────────┐  │
│  │  AuthenticationMode.BEARER_TOKEN added for protected endpoints │  │
│  └────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────┘
```

# Component Responsibilities

## `dev.realworld.authuser.boundary` — Boundary Layer

### `UserResource`
- JAX-RS resource class mapped to `/api/user`.
- Handles `GET /api/user` (current user retrieval) and `PUT /api/user` (current user update).
- Injects `@PathParam` or JWT principal to identify the current user.
- Delegates to `UserControl` for business logic.
- Maps domain results to RealWorld `user` JSON envelope responses.
- Maps validation and domain errors to `422`, `401`, `404` error envelope responses.
- Uses `@Valid` on an update wrapper DTO whose `user` attribute contains `UpdateUserRequest`, triggering Hibernate Validator constraints before delegating to controls.

### `AuthResource`
- JAX-RS resource class mapped to `/api/users`.
- Handles `POST /api/users` (registration) and `POST /api/users/login` (authentication).
- Delegates to `AuthControl` for registration and login logic.
- Maps domain results to `user` JSON envelope responses containing the JWT token.
- Maps validation and duplicate-key errors to `422` error envelope responses.
- Uses `@Valid` on request wrapper DTOs whose `user` attributes contain `NewUserRequest` or `LoginRequest`, triggering Hibernate Validator constraints before delegating to controls.

## `dev.realworld.authuser.control` — Control Layer

### `UserControl`
- `findCurrentUser(String username)` — retrieves the authenticated user by username from `UserRepository`.
- `updateCurrentUser(String username, String email, String bio, String image)` — updates user fields, validates uniqueness constraints, persists via `UserRepository`.

### `AuthControl`
- `register(String username, String email, String password)` — validates input, hashes password with PBKDF2, creates a `User` entity via `UserRepository`, generates JWT via `TokenService`, returns user data with token.
- `login(String email, String password)` — looks up user by email via `UserRepository`, validates PBKDF2 password hash, generates JWT via `TokenService`, returns user data with token.

## `dev.realworld.authuser.entity` — Entity Layer

### `User`
- JNoSQL document entity annotated with `@Entity`.
- Fields: `id` (username, document key), `email`, `passwordHash`, `passwordSalt`, `passwordAlgorithm`, `bio`, `image`.
- Stored in the `users` collection.

### `UserRepository`
- JNoSQL repository interface for `User` entities.
- Provides `findByEmail(String email)` for login and uniqueness checks.
- Delegates persistence to `quarkus-jnosql-mongodb`.

### `TokenService`
- CDI bean responsible for JWT token generation.
- Sets only the `sub` claim from the authenticated username.
- Uses SmallRye JWT Build `Jwt.subject(username).sign()`.
- Delegates issuer, issued-at/expiration claims, signing key selection, and token lifetime to SmallRye JWT configuration/defaults (`smallrye.jwt.new-token.issuer`, `smallrye.jwt.sign.key.location`, `smallrye.jwt.new-token.lifespan`).

### Request DTOs (Hibernate Validator constraints)
- `RegistrationRequest`: RealWorld request envelope with `user: NewUserRequest`.
- `LoginWrapperRequest`: RealWorld request envelope with `user: LoginRequest`.
- `UpdateWrapperRequest`: RealWorld request envelope with `user: UpdateUserRequest`.
- `NewUserRequest`: inner `user` object for registration with `username` (`@NotBlank`, `@Pattern(regexp = "^[a-zA-Z0-9]+$")`), `email` (`@NotBlank`, `@Email`), `password` (`@NotBlank`, `@Size(min = 8)`).
- `LoginRequest`: inner `user` object for login with `email` (`@NotBlank`, `@Email`), `password` (`@NotBlank`).
- `UpdateUserRequest`: inner `user` object for update with `email` (`@Email` when present), `password` (`@Size(min = 8)` when present). All fields optional for partial updates.

## `realworld-api-st` Changes

### `TargetApiClient` (updated)
- Add endpoint methods: `register`, `login`, `getCurrentUser`, `updateCurrentUser`.
- Keep `@RegisterRestClient(configKey = "service_uri")` so the auth/user system tests reuse the existing shared target-service configuration.
- Define the base client path as `/api`; endpoint method paths are relative (`/users`, `/users/login`, `/user`).
- Use `@POST`, `@GET`, `@PUT` with appropriate paths and `@HeaderParam("Authorization")` for protected endpoints.

### `AuthenticationMode` (updated)
- Add `BEARER_TOKEN` enum value for protected endpoint system tests.

# Data Flow

## Registration (`POST /api/users`)

1. Client sends `{ "user": { "username": "...", "email": "...", "password": "..." } }`.
2. `AuthResource` receives the request, extracts envelope fields, delegates to `AuthControl.register()`.
3. `AuthControl` validates input (username alphanumeric, email well-formed, password ≥ 8 chars).
4. `AuthControl` hashes password with PBKDF2, generating salt, hash, and algorithm.
5. `AuthControl` calls `UserRepository.insert(User)` to persist.
6. MongoDB enforces unique `email` index; duplicate returns `422`.
7. `AuthControl` calls `TokenService.generateToken(username)` to create JWT.
8. `AuthResource` maps the result to `{ "user": { "email": "...", "token": "jwt...", "username": "...", "bio": "", "image": "" } }`.

## Login (`POST /api/users/login`)

1. Client sends `{ "user": { "email": "...", "password": "..." } }`.
2. `AuthResource` receives the request, delegates to `AuthControl.login()`.
3. `AuthControl` calls `UserRepository.findByEmail(email)`.
4. If user not found, return `401`.
5. `AuthControl` validates PBKDF2 password hash.
6. If invalid, return `401`.
7. `AuthControl` calls `TokenService.generateToken(username)` to create JWT.
8. `AuthResource` maps the result to `user` envelope with token.

## Current User (`GET /api/user`)

1. Client sends `Authorization: Bearer <token>`.
2. SmallRye JWT validates token and injects `JsonWebToken` principal.
3. `UserResource.getCurrentUser()` extracts `token.getName()` (username).
4. Delegates to `UserControl.findCurrentUser(username)`.
5. `UserControl` calls `UserRepository.findById(username)`.
6. If not found, return `404`.
7. `UserResource` maps the result to `user` envelope (without token).

## Update Current User (`PUT /api/user`)

1. Client sends `Authorization: Bearer <token>` with `{ "user": { "email": "...", "bio": "...", "image": "..." } }`.
2. SmallRye JWT validates token and injects principal.
3. `UserResource.updateCurrentUser()` extracts username from token, delegates to `UserControl.updateCurrentUser()`.
4. `UserControl` validates input, checks email uniqueness if changed, updates fields.
5. `UserControl` calls `UserRepository.update(user)`.
6. `UserResource` maps the result to `user` envelope (without token).

# Security and Observability Requirements

## Authentication

- JWT bearer tokens per `quarkus-smallrye-jwt`.
- Asymmetric signing and verification using SmallRye JWT key-location configuration.
- Dev profile uses `%dev.smallrye.jwt.sign.key.location=privateKey.pem` and `%dev.mp.jwt.verify.publickey.location=publicKey.pem`.
- Token expiration: 5 minutes.
- Claims: `sub` is set by `TokenService` from username; `iss`, `iat`, and `exp` are provided by SmallRye JWT Build using configuration/defaults.
- `mp.jwt.token.header=Authorization` and `mp.jwt.token.prefix=Bearer` align protected endpoint authentication with the RealWorld `Authorization: Bearer <token>` contract.
- Protected endpoints (`GET /api/user`, `PUT /api/user`) require valid `Authorization: Bearer <token>`.
- Unprotected endpoints (`POST /api/users`, `POST /api/users/login`) do not require authentication.

## Password Security

- PBKDF2 hashing for password storage (separate salt, hash, and algorithm fields in User entity).
- Passwords never returned in responses.
- Minimum 8-character password length.

## Validation

- Declarative input validation via `quarkus-hibernate-validator` (Bean Validation / Jakarta Validation).
- Boundary resources use `@Valid` on request DTOs to trigger validation before control delegation.
- Boundary resources receive wrapper DTOs with a top-level `user` attribute and use nested `@Valid` validation for the inner DTO.
- `NewUserRequest`: `@NotBlank` + `@Pattern(regexp = "^[a-zA-Z0-9]+$")` on username, `@NotBlank` + `@Email` on email, `@NotBlank` + `@Size(min = 8)` on password.
- `LoginRequest`: `@NotBlank` + `@Email` on email, `@NotBlank` on password.
- `UpdateUserRequest`: `@Email` on email when present, `@Size(min = 8)` on password when present.
- Constraint violations produce `422` responses with the RealWorld error envelope format.
- Duplicate username or email (domain-level uniqueness) also returns `422` with descriptive error messages.

## Observability

- Quarkus SmallRye Health endpoints remain at `/q/health`, `/q/health/live`, `/q/health/ready`.
- MongoDB readiness check should be added to the health endpoint in a future workflow (out of scope for this workflow).

# Trade-Offs and Alternatives

## Unified `authuser` component vs. separate `authentication` + `users` components

**Chosen:** Unified `authuser` component.

**Rationale:** Registration, login, and current-user operations share the same `User` entity and `UserRepository`. Splitting into two components would require cross-component entity sharing that adds complexity without benefit for this demo scope. A future refactor can split authentication concerns (token management) from user management (CRUD) if the domain grows.

## Explicit RSA key pair vs. auto-generated dev/test keys

**Chosen:** Explicit key files for configured profiles.

**Rationale:** The implementation uses the Quarkus SmallRye JWT Build defaults through `Jwt.sign()` and provides key locations in configuration where explicit verification/signing material is required. This keeps token generation aligned with the Quarkus extension instead of manually managing symmetric key material in application code. Profiles without explicit key locations can still rely on Quarkus dev/test key generation behavior.

## JSON-B vs. Jackson for `realworld-api`

**Chosen:** JSON-B per architecture baseline (`quarkus-rest-jsonb`).

**Rationale:** Architecture baseline already approved JSON-B for production API payloads. Consistent with the boundary-only envelope mapping approach.

## Jackson vs. JSON-B for `realworld-api-st` REST Client

**Trade-off:** `realworld-api-st` currently uses REST Client Jackson. The production API uses JSON-B. For system tests, the REST client consumes JSON responses from `realworld-api`, so Jackson is acceptable as a deserialization layer — it does not need to match the production API serialization technology. Keeping Jackson avoids unnecessary dependency changes in the ST module.

## MongoDB Dev Services for tests vs. external MongoDB

**Chosen:** MongoDB Dev Services.

**Rationale:** Zero-configuration for `%dev` and `%test` profiles. No external infrastructure required for development or CI.

## Hibernate Validator vs. manual validation in controls

**Chosen:** `quarkus-hibernate-validator` for declarative Bean Validation.

**Rationale:** Bean Validation annotations on request DTOs (`@NotBlank`, `@Email`, `@Size`, `@Pattern`) enforce input constraints at the boundary layer before control delegation. This keeps controls focused on business logic rather than repetitive field checks, produces consistent `422` error envelopes via JAX-RS exception mapping, and integrates naturally with Quarkus REST. Domain-level uniqueness checks (duplicate email/username) remain in controls since they require repository access.

# High-Level Test Scenario Map

## Unit Tests (in `realworld-api`, no Quarkus startup)

| Scenario | Validates |
|---|---|
| `AuthControl.register` — valid input creates user with hashed password | Business logic, validation, PBKDF2 hashing |
| `AuthControl.register` — duplicate email returns error | Uniqueness enforcement |
| `AuthControl.register` — duplicate username returns error | Uniqueness enforcement |
| `AuthControl.register` — invalid username (non-alphanumeric) returns error | Input validation |
| `AuthControl.register` — invalid email returns error | Input validation |
| `AuthControl.register` — short password returns error | Input validation |
| `AuthControl.login` — valid credentials return user with token | Authentication flow |
| `AuthControl.login` — wrong password returns 401 | Authentication failure |
| `AuthControl.login` — unknown email returns 401 | Authentication failure |
| `UserControl.updateCurrentUser` — valid update persists changes | Update flow |
| `UserControl.updateCurrentUser` — email conflict returns error | Uniqueness on update |
| `TokenService.generateToken` — token contains username subject claim | JWT subject claim correctness |
| JWT configuration — generated token uses configured 5-minute lifespan | Token expiration configuration |

## Integration Tests (in `realworld-api`, `@QuarkusTest`)

| Scenario | Validates |
|---|---|
| `POST /api/users` — successful registration returns 201 with user envelope | End-to-end registration |
| `POST /api/users` — duplicate email returns 422 | Duplicate handling via HTTP |
| `POST /api/users/login` — successful login returns 200 with user envelope and token | End-to-end login |
| `POST /api/users/login` — invalid credentials return 401 | HTTP auth failure |
| `GET /api/user` — with valid token returns current user | Protected endpoint access |
| `GET /api/user` — without token returns 401 | Missing authentication |
| `GET /api/user` — with expired token returns 401 | Token expiration |
| `PUT /api/user` — with valid token updates user | Protected update |
| `PUT /api/user` — without token returns 401 | Missing authentication on update |

## System Tests (in `realworld-api-st`, standalone HTTP)

| Scenario | Validates |
|---|---|
| Register a new user — verify response envelope and fields | Black-box registration |
| Register duplicate email — verify 422 error | Black-box duplicate handling |
| Login with valid credentials — verify token present | Black-box login |
| Login with invalid password — verify 401 | Black-box auth failure |
| Get current user with valid token — verify user fields | Black-box protected GET |
| Get current user without token — verify 401 | Black-box missing auth |
| Update current user with valid token — verify updated fields | Black-box protected PUT |
| Update current user email to existing email — verify 422 | Black-box uniqueness on update |
