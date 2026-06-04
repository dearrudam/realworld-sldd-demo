# Product Intent: BCE Architecture Baseline

## Workflow Kind

`feature`

## Parent Workflow-Set

`realworld-capability-decomposition`

## Origin

This Step 01 draft was scaffolded from:

- Parent journal: `../realworld-capability-decomposition/_spec-journal.json`
- Parent artifact: `../realworld-capability-decomposition/01-workflow-set-plan.md`

## Scope

Included:

- Define backend component/package rules for RealWorld API capabilities.
- Define cross-component interaction policy.
- Define boundary, control, and entity responsibilities.
- Define test isolation policy for capability workflows.

Excluded:

- RealWorld endpoint behavior implementation.
- Persistence schema changes beyond architecture conventions.
- Child capability execution or approval.

## Workflow Precedence

Required predecessors:

- `../realworld-domain-model/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Establish a BCE architecture baseline so later RealWorld capability workflows can implement behavior inside clear component boundaries with consistent package ownership, dependency direction, and test strategy.

## Acceptance Criteria Draft

- The API project has documented BCE package/component rules for RealWorld capabilities.
- Boundary, control, and entity responsibilities are clear enough for later child workflows to follow.
- Cross-component calls and shared model usage have an explicit policy.
- Test isolation expectations cover unit, integration, and black-box system tests without coupling `realworld-api-st` to API internals.

## Open Questions

- Should this workflow make structural source package changes, or only document and prepare the architecture baseline?
- Which existing generated or placeholder API code should be treated as outside the RealWorld contract?

## Approval Status

Pending explicit Step 01 approval.
