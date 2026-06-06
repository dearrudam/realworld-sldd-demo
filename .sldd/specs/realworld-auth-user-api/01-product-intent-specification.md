# Product Intent: RealWorld Auth and User API

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

- Implement registration, login, current user, user update, token behavior, validation, and HTTP system tests.

Excluded:

- Profile follow behavior.
- Articles, tags, favorites, and comments behavior.
- Frontend authentication flows.

## Workflow Precedence

Required predecessors:

- `../realworld-api-application-shell/_spec-journal.json`
- `../realworld-api-st-shell/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Deliver the first RealWorld business API slice for authentication and current-user behavior with corresponding standalone HTTP system tests.

## Acceptance Criteria Draft

- The workflow implements registration and login behavior according to the approved API contract.
- The workflow implements current-user retrieval and update behavior.
- The workflow defines and verifies token behavior and validation failures.
- HTTP system tests cover the approved auth and user scenarios through `realworld-api-st`.

## Open Questions

- Which token format and signing approach should be used?
- What password storage and validation constraints are required for the demo scope?

## Approval Status

Pending explicit Step 01 approval.
