# Agent Instructions

Scope: entire repository.

## Repository purpose

This repository is a RealWorld backend demo implemented with Quarkus and governed by SLDD. Treat `.sldd/specs/` as the source of truth for approved product intent, design, tests, implementation, and verification decisions.

## Workflow rules

- Do not make application behavior changes outside the active SLDD workflow.
- Keep root documentation aligned with generated Quarkus applications and approved SLDD artifacts.
- Preserve the independent sibling application layout unless a later SLDD workflow approves a different structure.
- Keep generated build outputs, logs, and local environment files out of version control.

## Architecture rules

- `realworld-api` owns the RealWorld HTTP API implementation.
- `realworld-api-st` owns standalone black-box system tests that call `realworld-api` over HTTP.
- Do not add compile-time dependencies from `realworld-api-st` to `realworld-api`.
- Defer RealWorld endpoint behavior until a later approved SLDD workflow defines it.
