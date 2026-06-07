# RealWorld Quarkus SLDD Demo

This repository implements the RealWorld backend API using Quarkus and an SLDD workflow.

## Workspace

- `realworld-api/` — Quarkus REST application for the future RealWorld backend API.
- `realworld-api-st/` — standalone Quarkus application for future HTTP system tests against `realworld-api`.
- `.sldd/specs/` — SLDD workflow journals and approved artifacts.
- `scripts/` — repository-level verification scripts.

## Current baseline

The workspace and architecture baselines have been created, but RealWorld business endpoints are not implemented yet. API behavior and implementation architecture must trace to:

- `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md`
- `.sldd/specs/realworld-architecture-baseline/realworld-architecture-baseline.md`

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

Local ports:

- `realworld-api`: `8080`
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
- [Writing JSON REST Services](https://quarkus.io/guides/rest-json)
- [Quarkus REST Client](https://quarkus.io/guides/rest-client)
- [Using JWT RBAC](https://quarkus.io/guides/security-jwt)
- [Creating your first Quarkus application](https://quarkus.io/guides/getting-started)
