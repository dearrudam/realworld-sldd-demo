# AGENTS.md

This directory contains `realworld-api-st`, the standalone Quarkus system-test scaffold for `realworld-api`.

## Boundaries

- Validate `realworld-api` only through external HTTP calls.
- Do not add a Maven dependency on `realworld-api`.
- Do not import API implementation packages, resources, entities, repositories, or DTOs.
- Duplicate HTTP payload DTOs locally when future tests need typed payloads.
- Keep test target URLs configurable instead of hardcoding environment-specific addresses.

## Quarkus Rules

- Search Quarkus documentation and extension tooling before adding new capabilities.
- Load applicable Quarkus skills before changing REST client or test code.
- Use Quarkus REST Client for future black-box HTTP clients.
- Keep `README.md` aligned with the current app purpose, extensions, and test commands.

## Current Scaffold

- Approved extension: `quarkus-rest-client`.
- This scaffold does not yet contain RealWorld system-test scenarios.
