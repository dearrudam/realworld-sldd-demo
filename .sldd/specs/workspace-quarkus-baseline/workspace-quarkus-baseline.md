# Quarkus Workspace Baseline

This repository contains the SLDD-guided RealWorld backend workspace. The workspace baseline establishes two independent Quarkus Maven application roots and local development conventions.

## Applications

| Application | Purpose | Default local port |
|---|---|---:|
| `realworld-api` | Future RealWorld backend API implementation | `8080` |
| `realworld-api-st` | Future standalone HTTP system-test application targeting `realworld-api` | `8081` |

## Repository Layout

```text
.
├── realworld-api/                       # Quarkus REST backend application
├── realworld-api-st/                    # Quarkus REST Client based system-test application
├── scripts/check-workspace-quarkus-baseline.sh
└── .sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md
```

## Build, Test, and Dev Commands

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

## Local HTTP Conventions

- `realworld-api` uses `quarkus.http.port=8080`.
- `realworld-api-st` uses `quarkus.http.port=8081` when run as an application.
- `realworld-api-st` targets `realworld-api` at `http://localhost:8080` by default.

## Contract Source

Future API behavior must trace to the approved contract baseline:

- `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md`

## Scope Boundary

No RealWorld business endpoint behavior is implemented by this workflow. Generated Quarkus starter code is scaffolding only and is not part of the RealWorld API contract.
