# Product Intent: RealWorld Standalone System-Test Strategy

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

- Define how `realworld-api-st` validates `realworld-api` over HTTP, including target configuration, data isolation, auth setup, scenario style, and verification boundaries.

Excluded:

- Implementing endpoint-specific system tests.
- Implementing business endpoints in `realworld-api`.
- Non-HTTP integration testing strategy.

## Workflow Precedence

Required predecessors:

- `../workspace-quarkus-baseline/_spec-journal.json`
- `../realworld-api-contract-baseline/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Define the standalone HTTP system-test approach before endpoint workflows add scenario coverage through `realworld-api-st`.

## Acceptance Criteria Draft

- The workflow defines target API configuration for system tests.
- The workflow covers data isolation, authentication setup, scenario style, and verification boundaries.
- The strategy supports endpoint-specific workflows without coupling test progress to the parent workflow-set.
- The strategy is concrete enough to guide the `realworld-api-st` shell workflow.

## Open Questions

- How should the system-test app discover the target API URL?
- What data reset or isolation mechanism should endpoint scenarios expect?

## Approval Status

Pending explicit Step 01 approval.
