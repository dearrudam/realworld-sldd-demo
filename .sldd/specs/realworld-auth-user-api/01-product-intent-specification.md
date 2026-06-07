# Product Intent: RealWorld Auth and User API

## Workflow Kind

`feature`

## Parent Workflow-Set

`realworld-quarkus-sldd-workspace`

## Origin

This Step 01 draft was scaffolded from:

- Parent journal: `../realworld-quarkus-sldd-workspace/_spec-journal.json`
- Parent artifact: `../realworld-quarkus-sldd-workspace/01-workflow-set-plan.md`

## Problem Statement

The RealWorld API needs its first business API slice for account registration, login, and current-user behavior. Downstream profile and article workflows depend on a clear authenticated-user contract, token behavior, validation behavior, and system-test coverage through the standalone `realworld-api-st` application.

## Target Users

- RealWorld API clients that register accounts, log in, retrieve the current user, and update user settings.
- Downstream endpoint workflows that require authenticated user identity.
- System-test authors validating the API through HTTP only.

## Scope

Included:

- Implement registration, login, current user retrieval, current user update, token behavior, validation, and HTTP system tests.
- Add the Quarkus validation capability needed for Jakarta Bean Validation at REST boundaries.
- Persist users with password salt and password hash fields.

Excluded:

- Profile follow behavior.
- Articles, tags, favorites, and comments behavior.
- Frontend authentication flows.
- Production-grade external identity-provider integration or asymmetric key management.

## Workflow Precedence

Required predecessors:

- `../realworld-api-application-shell/_spec-journal.json`
- `../realworld-api-st-shell/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Formalized Exploration Decisions

- Token format: JWT Bearer token.
- Signing approach: HS256 shared secret for the demo scope.
- Token TTL: 2 minutes.
- Protected endpoints require `Authorization: Bearer <token>`.
- Missing, invalid, or expired tokens return HTTP 401.
- Password storage: PBKDF2 password hash with a per-user salt.
- Stored user credentials must include the salt and password hash, never the plaintext password.
- Password validation: minimum size of 5 characters.
- Boundary validation should use Jakarta Bean Validation via the Quarkus Hibernate Validator extension where applicable.

## Product Intent

Deliver the first RealWorld business API slice for authentication and current-user behavior with corresponding standalone HTTP system tests.

## Success Metrics

- Auth/user endpoints satisfy the approved RealWorld API contract and JSON envelope conventions.
- Registration and login return a `user` object containing a valid short-lived token.
- Protected current-user operations reject missing, invalid, and expired tokens.
- Passwords are persisted only as PBKDF2 hashes with salts.
- HTTP system tests in `realworld-api-st` cover successful and failing auth/user scenarios.

## Out of Scope

- Profile, article, tag, favorite, and comment endpoint behavior.
- Refresh tokens, token revocation, sessions, password reset, or email verification.
- Role-based authorization beyond authenticated current-user identity.
- Non-demo production key rotation or external secret-management design.

## Risks and Assumptions

- A 2-minute token TTL improves testability of expiration behavior but may require careful system-test timing to avoid flakiness.
- HS256 is accepted for the demo scope because the architecture baseline already chooses JWT Bearer and this workflow is not responsible for production key management.
- Validation failures should use the RealWorld error response envelope from the contract baseline.
- Email and username uniqueness are expected registration constraints.

## Acceptance Criteria (Given/When/Then)

- Given a valid new user registration request with a password of at least 5 characters, when the client posts to registration, then the API creates the user and returns a RealWorld `user` response containing email, username, bio, image, and JWT token.
- Given a registration request with duplicate email or username, missing required fields, or password shorter than 5 characters, when the client posts to registration, then the API returns a validation failure using the RealWorld errors envelope.
- Given valid login credentials, when the client posts to login, then the API returns the current user representation and a JWT token with a 2-minute TTL.
- Given invalid login credentials, when the client posts to login, then the API returns an authentication failure.
- Given a valid Bearer token, when the client requests `GET /api/user`, then the API returns the current user's representation.
- Given a valid Bearer token and valid update fields, when the client requests `PUT /api/user`, then the API updates allowed current-user fields and returns the updated user representation.
- Given a missing, invalid, or expired Bearer token, when the client calls a protected `/api/user` endpoint, then the API returns HTTP 401.
- Given persisted user credentials, when repository storage is inspected through behavior, then plaintext passwords are never exposed and authentication succeeds only through PBKDF2 salt/hash verification.
- Given the auth/user workflow is implemented, when `realworld-api-st` runs its HTTP system tests, then registration, login, current-user retrieval, current-user update, validation failures, and token failures are covered.

## Open Questions

Resolved:

- Token format and signing approach: JWT Bearer token signed with HS256 shared secret for demo scope.
- Password storage and validation constraints: PBKDF2 with per-user salt and stored password hash; minimum password size is 5 characters; boundary validation should use Quarkus Hibernate Validator / Jakarta Bean Validation where applicable.

## Approval Status

Approved on 2026-06-07 after resolving Step 01 open questions.
