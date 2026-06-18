# Compliance Matrix

| Requirement | Verification Result |
|---|---|
| Registration request/response contract | Verified by `AuthResourceIT` and `AuthUserIT`; request attributes are under top-level `user`, response returns `user` envelope with token and profile fields. |
| Login request/response contract | Verified by `AuthResourceIT` and `AuthUserIT`; request attributes are under top-level `user`, invalid credentials return `401`. |
| Current-user retrieval | Verified by `UserResourceIT` and `AuthUserIT`; protected endpoint returns current user envelope for valid bearer token and `401` without token. |
| Current-user update | Verified by `UserResourceIT` and `AuthUserIT`; protected endpoint updates optional user fields and rejects duplicate email with `422`. |
| Validation and duplicate errors | Verified by HTTP integration and system tests; invalid input and uniqueness violations return RealWorld error envelope. |
| Password storage | Covered by unit tests for PBKDF2 hash, salt, and algorithm fields. |
| JWT token behavior | Covered by `TokenServiceTest`, HTTP integration tests, and system tests. |
| MongoDB/JNoSQL persistence | Covered by Quarkus integration tests with MongoDB Dev Services. |

# Version and Dependency Validation

- Quarkus project update check was executed for `realworld-api`.
- Current Quarkus version: `3.36.1`.
- Latest available version reported by update check: `3.36.2`.
- The update was not applied during this workflow because it is outside the auth/user API verification scope.
- Existing warning remains: `io.quarkus:quarkus-junit5-mockito` is relocated to `io.quarkus:quarkus-junit-mockito`.

# Test Convention Compliance

- API unit suite executed with `./mvnw test` in `realworld-api`.
- API HTTP integration suite executed with `./mvnw test -Dtest="dev.realworld.authuser.boundary.AuthResourceIT,dev.realworld.authuser.boundary.UserResourceIT"` in `realworld-api`.
- ST suite executed with `./mvnw test -Dtest="dev.realworld.systemtest.AuthUserIT,dev.realworld.systemtest.boundary.GeneratedSampleRemovalTest,dev.realworld.systemtest.boundary.TargetApiClientContractTest,dev.realworld.systemtest.control.ApplicationPropertiesContractTest,dev.realworld.systemtest.entity.SystemTestStrategyTest"` in `realworld-api-st` against `realworld-api` on `localhost:8080`.
- Test data was made unique in HTTP and system tests to avoid cross-test persistence collisions in MongoDB-backed scenarios.

# Risks by Severity

| Severity | Risk |
|---|---|
| Medium | Quarkus `3.36.2` is available but not applied. |
| Medium | `quarkus-junit5-mockito` relocation warning remains in `realworld-api/pom.xml`. |
| Low | API dev-mode start through Dev MCP was unstable due debugger handshake failure, so final verification used Maven commands directly. |

# Remediation Steps

1. Upgrade Quarkus from `3.36.1` to `3.36.2` in a separate maintenance workflow.
2. Replace relocated `quarkus-junit5-mockito` artifact with `quarkus-junit-mockito` in a dependency maintenance pass.
3. Consider making system-test startup orchestration explicit so `AuthUserIT` can start/stop the target API automatically instead of requiring an already running service.

# Go/No-Go Decision and Rationale

Go.

The auth/user API slice satisfies the approved RealWorld envelope contract, protected endpoint behavior, validation/error behavior, PBKDF2 password handling, JWT issuance, MongoDB persistence, and standalone system-test coverage. All required verification commands completed successfully after eliminating test-data collisions.
