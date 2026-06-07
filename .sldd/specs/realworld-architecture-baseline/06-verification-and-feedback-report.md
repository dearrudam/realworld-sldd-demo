# Compliance Matrix

| Requirement | Evidence | Result |
|---|---|---|
| BCE conventions documented | `.sldd/specs/realworld-architecture-baseline/realworld-architecture-baseline.md` includes the `dev.realworld.<business-component>.<boundary|control|entity>` convention and layer responsibilities. | Pass |
| MongoDB/JNoSQL persistence direction documented | Architecture artifact records MongoDB and `quarkus-jnosql-mongodb`. | Pass |
| JSON-B direction documented | Architecture artifact records JSON-B and `quarkus-rest-jsonb`. | Pass |
| JWT security direction documented | Architecture artifact records JWT, `quarkus-smallrye-jwt`, and `Authorization: Bearer <token>` compatibility. | Pass |
| Configuration and test layering documented | Architecture artifact records profile-scoped Quarkus configuration and unit/integration/system test layering. | Pass |
| App/ST boundary clarified | Architecture artifact states that `realworld-api-st` calls `realworld-api` over HTTP and does not depend on production Java classes. | Pass |
| Executable verification added | `scripts/check-realworld-architecture-baseline.sh` verifies the architecture artifact and README integration. | Pass |
| README updated | `README.md` links the architecture baseline, lists the verification command, and references relevant Quarkus guides. | Pass |

# Version and Dependency Validation

No Maven dependencies, plugins, Quarkus extensions, or wrapper files were changed by this workflow.

Quarkus update checks were run for both applications. The dry-run output reported both projects as up to date for their configured Quarkus setup. The tool also reported a lower latest-version label than the current configured `3.36.1`, so no downgrade or automated update was applied.

Future dependency additions remain gated:

- `quarkus-jnosql-mongodb` must be validated for availability and compatibility before installation.
- `quarkus-rest-jsonb` should be selected for API JSON-B support when JSON payload code is introduced.
- `quarkus-smallrye-jwt` should be selected for protected endpoint workflows when JWT security is implemented.

# Test Convention Compliance

Red phase was confirmed with:

```bash
./scripts/check-realworld-architecture-baseline.sh
```

Expected failing result:

```text
architecture baseline check failed: missing file: .sldd/specs/realworld-architecture-baseline/realworld-architecture-baseline.md
```

Green and final verification were confirmed with:

```bash
./scripts/check-realworld-contract-baseline.sh
./scripts/check-workspace-quarkus-baseline.sh
./scripts/check-realworld-architecture-baseline.sh
```

Passing results:

```text
contract baseline check passed
workspace baseline check passed
architecture baseline check passed
```

During Green, `realworld-api-st/src/test/java` was restored with `.gitkeep` because the workspace baseline check requires the directory to exist.

# Risks by Severity

- Medium: `quarkus-jnosql-mongodb` may be outside the Quarkus platform-managed extension set or may require explicit version compatibility validation.
- Medium: The system-test module currently uses `quarkus-rest-client-jackson`; future JSON-B-aligned system-test payload mapping should evaluate `quarkus-rest-client-jsonb`.
- Medium: JWT signing, verification, claims, roles, expiration, keys, and secrets remain undefined until future security workflows.
- Low: Generated greeting scaffolding remains in `realworld-api`; future endpoint workflows should remove it when introducing RealWorld resources.
- Low: The architecture baseline is documentation plus shell verification; package-level enforcement should be added once RealWorld business components exist.

# Remediation Steps

- In the first persistence workflow, run dynamic Quarkus extension discovery for `quarkus-jnosql-mongodb` and document coordinates/compatibility before changing `pom.xml`.
- In the first JSON endpoint workflow, add `quarkus-rest-jsonb` only after extension discovery and approval.
- In the first authenticated endpoint workflow, add `quarkus-smallrye-jwt` only after extension discovery and approval.
- In the first endpoint workflow, remove generated greeting scaffolding when introducing RealWorld resources.
- Add package and dependency checks after RealWorld business components and selected extensions exist.

# Go/No-Go Decision and Rationale

Go. The architecture baseline is complete, the selected MongoDB/JNoSQL, JSON-B, and JWT directions are documented, executable verification passes, no runtime dependencies were changed, and downstream workflows now have a concrete package, persistence, configuration, security, JSON, test-layering, and extension-policy reference.
