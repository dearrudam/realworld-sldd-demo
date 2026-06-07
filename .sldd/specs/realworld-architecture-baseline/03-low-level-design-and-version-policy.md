# Requirement-to-Design Traceability

| Requirement | Concrete coverage |
|---|---|
| BCE conventions | Architecture artifact must include `dev.realworld.<business-component>.<boundary|control|entity>` and define boundary, control, entity responsibilities. |
| MongoDB/JNoSQL persistence | Architecture artifact must name MongoDB and `quarkus-jnosql-mongodb`, while stating this workflow does not add persistence code or dependencies. |
| JSON-B REST payloads | Architecture artifact must name JSON-B and `quarkus-rest-jsonb` for future RealWorld JSON envelopes. |
| JWT bearer security | Architecture artifact must name JWT, `quarkus-smallrye-jwt`, and `Authorization: Bearer <token>` compatibility. |
| Configuration decisions | Architecture artifact must require profile-scoped Quarkus configuration and keep ports/base URLs from the workspace baseline. |
| Test layering | Architecture artifact must distinguish unit tests, `@QuarkusTest` integration tests, and standalone HTTP system tests in `realworld-api-st`. |
| Application/ST boundaries | Architecture artifact must state that `realworld-api-st` calls `realworld-api` over HTTP and does not depend on production Java classes. |
| Executable verification | Add `scripts/check-realworld-architecture-baseline.sh` with required-text checks. |
| README integration | Root README must link the architecture artifact and list the verification command. |

# API Contracts

This workflow does not add or change HTTP API contracts. All endpoint contracts remain owned by `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md`.

Future endpoint workflows must keep the RealWorld JSON envelope and error conventions compatible with the contract baseline while using JSON-B-compatible representation types.

# Data Models

No Java data models, MongoDB documents, indexes, repositories, or JSON DTOs are introduced by this workflow. Future workflows should place domain records/documents in the owning business component's `entity` package and map JSON envelopes in boundary classes.

Future MongoDB/JNoSQL designs must decide document ownership, identifier strategy, indexes, relation/reference shape, consistency rules, and query patterns before implementation.

# Error Model

No runtime error behavior is introduced by this workflow. Future endpoint workflows must keep error responses compatible with the contract baseline's `errors` envelope and status categories.

JWT failures should map to `401`, authorization failures to `403`, missing resources to `404`, and validation failures to `422` unless a future approved contract change says otherwise.

# Test Strategy

- Red phase: create `scripts/check-realworld-architecture-baseline.sh` before the architecture artifact and README references exist; run it and confirm expected failure.
- Green phase: create the architecture artifact, update README references, restore any missing required workspace directory, and rerun the script until it passes.
- Verification phase: run all repository baseline scripts:
  - `./scripts/check-realworld-contract-baseline.sh`
  - `./scripts/check-workspace-quarkus-baseline.sh`
  - `./scripts/check-realworld-architecture-baseline.sh`

# Test Scenario Catalog

- Missing architecture artifact fails verification.
- Missing required BCE package convention fails verification.
- Missing MongoDB or `quarkus-jnosql-mongodb` decision fails verification.
- Missing JSON-B or `quarkus-rest-jsonb` decision fails verification.
- Missing JWT or `quarkus-smallrye-jwt` decision fails verification.
- Missing configuration convention fails verification.
- Missing test-layering decision fails verification.
- Missing `realworld-api` / `realworld-api-st` boundary decision fails verification.
- Missing README link or verification command fails verification.

# Dependency and Version Policy

Current dependency set is sufficient for this documentation-and-verification workflow. No Maven or Quarkus extension changes are required now.

Future dependency direction:

- Persistence: `quarkus-jnosql-mongodb`, with version and availability validated before installation. If the extension is outside the Quarkus platform BOM, the future workflow must document its coordinates and compatibility constraints.
- JSON: `quarkus-rest-jsonb`, version-managed by the Quarkus BOM when added.
- JWT/security: `quarkus-smallrye-jwt`, version-managed by the Quarkus BOM when added.
- System-test JSON client alignment: evaluate `quarkus-rest-client-jsonb` in a future system-test workflow if JSON-B client mapping is required.

Runtime impact: none in this workflow. Test impact: adds one repository shell verification script. Maintenance impact: downstream workflows get a stable architecture reference and an executable guard.

# Ordered Implementation Plan

1. Save approved Step 99, Step 02, and Step 03 artifacts.
2. Create `scripts/check-realworld-architecture-baseline.sh` and run it before the implementation artifact exists to confirm Red.
3. Create `.sldd/specs/realworld-architecture-baseline/realworld-architecture-baseline.md`.
4. Update `README.md` with the architecture baseline link and verification command.
5. Restore `realworld-api-st/src/test/java` if missing so the existing workspace baseline check remains valid.
6. Rerun the architecture baseline check to confirm Green.
7. Run all baseline checks and save Step 06 verification.
