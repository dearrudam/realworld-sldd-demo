# Product Intent: Favorites Capability

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

- Define favorite and unfavorite behavior.
- Define favorites count.
- Define viewer-relative `favorited` calculation.

Excluded:

- Article CRUD behavior outside favorite state projection.
- Profile follow behavior.
- Tag listing behavior.

## Workflow Precedence

Required predecessors:

- `../realworld-articles/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Add RealWorld article favoriting so authenticated users can mark articles as favorites and article responses expose accurate favorite counts and viewer-relative favorite state.

## Acceptance Criteria Draft

- Authenticated users can favorite an existing article.
- Authenticated users can unfavorite an article they previously favorited.
- Article responses include accurate `favoritesCount` and `favorited` values.
- Repeated favorite or unfavorite requests behave consistently and safely.

## Open Questions

- Should repeated favorite/unfavorite be idempotent?
- Should favorites be modeled as a relationship collection independent of articles?

## Approval Status

Pending explicit Step 01 approval.
