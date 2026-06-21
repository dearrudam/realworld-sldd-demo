# RealWorld Quarkus SLDD Demo

This repository implements the RealWorld backend API using Quarkus and an SLDD workflow.

## Workspace

- `realworld-api/` — Quarkus REST application for the future RealWorld backend API.
- `realworld-api-st/` — standalone Quarkus application for future HTTP system tests against `realworld-api`.
- `.sldd/specs/` — SLDD workflow journals and approved artifacts.
- `scripts/` — repository-level verification scripts.

## Current baseline

The workspace and architecture baselines have been created. `realworld-api` provides Auth and User business endpoints plus SmallRye Health.

### Implemented Features — Auth & User API

| Endpoint | Method | Description | Auth |
|---|---|---|---|
| `/api/users` | POST | Register a new user | No |
| `/api/users/login` | POST | Login with email + password | No |
| `/api/user` | GET | Get current user profile | JWT Bearer |
| `/api/user` | PUT | Update current user profile | JWT Bearer |

All responses use the RealWorld `{user: {email, token, username, bio, image}}` envelope.

### Key technologies

- MongoDB persistence via JNoSQL (`quarkus-jnosql-mongodb`)
- JWT authentication via SmallRye JWT (`quarkus-smallrye-jwt`)
- PBKDF2 password hashing (JDK `SecretKeyFactory`)
- Bean Validation via Hibernate Validator (`quarkus-hibernate-validator`)
- JSON-B serialization (`quarkus-rest-jsonb`)
- BCE architecture: `dev.realworld.authuser.{boundary,control,entity}`

API behavior and implementation architecture trace to:

- `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md`
- `.sldd/specs/realworld-architecture-baseline/realworld-architecture-baseline.md`
- `.sldd/specs/realworld-auth-user-api/` (approved SLDD workflow artifacts)

## Local development

Run commands from each application directory.

```bash
cd realworld-api
./mvnw quarkus:dev
./mvnw test
./mvnw install
```

```bash
cd realworld-api-st
./mvnw quarkus:dev
./mvnw test
./mvnw install
```

Local ports and shell endpoints:

- `realworld-api`: `8080`
- `realworld-api` health: `/q/health`, `/q/health/live`, `/q/health/ready`
- `realworld-api-st`: `8081`
- `realworld-api-st` target API URL: `http://localhost:8080`

## Verification

```bash
./scripts/check-realworld-contract-baseline.sh
./scripts/check-workspace-quarkus-baseline.sh
./scripts/check-realworld-architecture-baseline.sh
```

## Quarkus guides

- [Quarkus REST](https://quarkus.io/guides/rest)
- [SmallRye Health](https://quarkus.io/guides/smallrye-health)
- [Writing JSON REST Services](https://quarkus.io/guides/rest-json)
- [Quarkus REST Client](https://quarkus.io/guides/rest-client)
- [Using JWT RBAC](https://quarkus.io/guides/security-jwt)
- [JNoSQL MongoDB](https://quarkus.io/guides/jnosql)
- [Using Hibernate Validator](https://quarkus.io/guides/validation)
- [Creating your first Quarkus application](https://quarkus.io/guides/getting-started)
