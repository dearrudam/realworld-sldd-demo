# AGENTS.md

This repository is a RealWorld backend demo built with Quarkus and governed by SLDD.

## Workspace Rules

- Treat `.sldd/specs/realworld-quarkus-apps/` as the source of approved scope for this scaffold workflow.
- Do not implement RealWorld API behavior outside an approved SLDD step.
- Keep `realworld-api` and `realworld-api-st` as independent sibling Maven projects.
- Keep `realworld-api-st` black-box: it must communicate with `realworld-api` only over HTTP and must not depend on API internals.
- Prefer Quarkus extensions and Quarkus tooling over custom infrastructure.
- Keep README documentation current after structural changes.

## Current Projects

- `realworld-api`: Quarkus REST JSON-B backend scaffold with JNoSQL MongoDB dependency for future persistence work.
- `realworld-api-st`: standalone Quarkus REST Client scaffold for future external system tests.

## Verification

- Run `.sldd/specs/realworld-quarkus-apps/verify-scaffold.sh` after scaffold changes.
- Build each subproject independently from its own directory.
