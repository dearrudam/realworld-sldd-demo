# Existing Codebase Understanding: RealWorld Standalone System-Test Strategy

## Repository Structure Overview

- `realworld-api/` is a Quarkus REST application with generated greeting scaffold, Quarkus REST, ArC, JUnit, and RestAssured test dependencies.
- `realworld-api-st/` is a separate Quarkus application intended for standalone HTTP system tests. It already has REST Client, REST Client Jackson, REST, ArC, JUnit, and RestAssured dependencies.
- Both modules use Java 25, Quarkus platform `3.36.1`, Maven wrapper scripts, and generated Dockerfiles.
- `.sldd/specs/` contains workflow journals and numbered artifacts. This workflow depends on completed workspace and API-contract baseline workflows.

## Architecture Summary

- The API module currently exposes only `GreetingResource` at `/hello`; no RealWorld business endpoints or auth behavior exist yet.
- The system-test module currently contains generated REST Client sample `MyRemoteService`, which points to Quarkus stage APIs and does not represent the RealWorld target.
- `realworld-api-st/src/main/resources/application.properties` already declares:
  - `quarkus.http.port=8081`
  - `realworld-api.base-url=http://localhost:8080`
- The dedicated `-st` module matches the project convention for system tests and should host reusable clients in `src/main/java` plus executable tests in `src/test/java`.

## Conventions to Preserve

- Use Quarkus extension-first behavior; existing REST Client extensions are sufficient for this workflow.
- Keep system-test client interfaces in `realworld-api-st/src/main/java`.
- Use `@RegisterRestClient(configKey = "service_uri")` for REST Client interfaces, preserving the MicroProfile server skill convention.
- Keep endpoint-specific system scenarios out of this baseline workflow.
- Do not add Maven dependencies or change platform versions for this workflow.
- Keep README changes concise and focused on advanced developers.

## Integration Points

- Target API URL: `realworld-api.base-url`, bridged to `quarkus.rest-client.service_uri.url`.
- REST Client contract: a reusable `TargetApiClient` config-key contract for future endpoint-specific clients.
- Strategy metadata: a small production model in `realworld-api-st` can expose authentication, data isolation, and verification-boundary decisions without requiring a running API.
- Future endpoint workflows can add additional REST Client methods or specific clients while keeping the same target URL convention.

## Risks and Unknowns

- Quarkus doc search reports 3.36.0 docs for a 3.36.1 project; the relevant REST Client configuration conventions are unchanged for this scope.
- Generated scaffold code in `realworld-api-st` references an unrelated remote service and should not be treated as a RealWorld contract.
- Real data reset and auth token acquisition cannot be finalized until persistence and security workflows exist.
- Running actual HTTP system tests against `realworld-api` will require either a running target service or later CI orchestration.

## Context to Carry Into Steps 02-06

- Implement the strategy in `realworld-api-st`; avoid production changes in `realworld-api` unless required by a later endpoint workflow.
- Tests should verify strategy contracts without requiring a live `realworld-api` instance.
- Keep behavior minimal: configuration bridge, target-client contract, strategy defaults, and README guidance.
- Do not introduce endpoint-specific scenario tests in this workflow.
