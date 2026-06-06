# Product Intent: RealWorld API Architecture Baseline

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

- Define BCE structure, package rules, persistence approach, configuration conventions, test layering, and cross-application boundaries.

Excluded:

- Endpoint implementation.
- Creating the standalone system-test strategy in detail.
- Production deployment architecture.

## Workflow Precedence

Required predecessors:

- `../workspace-quarkus-baseline/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Set the shared technical architecture rules for the RealWorld Quarkus API before endpoint-focused implementation workflows begin.

## Acceptance Criteria Draft

- The workflow defines package and BCE layering conventions for `realworld-api`.
- The workflow documents persistence, configuration, and test-layering decisions.
- The workflow clarifies boundaries between `realworld-api` and `realworld-api-st`.
- The decisions are specific enough for later low-level designs to follow consistently.

## Open Questions

- Which persistence technology and database should the baseline assume?
- Which Quarkus extensions should be part of the initial architecture baseline?

## Approval Status

Pending explicit Step 01 approval.
