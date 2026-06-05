# Workflow-Set Plan: RealWorld Quarkus SLDD Workspace

## Workflow Kind

`workflow-set`

## Large Idea

This repository is a fork of the RealWorld example-app initiative template. Its purpose is to implement the RealWorld backend API using Quarkus and SLDD.

The workspace should contain two Quarkus applications:

- `realworld-api`: the RealWorld REST API implementation.
- `realworld-api-st`: a standalone Quarkus system-test application that validates `realworld-api` over HTTP.

The project should demonstrate a spec-driven development flow where SLDD guides intent, design, tests, implementation, and verification.

## Source Inputs

- User-provided project context and workspace goals.
- RealWorld example-app initiative template expectations.
- Requirement for two Quarkus applications: API and standalone HTTP system tests.
- Requirement to demonstrate SLDD-driven intent, design, tests, implementation, and verification.
- Discussion that shared/foundational definitions should happen before implementation-focused workflows.

## Why Decomposition Is Recommended

- The idea spans repository structure, two Quarkus applications, API contract decisions, architecture decisions, system-test strategy, feature implementation, and documentation.
- Many foundational decisions should be defined before implementation code is written.
- Child workflows can independently own detailed SLDD artifacts for their scope while the parent coordinates ordering and dependencies.
- Baseline definition workflows reduce ambiguity for later endpoint implementation workflows.

## Proposed Child Workflows

| Name | Title | Kind | Scope | Predecessors | Notes |
|---|---|---|---|---|---|
| `realworld-api-contract-baseline` | RealWorld API Contract Baseline | `feature` | Define RealWorld API contract source, endpoint inventory, request/response conventions, error conventions, authentication expectations, and initial acceptance boundaries. | none | Definition-heavy workflow; no application implementation required. |
| `workspace-quarkus-baseline` | Quarkus Workspace Baseline | `feature` | Define and create the repository/module layout for `realworld-api` and `realworld-api-st`, including build and run conventions. | `realworld-api-contract-baseline` | Establishes the physical workspace for later workflows. |
| `realworld-architecture-baseline` | RealWorld API Architecture Baseline | `feature` | Define BCE structure, package rules, persistence approach, configuration conventions, test layering, and cross-application boundaries. | `workspace-quarkus-baseline` | Shared architecture decisions for implementation workflows. |
| `realworld-system-test-strategy` | RealWorld Standalone System-Test Strategy | `feature` | Define how `realworld-api-st` validates `realworld-api` over HTTP, including target configuration, data isolation, auth setup, scenario style, and verification boundaries. | `workspace-quarkus-baseline`, `realworld-api-contract-baseline` | Strategy before writing endpoint-specific system tests. |
| `realworld-api-application-shell` | RealWorld API Application Shell | `feature` | Implement the minimal `realworld-api` Quarkus shell, configuration baseline, health/readiness basics, and BCE skeleton. | `realworld-architecture-baseline` | First implementation workflow for the API application. |
| `realworld-api-st-shell` | RealWorld API System-Test Application Shell | `feature` | Implement the minimal `realworld-api-st` Quarkus application that can target `realworld-api` over HTTP. | `realworld-system-test-strategy` | Enables later feature workflows to add HTTP system tests. |
| `realworld-auth-user-api` | RealWorld Auth and User API | `feature` | Implement registration, login, current user, user update, token behavior, validation, and HTTP system tests. | `realworld-api-application-shell`, `realworld-api-st-shell` | First business API slice. |
| `realworld-profiles-api` | RealWorld Profiles API | `feature` | Implement profile retrieval, follow, unfollow, and related HTTP system tests. | `realworld-auth-user-api` | Depends on user identity and authentication. |
| `realworld-articles-api` | RealWorld Articles API | `feature` | Implement articles, tags, feed, favorites, comments, filtering, and related HTTP system tests. | `realworld-auth-user-api`, `realworld-profiles-api` | Larger business slice; may be decomposed later if needed. |
| `sldd-demo-documentation` | SLDD Demo Documentation | `feature` | Document how SLDD guided the project through intent, design, tests, implementation, and verification. | `realworld-api-application-shell`, `realworld-api-st-shell`, `realworld-auth-user-api`, `realworld-profiles-api`, `realworld-articles-api` | Captures the demonstration value of the repository. |

## Execution Guidance

- Complete definition-heavy workflows before implementation workflows.
- Start with the API contract baseline so later workflows share a consistent external behavior target.
- Establish the workspace baseline before architecture and test strategy become concrete.
- Define architecture and system-test strategy before writing application shells.
- Build the API shell and system-test shell before implementing business endpoints.
- Implement business API slices incrementally, adding HTTP system tests through `realworld-api-st`.
- Treat `realworld-articles-api` as a candidate for later decomposition if its Step 01 scope becomes too broad.
- Complete documentation after enough child workflows exist to demonstrate the SLDD loop.

## Out Of Scope

- Parent workflow-set execution does not implement application code.
- Parent workflow-set execution does not approve child Step 01 product intents.
- Parent workflow-set execution does not record child implementation progress.
- RealWorld frontend implementation is out of scope.
- Production deployment, cloud infrastructure, and advanced observability are out of scope unless introduced by later approved child workflows.

## Scaffold Policy

If scaffold is approved, all proposed child workflows in this plan will be created as Step 01 pending drafts.

Partial scaffold selection is not part of the first version.

## Approval

Status: Approved. Step 01 is complete.
