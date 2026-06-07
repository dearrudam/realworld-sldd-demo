# Product Intent: RealWorld Standalone System-Test Strategy

## Problem Statement

Endpoint workflows need a repeatable way to add HTTP-level system tests in `realworld-api-st` without coupling each scenario to ad hoc target URLs, authentication assumptions, or data-cleanup conventions.

## Target Users

- RealWorld API developers adding endpoint-specific workflows.
- Maintainers running standalone system tests against `realworld-api` in local, CI, or packaged environments.

## Formalized Exploration Decisions

- System tests live in the dedicated `realworld-api-st` Quarkus module.
- System tests target `realworld-api` over HTTP through a configurable base URL.
- Endpoint-specific workflows own their scenario details; this workflow owns only the reusable strategy and shell conventions.
- The target URL is expressed by `realworld-api.base-url` and bridged to the existing REST Client configuration key `service_uri`.
- Authentication is a declared strategy concern but remains unauthenticated until a security workflow introduces credentials.
- Data isolation is scenario-owned for now: tests create unique data and avoid global reset assumptions until persistence/reset endpoints exist.

## Success Metrics

- `realworld-api-st` documents and exposes a stable target-API configuration convention.
- Future endpoint system tests can reuse the same REST Client config key and strategy rules.
- The strategy is verified by executable tests that do not require a running `realworld-api` service.
- README guidance is clear enough for local and CI users to set a target API URL.

## Out of Scope

- Implementing endpoint-specific RealWorld scenarios.
- Implementing business endpoints in `realworld-api`.
- Implementing authentication, persistence cleanup, data reset APIs, containers, or non-HTTP integration tests.
- Changing the Quarkus platform version or adding Maven dependencies.

## Risks and Assumptions

- The current API module is scaffold-only, so strategy verification must avoid depending on endpoint behavior.
- Future security and persistence workflows may replace the unauthenticated and scenario-owned data-isolation defaults.
- `realworld-api-st` already includes REST Client extensions; no extension changes are required for this workflow.

## Acceptance Criteria (Given/When/Then)

### AC1: Target API configuration

Given a developer runs `realworld-api-st`, when system-test clients are configured, then they use a single target API base URL convention through `realworld-api.base-url` and REST Client config key `service_uri`.

### AC2: Data isolation strategy

Given endpoint-specific system tests are added later, when they need data isolation, then the baseline strategy tells them to use scenario-owned unique data and not assume a global reset endpoint.

### AC3: Authentication strategy

Given authentication is not implemented yet, when system tests are authored, then the baseline strategy explicitly declares unauthenticated execution until a later security workflow changes the contract.

### AC4: Verification boundaries

Given this workflow is only the standalone strategy, when it completes, then it verifies reusable strategy contracts and README guidance but does not add endpoint-specific scenario coverage.
