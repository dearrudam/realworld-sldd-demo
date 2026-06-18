# Repository Structure Overview

- Repository root contains two Quarkus Maven applications and SLDD workflow state:
  - `realworld-api/` — production API application, currently a shell with Quarkus REST, ArC, and SmallRye Health. No RealWorld business components or endpoints exist yet.
  - `realworld-api-st/` — standalone system-test application with Quarkus REST, ArC, REST Client, and REST Client Jackson. Contains system-test strategy entities (`SystemTestStrategy`, `AuthenticationMode`, `DataIsolationMode`, `VerificationBoundary`) and a `TargetApiClient` REST client interface with no endpoint methods.
- `.sldd/specs/` stores workflow journals and approved artifacts.
- `scripts/` stores repository-level verification checks.
- `realworld-api` still contains no main Java source files — the generated greeting resource was removed in the application-shell workflow. Only health endpoint tests remain.
- Both projects use Java 25 and Quarkus platform 3.36.1.

# Architecture Summary

- The approved architecture baseline (`realworld-architecture-baseline`) mandates:
  - BCE package convention: `dev.realworld.<business-component>.<boundary|control|entity>`.
  - MongoDB persistence with `quarkus-jnosql-mongodb` (not yet added to POM).
  - JSON-B REST payloads with `quarkus-rest-jsonb` (not yet added to POM).
  - JWT bearer security with `quarkus-smallrye-jwt` (not yet added to POM).
  - JSON envelope mapping in boundary classes; domain entities should not depend on transport envelopes.
  - Business logic in control classes; JAX-RS resources in boundary packages.
- The approved contract baseline (`realworld-api-contract-baseline`) defines:
  - `POST /api/users` — register.
  - `POST /api/users/login` — login.
  - `GET /api/user` — current user (protected).
  - `PUT /api/user` — update current user (protected).
  - JSON envelopes: `user` with `email`, `token`, `username`, `bio`, `image`.
  - Error convention: `422` for validation, `401` for auth failures, `404` for missing resources.
- The approved system-test strategy defines:
  - `TargetApiClient` as the REST client in `realworld-api-st`.
  - `SystemTestStrategy` with `AuthenticationMode`, `DataIsolationMode`, `VerificationBoundary` entities.
  - System tests in `realworld-api-st/src/test/java` with `IT` suffix.
- `realworld-api` currently has: `quarkus-rest`, `quarkus-arc`, `quarkus-smallrye-health`.
- `realworld-api-st` currently has: `quarkus-rest`, `quarkus-arc`, `quarkus-rest-client`, `quarkus-rest-client-jackson`.

# Conventions to Preserve

- Use Java 25 and Quarkus 3.36.1 as declared in both Maven projects.
- Follow Quarkus extension-first rules before adding any capability.
- Keep `realworld-api` on port `8080` and `realworld-api-st` on port `8081`.
- Keep `realworld-api-st` black-box from production Java classes.
- Use BCE package organization for new business components.
- Keep JSON envelope mapping in boundary classes.
- Keep business logic in control classes, not in JAX-RS resources.
- Use profile-scoped Quarkus configuration (`%dev`, `%test`) for environment-specific values.
- System-test REST client interfaces under `realworld-api-st/src/main/java`; system-test classes under `realworld-api-st/src/test/java` with `IT` suffix.
- Follow the three-layer test approach: unit tests, `@QuarkusTest` integration tests, and standalone HTTP system tests via `realworld-api-st`.

# Integration Points

- API behavior traces to `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md`.
- Architecture decisions trace to `.sldd/specs/realworld-architecture-baseline/realworld-architecture-baseline.md`.
- System-test strategy traces to `.sldd/specs/realworld-system-test-strategy/realworld-system-test-strategy.md`.
- `TargetApiClient` in `realworld-api-st` is the integration point for adding HTTP system-test methods.
- `AuthenticationMode.NONE` will need a `BEARER_TOKEN` mode for protected endpoints in this workflow.
- MongoDB Dev Services will auto-provision a test MongoDB instance for `%dev` and `%test` profiles.

# Risks and Unknowns

- `quarkus-jnosql-mongodb` availability and API compatibility must be validated before adding the dependency — JNoSQL API surface may differ across Quarkus versions.
- `quarkus-smallrye-jwt` integration with JNoSQL and REST layers must be validated together.
- `quarkus-rest-jsonb` must be added and tested alongside the current `quarkus-rest` extension.
- The `realworld-api-st` POM uses REST Client Jackson; this workflow should evaluate whether to align with JSON-B via `quarkus-rest-client-jsonb` for consistency with the production API's JSON-B direction.
- MongoDB unique index on `email` must be created at startup; there is no schema migration tooling.
- 5-minute JWT token expiration may require clock-skew tolerance configuration in test scenarios.

# Context to Carry Into Steps 02-06

- This workflow adds the first RealWorld business API slice: auth and user endpoints.
- It must add `quarkus-jnosql-mongodb`, `quarkus-rest-jsonb`, and `quarkus-smallrye-jwt` to `realworld-api`.
- It must create BCE packages: `dev.realworld.authentication.{boundary,control,entity}` and `dev.realworld.users.{boundary,control,entity}` (or a unified `dev.realworld.authuser` component — to be decided in Step 02).
- It must add `AuthenticationMode.BEARER_TOKEN` to `realworld-api-st` for protected endpoint system tests.
- It must add endpoint methods to `TargetApiClient` for `POST /api/users`, `POST /api/users/login`, `GET /api/user`, `PUT /api/user`.
- The generated greeting resource was already removed; no cleanup needed there.
- Step 02 should design the BCE component split, persistence document model, JWT claims/signing configuration, and REST resource structure.
- Step 03 should specify low-level details: entity fields, JNoSQL annotations, repository interfaces, JWT secret configuration, validation annotations, and exact package/class names.
- Step 04 should write Red-phase tests before any production code changes.
- Step 05 should implement the minimum changes to pass all tests.
- Step 06 should verify all test layers pass and the contract baseline is satisfied.