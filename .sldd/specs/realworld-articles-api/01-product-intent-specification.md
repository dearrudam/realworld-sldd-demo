# Product Intent: RealWorld Articles API

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

- Implement articles, tags, feed, favorites, comments, filtering, and related HTTP system tests.

Excluded:

- Authentication, user, and profile foundations except where consumed by article behavior.
- Frontend article pages.
- Later decomposition of article behavior unless approved during this workflow.

## Workflow Precedence

Required predecessors:

- `../realworld-auth-user-api/_spec-journal.json`
- `../realworld-profiles-api/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Implement the article-centered RealWorld API capabilities after identity and profile foundations are complete.

## Acceptance Criteria Draft

- The workflow implements article creation, update, deletion, retrieval, and listing behavior according to the approved API contract.
- The workflow implements tags, feed, favorites, comments, and filtering as approved in Step 01 and design.
- The workflow verifies authentication, ownership, and validation boundaries.
- HTTP system tests cover the approved article scenarios through `realworld-api-st`.

## Open Questions

- Should this workflow be decomposed before Step 02 because of its breadth?
- Which article and feed scenarios are required for the first acceptance boundary?

## Approval Status

Pending explicit Step 01 approval.
