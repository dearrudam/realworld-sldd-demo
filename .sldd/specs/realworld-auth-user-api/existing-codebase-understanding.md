# Existing Codebase Understanding — RealWorld Auth and User API

## Repository Structure Overview

```
realworld-api/              # REST API application (port 8080)
  src/main/
    resources/
      application.properties
    docker/                   # Container config stubs
  src/test/
    java/dev/realworld/
      ApplicationShellHealthTest.java    # QuarkusTest — health endpoint smoke tests
      ApplicationShellHealthIT.java      # QuarkusIntegrationTest — same tests, native/container

realworld-api-st/           # Standalone HTTP system-test application (port 8081)
  src/main/
    java/dev/realworld/systemtest/
      boundary/
        TargetApiClient.java             # REST Client interface (empty, no methods yet)
      entity/
        AuthenticationMode.java          # enum: { NONE }
        DataIsolationMode.java           # enum: { SCENARIO_OWNED_UNIQUE_DATA }
        SystemTestStrategy.java          # record: target URI + auth + isolation + verification boundary
        VerificationBoundary.java        # enum: { STRATEGY_CONTRACT_ONLY }
    resources/
      application.properties
  src/test/
    java/dev/realworld/systemtest/
      boundary/
        TargetApiClientContractTest.java       # Verifies RestClient registration + config key
        GeneratedSampleRemovalTest.java
      control/
        ApplicationPropertiesContractTest.java # Verifies config bridge conventions
      entity/
        SystemTestStrategyTest.java

scripts/
  check-realworld-contract-baseline.sh       # Validates contract baseline artifacts
  check-workspace-quarkus-baseline.sh        # Validates workspace structure
  check-realworld-architecture-baseline.sh   # Validates architecture conventions
```

## Architecture Summary

### Current State

- **realworld-api** is a bare application shell. It has:
  - `quarkus-rest` (JAX-RS) for REST endpoints
  - `quarkus-arc` for CDI
  - `quarkus-smallrye-health` for `/q/health` endpoints
  - No production code, no business components, no persistence, no security
  - Only `ApplicationShellHealthTest` + `ApplicationShellHealthIT` as tests

- **realworld-api-st** is a system-test shell. It has:
  - `quarkus-rest-client` + `quarkus-rest-client-jackson` to call realworld-api over HTTP
  - BCE package structure: `dev.realworld.systemtest.{boundary, control, entity}`
  - Strategy pattern for system-test configuration (target URI, auth mode, isolation, boundary)
  - `TargetApiClient` is an empty RestClient interface — no endpoint methods yet
  - Only contract/strategy tests, no business scenario tests

### Planned from Architecture Baseline (not yet implemented)

- BCE layering: `dev.realworld.<component>.{boundary, control, entity}`
- `quarkus-jnosql-mongodb` for persistence
- `quarkus-smallrye-jwt` for JWT security
- `quarkus-rest-jsonb` for JSON payloads (currently no JSONB or Jackson in realworld-api)
- System tests via realworld-api-st calling realworld-api over HTTP

### Key Dependencies (realworld-api/pom.xml)

| Extension | Purpose |
|---|---|
| `quarkus-rest` | JAX-RS REST endpoints |
| `quarkus-arc` | CDI dependency injection |
| `quarkus-smallrye-health` | Health check endpoints |
| `quarkus-junit` | Test framework |
| `rest-assured` | HTTP test assertions |

### Key Dependencies (realworld-api-st/pom.xml)

| Extension | Purpose |
|---|---|
| `quarkus-rest-client` | HTTP client to call realworld-api |
| `quarkus-rest-client-jackson` | JSON deserialization for REST client |
| `quarkus-arc` | CDI |
| `quarkus-rest` | JAX-RS |
| `quarkus-junit` | Test framework |
| `rest-assured` | HTTP test assertions |

## Conventions to Preserve

1. **BCE package naming**: `dev.realworld.<component>.{boundary, control, entity}` (architecture baseline)
2. **System test pattern**: RestClient interface per API surface, configured via `application.properties` with the `realworld-api.base-url` → `quarkus.rest-client.service_uri.url` bridge
3. **Test layering**:
   - Unit tests: plain JUnit 5, no Quarkus bootstrap
   - Integration tests: `@QuarkusTest` for in-process testing
   - System tests: standalone application (realworld-api-st) calling realworld-api over HTTP
4. **Health check pattern** from `ApplicationShellHealthTest`: RestAssured `given()/when()/then()` with `@QuarkusTest`
5. **Config pattern**: Port-based separation (8080 realworld-api, 8081 realworld-api-st), `realworld-api.base-url` as the bridge property
6. **Error handling**: RealWorld `errors` JSON envelope per contract baseline

## Integration Points

| Source | Target | Mechanism | Config |
|---|---|---|---|
| realworld-api-st | realworld-api | HTTP (REST Client) | `realworld-api.base-url=http://localhost:8080` |
| realworld-api-st TargetApiClient | realworld-api endpoints | MicroProfile RestClient | `quarkus.rest-client.service_uri.url=${realworld-api.base-url}` |

## Risks and Unknowns

1. **Missing extensions** — The following extensions are not yet installed and must be added in this workflow:
   - `quarkus-jnosql-mongodb` (persistence — MongoDB via Jakarta Data)
   - `quarkus-smallrye-jwt` (JWT token auth)
   - `quarkus-rest-jsonb` (JSON-B binding for envelope serialization)
   - `quarkus-jnosql-mongodb` requires annotation processor config for Java 25

2. **No BCE packages exist yet** in realworld-api. The auth/user business component (e.g., `dev.realworld.auth.{boundary, control, entity}`) must be created from scratch.

3. **TargetApiClient is empty** — auth/user endpoint methods must be added for system tests.

4. **AuthenticationMode is currently `NONE`** — it will need to support a `BEARER_TOKEN` variant for authenticated system tests in this and downstream workflows.

5. **API contract baseline is established** — request/response envelopes for auth/user endpoints are pre-defined in `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md`.

## Context to Carry Into Steps 02-06

- The API shell is bare — all business code (resources, services, repositories, entities) must be created from scratch.
- The system-test shell has BCE structure ready — new RestClient methods and test scenarios will be added to existing patterns.
- MongoDB + JNoSQL will be the persistence layer; Dev Services handles embedded MongoDB for dev/test.
- JWT with RS256 + `quarkus-smallrye-jwt` will be the auth layer; configure with dev/test profiles.
- Test layering convention: unit → `@QuarkusTest` integration → system-test via realworld-api-st.
- All RealWorld JSON envelopes and error conventions are baseline-approved.
