# Compliance Matrix

| Requirement | Verification | Result |
| --- | --- | --- |
| `realworld-api` exists as a Quarkus Maven project | `scripts/verify-scaffold.sh` checked `realworld-api/pom.xml`; `mvn verify` ran in `realworld-api` | Pass |
| `realworld-api-st` exists as a standalone Quarkus Maven project | `scripts/verify-scaffold.sh` checked `realworld-api-st/pom.xml`; `mvn verify` ran in `realworld-api-st` | Pass |
| `realworld-api` includes REST JSON-B | `scripts/verify-scaffold.sh` checked `quarkus-rest-jsonb` in `realworld-api/pom.xml` | Pass |
| `realworld-api` includes JNoSQL MongoDB | `scripts/verify-scaffold.sh` checked `quarkus-jnosql-mongodb` in `realworld-api/pom.xml` | Pass |
| `realworld-api-st` includes REST Client | `scripts/verify-scaffold.sh` checked `quarkus-rest-client` in `realworld-api-st/pom.xml` | Pass |
| `realworld-api-st` does not depend on `realworld-api` internals | `scripts/verify-scaffold.sh` checked no Maven dependency on `realworld-api` in `realworld-api-st/pom.xml` | Pass |
| README documents Quarkus, SLDD, and both apps | `scripts/verify-scaffold.sh` and read-only inspection checked root `README.md` | Pass |
| Root and subproject guidance files exist | `scripts/verify-scaffold.sh` checked root and subproject `AGENTS.md` files | Pass |
| `.gitignore` covers Java, Maven, and Quarkus outputs | `scripts/verify-scaffold.sh` and read-only inspection checked root `.gitignore` | Pass |

# Version and Dependency Validation

- `realworld-api` generated with Quarkus platform `3.36.1`.
- `realworld-api-st` generated with Quarkus platform `3.36.1`.
- `realworld-api` includes `io.quarkus:quarkus-rest-jsonb`.
- `realworld-api` includes `io.quarkiverse.jnosql:quarkus-jnosql-mongodb` version `3.4.12`.
- `realworld-api-st` includes `io.quarkus:quarkus-rest-client`.
- `realworld-api-st` also includes generator-added `io.quarkus:quarkus-rest-client-jackson`; this does not violate the approved scaffold boundary, but future JSON serialization choices should be reviewed in later SLDD work if typed payload tests are added.
- The generated projects use Maven and remain independent sibling projects, not a root multi-module build.

# Test Convention Compliance

- Step 04 Red test artifact was preserved: `scripts/verify-scaffold.sh` was not modified during Step 05 or Step 06.
- Step 06 re-ran the scaffold verification from the repository root: `rtk sh scripts/verify-scaffold.sh`.
- Step 06 re-ran the API build from `realworld-api`: `rtk mvn verify`.
- Step 06 re-ran the system-test app build from `realworld-api-st`: `rtk mvn verify`.
- `realworld-api` Maven verification passed with 2 tests, 0 failures, 0 errors, 0 skipped.
- `realworld-api-st` Maven verification passed; no tests are currently present in the generated scaffold.
- The API build used Quarkus Dev Services/Testcontainers for MongoDB during generated scaffold tests.

# Risks by Severity

## Medium

- Generated Quarkus codestart examples are present, including greeting and JNoSQL sample classes. They are documented as scaffold-only and are not RealWorld API contract.
- `realworld-api-st` includes `quarkus-rest-client-jackson` from generation even though Step 03 selected plain REST Client. The required `quarkus-rest-client` dependency is present and the black-box boundary is preserved.

## Low

- The generated Maven wrappers were not used for verification because Quarkus MCP refused untracked wrapper scripts. System Maven verification passed.
- `realworld-api` JNoSQL annotation processors warn that they support source version 17 while Maven compiles with release 25. The build still passes.

# Remediation Steps

- In the next RealWorld behavior workflow, explicitly decide whether to keep or remove generated starter resources before treating any endpoint as part of the API contract.
- In a future system-test workflow, decide whether `realworld-api-st` should keep `quarkus-rest-client-jackson`, switch to `quarkus-rest-client-jsonb`, or remain with plain REST Client only.
- Continue using standalone HTTP-based tests for `realworld-api-st`; do not introduce shared DTO or Maven dependencies on `realworld-api`.
- Keep running `sh scripts/verify-scaffold.sh` after scaffold-affecting changes.

# Go/No-Go Decision and Rationale

Go.

The scaffold satisfies the approved Step 01 intent, Step 02 architecture, Step 03 low-level design, Step 04 Red tests, and Step 05 Green implementation constraints. The required Quarkus applications exist, dependencies are present, documentation and guidance are updated, the black-box boundary is preserved, and all verification commands pass.
