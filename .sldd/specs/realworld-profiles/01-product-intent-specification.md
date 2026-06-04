# Product Intent: Profiles Capability

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

- Define profile projection.
- Define profile lookup by username.
- Define follow and unfollow behavior.
- Define viewer-relative `following` calculation.

Excluded:

- User registration and account update behavior.
- Article author projection details except profile data supplied to articles.

## Workflow Precedence

Required predecessors:

- `../realworld-users/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Expose RealWorld profiles independently from account management so clients can view public user profile data and manage following relationships.

## Acceptance Criteria Draft

- Profile lookup returns username, bio, image, and viewer-relative following state.
- Authenticated follow creates the expected relationship and returns an updated profile.
- Authenticated unfollow removes the relationship and returns an updated profile.
- Anonymous profile reads remain supported where the RealWorld contract allows them.

## Open Questions

- How should self-follow attempts be handled?
- Should follow relationships be represented as a separate persistence collection or embedded relationship data?

## Approval Status

Pending explicit Step 01 approval.
