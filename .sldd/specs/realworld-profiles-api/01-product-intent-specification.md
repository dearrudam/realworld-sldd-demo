# Product Intent: RealWorld Profiles API

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

- Implement profile retrieval, follow, unfollow, and related HTTP system tests.

Excluded:

- Authentication and current-user implementation except where consumed by profile behavior.
- Article feed, favorite, or comment behavior.
- Frontend profile pages.

## Workflow Precedence

Required predecessors:

- `../realworld-auth-user-api/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Implement RealWorld profile behavior on top of the completed authentication and user API foundation.

## Acceptance Criteria Draft

- The workflow implements profile retrieval according to the approved API contract.
- The workflow implements follow and unfollow behavior for authenticated users.
- The workflow verifies authorization and validation boundaries for profile behavior.
- HTTP system tests cover the approved profile scenarios through `realworld-api-st`.

## Open Questions

- How should profile bio and image defaults be represented?
- Which follow/unfollow edge cases are required in the first implementation?

## Approval Status

Pending explicit Step 01 approval.
