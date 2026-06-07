# Repository Structure Overview

The repository contains two Quarkus Maven applications and SLDD workflow state:

- `realworld-api/` is the future RealWorld backend API application.
- `realworld-api-st/` is a standalone Quarkus application for HTTP system tests against `realworld-api`.
- `.sldd/specs/` stores workflow journals and approved artifacts.
- `scripts/` stores repository-level verification checks.

The current workspace baseline created application scaffolding only. RealWorld endpoint behavior is not implemented yet.

# Architecture Summary

`realworld-api` currently depends on Quarkus REST and ArC. It contains generated greeting scaffolding and no RealWorld business components. `realworld-api-st` currently depends on Quarkus REST, ArC, REST Client, and REST Client Jackson. It is intended to hold executable system tests and REST client interfaces for the API application.

The architecture baseline decision from Step 01 is MongoDB persistence with `quarkus-jnosql-mongodb`, JSON-B REST payload support with `quarkus-rest-jsonb`, and JWT bearer security direction with `quarkus-smallrye-jwt`. These are architecture decisions for downstream workflows; the current POM files do not yet include those extensions.

# Conventions to Preserve

- Use Java 25 and Quarkus 3.36.1 as already declared in both Maven projects.
- Keep RealWorld business behavior out of this architecture baseline workflow.
- Keep `realworld-api` as the service application on port `8080`.
- Keep `realworld-api-st` as the standalone system-test application on port `8081`, targeting `http://localhost:8080`.
- Use SLDD journals as journal-only state; store artifact content in Markdown files.
- Follow Quarkus extension-first rules before adding future capabilities.
- Follow BCE package organization for future RealWorld business components.

# Integration Points

- API behavior traces to `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md`.
- Workspace structure traces to `.sldd/specs/workspace-quarkus-baseline/workspace-quarkus-baseline.md`.
- Future endpoint workflows should consume this architecture baseline before adding packages, dependencies, persistence, JSON serialization, JWT security, or system tests.

# Risks and Unknowns

- The generated greeting resource remains in `realworld-api` and should be removed by the first endpoint implementation workflow that establishes RealWorld API resources.
- `quarkus-jnosql-mongodb` availability and compatibility must be validated before dependency installation.
- `quarkus-rest-jsonb` and `quarkus-smallrye-jwt` must be added only in a future implementation workflow that performs extension discovery and receives approval to change `pom.xml`.
- The `realworld-api-st` POM currently uses REST Client Jackson; future system-test JSON payload mapping should revisit whether to align with JSON-B via `quarkus-rest-client-jsonb`.

# Context to Carry Into Steps 02-06

The architecture baseline should be a documentation and verification workflow, not an endpoint implementation workflow. It should produce a durable architecture artifact and an executable repository script that verifies the artifact and README references are present. No Maven dependency changes should be made in this workflow.
