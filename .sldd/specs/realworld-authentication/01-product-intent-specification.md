# Product Intent: Authentication Capability

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

- Define login behavior.
- Define token issuance.
- Define authenticated principal handling.
- Define password verification policy.
- Define authentication error behavior.

Excluded:

- User registration and profile update behavior except where needed for authentication contracts.
- Article, comment, favorite, tag, and profile feature behavior.

## Workflow Precedence

Required predecessors:

- `../realworld-bce-architecture/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Provide the authentication foundation for RealWorld API workflows so later capabilities can consistently identify the current user and return contract-compliant authentication failures.

## Acceptance Criteria Draft

- Login accepts valid RealWorld credentials and returns the expected user response with token.
- Invalid credentials return the expected error status and response shape.
- Authenticated request handling exposes the current user to downstream capability boundaries without leaking transport details.
- Password verification and token creation policies are explicit and testable.

## Open Questions

- Which token format should be used for the initial implementation?
- Should token verification be implemented with a Quarkus security extension or a minimal capability-local mechanism?

## Approval Status

Pending explicit Step 01 approval.
