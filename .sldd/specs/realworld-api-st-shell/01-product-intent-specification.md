# Product Intent: RealWorld API System-Test Application Shell

## Workflow Kind

`feature`

## Parent Workflow-Set

`realworld-quarkus-sldd-workspace`

## Origin

This Step 01 draft was scaffolded from:

- Parent journal: `../realworld-quarkus-sldd-workspace/_spec-journal.json`
- Parent artifact: `../realworld-quarkus-sldd-workspace/01-workflow-set-plan.md`

## Scope

Included:

- Implement the minimal `realworld-api-st` Quarkus application that can target `realworld-api` over HTTP.

Excluded:

- Endpoint-specific system-test scenarios.
- RealWorld API business endpoint implementation.
- Browser or frontend testing.

## Workflow Precedence

Required predecessors:

- `../realworld-system-test-strategy/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Create the standalone Quarkus system-test application shell so later workflows can add HTTP scenario coverage against `realworld-api`.

## Acceptance Criteria Draft

- The workflow creates a minimal `realworld-api-st` Quarkus application following the approved workspace and system-test strategy.
- The shell can target a configurable `realworld-api` base URL over HTTP.
- The shell provides a place for later scenario-oriented system tests.
- The shell can be built and run locally.

## Open Questions

- What is the first smoke scenario for verifying connectivity to `realworld-api`?
- Should the system-test app expose endpoints, run tests at startup, or use another execution model?

## Approval Status

Pending explicit Step 01 approval.
