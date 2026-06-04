# RealWorld Quarkus SLDD Demo

This repository implements the RealWorld backend API with Quarkus using an SLDD workflow.

The workspace currently contains scaffolded Quarkus applications. Full RealWorld behavior is intentionally deferred to later SLDD-scoped changes.

## Projects

- `realworld-api`: Quarkus backend API application using Quarkus REST JSON-B and the approved JNoSQL MongoDB extension.
- `realworld-api-st`: standalone Quarkus system-test application using Quarkus REST Client to validate `realworld-api` over HTTP.
- `.sldd/specs/realworld-quarkus-apps/`: SLDD journal and approved design artifacts for this scaffold.
- `realworld-api/docs/domain-model.md`: canonical RealWorld domain model reference for future domain implementation workflows.

## Architecture

`realworld-api-st` must stay black-box. It communicates with `realworld-api` over HTTP and must not depend on API implementation classes or Maven artifacts.

Generated starter endpoints may exist as Quarkus scaffold code, but they are not RealWorld API contract. RealWorld features such as authentication, users, profiles, articles, comments, favorites, feeds, and tags will be specified and implemented in later workflows.

Domain-affecting implementation work must follow `realworld-api/docs/domain-model.md` before changing entities, DTOs, repositories, validation rules, persistence mappings, or relationship behavior.

## Build

Build the API application:

```bash
cd realworld-api
mvn verify
```

Build the standalone system-test application:

```bash
cd realworld-api-st
mvn verify
```

Verify the workspace scaffold from the repository root:

```bash
sh scripts/verify-scaffold.sh
```

## Quarkus Guides

- Quarkus REST: https://quarkus.io/guides/rest
- Quarkus REST Client: https://quarkus.io/guides/rest-client
- Quarkus MongoDB guide: https://quarkus.io/guides/mongodb

## SLDD

SLDD keeps product intent, codebase context, design, Red tests, Green implementation, and verification artifacts under `.sldd/specs/<feature-name>/`.

The active scaffold workflow is `.sldd/specs/realworld-quarkus-apps/`.
