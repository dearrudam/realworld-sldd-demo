# Compliance Matrix

The `realworld-auth-user-api` workflow is complete with local verification subject to the local Docker/Testcontainers limitation for MongoDB-backed runtime data operations. The approved MongoDB/JNoSQL, JWT, validation, REST, and system-test client mechanisms are implemented without an in-memory or opaque-token substitute.

# Architecture Compliance Matrix

| Decision ID | Required Mechanism | Implemented Mechanism | Evidence Files | Verification Commands | Result | Go/No-Go Impact |
|---|---|---|---|---|---|---|
| R01 | `POST /api/users` registration through BCE resource/control/repository/token path | `AuthResource.register()` delegates to `AuthControl.register()` and persists a JNoSQL `User` with PBKDF2 credentials and JWT response | `realworld-api/src/main/java/dev/realworld/authuser/boundary/AuthResource.java`, `realworld-api/src/main/java/dev/realworld/authuser/control/AuthControl.java`, `realworld-api/src/main/java/dev/realworld/authuser/entity/User.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R02 | `POST /api/users/login` via persisted user email lookup and password verification | `AuthResource.login()` delegates to `AuthControl.login()` with `UserRepository.findByEmail()` and PBKDF2 verification | `realworld-api/src/main/java/dev/realworld/authuser/boundary/AuthResource.java`, `realworld-api/src/main/java/dev/realworld/authuser/control/AuthControl.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R03 | Protected `GET /api/user` using SmallRye JWT principal | `UserResource.getCurrentUser()` is `@Authenticated`, reads `JsonWebToken.getSubject()`, and returns the RealWorld user envelope | `realworld-api/src/main/java/dev/realworld/authuser/boundary/UserResource.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R04 | Protected `PUT /api/user` with optional user-envelope updates | `UserResource.updateCurrentUser()` delegates to `UserControl.updateCurrentUser()` and applies non-null update fields immutably | `realworld-api/src/main/java/dev/realworld/authuser/boundary/UserResource.java`, `realworld-api/src/main/java/dev/realworld/authuser/control/UserControl.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R05 | JWT bearer tokens via SmallRye JWT Build, configured issuer and five-minute lifetime | `TokenService` signs JWTs with `Jwt.subject(username).sign()` and `application.properties` contains SmallRye JWT generation and verification settings plus RSA key resources | `realworld-api/src/main/java/dev/realworld/authuser/entity/TokenService.java`, `realworld-api/src/main/resources/application.properties`, `realworld-api/src/main/resources/privateKey.pem`, `realworld-api/src/main/resources/publicKey.pem` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R06 | PBKDF2 password hashing with separate salt/hash/algorithm fields | `PasswordHashing` uses `PBKDF2WithHmacSHA256`, 65,536 iterations, 256-bit key length, and stores values on `User` | `realworld-api/src/main/java/dev/realworld/authuser/control/PasswordHashing.java`, `realworld-api/src/main/java/dev/realworld/authuser/entity/User.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R07 | Bean Validation request validation | Request envelope and inner DTO records use Jakarta Validation annotations and resource methods use `@Valid`; exceptions map to RealWorld errors | `realworld-api/src/main/java/dev/realworld/authuser/boundary/*.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R08 | MongoDB persistence with JNoSQL and unique email index | `User` uses JNoSQL document annotations, `UserRepository` extends `NoSQLRepository`, and startup creates a unique email index using MongoDB client; full DB-backed HTTP behavior needs Docker or MongoDB locally | `realworld-api/src/main/java/dev/realworld/authuser/entity/User.java`, `realworld-api/src/main/java/dev/realworld/authuser/entity/UserRepository.java`, `realworld-api/src/main/java/dev/realworld/authuser/control/MongoIndexStartup.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | environment-blocked | Go with remediation: run with Docker/Testcontainers or configured MongoDB for full data-path verification |
| R09 | HTTP system-test client through `realworld-api-st` | `TargetApiClient` exposes auth/user endpoints with `@RegisterRestClient(configKey = "service_uri")` and bearer-token mode is represented in `AuthenticationMode` | `realworld-api-st/src/main/java/dev/realworld/systemtest/boundary/TargetApiClient.java`, `realworld-api-st/src/main/java/dev/realworld/systemtest/entity/AuthenticationMode.java` | `./realworld-api-st/mvnw -f realworld-api-st/pom.xml test -DskipITs -q` | satisfied | Go |
| R10 | RealWorld `user` request/response envelope | Registration, login, update, and response DTOs model the top-level `user` envelope | `realworld-api/src/main/java/dev/realworld/authuser/boundary/RegistrationRequest.java`, `realworld-api/src/main/java/dev/realworld/authuser/boundary/LoginWrapperRequest.java`, `realworld-api/src/main/java/dev/realworld/authuser/boundary/UpdateWrapperRequest.java`, `realworld-api/src/main/java/dev/realworld/authuser/boundary/UserResponse.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R11 | RealWorld error envelope for validation/domain failures | `RealworldExceptionMapper` maps validation and JAX-RS/domain exceptions into `{ errors: { body: [...] } }` | `realworld-api/src/main/java/dev/realworld/authuser/boundary/RealworldExceptionMapper.java`, `realworld-api/src/main/java/dev/realworld/authuser/boundary/ErrorsResponse.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R12 | JSON-B REST serialization | `quarkus-rest-jsonb` dependency added and public record DTOs are JSON-B serializable | `realworld-api/pom.xml`, `realworld-api/src/main/java/dev/realworld/authuser/boundary/*.java` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R13 | SmallRye JWT verification | `quarkus-smallrye-jwt` dependency and verification properties are configured | `realworld-api/pom.xml`, `realworld-api/src/main/resources/application.properties` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |
| R14 | BCE package convention | Auth/user implementation is split under `dev.realworld.authuser.boundary`, `.control`, and `.entity` | `realworld-api/src/main/java/dev/realworld/authuser` | `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q` | satisfied | Go |

# Version and Dependency Validation

- Quarkus platform remains `3.36.1`.
- Added approved auth/user API dependencies: JNoSQL MongoDB, REST JSON-B, SmallRye JWT, Hibernate Validator, and SmallRye JWT Build.
- `realworld-api-st` kept its existing dependency set and extended only client code/tests.

# Test Convention Compliance

- Service module verification command passed: `./realworld-api/mvnw -f realworld-api/pom.xml test -DskipITs -q`.
- System-test module verification command passed: `./realworld-api-st/mvnw -f realworld-api-st/pom.xml test -DskipITs -q`.
- The local environment lacks Docker, so Quarkus MongoDB Dev Services could not start. The implementation preserves the approved MongoDB/JNoSQL mechanism and avoids in-memory fallback behavior.

# Risks by Severity

- Medium: full MongoDB-backed registration/login/current-user/update behavior still needs verification in an environment with Docker/Testcontainers or a configured MongoDB endpoint.
- Low: the startup index creation tolerates constrained local environments so unrelated health/shell tests can run without Docker; repository operations still require MongoDB at runtime.

# Remediation Steps

1. Run service integration tests in an environment with Docker or configure `quarkus.mongodb.connection-string` for a reachable MongoDB instance.
2. Add fuller black-box system tests once the target API and MongoDB service are available in CI.
3. Consider adding generated or hand-written OpenAPI contract checks in a follow-up workflow.

# Go/No-Go Decision and Rationale

Go. The approved production mechanisms are implemented and module-level tests compile/start/pass in the available environment. The only non-satisfied decision is environment-blocked MongoDB runtime verification, not an architectural violation, because no unapproved fallback persistence mechanism was introduced.
