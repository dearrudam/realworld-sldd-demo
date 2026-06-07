# Requirement-to-Design Traceability

| Requirement | Low-level coverage |
|---|---|
| Minimal `realworld-api-st` shell | Delete generated external sample client `dev.realworld.MyRemoteService`; keep Quarkus app structure and existing system-test package. |
| Configurable target API URL | Preserve `application.properties` values for `realworld-api.base-url` and `quarkus.rest-client.service_uri.url`. |
| Place for later scenario tests | Keep `TargetApiClient` as a REST Client registration contract and preserve `SystemTestStrategy` defaults. |
| JUnit/Quarkus test-suite execution model only | Do not add resource classes, test-control endpoints, startup observers, schedulers, or command-mode runners. |
| Skip initial smoke scenario | Remove endpoint-specific `TargetApiClient#hello()` method and verify the client declares no endpoint methods in this workflow. |
| Build and run locally | Keep Maven/Quarkus configuration and README instructions current. |

# API Contracts

- `dev.realworld.systemtest.boundary.TargetApiClient`
  - Annotation: `@RegisterRestClient(configKey = "service_uri")`.
  - No declared endpoint methods in this shell workflow.
  - Serves as a stable registration/configuration contract for later endpoint-specific clients or methods.
- `src/main/resources/application.properties`
  - `quarkus.http.port=8081`.
  - `realworld-api.base-url=http://localhost:8080`.
  - `quarkus.rest-client.service_uri.url=${realworld-api.base-url}`.
- README
  - Must state that the shell currently performs no live target API smoke call because the target has no exposed endpoint.
  - Must state that execution is through JUnit/Quarkus tests, not application endpoints or startup execution.

# Data Models

- `SystemTestStrategy`
  - Keep fields `targetApiBaseUrl`, `authenticationMode`, `dataIsolationMode`, and `verificationBoundary`.
  - Keep `defaults(URI)` behavior.
- `AuthenticationMode`
  - Keep `NONE`.
- `DataIsolationMode`
  - Keep `SCENARIO_OWNED_UNIQUE_DATA`.
- `VerificationBoundary`
  - Keep `STRATEGY_CONTRACT_ONLY`.

# Error Model

- No HTTP error mapping is introduced in this shell workflow because no target endpoint is called.
- No target connectivity failures are modeled until a later workflow adds a real health or business endpoint smoke scenario.
- `SystemTestStrategy.defaults(URI)` continues to reject `null` target URLs through `Objects.requireNonNull`.

# Test Strategy

- Add or update focused JUnit tests that run without a live `realworld-api`.
- Tests should verify structural shell contracts:
  - no generated external sample client remains;
  - `TargetApiClient` keeps `service_uri` registration;
  - `TargetApiClient` declares no endpoint methods for this shell workflow;
  - configuration bridge and strategy defaults remain intact.
- Do not add tests that call `/hello`, `/q/health`, `/api/tags`, or any other live target API endpoint.

# Test Scenario Catalog

| ID | Scenario | Expected red reason before implementation | Acceptance criteria |
|---|---|---|---|
| STSH-001 | `GeneratedSampleRemovalTest` verifies `dev.realworld.MyRemoteService` is absent. | Generated sample class still exists. | Minimal shell; no external Quarkus registry sample behavior. |
| STSH-002 | `TargetApiClientContractTest` verifies `service_uri` registration and zero endpoint methods. | `TargetApiClient#hello()` still exists. | Configurable target URL retained; no initial smoke scenario. |
| STSH-003 | `ApplicationPropertiesContractTest` continues to verify target URL bridge. | Fails only if configuration drift occurs. | Configurable target URL. |
| STSH-004 | `SystemTestStrategyTest` continues to verify strategy defaults. | Fails only if predecessor strategy contracts drift. | Place for later scenario-oriented tests. |
| STSH-005 | README review documents JUnit/Quarkus execution and no live smoke. | README still implies endpoint-specific behavior or omits shell boundary. | Build/run locally and approved execution model. |

# Dependency and Version Policy

- Current dependency set is sufficient:
  - `quarkus-rest-client`
  - `quarkus-rest-client-jackson`
  - `quarkus-rest`
  - `quarkus-arc`
  - `quarkus-junit`
  - `rest-assured` test dependency
- No new dependencies are required.
- No dependency or Quarkus platform version changes are part of this workflow.
- Compatibility constraint: keep Quarkus platform `3.36.1` and Java release `25` as currently configured.
- Runtime impact: removing sample client and endpoint method reduces accidental external behavior; the app remains a Quarkus shell with configuration contracts.
- Test impact: tests remain local and deterministic; no live target API is required.
- Maintenance impact: later endpoint workflows can add methods/tests deliberately when target endpoints exist.

# Ordered Implementation Plan

1. Step 04 Red: add/update tests for generated sample removal and zero endpoint methods on `TargetApiClient`; do not change production code except minimal compile-preserving stubs if absolutely necessary.
2. Confirm Red: tests fail because `dev.realworld.MyRemoteService` and `TargetApiClient#hello()` still exist.
3. Step 05 Green: delete `MyRemoteService`, remove `hello()` and its JAX-RS method/path imports from `TargetApiClient`, and update README to document the shell boundary and no live smoke test.
4. Run affected/all tests through Quarkus Dev MCP test tools.
5. Step 06: record verification evidence and mark the workflow complete.
