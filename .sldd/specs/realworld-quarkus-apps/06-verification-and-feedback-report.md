# Compliance Matrix

| Approved requirement | Verification result | Evidence |
| --- | --- | --- |
| Generate `realworld-api` as an independent Quarkus Maven application | Pass | `realworld-api/pom.xml` exists and `./mvnw test` succeeds in `realworld-api`. |
| Generate `realworld-api-st` as an independent Quarkus Maven application | Pass | `realworld-api-st/pom.xml` exists and `./mvnw test` succeeds in `realworld-api-st`. |
| Use Maven groupId `org.soujava.demo.sldd` | Pass | Both generated POMs use the approved groupId. |
| Add REST JSON-B support to `realworld-api` | Pass | `realworld-api/pom.xml` declares `io.quarkus:quarkus-rest-jsonb`. |
| Add requested JNoSQL MongoDB support to `realworld-api` without fallback | Pass | Quarkus generation resolved `io.quarkiverse.jnosql:quarkus-jnosql-mongodb`; `realworld-api/pom.xml` declares it. |
| Add REST Client support to `realworld-api-st` | Pass | `realworld-api-st/pom.xml` declares `io.quarkus:quarkus-rest-client`. |
| Keep system-test app decoupled from API internals | Pass | Scaffold verification confirms `realworld-api-st` does not declare a Maven dependency on `realworld-api`; generated sample client code was removed so future clients can target the RealWorld API only. |
| Add workspace and subproject guidance | Pass | Root, API, and system-test `AGENTS.md` files exist and document SLDD, Quarkus, and black-box boundary rules. |
| Update README from inherited template | Pass | README describes Quarkus, SLDD, `realworld-api`, and `realworld-api-st`, and no longer contains `[YOUR_FRAMEWORK]` placeholders. |
| Update `.gitignore` for Java/Maven/Quarkus outputs | Pass | Root `.gitignore` includes Maven/Quarkus build outputs, Maven wrapper jar ignores, and log ignores. |

# Version and Dependency Validation

- Quarkus platform version: `3.36.0` in both generated Maven projects.
- Java release: `25` in both generated Maven projects.
- `realworld-api` approved dependencies are present:
  - `io.quarkus:quarkus-rest-jsonb`
  - `io.quarkiverse.jnosql:quarkus-jnosql-mongodb:3.4.12`
- `realworld-api-st` approved dependency is present:
  - `io.quarkus:quarkus-rest-client`
- `realworld-api-st` has no dependency on `org.soujava.demo.sldd:realworld-api`.
- Both projects were generated as independent sibling Maven projects, not a root multi-module build.

# Test Convention Compliance

- Step 04 created `scripts/verify-realworld-scaffold.py` as an executable scaffold verification test artifact.
- The verification script failed before scaffolding, confirming the Red phase.
- Step 05 did not modify the Step 04 test artifact after Red confirmation.
- The verification script passed after scaffolding and documentation/guidance updates.
- Both generated Quarkus applications pass `./mvnw test`.
- Generated sample code and generated sample tests were removed because concrete RealWorld endpoint behavior and standalone HTTP system-test scenarios are deferred to later SLDD workflows.

# Risks by Severity

## Medium

- The JNoSQL MongoDB extension resolved successfully, but future persistence workflows may still need explicit MongoDB runtime configuration and integration tests.
- The standalone system-test app currently has no executable HTTP scenarios because this scaffold workflow intentionally defers RealWorld behavior.

## Low

- The generated applications use Quarkus `3.36.0` and Java release `25`; future environments should use a compatible JDK.
- The workspace remains an independent sibling-project layout. A root aggregate build can be introduced later if approved by another SLDD workflow.

# Remediation Steps

1. Start a new SLDD workflow for the first concrete RealWorld API behavior slice.
2. Add black-box HTTP system-test scenarios in `realworld-api-st` only after the corresponding acceptance criteria and design are approved.
3. Add MongoDB runtime configuration and persistence tests when a persistence-backed RealWorld behavior is in scope.
4. Consider a later SLDD workflow for aggregate build orchestration if repeated sibling-project commands become cumbersome.

# Go/No-Go Decision and Rationale

Go.

The approved scaffold is complete: both Quarkus applications exist, required dependencies are present, documentation and guidance files are updated, scaffold verification passes, and both application builds pass. The remaining RealWorld API behavior is intentionally out of scope for this workflow and should proceed through future SLDD cycles.
