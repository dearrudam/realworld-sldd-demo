# AGENTS.md -- RealWorld SLDD Workspace

This repository is a Quarkus + SLDD RealWorld backend workspace.

## Scope

- `realworld-api/` owns the RealWorld backend API implementation.
- `realworld-api-st/` owns standalone black-box system tests that call `realworld-api` over HTTP.
- `.sldd/specs/` owns SLDD workflow artifacts and journals.

## Rules

- Respect the active SLDD workflow before making implementation changes.
- Keep `realworld-api-st` independent from `realworld-api` internals; do not add a Maven dependency from the system-test app to the API app.
- Prefer Quarkus extensions over custom infrastructure when adding capabilities.
- Keep README documentation current after structural or behavioral changes.
- Do not treat generated starter endpoints as RealWorld API contract.

## Verification

- Run `sh scripts/verify-scaffold.sh` after scaffold-related changes.
- Build each Quarkus project from its own directory with Maven.
