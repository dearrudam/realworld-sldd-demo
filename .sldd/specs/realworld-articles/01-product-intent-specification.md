# Product Intent: Articles Capability

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

- Define article create, read, update, and delete behavior.
- Define slug generation and lookup behavior.
- Define article response projection.
- Define author profile projection in article responses.
- Define article listing foundation.

Excluded:

- Comment creation and deletion.
- Favorite/unfavorite behavior.
- Full tag listing behavior beyond article tag list capture.

## Workflow Precedence

Required predecessors:

- `../realworld-users/_spec-journal.json`
- `../realworld-profiles/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Provide the core RealWorld article publishing and retrieval capability so authenticated users can manage authored articles and clients can retrieve article projections with author profile data.

## Acceptance Criteria Draft

- Authenticated article creation stores article data and returns the expected article response.
- Article lookup by slug returns the expected article projection.
- Authenticated article update and delete enforce author ownership.
- Article list endpoints establish the baseline query and response shape needed by later capabilities.

## Open Questions

- Which listing filters should be included in this workflow versus deferred to later child workflows?
- How should slug uniqueness conflicts be handled?

## Approval Status

Pending explicit Step 01 approval.
