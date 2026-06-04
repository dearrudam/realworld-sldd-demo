# Product Intent: Users Capability

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

- Define user registration behavior.
- Define current user retrieval.
- Define user update behavior.
- Define user response shape.
- Define uniqueness handling for username and email.

Excluded:

- Login/token issuance details owned by authentication except where user responses include tokens.
- Profile follow behavior.
- Article authorship behavior beyond user identity availability.

## Workflow Precedence

Required predecessors:

- `../realworld-authentication/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Implement the RealWorld user lifecycle foundation so clients can register, retrieve, and update the authenticated user's account using the canonical RealWorld response shape.

## Acceptance Criteria Draft

- Registration creates a user and returns a user response with token.
- Duplicate username or email produces a contract-compliant validation error.
- Authenticated current-user retrieval returns the current user's latest data.
- Authenticated user update supports allowed mutable fields while preserving uniqueness rules.

## Open Questions

- Which user fields are optional during update when submitted as null or omitted?
- Should registration immediately reuse the authentication token issuing mechanism from the authentication workflow?

## Approval Status

Pending explicit Step 01 approval.
