# Requirement-to-Design Traceability

| ID | Requirement | Step 02 Design Decisions | Step 03 Low-Level Coverage |
|---|---|---|---|
| R01 | `POST /api/users` — registration | `AuthResource` boundary + `AuthControl.register()` + `UserRepository` + `TokenService` | API contract, data model, DTO validation, error model, test scenarios |
| R02 | `POST /api/users/login` — authentication | `AuthResource` boundary + `AuthControl.login()` + `UserRepository.findByEmail()` + `TokenService` | API contract, data model, DTO validation, error model, test scenarios |
| R03 | `GET /api/user` — current user (protected) | `UserResource` boundary + `UserControl.findCurrentUser()` + SmallRye JWT principal | API contract, JWT config, error model, test scenarios |
| R04 | `PUT /api/user` — update current user (protected) | `UserResource` boundary + `UserControl.updateCurrentUser()` + `UserRepository` | API contract, DTO validation, error model, test scenarios |
| R05 | JWT bearer tokens signed with configured SmallRye JWT key material, 5-min expiration | `TokenService` using SmallRye JWT Build API (`Jwt.subject().sign()`) and `application.properties` issuer/key/lifetime configuration | Dependency, config properties, test scenarios |
| R06 | PBKDF2 password hashing with separate salt, hash, and algorithm fields | `AuthControl` hashes password before persisting | Implementation step |
| R07 | Hibernate Validator for input validation | `@Valid` on boundary wrapper DTOs containing `NewUserRequest`, `LoginRequest`, `UpdateUserRequest` under `user` | Dependency, DTO annotations, error model |
| R08 | MongoDB persistence with JNoSQL | `User` entity + `UserRepository` + `quarkus-jnosql-mongodb` | Dependency, data model, config, index creation |
| R09 | HTTP system tests via `realworld-api-st` | `TargetApiClient` methods + `AuthenticationMode.BEARER_TOKEN` | Test scenarios, dependency |
| R10 | JSON `user` envelope | Boundary request and response DTOs | API contract |
| R11 | Error responses `422`/`401`/`404` | Exception mapper + validation constraint violations | Error model |
| R12 | `quarkus-rest-jsonb` | Production API JSON serialization | Dependency |
| R13 | `quarkus-smallrye-jwt` | JWT generation and verification | Dependency, config |
| R14 | BCE package convention | `dev.realworld.authuser.{boundary,control,entity}` | Package structure |

# API Contracts

## `POST /api/users` — Register

```text
Request:
  POST /api/users
  Content-Type: application/json
  Body: { "user": { "username": "string", "email": "string", "password": "string" } }

Success Response:
  201 Created
  Body: { "user": { "email": "string", "token": "string", "username": "string", "bio": "string", "image": "string|null" } }

Error Responses:
  422 Unprocessable Entity — validation failure or duplicate username/email
  Body: { "errors": { "body": ["string"] } }
```

## `POST /api/users/login` — Login

```text
Request:
  POST /api/users/login
  Content-Type: application/json
  Body: { "user": { "email": "string", "password": "string" } }

Success Response:
  200 OK
  Body: { "user": { "email": "string", "token": "string", "username": "string", "bio": "string", "image": "string|null" } }

Error Responses:
  401 Unauthorized — invalid credentials
  422 Unprocessable Entity — validation failure
  Body: { "errors": { "body": ["string"] } }
```

## `GET /api/user` — Current User

```text
Request:
  GET /api/user
  Authorization: Bearer <token>

Success Response:
  200 OK
  Body: { "user": { "email": "string", "token": "string", "username": "string", "bio": "string", "image": "string|null" } }

Error Responses:
  401 Unauthorized — missing or invalid token
```

## `PUT /api/user` — Update Current User

```text
Request:
  PUT /api/user
  Authorization: Bearer <token>
  Content-Type: application/json
  Body: { "user": { "email": "string?", "bio": "string?", "image": "string?", "password": "string?" } }

Success Response:
  200 OK
  Body: { "user": { "email": "string", "token": "string", "username": "string", "bio": "string", "image": "string|null" } }

Error Responses:
  401 Unauthorized — missing or invalid token
  422 Unprocessable Entity — validation failure or duplicate email
  Body: { "errors": { "body": ["string"] } }
```

# Data Models

## `User` Entity (JNoSQL Document — Java Record)

```java
package dev.realworld.authuser.entity;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity("users")
public record User(
    @Id String username,
    @Column String email,
    @Column String passwordHash,
    @Column String passwordSalt,
    @Column String passwordAlgorithm,   // e.g. "PBKDF2WithHmacSHA256"
    @Column String bio,
    @Column String image               // nullable, URL
) {
    public User withEmail(String email) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withPasswordHash(String passwordHash) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withPasswordSalt(String passwordSalt) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withPasswordAlgorithm(String passwordAlgorithm) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withBio(String bio) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }

    public User withImage(String image) {
        return new User(username, email, passwordHash, passwordSalt, passwordAlgorithm, bio, image);
    }
}
```

Java Records are immutable data carriers supported by JNoSQL. Update operations use `with*` factory methods to produce new instances. This aligns with Java 25 idiomatic style and eliminates mutable setters on the persistence entity.

MongoDB collection: `users`.
Unique index on `email` field, created at application startup.

## `UserRepository` (Jakarta Data Repository)

```java
package dev.realworld.authuser.entity;

import jakarta.data.repository.Repository;
import org.eclipse.jnosql.mapping.NoSQLRepository;
import java.util.Optional;

@Repository
public interface UserRepository extends NoSQLRepository<User, String> {
    Optional<User> findByEmail(String email);
}
```

## Request DTOs (Boundary Layer — Java Records)

```java
package dev.realworld.authuser.boundary;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// NewUserRequest — registration
public record NewUserRequest(
    @NotBlank @Pattern(regexp = "^[a-zA-Z0-9]+$") String username,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password
) {}

// RegistrationRequest — RealWorld registration request envelope
public record RegistrationRequest(
    @Valid NewUserRequest user
) {}

// LoginRequest — login
public record LoginRequest(
    @NotBlank @Email String email,
    @NotBlank String password
) {}

// LoginWrapperRequest — RealWorld login request envelope
public record LoginWrapperRequest(
    @Valid LoginRequest user
) {}

// UpdateUserRequest — update current user (all fields optional)
// Null fields are ignored during update
public record UpdateUserRequest(
    @Email String email,
    @Size(min = 8) String password,
    String bio,
    String image
) {}

// UpdateWrapperRequest — RealWorld current-user update request envelope
public record UpdateWrapperRequest(
    @Valid UpdateUserRequest user
) {}
```

`NewUserRequest`, `LoginRequest`, and `UpdateUserRequest` model the inner object under the top-level RealWorld `user` attribute. Boundary resource methods accept only the wrapper records for request bodies.

## Response DTOs (Boundary Layer — Java Records)

```java
package dev.realworld.authuser.boundary;

// UserResponse — envelope for all user responses
public record UserResponse(UserDto user) {
    public static UserResponse from(UserDto userDto) {
        return new UserResponse(userDto);
    }
}

// UserDto — user data in responses
public record UserDto(
    String email,
    String token,
    String username,
    String bio,
    String image
) {}
```

Note: `GET /api/user` and `PUT /api/user` re-emit the current JWT token in the response to match the RealWorld API contract. The token is not stored in the database.

Since `User` is a Java Record (immutable), update operations create new instances via `with*` methods. `UserControl.updateCurrentUser()` applies non-null fields from `UpdateUserRequest` onto the existing `User` record, producing a new `User` instance for persistence.

## `TokenService` (Entity Layer)

```java
package dev.realworld.authuser.entity;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {
    public String generateToken(String username) {
        return Jwt.subject(username).sign();
    }
}
```

Uses SmallRye JWT Build API. `TokenService` sets only `sub` from the username. `Jwt.sign()` signs with the key configured through `smallrye.jwt.sign.key.location` when present. Token issuer, issued-at, expiration, and lifespan are provided by SmallRye JWT Build using `smallrye.jwt.new-token.issuer`, `smallrye.jwt.new-token.lifespan`, and related defaults. Verification uses `mp.jwt.verify.issuer` and `mp.jwt.verify.token.age`.

## `AuthenticationMode` Update (ST Module)

```java
// Add to existing enum in realworld-api-st
public enum AuthenticationMode {
    NONE,
    BEARER_TOKEN
}
```

## `TargetApiClient` Update (ST Module)

```java
package dev.realworld.systemtest.boundary;

import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "service_uri")
@Path("/api")
@Consumes("application/json")
@Produces("application/json")
public interface TargetApiClient {
    // existing methods: none

    @POST
    @Path("/users")
    UserResponse register(NewUserRequest request);

    @POST
    @Path("/users/login")
    UserResponse login(LoginRequest request);

    @GET
    @Path("/user")
    UserResponse getCurrentUser(@HeaderParam("Authorization") String authHeader);

    @PUT
    @Path("/user")
    UserResponse updateCurrentUser(@HeaderParam("Authorization") String authHeader, UpdateUserRequest request);
}
```

`TargetApiClientContractTest` verifies the REST client keeps `@RegisterRestClient(configKey = "service_uri")`, preserving the shared `quarkus.rest-client.service_uri.url` configuration used by the system-test module.

# Error Model

## Error Response Format (RealWorld Convention)

```json
{ "errors": { "body": ["error message 1", "error message 2"] } }
```

## Error Scenarios

| HTTP Status | Condition | Error Messages |
|---|---|---|
| 422 | Bean Validation constraint violation on `RegistrationRequest.user` / `NewUserRequest` | Field-specific messages: "username must be alphanumeric", "email must be well-formed", "password must be at least 8 characters" |
| 422 | Bean Validation constraint violation on `LoginWrapperRequest.user` / `LoginRequest` | "email must be well-formed", "password must not be blank" |
| 422 | Bean Validation constraint violation on `UpdateWrapperRequest.user` / `UpdateUserRequest` | "email must be well-formed", "password must be at least 8 characters" |
| 422 | Duplicate username on registration | "username has already been taken" |
| 422 | Duplicate email on registration or update | "email has already been taken" |
| 401 | Invalid credentials on login | "invalid credentials" |
| 401 | Missing or invalid/expired JWT on protected endpoints | Handled by SmallRye JWT — returns 401 automatically |

## Exception Mapping Strategy

- A JAX-RS `@ServerExceptionMapper` method in a boundary class catches `ConstraintViolationException` from Hibernate Validator and maps it to `422` with the RealWorld error envelope.
- A `@ServerExceptionMapper` method catches domain exceptions (e.g., `DuplicateUserException`, `InvalidCredentialsException`) and maps them to `422` or `401` with the RealWorld error envelope.
- SmallRye JWT automatically returns `401` for missing/invalid tokens on protected endpoints.

# Test Strategy

Three test layers as defined in the architecture baseline:

1. **Unit tests** (in `realworld-api`, no Quarkus startup): Validate `AuthControl` and `UserControl` business logic in isolation. Mock `UserRepository` and supporting collaborators as needed.
2. **Quarkus component tests** (in `realworld-api`, `@QuarkusTest`): Validate `TokenService` with SmallRye JWT configuration and generated key material.
3. **Integration tests** (in `realworld-api`, `@QuarkusTest`): Validate full HTTP request/response cycles including validation, JWT, and persistence via MongoDB Dev Services.
4. **System tests** (in `realworld-api-st`, standalone HTTP): Validate end-to-end behavior against a running `realworld-api` over HTTP.

# Test Scenario Catalog

## Unit Tests (`realworld-api/src/test/java/dev/realworld/authuser/`)

| ID | Class | Method | Validates |
|---|---|---|---|
| U01 | `AuthControlTest` | `register_validInput_createsUserWithHashedPassword` | Business logic, PBKDF2 hashing |
| U02 | `AuthControlTest` | `register_duplicateEmail_throwsException` | Uniqueness enforcement |
| U03 | `AuthControlTest` | `register_duplicateUsername_throwsException` | Uniqueness enforcement |
| U04 | `UserControlTest` | `findCurrentUser_existingUser_returnsUser` | Retrieve by username |
| U05 | `UserControlTest` | `findCurrentUser_nonexistentUser_throwsException` | Missing user |
| U06 | `UserControlTest` | `updateCurrentUser_validUpdate_persistsChanges` | Update flow with immutable record `with*` methods |
| U07 | `UserControlTest` | `updateCurrentUser_emailConflict_throwsException` | Uniqueness on update |
| U08 | `UserControlTest` | `updateCurrentUser_changePassword_hashesNewPassword` | Password re-hash on update using PBKDF2 |

## Quarkus Component Tests (`realworld-api/src/test/java/dev/realworld/authuser/`)

| ID | Class | Method | Validates |
|---|---|---|---|
| Q01 | `TokenServiceTest` | `generateToken_containsSubjectClaim` | JWT generation sets the username as `sub` via `Jwt.subject(username)` |
| Q02 | `TokenServiceTest` | `generateToken_expiresInFiveMinutes` | Generated JWT shape under configured SmallRye JWT lifespan |
| Q03 | `TokenServiceTest` | `generateToken_containsIssuer` | Generated JWT shape under configured SmallRye JWT issuer |

## Integration Tests (`realworld-api/src/test/java/dev/realworld/authuser/`)

| ID | Class | Method | Validates |
|---|---|---|---|
| I01 | `AuthResourceIT` | `register_validInput_returns201WithUserEnvelope` | End-to-end registration |
| I02 | `AuthResourceIT` | `register_duplicateEmail_returns422` | Duplicate email via HTTP |
| I03 | `AuthResourceIT` | `register_duplicateUsername_returns422` | Duplicate username via HTTP |
| I04 | `AuthResourceIT` | `register_invalidInput_returns422` | Bean Validation via HTTP |
| I05 | `AuthResourceIT` | `login_validCredentials_returns200WithToken` | End-to-end login |
| I06 | `AuthResourceIT` | `login_invalidCredentials_returns401` | Auth failure via HTTP |
| I07 | `AuthResourceIT` | `login_invalidInput_returns422` | Bean Validation on login |
| I08 | `UserResourceIT` | `getCurrentUser_withValidToken_returns200WithUserEnvelope` | Protected GET |
| I09 | `UserResourceIT` | `getCurrentUser_withoutToken_returns401` | Missing auth |
| I10 | `UserResourceIT` | `getCurrentUser_withExpiredToken_returns401` | Token expiration |
| I11 | `UserResourceIT` | `updateCurrentUser_withValidToken_returns200WithUpdatedUser` | Protected PUT |
| I12 | `UserResourceIT` | `updateCurrentUser_withoutToken_returns401` | Missing auth on update |
| I13 | `UserResourceIT` | `updateCurrentUser_duplicateEmail_returns422` | Uniqueness via HTTP |

## System Tests (`realworld-api-st/src/test/java/dev/realworld/systemtest/`)

| ID | Class | Method | Validates |
|---|---|---|---|
| S01 | `AuthUserIT` | `register_newUser_returnsUserEnvelopeWithToken` | Black-box registration |
| S02 | `AuthUserIT` | `register_duplicateEmail_returns422` | Black-box duplicate handling |
| S03 | `AuthUserIT` | `login_validCredentials_returnsUserEnvelopeWithToken` | Black-box login |
| S04 | `AuthUserIT` | `login_invalidPassword_returns401` | Black-box auth failure |
| S05 | `AuthUserIT` | `getCurrentUser_withValidToken_returnsUserEnvelope` | Black-box protected GET |
| S06 | `AuthUserIT` | `getCurrentUser_withoutToken_returns401` | Black-box missing auth |
| S07 | `AuthUserIT` | `updateCurrentUser_withValidToken_updatesUserFields` | Black-box protected PUT |
| S08 | `AuthUserIT` | `updateCurrentUser_duplicateEmail_returns422` | Black-box uniqueness |
| S09 | `TargetApiClientContractTest` | `registersWithSharedServiceUriConfigKey` | REST client keeps shared `service_uri` config key |

# Dependency and Version Policy

## New Dependencies for `realworld-api`

| Dependency | Purpose | Version Policy |
|---|---|---|
| `io.quarkiverse.jnosql:quarkus-jnosql-mongodb` | MongoDB document persistence via JNoSQL | Managed by Quarkus platform BOM if available; otherwise version `3.4.13` per JNoSQL documentation |
| `io.quarkus:quarkus-rest-jsonb` | JSON-B serialization for REST payloads | Managed by Quarkus platform BOM (3.36.1) |
| `io.quarkus:quarkus-smallrye-jwt` | JWT bearer token verification | Managed by Quarkus platform BOM (3.36.1) |
| `io.quarkus:quarkus-hibernate-validator` | Bean Validation for request DTOs | Managed by Quarkus platform BOM (3.36.1) |

## New Dependencies for `realworld-api-st`

| Dependency | Purpose | Version Policy |
|---|---|---|
| (no new dependencies) | REST Client Jackson is sufficient for system-test JSON deserialization | Existing dependency set |

## Configuration Changes for `realworld-api`

```properties
# application.properties additions

# JNoSQL MongoDB database name
jnosql.document.database=realworld

# SmallRye JWT — token generation lifespan (5 minutes = 300 seconds)
smallrye.jwt.new-token.lifespan=300

# SmallRye JWT — issuer (must match verify issuer)
smallrye.jwt.new-token.issuer=realworld-api
mp.jwt.token.header=Authorization
mp.jwt.token.prefix=Bearer
mp.jwt.verify.issuer=realworld-api
mp.jwt.verify.token.age=300

# Dev profile: configured RSA key pair in src/main/resources
%dev.mp.jwt.verify.publickey.location=publicKey.pem
%dev.smallrye.jwt.sign.key.location=privateKey.pem
%dev.quarkus.native.resources.includes=publicKey.pem
```

## Build Configuration Changes for `realworld-api`

- Add `<maven.compiler.proc>full</maven.compiler.proc>` to `pom.xml` properties (required for JNoSQL annotation processor on Java 25).
- Add `quarkus-jnosql-mongodb`, `quarkus-rest-jsonb`, `quarkus-smallrye-jwt`, `quarkus-hibernate-validator` dependencies.
- Add `smallrye-jwt-build` dependency for token generation (`io.smallrye:smallrye-jwt-build`, managed by platform BOM).
- Add `privateKey.pem` and `publicKey.pem` resources for configured JWT signing and verification profiles.
- Use Java `javax.crypto.SecretKeyFactory` with `PBKDF2WithHmacSHA256` for password hashing (no additional dependency required — part of JDK).

## MongoDB Index Creation

A CDI bean that runs at startup to create the unique index on `email`:

```java
package dev.realworld.authuser.control;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
// Use MongoDB client to create unique index on email field
// in the users collection at application startup
```

This will use the MongoDB Java driver directly (`com.mongodb.client.MongoClient`) to create:

```java
database.createIndex(new Document("email", 1), new IndexOptions().unique(true));
```

# Ordered Implementation Plan

| Step | Action | Module | Affects |
|---|---|---|---|
| 1 | Add `quarkus-jnosql-mongodb`, `quarkus-rest-jsonb`, `quarkus-smallrye-jwt`, `quarkus-hibernate-validator`, `smallrye-jwt-build` to `realworld-api/pom.xml`; add `<maven.compiler.proc>full</maven.compiler.proc>` property | `realworld-api` | pom.xml |
| 2 | Add JNoSQL configuration (`jnosql.document.database=realworld`) and JWT configuration to `realworld-api/src/main/resources/application.properties` | `realworld-api` | application.properties |
| 3 | Create `dev.realworld.authuser.entity.User` as a Java Record with JNoSQL `@Entity`, `@Id`, `@Column` annotations and `with*` factory methods | `realworld-api` | new file |
| 4 | Create `dev.realworld.authuser.entity.UserRepository` extending `NoSQLRepository<User, String>` with `findByEmail()` | `realworld-api` | new file |
| 5 | Create `dev.realworld.authuser.entity.TokenService` with `generateToken()` using SmallRye JWT Build | `realworld-api` | new file |
| 6 | Create `dev.realworld.authuser.control.AuthControl` with `register()` and `login()` | `realworld-api` | new file |
| 7 | Create `dev.realworld.authuser.control.UserControl` with `findCurrentUser()` and `updateCurrentUser()` | `realworld-api` | new file |
| 8 | Create MongoDB startup index bean `dev.realworld.authuser.control.MongoIndexStartup` for unique `email` index | `realworld-api` | new file |
| 9 | Create request DTOs: wrapper records `RegistrationRequest`, `LoginWrapperRequest`, `UpdateWrapperRequest` and inner records `NewUserRequest`, `LoginRequest`, `UpdateUserRequest` in `dev.realworld.authuser.boundary` | `realworld-api` | new file |
| 10 | Create response DTOs: `UserResponse`, `UserDto` in `dev.realworld.authuser.boundary` | `realworld-api` | new file |
| 11 | Create `dev.realworld.authuser.boundary.AuthResource` with `POST /api/users` and `POST /api/users/login` | `realworld-api` | new file |
| 12 | Create `dev.realworld.authuser.boundary.UserResource` with `GET /api/user` and `PUT /api/user` | `realworld-api` | new file |
| 13 | Create exception mapper methods using `@ServerExceptionMapper` for `ConstraintViolationException` and domain exceptions | `realworld-api` | new file |
| 14 | Add `AuthenticationMode.BEARER_TOKEN` to `realworld-api-st` | `realworld-api-st` | modified file |
| 15 | Add `TargetApiClient` endpoint methods and ST DTOs for auth/user endpoints while preserving `@RegisterRestClient(configKey = "service_uri")` | `realworld-api-st` | modified file |
| 16 | Write unit tests for `AuthControl`, `UserControl`, `TokenService` | `realworld-api` | new test files |
| 17 | Write integration tests for `AuthResource` and `UserResource` | `realworld-api` | new test files |
| 18 | Write system tests `AuthUserIT` in `realworld-api-st` | `realworld-api-st` | new test files |
