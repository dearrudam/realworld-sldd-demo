# AGENTS.md

This directory contains `realworld-api`, the Quarkus backend scaffold for the RealWorld API.

## Boundaries

- Own future RealWorld HTTP API resources, application logic, entities, DTOs, persistence adapters, and API configuration.
- Expose JSON over Quarkus REST JSON-B.
- Use JNoSQL MongoDB for future MongoDB persistence work unless a later SLDD workflow changes that decision.
- Do not depend on `realworld-api-st`.
- Do not implement users, profiles, articles, comments, favorites, feeds, authentication, or pagination behavior in this scaffold workflow.

## Quarkus Rules

- Search Quarkus documentation and extension tooling before adding new capabilities.
- Load applicable Quarkus skills before changing endpoint, CDI, persistence, or test code.
- Keep business logic out of REST resource classes.
- Use `@QuarkusTest` for Quarkus integration tests.
- Keep `README.md` aligned with the current app purpose, extensions, and endpoints.

## Current Scaffold

- Approved extensions: `quarkus-rest-jsonb`, `quarkus-jnosql-mongodb`.
- Generated starter endpoints are scaffold-only and are not RealWorld API contracts.
