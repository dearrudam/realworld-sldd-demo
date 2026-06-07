# Compliance Matrix

| Requirement | Evidence | Status |
|---|---|---|
| Minimal `realworld-api-st` Quarkus application shell | Removed generated external sample client `dev.realworld.MyRemoteService`; retained Quarkus app root and system-test package. | Pass |
| Configurable `realworld-api` base URL over HTTP | `application.properties` still defines `realworld-api.base-url=http://localhost:8080` and `quarkus.rest-client.service_uri.url=${realworld-api.base-url}`; `ApplicationPropertiesContractTest` passes. | Pass |
| Place for later scenario-oriented system tests | `TargetApiClient` remains registered with config key `service_uri`; `SystemTestStrategy` defaults remain covered by tests. | Pass |
| JUnit/Quarkus test-suite execution model only | No endpoints, startup runners, or test-control resources were added; verification uses Quarkus test tooling. | Pass |
| No initial smoke scenario | Removed `TargetApiClient#hello()` and verified zero endpoint methods; no test calls `realworld-api`. | Pass |
| Build and run locally | Quarkus dev mode started on port `8081`; README documents run/test commands, execution model, target URL, and no live smoke coverage. | Pass |

# Version and Dependency Validation

- `realworld-api-st` remains on Quarkus platform `3.36.1` and Java release `25`.
- No Maven dependencies or build-plugin versions were changed.
- Earlier Quarkus update dry run reported the project up to date.
- Current dependencies were sufficient for the approved scope: Quarkus REST Client, REST Client Jackson, REST, Arc, Quarkus JUnit, and Rest Assured.

# Test Convention Compliance

Step 04 Red evidence:

- Tool: `devui-testing_runTests`
- Result: 3 passed, 2 failed.
- Expected failures:
  - `TargetApiClientContractTest#declaresNoEndpointSmokeMethods()` failed with `expected: <0> but was: <1>` because `TargetApiClient#hello()` still existed.
  - `GeneratedSampleRemovalTest#generatedExternalSampleClientIsAbsent()` failed because `dev.realworld.MyRemoteService` still existed.

Step 05 Green evidence:

- Tool: `devui-logstream_forceRestart`
- Tool: `devui-testing_runTests`
- Result: 5 passed, 0 failed, 0 skipped.
- Passing tests:
  - `SystemTestStrategyTest#definesBaselineStrategyDefaults()`
  - `TargetApiClientContractTest#registersWithSharedServiceUriConfigKey()`
  - `TargetApiClientContractTest#declaresNoEndpointSmokeMethods()`
  - `GeneratedSampleRemovalTest#generatedExternalSampleClientIsAbsent()`
  - `ApplicationPropertiesContractTest#bridgesTargetApiBaseUrlToSharedRestClientConfigKey()`

Test integrity:

- Step 04 tests were added/updated before production changes.
- Step 05 did not modify tests after Red confirmation.
- No test requires a running `realworld-api` or any nonexistent endpoint.

# Risks by Severity

- Low: `TargetApiClient` is currently a marker-style REST Client registration contract with no endpoint methods. Later endpoint workflows must add concrete methods deliberately when target endpoints exist.
- Low: No live connectivity smoke exists yet. This is intentional because `realworld-api` currently exposes no endpoint for this workflow.
- Low: Quarkus tool update metadata reported a lower latest release index than the project version, but dry run still reported the project up to date.

# Remediation Steps

- When `realworld-api` exposes a health or RealWorld business endpoint, create a separate workflow to add the first live smoke/system scenario.
- Future endpoint workflows should reuse `realworld-api.base-url` and REST Client config key `service_uri`.
- Keep system-test execution JUnit/Quarkus-test based unless a later approved product intent changes the model.

# Go/No-Go Decision and Rationale

Go.

The shell workflow is complete: open Step 01 questions were resolved, brownfield context was captured, design artifacts were approved by instruction, Red tests failed for the expected missing shell behavior, minimal production changes made those tests pass, and README documentation now reflects the approved execution model and no-smoke boundary.
