# Existing Codebase Understanding: RealWorld Auth and User API

## Repository Structure Overview

- Repository contains two Quarkus Maven applications:
  - `realworld-api/`: production RealWorld backend API shell.
  - `realworld-api-st/`: standalone HTTP system-test application shell targeting `realworld-api`.
- SLDD state and approved artifacts live under `.sldd/specs/`.
- Baseline scripts live under `scripts/`.
- `realworld-api` currently has no production Java source files; it has shell health tests only.
- `realworld-api-st` has reusable system-test strategy and REST-client shell classes under `dev.realworld.systemtest`.

## Architecture Summary

- `realworld-api` is a Quarkus 3.36.1 Java 25 app with `quarkus-rest`, `quarkus-arc`, and `quarkus-smallrye-health`.
- `realworld-api-st` is a Quarkus 3.36.1 Java 25 app with `quarkus-rest`, `quarkus-arc`, `quarkus-rest-client`, and `quarkus-rest-client-jackson`.
- Architecture baseline directs future API code to BCE packages: `dev.realworld.<business-component>.<boundary|control|entity>`.
- Contract baseline defines RealWorld auth/user endpoints and Bearer JWT convention.
- Step 01 for this workflow selects HS256 JWTs, 2-minute TTL, PBKDF2 salted hashes, password min size 5, and Jakarta Bean Validation via Quarkus Hibernate Validator.

## Conventions to Preserve

- Keep production business behavior in `realworld-api`; keep system tests black-box in `realworld-api-st` without depending on production Java classes.
- Preserve ports: `realworld-api` on `8080`, `realworld-api-st` on `8081`, ST target URL `http://localhost:8080`.
- Preserve RealWorld JSON envelope and error-envelope conventions.
- Preserve generated greeting endpoint absence (`/hello` must remain 404).
- Add real BCE packages only when they introduce behavior; avoid placeholder packages.
- Use profile-scoped config for environment-specific values and secrets.

## Integration Points

- `realworld-api` protected endpoints must integrate with JWT bearer validation compatible with `Authorization: Bearer <token>`.
- `realworld-api` registration/login must expose `POST /api/users` and `POST /api/users/login`; current-user behavior uses `GET /api/user` and `PUT /api/user`.
- `realworld-api-st` `TargetApiClient` uses REST Client config key `service_uri`, bridged from `realworld-api.base-url`.
- `SystemTestStrategy` currently defaults to `AuthenticationMode.NONE`; auth workflow should evolve this for token-based scenarios.
- Extension additions likely needed before implementation: `quarkus-rest-jsonb`, `quarkus-smallrye-jwt`, `quarkus-hibernate-validator`, and persistence support selected through Quarkus extension discovery.

## Risks and Unknowns

- `quarkus_update` dry-run reports both Quarkus apps are up-to-date, but the tool output labels 3.34.1 as latest while projects use 3.36.1; treat current 3.36.1 BOM as intentional unless later verified otherwise.
- `realworld-api` has no persistence implementation yet; Step 02/03 must decide whether this auth/user slice adds MongoDB/JNoSQL now or uses a scoped in-memory demo store.
- 2-minute token TTL can make expiry system tests flaky unless tests use controlled timing or direct token construction.
- HS256 secret management must be explicit and profile-safe.
- `realworld-api-st` currently has no endpoint methods in `TargetApiClient`; Step 04 should add auth/user scenario clients/tests without coupling to production classes.

## Context to Carry Into Steps 02-06

- Step 02 must design auth/user BCE boundaries, controls, entities, JWT service, password hashing service, validation/error mapping, and persistence strategy.
- Step 02/03 must discover and approve Quarkus extensions before Maven changes.
- Step 04 should create failing tests in both `realworld-api` and `realworld-api-st` where appropriate: registration, login, current user, update, validation failures, missing/invalid/expired token, and password non-exposure behavior.
- Step 05 must implement minimal production behavior to pass those tests without changing Step 04 tests.
- README files should be updated when endpoints/extensions are added.

## Evidence

- Inspected `realworld-api/pom.xml`, `realworld-api/src/main/resources/application.properties`, shell health tests, and module README.
- Inspected `realworld-api-st/pom.xml`, `realworld-api-st/src/main/resources/application.properties`, system-test shell classes, tests, and module README.
- Inspected approved architecture and API contract baselines.
- Ran Quarkus update dry-run for both Quarkus applications; both reported up-to-date.

## Approval Status

Approved and saved on 2026-06-07.
