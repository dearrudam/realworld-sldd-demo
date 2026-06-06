# Requirement-to-Design Traceability

- Repository layout requirement is covered by two application roots: `realworld-api/` and `realworld-api-st/`.
- Build/run convention requirement is covered by per-application Quarkus Maven wrapper/build instructions and a workspace baseline document.
- HTTP system-test target convention is covered by `realworld-api-st` configuration for a target `realworld-api` base URL.
- No-business-logic requirement is covered by limiting implementation to generated/minimal Quarkus structure and workspace documentation.
- Contract compatibility is covered by referencing `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md` as downstream behavior input.

# API Contracts

This workflow does not define or implement new business API contracts.

The only API contract dependency is the approved predecessor artifact:

- `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md`

Any generated starter endpoint must be considered scaffolding only and must not represent RealWorld API behavior.

# Data Models

No RealWorld data model is introduced in this workflow.

Workspace-level configuration concepts:

- `realworld-api` local port: `8080` by default.
- `realworld-api-st` local port: `8081` when run as an application.
- `realworld-api-st` target API base URL: default `http://localhost:8080`, documented for later system-test workflows.

# Error Model

No application error model is introduced in this workflow.

Future HTTP error behavior remains governed by the completed API contract baseline and endpoint-specific workflows.

# Test Strategy

Step 04 should add Red checks that fail before the workspace exists, such as:

- root-level workspace baseline document/check expects `realworld-api/` and `realworld-api-st/`;
- each application root must have a Quarkus build descriptor;
- each application root must have standard `src/main` and `src/test` structure;
- conventions must document build/run commands and reserved ports;
- checks must not require RealWorld business endpoints.

Step 05 should make only the minimal workspace changes required to pass those checks.

# Test Scenario Catalog

- Workspace directories exist: `realworld-api/` and `realworld-api-st/`.
- Application build descriptors exist in both application roots.
- Standard Quarkus source/resource/test directories exist in both application roots.
- Workspace baseline documentation names build, test, and dev-mode commands.
- Workspace baseline documentation reserves `8080` for `realworld-api` and `8081` for `realworld-api-st`.
- Workspace baseline references the approved RealWorld API contract baseline.
- No required check asserts implemented RealWorld business behavior in this workflow.

# Dependency and Version Policy

- Current repository dependency set is not sufficient for Step 05 because the Quarkus applications do not exist yet.
- New dependencies will be introduced only through Quarkus project generation for `realworld-api` and `realworld-api-st`.
- Before Step 05 creates the Quarkus applications or writes application code, the agent must follow Quarkus extension discovery rules: discover matching extensions, present the full matching list to the user, wait for the user to choose, and load the selected extension skills before writing code.
- Maven is the preferred build tool for the initial baseline because Quarkus getting-started documentation documents Maven structure, `./mvnw quarkus:dev`, and `./mvnw install` conventions.
- Quarkus platform version should be selected by Quarkus tooling/latest platform unless the user explicitly pins a version.
- Runtime behavior impact is limited to generated minimal Quarkus applications; no RealWorld endpoint behavior is introduced.
- Maintenance impact: separate application roots duplicate wrapper/build files but keep app boundaries simple and explicit for early workflows.

# Ordered Implementation Plan

1. Step 04: create a Red workspace verification check that expects both Quarkus app roots and workspace conventions.
2. Step 04: run the check and confirm it fails because the Quarkus app roots do not exist yet.
3. Step 05: perform required Quarkus extension discovery and user selection before generating applications.
4. Step 05: create `realworld-api` as a minimal Quarkus Maven application.
5. Step 05: create `realworld-api-st` as a minimal Quarkus Maven application with separate local run convention.
6. Step 05: add or update workspace documentation with build/test/dev commands and port conventions.
7. Step 05: run the workspace verification check and any generated application tests needed for Green confirmation.
8. Step 06: verify all checks, confirm no RealWorld business behavior was implemented, and record Go/No-Go.
