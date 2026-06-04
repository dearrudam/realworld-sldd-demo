# Product Intent: Tags Capability

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

- Define tag value capture.
- Define tag listing behavior.
- Define tag response shape.
- Define interaction with article tag lists.

Excluded:

- Article CRUD behavior outside tag capture and projection.
- Favorites, comments, and profile follow behavior.

## Workflow Precedence

Required predecessors:

- `../realworld-articles/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Provide the RealWorld tags capability so clients can discover tag values used by articles and article workflows can consistently capture and project tags.

## Acceptance Criteria Draft

- Article tag values are captured consistently when articles are created or updated.
- The tags endpoint returns the expected tag response shape.
- Tag listing excludes duplicates and uses a deterministic normalization policy.
- The workflow decides whether tags remain embedded on articles or become normalized records.

## Open Questions

- Should tag values preserve submitted case or be normalized?
- Should tags be persisted as standalone records or derived from article documents?

## Approval Status

Pending explicit Step 01 approval.
