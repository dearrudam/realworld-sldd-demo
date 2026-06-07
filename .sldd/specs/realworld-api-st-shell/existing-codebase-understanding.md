# Repository Structure Overview

- The repository is a multi-root RealWorld Quarkus workspace with separate Maven applications in `realworld-api/` and `realworld-api-st/`.
- SLDD workflow state and artifacts live under `.sldd/specs/`; this workflow is `.sldd/specs/realworld-api-st-shell/`.
- Verification/helper scripts live under `scripts/`.
- The target codebase for this workflow is `realworld-api-st/`.

# Architecture Summary

- `realworld-api-st` is a standalone Quarkus Maven application using Java 25 and Quarkus platform `3.36.1`.
- Existing dependencies include Quarkus REST Client, REST Client Jackson, REST, Arc, Quarkus JUnit, and Rest Assured.
- `src/main/resources/application.properties` sets:
  - `quarkus.http.port=8081`
  - `realworld-api.base-url=http://localhost:8080`
  - `quarkus.rest-client.service_uri.url=${realworld-api.base-url}`
- Existing system-test strategy code is organized under `dev.realworld.systemtest`:
  - Boundary: `TargetApiClient`, registered with REST Client config key `service_uri`.
  - Entity/model: `SystemTestStrategy`, `AuthenticationMode`, `DataIsolationMode`, and `VerificationBoundary`.
  - Tests: contract tests for the REST Client config key, application property bridge, and strategy defaults.
- The current execution model is JUnit/Quarkus test-suite oriented. Step 01 explicitly excludes system-test application endpoints and startup test execution.

# Conventions to Preserve

- Keep `realworld-api-st` as a standalone system-test application targeting `realworld-api` over HTTP.
- Preserve the shared target API property `realworld-api.base-url` and REST Client config key `service_uri`.
- Preserve default local target URL `http://localhost:8080` and system-test app port `8081` unless a later approved design changes them.
- Preserve the default strategy decisions from the completed system-test-strategy workflow:
  - Authentication mode: `NONE` until security workflows introduce credentials.
  - Data isolation: `SCENARIO_OWNED_UNIQUE_DATA`; tests must not assume a global reset endpoint.
  - Verification boundary: `STRATEGY_CONTRACT_ONLY` until endpoint-specific workflows add scenarios.
- Keep endpoint-specific RealWorld scenario coverage out of this shell workflow.

# Integration Points

- `realworld-api-st` targets `realworld-api` through Quarkus REST Client configuration.
- Later workflows can add REST Client interfaces or tests that reuse `quarkus.rest-client.service_uri.url` via config key `service_uri`.
- `README.md` documents local run/test commands and target URL override guidance.
- SLDD predecessor decisions are in:
  - `.sldd/specs/workspace-quarkus-baseline/`
  - `.sldd/specs/realworld-api-contract-baseline/`
  - `.sldd/specs/realworld-system-test-strategy/`

# Risks and Unknowns

- Generated sample scaffold `dev.realworld.MyRemoteService` still exists and points at `https://stage.code.quarkus.io/api`; it is not RealWorld behavior and should not be expanded.
- `TargetApiClient#hello()` currently references `/hello`; Step 01 now decides to skip initial smoke coverage because `realworld-api` currently exposes no endpoint. Treat `/hello` as scaffold metadata, not an approved smoke scenario.
- `realworld-api` currently has no exposed endpoint suitable for this workflow's first smoke scenario.
- Quarkus update check reports the project is up to date via dry run. The tool report also shows local/current version `3.36.1` while its latest-release index reports `3.34.1`; no version change is part of this workflow.

# Context to Carry Into Steps 02-06

- Step 02 should design the minimal shell around the approved JUnit/Quarkus test-suite execution model, without app endpoints or startup test runners.
- Step 02 should decide whether to remove, ignore, or replace generated sample REST Client scaffold without introducing endpoint-specific RealWorld smoke tests.
- Step 03 should preserve the existing configuration bridge and strategy contracts unless there is an approved reason to change them.
- Step 04 tests should not require a running `realworld-api` or any currently nonexistent endpoint.
- Step 05 implementation should make only minimal shell changes needed by approved tests and should not add business endpoint scenarios.
