# ![RealWorld Example App](logo.png)

Quarkus + SLDD backend workspace for the [RealWorld](https://github.com/gothinkster/realworld) API.

This repository currently contains the initial scaffold for two independent Quarkus Maven applications. RealWorld API behavior is intentionally deferred to later SLDD workflows.

## Projects

- `realworld-api`: Quarkus REST JSON-B backend scaffold for the future RealWorld API implementation.
- `realworld-api-st`: standalone Quarkus REST Client scaffold for future black-box system tests against `realworld-api`.

## SLDD Workflow

Specification-driven work is tracked under `.sldd/specs/realworld-quarkus-apps/`.

Current scaffold verification lives at `.sldd/specs/realworld-quarkus-apps/verify-scaffold.sh` and checks the generated projects, guidance files, dependencies, documentation, and ignore rules.

## Getting Started

Run scaffold verification from the repository root:

```bash
bash .sldd/specs/realworld-quarkus-apps/verify-scaffold.sh
```

Build the API project:

```bash
cd realworld-api
mvn test
```

Build the standalone system-test project:

```bash
cd realworld-api-st
mvn test
```

Run either Quarkus app in dev mode from its own directory:

```bash
mvn quarkus:dev
```

## Quarkus Guides

- [Writing REST Services with Quarkus REST](https://quarkus.io/guides/rest)
- [Writing JSON REST Services](https://quarkus.io/guides/rest-json)
- [Using the REST Client](https://quarkus.io/guides/rest-client)
- [Quarkus JNoSQL](https://docs.quarkiverse.io/quarkus-jnosql/dev/)

## Current Scope

This scaffold does not yet implement authentication, users, profiles, articles, comments, favorites, feeds, or pagination. Those behaviors require later approved SLDD steps.
