# RealWorld API Contract Baseline

This document is the local contract baseline for downstream SLDD workflows implementing the RealWorld/Conduit backend API with Quarkus.

## Endpoint Inventory

### User and Authentication

- `POST /api/users` — register a new user.
- `POST /api/users/login` — authenticate an existing user.
- `GET /api/user` — return the current user; requires `Authorization: Bearer <token>`.
- `PUT /api/user` — update the current user; requires `Authorization: Bearer <token>`.

### Profiles

- `GET /api/profiles/{username}` — return a public profile.
- `POST /api/profiles/{username}/follow` — follow a profile; requires `Authorization: Bearer <token>`.
- `DELETE /api/profiles/{username}/follow` — unfollow a profile; requires `Authorization: Bearer <token>`.

### Articles, Feed, Favorites, Comments, and Tags

- `GET /api/articles` — list articles with filtering and pagination.
- `GET /api/articles/feed` — list the authenticated user's feed; requires `Authorization: Bearer <token>`.
- `POST /api/articles` — create an article; requires `Authorization: Bearer <token>`.
- `GET /api/articles/{slug}` — return one article.
- `PUT /api/articles/{slug}` — update an article; requires `Authorization: Bearer <token>` and author authorization.
- `DELETE /api/articles/{slug}` — delete an article; requires `Authorization: Bearer <token>` and author authorization.
- `POST /api/articles/{slug}/favorite` — favorite an article; requires `Authorization: Bearer <token>`.
- `DELETE /api/articles/{slug}/favorite` — unfavorite an article; requires `Authorization: Bearer <token>`.
- `GET /api/articles/{slug}/comments` — list comments for an article.
- `POST /api/articles/{slug}/comments` — create a comment; requires `Authorization: Bearer <token>`.
- `DELETE /api/articles/{slug}/comments/{id}` — delete a comment; requires `Authorization: Bearer <token>` and authorization.
- `GET /api/tags` — list tags.

## Authentication Convention

Protected endpoints require an HTTP `Authorization: Bearer <token>` header. The token is treated as a JWT at the contract level. JWT signing, claims, expiration policy, and secret management are implementation concerns for later workflows.

## JSON Envelope Convention

The API uses RealWorld-style JSON envelopes:

- `user`: contains `email`, `token`, `username`, `bio`, and `image`.
- `profile`: contains `username`, `bio`, `image`, and `following`.
- `article`: contains `slug`, `title`, `description`, `body`, `tagList`, `createdAt`, `updatedAt`, `favorited`, `favoritesCount`, and `author`.
- `comment`: contains `id`, `createdAt`, `updatedAt`, `body`, and `author`.
- `articles` with `articlesCount`: article list responses.
- `comments`: comment list responses.
- `tags`: tag list responses.

## Error Convention

Error responses use a JSON object containing `errors` with field or category messages. Baseline status categories:

- `422` for input validation failures.
- `401` for missing or invalid authentication on protected endpoints.
- `403` for authenticated users attempting an unauthorized operation.
- `404` for missing resources where applicable.

## Acceptance Boundaries

This workflow defines contract documentation only. This workflow does not create Quarkus modules or production endpoint implementation.

Downstream workflows may refine implementation details, validation specificity, persistence, security internals, and system-test mechanics only when compatible with this baseline.
