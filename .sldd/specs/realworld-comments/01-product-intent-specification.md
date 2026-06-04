# Product Intent: Comments Capability

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

- Define comment creation for articles.
- Define listing comments for an article.
- Define deleting comments.
- Define comment author projection.
- Define article-comment relationship behavior.

Excluded:

- Article CRUD behavior outside comment attachment and lookup needs.
- Favorites and tags behavior.

## Workflow Precedence

Required predecessors:

- `../realworld-articles/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Enable authenticated users to discuss articles through RealWorld comments while preserving article ownership boundaries and consistent author profile projection.

## Acceptance Criteria Draft

- Authenticated users can add comments to existing articles.
- Clients can list comments for an article with author profile data.
- Authorized deletion removes a comment and returns the expected status.
- Nonexistent articles or comments return contract-compliant errors.

## Open Questions

- Can article authors delete any comment on their article, or only comment authors can delete their own comments?
- Should comment IDs be numeric, UUID, or persistence-generated strings?

## Approval Status

Pending explicit Step 01 approval.
