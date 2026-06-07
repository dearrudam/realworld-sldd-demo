# Product Intent: RealWorld API Architecture Baseline

## Workflow Kind

`feature`

## Parent Workflow-Set

`realworld-quarkus-sldd-workspace`

## Origin

This Step 01 artifact was scaffolded from:

- Parent journal: `../realworld-quarkus-sldd-workspace/_spec-journal.json`
- Parent artifact: `../realworld-quarkus-sldd-workspace/01-workflow-set-plan.md`

## Problem Statement

The RealWorld Quarkus workspace has application scaffolding and a contract baseline, but it still needs a shared implementation architecture baseline before endpoint-focused workflows begin. Downstream workflows need stable decisions for package layout, BCE layering, persistence direction, JSON handling, JWT/security direction, configuration conventions, test layering, and the boundary between the API application and standalone system tests.

## Target Users

- Developers implementing RealWorld API workflows in `realworld-api`.
- Developers writing standalone HTTP system tests in `realworld-api-st`.
- Reviewers validating that downstream SLDD workflows follow a consistent architecture baseline.

## Formalized Exploration Decisions

- Persistence/database baseline: MongoDB using `quarkus-jnosql-mongodb`.
- Initial Quarkus extension baseline for `realworld-api`: current scaffold extensions plus:
  - `quarkus-jnosql-mongodb` for MongoDB/JNoSQL persistence.
  - `quarkus-rest-jsonb` for RealWorld JSON envelopes.
  - `quarkus-smallrye-jwt` for JWT bearer-token security direction.
- Existing scaffold extensions remain part of the baseline:
  - `realworld-api`: `quarkus-rest`, `quarkus-arc`.
  - `realworld-api-st`: `quarkus-rest`, `quarkus-arc`, `quarkus-rest-client`, `quarkus-rest-client-jackson`.
- This Step 01 records architecture intent only. Dependency changes, endpoint implementation, persistence implementation, and security implementation are deferred to later approved steps or workflows.

## Success Metrics

- Downstream designs can derive package and BCE layer placement without introducing new top-level conventions.
- Downstream designs can rely on MongoDB/JNoSQL as the persistence direction.
- Downstream designs can rely on JSON-B-backed JSON envelopes and SmallRye JWT-backed bearer authentication as architecture direction.
- The boundary between production API code and standalone HTTP system tests is explicit.
- The final architecture baseline can be verified by a repository-level check.

## Out of Scope

- Implementing RealWorld endpoints.
- Adding or modifying Maven dependencies in this step.
- Creating MongoDB entities, repositories, indexes, migrations, seed data, or runtime configuration.
- Implementing JWT signing, verification, roles, claims, token expiration, or secret management.
- Defining the complete standalone system-test strategy in detail.
- Defining production deployment architecture.

## Risks and Assumptions

- `quarkus-jnosql-mongodb` availability and compatibility must be validated before dependency installation in a later implementation step.
- JSON and JWT extension choices must be revalidated through Quarkus extension discovery before changing `pom.xml`.
- The contract baseline treats tokens as JWTs but leaves signing, claims, expiration, and secret management to later workflows.
- MongoDB data modeling will require future low-level design decisions for document boundaries, identifiers, indexes, and consistency rules.
- Generated scaffold code may remain until a later endpoint workflow replaces it.

## Acceptance Criteria (Given/When/Then)

- Given future RealWorld API implementation workflows, when they design Java packages, then they use documented BCE package and layer conventions for `realworld-api`.
- Given future persistence workflows, when they design storage, then they use MongoDB with `quarkus-jnosql-mongodb` as the architecture baseline unless a later approved workflow changes the decision.
- Given future JSON API workflows, when they design request and response mapping, then they use RealWorld JSON envelopes with the `quarkus-rest-jsonb` direction.
- Given future authenticated endpoint workflows, when they design bearer-token handling, then they use the `quarkus-smallrye-jwt` direction and remain compatible with the contract baseline's `Authorization: Bearer <token>` convention.
- Given future configuration work, when environment-specific values are introduced, then they are profile-scoped and do not hardcode deployment-specific secrets or URLs globally.
- Given future test workflows, when they choose test layers, then unit/integration tests remain in `realworld-api` and standalone HTTP system tests remain in `realworld-api-st`.
- Given `realworld-api-st`, when it tests API behavior, then it calls `realworld-api` over HTTP and does not depend on production Java classes.
- Given the completed architecture baseline, when repository verification is run, then an executable check validates that the architecture artifact and README references are present.

## Workflow Precedence

Required predecessor:

- `../workspace-quarkus-baseline/_spec-journal.json`

The predecessor has completed Step 06 verification, so this Step 01 may be marked complete.

## Approval Status

Approved and saved after rerun.
