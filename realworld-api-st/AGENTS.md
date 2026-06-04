# AGENTS.md -- realworld-api-st

This directory contains the standalone Quarkus system-test application for `realworld-api`.

## Boundaries

- Validate `realworld-api` only through externally observable HTTP behavior.
- Use Quarkus REST Client for future calls to the API application.
- Do not import API implementation classes, domain classes, repositories, or generated sources.
- Do not declare a Maven dependency on `realworld-api`.
- Follow `../realworld-api/docs/bce-architecture.md` for black-box system-test isolation rules.

## Current Scaffold Contract

- This app is a test harness scaffold; RealWorld behavior scenarios are deferred to later SLDD workflows.
- System-test DTOs may duplicate HTTP payload shapes later to preserve black-box independence.

## Verification

- Build this project from this directory with Maven.
- Keep tests and clients independent from `realworld-api` internals.
