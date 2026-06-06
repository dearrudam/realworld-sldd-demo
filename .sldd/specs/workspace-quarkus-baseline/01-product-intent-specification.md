# Product Intent: Quarkus Workspace Baseline

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

- Define and create the repository or module layout for `realworld-api` and `realworld-api-st`, including build and run conventions.

Excluded:

- Business endpoint implementation.
- Detailed BCE architecture decisions beyond workspace-level structure.
- System-test scenario implementation.

## Workflow Precedence

Required predecessors:

- `../realworld-api-contract-baseline/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Establish the physical Quarkus workspace baseline needed for the RealWorld API application and standalone HTTP system-test application.

## Acceptance Criteria Draft

- The workflow defines the intended repository layout for both Quarkus applications.
- The workflow establishes build, run, and local development conventions.
- The workflow creates only the minimal workspace structure approved by the design steps.
- The resulting layout supports later API and system-test workflows.

## Open Questions

- Should the two Quarkus applications use separate build roots or a multi-module layout?
- Which ports, profiles, and local run conventions should be reserved for each application?

## Approval Status

Pending explicit Step 01 approval.
