# Compliance Matrix

| Requirement | Evidence | Status |
|---|---|---|
| Two Quarkus application roots exist | `realworld-api/`, `realworld-api-st/` | Pass |
| Each application has Maven wrapper and build descriptor | `mvnw`, `pom.xml` in both app roots | Pass |
| Standard source/resource/test structure exists | `src/main/java`, `src/main/resources`, `src/test/java` in both app roots | Pass |
| Build/run/test conventions documented | `README.md`, app READMEs, `workspace-quarkus-baseline.md` | Pass |
| Local port and target URL conventions documented/configured | `quarkus.http.port=8080`, `quarkus.http.port=8081`, `realworld-api.base-url=http://localhost:8080` | Pass |
| Predecessor contract baseline is referenced | `workspace-quarkus-baseline.md` references `realworld-api-contract-baseline.md` | Pass |
| No RealWorld business endpoint behavior implemented | Documentation scope boundary; only generated Quarkus scaffolding exists | Pass |

# Version and Dependency Validation

- `realworld-api` was generated as a Maven Quarkus application with the selected `quarkus-rest` extension.
- `realworld-api-st` was generated as a Maven Quarkus application with the selected `quarkus-rest-client` extension.
- `quarkus-rest` was also added to `realworld-api-st` after approval because the approved baseline requires an HTTP app port convention and Quarkus warned that `quarkus.http.port` is ignored without a server extension.
- Quarkus platform/tooling selected version `3.36.1` in the generated applications.
- No RealWorld runtime business dependencies, persistence dependencies, or security implementation dependencies were added.

# Test Convention Compliance

Step 04 Red:

```bash
./scripts/check-workspace-quarkus-baseline.sh
```

Initial result: failed because `.sldd/specs/workspace-quarkus-baseline/workspace-quarkus-baseline.md` and the Quarkus app roots did not exist.

Step 05 Green:

```bash
./scripts/check-workspace-quarkus-baseline.sh
./scripts/check-realworld-contract-baseline.sh
```

Final result: both checks passed.

Quarkus dev test evidence:

- `realworld-api`: `devui-testing_runTests` passed 1 test (`GreetingResourceTest#testHelloEndpoint`).
- `realworld-api-st`: `devui-testing_runTests` completed with 0 failing tests and 0 tests present.

The Step 04 workspace check was not modified during Step 05.

# Risks by Severity

- Medium: `realworld-api-st` currently contains generated REST Client sample scaffolding that points to a Quarkus registry endpoint. It is starter scaffolding only and should be replaced by the system-test strategy workflow.
- Low: The applications are separate Maven roots, which duplicates wrapper/build files but keeps early app boundaries explicit.
- Low: Generated starter endpoint in `realworld-api` is not RealWorld behavior and should be removed or replaced by the application shell workflow.

# Remediation Steps

- In `realworld-architecture-baseline`, define package/BCE structure and whether generated starter classes remain.
- In `realworld-system-test-strategy`, replace REST Client sample scaffolding with target-API test strategy code.
- In `realworld-api-application-shell`, add the minimal RealWorld API application shell without implementing business endpoints.

# Go/No-Go Decision and Rationale

Go.

The workspace baseline is complete: both Quarkus application roots exist, build/run conventions are documented, local port/target URL conventions are configured, verification checks pass, and no RealWorld business endpoint behavior was introduced beyond generated Quarkus starter scaffolding.
