# Problem Statement

Implement the RealWorld Auth and User API — the first business API slice providing user registration, authentication, and profile management endpoints — along with corresponding HTTP system tests in the standalone system-test application (`realworld-api-st`).

# Target Users

- RealWorld backend API consumers (frontend clients, mobile apps, third-party integrations).
- System-test application (`realworld-api-st`) that validates auth and user behavior over HTTP.

# Formalized Exploration Decisions

This workflow was scaffolded as a child from the `realworld-quarkus-sldd-workspace` workflow-set plan. Its scope, predecessors, and API contract baseline were pre-defined by the parent workflow-set.

# Success Metrics

- Registration, login, current-user retrieval, and user-update endpoints are implemented and pass HTTP system tests.
- Token issuance, validation, and expiration behave correctly (5-minute default expiry, configurable via `application.properties`).
- Input validation returns 422 with RealWorld `errors` envelope for invalid payloads.
- Missing/invalid tokens on protected endpoints return 401.
- All auth/user HTTP system tests in `realworld-api-st` pass.

# Out of Scope

- Profile follow/unfollow behavior (separate `realworld-profiles-api` workflow).
- Articles, tags, favorites, and comments (separate workflows).
- Frontend authentication flows.
- User roles/authorization groups beyond basic authentication.

# Risks and Assumptions

- **Persistence**: MongoDB via `quarkus-jnosql-mongodb` with Jakarta Data repositories, as recommended by the architecture baseline. The extension will be added to `realworld-api/pom.xml` in this workflow. Dev Services provides an auto-configured MongoDB instance for development/testing.
- **Token signing**: RS256 asymmetric keys via `quarkus-smallrye-jwt`. Key location, issuer, and expiry are configured through `application.properties`.
- **Password storage**: PBKDF2 via JDK built-in `SecretKeyFactory` — no extra dependencies.
- **API compatibility**: All endpoints, JSON envelopes, and error conventions follow the contract baseline from `realworld-api-contract-baseline`.

# Acceptance Criteria (Given/When/Then)

## Registration: `POST /api/users`

1. **Successful registration**
   - `GIVEN` request body
     ```json
     {
       "user": { "username": "Jacob", "email": "jake@jake.jake", "password": "JakeJake1" }
     }
     ```
   - `WHEN` POST `/api/users`
   - `THEN` returns **201**
   - `AND` response body matches
     ```json
     {
       "user": { "email": "jake@jake.jake", "token": "<valid-jwt>", "username": "Jacob", "bio": null, "image": null }
     }
     ```

2. **Duplicate email**
   - `GIVEN` an existing user with email `jake@jake.jake`
   - `AND` request body with the same email
     ```json
     {
       "user": { "username": "Jacob2", "email": "jake@jake.jake", "password": "JakeJake1" }
     }
     ```
   - `WHEN` POST `/api/users`
   - `THEN` returns **422**
   - `AND` response body contains `"errors"` envelope

3. **Validation failure — weak password**
   - `GIVEN` request body with invalid password (too short, no uppercase, or no digit)
     ```json
     {
       "user": { "username": "Jacob", "email": "jake@jake.jake", "password": "a" }
     }
     ```
   - `WHEN` POST `/api/users`
   - `THEN` returns **422**
   - `AND` response body contains `"errors"` envelope

## Login: `POST /api/users/login`

4. **Successful login**
   - `GIVEN` a registered user `{ "email": "jake@jake.jake", "password": "JakeJake1" }`
   - `AND` request body
     ```json
     {
       "user": { "email": "jake@jake.jake", "password": "JakeJake1" }
     }
     ```
   - `WHEN` POST `/api/users/login`
   - `THEN` returns **200**
   - `AND` response body matches
     ```json
     {
       "user": { "email": "jake@jake.jake", "token": "<valid-jwt>", "username": "Jacob", "bio": null, "image": null }
     }
     ```

5. **Invalid credentials**
   - `GIVEN` wrong password
     ```json
     {
       "user": { "email": "jake@jake.jake", "password": "WrongPass1" }
     }
     ```
   - `WHEN` POST `/api/users/login`
   - `THEN` returns **401**
   - `AND` response body contains `"errors"` envelope

## Current User: `GET /api/user`

6. **Get current user with valid token**
   - `GIVEN` header `Authorization: Bearer <valid-jwt>`
   - `WHEN` GET `/api/user`
   - `THEN` returns **200**
   - `AND` response body matches
     ```json
     {
       "user": { "email": "jake@jake.jake", "token": "<valid-jwt>", "username": "Jacob", "bio": null, "image": null }
     }
     ```

7. **No token**
   - `WHEN` GET `/api/user` without Authorization header
   - `THEN` returns **401**
   - `AND` response body contains `"errors"` envelope

8. **Expired token**
   - `GIVEN` header `Authorization: Bearer <expired-jwt>`
   - `WHEN` GET `/api/user`
   - `THEN` returns **401**
   - `AND` response body contains `"errors"` envelope

## Update User: `PUT /api/user`

9. **Successful update**
   - `GIVEN` header `Authorization: Bearer <valid-jwt>`
   - `AND` request body
     ```json
     {
       "user": { "email": "jake@newdomain.io", "username": "JacobNew", "bio": "I work at State Farm", "image": "https://example.com/avatar.jpg" }
     }
     ```
   - `WHEN` PUT `/api/user`
   - `THEN` returns **200**
   - `AND` response body matches
     ```json
     {
       "user": { "email": "jake@newdomain.io", "token": "<valid-jwt>", "username": "JacobNew", "bio": "I work at State Farm", "image": "https://example.com/avatar.jpg" }
     }
     ```

## Token and Security

10. **Token signature and claims**
    - `GIVEN` a JWT issued by the system
    - `WHEN` decoding the token
    - `THEN` it has RS256 signature
    - `AND` contains claims: `sub`, `iss`, `exp`, `iat`, `upn`

11. **Token expiry configurable**
    - `GIVEN` property `mp.jwt.token.expiry` in `application.properties`
    - `WHEN` the property is changed
    - `THEN` tokens are issued with the configured expiry duration (default: 5 minutes)

## Persistence

12. **Password hashed with PBKDF2**
    - `GIVEN` a user's stored password
    - `WHEN` inspecting persistence
    - `THEN` the stored hash is PBKDF2-derived (not plaintext)

13. **User stored in MongoDB**
    - `GIVEN` a registered user
    - `WHEN` the user data is persisted
    - `THEN` it is stored in MongoDB via a Jakarta Data repository

14. **Email uniqueness enforced at persistence level**
    - `GIVEN` an existing user with email `jake@jake.jake`
    - `WHEN` a second registration with `jake@jake.jake` is attempted
    - `THEN` the repository rejects the duplicate and returns 422
