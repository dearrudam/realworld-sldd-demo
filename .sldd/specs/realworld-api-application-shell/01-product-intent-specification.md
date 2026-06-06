# Product Intent: RealWorld API Application Shell

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

- Implement the minimal `realworld-api` Quarkus shell, configuration baseline, health or readiness basics, and BCE skeleton.

Excluded:

- RealWorld business endpoint behavior.
- Standalone system-test application implementation.
- Production deployment configuration.

## Workflow Precedence

Required predecessors:

- `../realworld-architecture-baseline/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Create the minimal runnable Quarkus API application shell that later business API workflows can extend safely.

## Acceptance Criteria Draft

- The workflow creates a minimal `realworld-api` Quarkus application following the approved workspace and architecture baselines.
- The shell includes baseline configuration and basic operational endpoints or readiness conventions as approved in design.
- The shell establishes the initial BCE skeleton without implementing business endpoints.
- The shell can be built and run locally.

## Open Questions

- Which health or readiness behavior is required for the initial shell?
- How much BCE package structure should exist before the first business slice?

## Approval Status

Pending explicit Step 01 approval.
