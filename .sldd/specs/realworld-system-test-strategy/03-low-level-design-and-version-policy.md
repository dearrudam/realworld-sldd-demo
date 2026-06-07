# Low-Level Design and Version Policy: RealWorld Standalone System-Test Strategy

## Requirement-to-Design Traceability

| Requirement | Low-level coverage |
|---|---|
| AC1 Target API configuration | `application.properties` defines `quarkus.rest-client.service_uri.url=${realworld-api.base-url}`. `TargetApiClient` uses `@RegisterRestClient(configKey = "service_uri")`. |
| AC2 Data isolation strategy | `SystemTestStrategy` default isolation mode is `SCENARIO_OWNED_UNIQUE_DATA`. |
| AC3 Authentication strategy | `SystemTestStrategy` default authentication mode is `NONE`. |
| AC4 Verification boundaries | `SystemTestStrategy` default boundary is `STRATEGY_CONTRACT_ONLY`; tests assert no endpoint scenario behavior is introduced. |

## API Contracts

- `dev.realworld.systemtest.boundary.TargetApiClient`
  - Annotated with `@RegisterRestClient(configKey = "service_uri")`.
  - Uses Jakarta REST annotations only as a reusable base client contract.
  - Provides a minimal `GET /hello` method only as scaffold connectivity metadata for future smoke checks; this workflow does not add a system-test scenario that calls it.
- `dev.realworld.systemtest.entity.SystemTestStrategy`
  - Record fields: `URI targetApiBaseUrl`, `AuthenticationMode authenticationMode`, `DataIsolationMode dataIsolationMode`, `VerificationBoundary verificationBoundary`.
  - Static factory: `defaults(URI targetApiBaseUrl)`.

## Data Models

- `AuthenticationMode`
  - `NONE`: baseline until security exists.
- `DataIsolationMode`
  - `SCENARIO_OWNED_UNIQUE_DATA`: endpoint scenarios own unique test data and avoid reset assumptions.
- `VerificationBoundary`
  - `STRATEGY_CONTRACT_ONLY`: this workflow verifies contracts/documentation, not endpoint behavior.

## Error Model

- `SystemTestStrategy.defaults(URI)` rejects `null` target URLs with `NullPointerException` through `Objects.requireNonNull`.
- No HTTP error mapping is added because endpoint calls are out of scope.
- No credential or reset errors are modeled until security and persistence workflows define those contracts.

## Test Strategy

- Add focused unit-style tests in `realworld-api-st/src/test/java`.
- Tests must not require a running `realworld-api` process.
- Tests must verify annotation/configuration contracts and strategy defaults.
- README updates are verified by inspection in the final verification step.

## Test Scenario Catalog

| ID | Scenario | Expected red reason before implementation | Acceptance criteria |
|---|---|---|---|
| STS-001 | `TargetApiClientContractTest` verifies `service_uri` registration. | Missing RealWorld target client contract or wrong annotation. | AC1 |
| STS-002 | `SystemTestStrategyTest` verifies default target URL, auth, isolation, and boundary. | Missing strategy model/defaults. | AC2, AC3, AC4 |
| STS-003 | `ApplicationPropertiesContractTest` verifies REST Client URL bridge. | Missing `quarkus.rest-client.service_uri.url` property. | AC1 |

## Dependency and Version Policy

- Current dependencies are sufficient: `quarkus-rest-client`, `quarkus-rest-client-jackson`, `quarkus-rest`, `quarkus-arc`, and `quarkus-junit`.
- No new dependencies are required.
- No Maven or Quarkus platform changes are part of this workflow.
- Compatibility constraint: keep Quarkus `3.36.1` project configuration as-is; update report shows the project is up to date for the installed plugin despite doc-search results being versioned as 3.36.0.

## Ordered Implementation Plan

1. Step 04: create tests and minimal throwing/signature stubs only where needed to compile.
2. Confirm Red: tests fail for the expected unimplemented strategy/configuration behavior.
3. Step 05: implement only the target client contract, strategy model defaults, configuration bridge, and README updates needed by the tests and approved docs.
4. Run affected/all tests in `realworld-api-st` via Quarkus Dev MCP test tools.
5. Step 06: save verification report and mark the workflow complete.
