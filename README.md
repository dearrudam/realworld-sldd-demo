# RealWorld Quarkus SLDD Demo

This repository is a Quarkus backend workspace for the [RealWorld](https://github.com/gothinkster/realworld) API, developed through an SLDD (spec-loop driven development) workflow.

The current scope is application scaffolding. Full RealWorld behavior such as authentication, users, profiles, articles, comments, favorites, feeds, and pagination is intentionally deferred to later SLDD workflows.

## Workspace applications

- [`realworld-api`](realworld-api/) — Quarkus application that will own the RealWorld REST API implementation.
- [`realworld-api-st`](realworld-api-st/) — standalone Quarkus application for black-box system tests that validate `realworld-api` over HTTP.

SLDD artifacts live under [`.sldd/specs/realworld-quarkus-apps/`](.sldd/specs/realworld-quarkus-apps/) and record the approved intent, codebase context, design, test, implementation, and verification decisions for this scaffold.

## Build and test

Run checks from each independent Maven project:

```bash
./scripts/verify-realworld-scaffold.py
cd realworld-api && ./mvnw test
cd ../realworld-api-st && ./mvnw test
```

Run the API in dev mode:

```bash
cd realworld-api
./mvnw quarkus:dev
```

Run future standalone system tests from `realworld-api-st` after starting `realworld-api` and after a later SLDD workflow adds concrete HTTP scenarios.

## Development rules

- Keep `realworld-api-st` decoupled from API internals; it must validate through HTTP only.
- Use SLDD before adding RealWorld endpoint behavior.
- Keep README and guidance files current when scaffold or workflow structure changes.
