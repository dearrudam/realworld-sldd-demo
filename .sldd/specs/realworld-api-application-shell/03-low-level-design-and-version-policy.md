# Requirement-to-Design Traceability

| Requirement | Concrete coverage |
|---|---|
| Minimal runnable API shell | Keep `realworld-api` as a Quarkus Maven application and remove generated greeting endpoint behavior. |
| Health/readiness endpoints | Add `quarkus-smallrye-health`; test `/q/health`, `/q/health/live`, and `/q/health/ready`. |
| Shell-only readiness | Do not add custom readiness checks; test only standard `UP` status from SmallRye Health defaults. |
| BCE convention only | Document `dev.realworld.<business-component>.<boundary|control|entity>` in `realworld-api/README.md`; do not add placeholder packages/classes. |
| Local build/run | Run Quarkus tests through Dev MCP tooling and repository baseline checks. |

# API Contracts

This workflow introduces no RealWorld business API contract. The only externally observable behavior is Quarkus non-application health endpoints:

- `GET /q/health`
- `GET /q/health/live`
- `GET /q/health/ready`

These endpoints return SmallRye Health JSON documents with `status: "UP"` for the shell baseline. Generated `GET /hello` behavior is removed and must return `404` in tests.

# Data Models

No RealWorld domain models, DTOs, MongoDB documents, repositories, JSON envelope records, or BCE entities are introduced.

# Error Model

No application error model is introduced. The removed `/hello` route should return the platform default `404` response. Health endpoint error behavior remains owned by SmallRye Health.

# Test Strategy

- Red phase: replace generated greeting tests with health-shell tests before adding the health extension/removing the resource. Expected failures:
  - Health endpoints may be unavailable before `quarkus-smallrye-health` is added.
  - `/hello` may still return `200` while generated scaffolding remains.
- Green phase: add `quarkus-smallrye-health`, remove generated greeting resource and integration test inheritance, update README, and rerun tests.
- Verification phase: run Quarkus tests plus repository baseline scripts.

# Test Scenario Catalog

- `GET /q/health` returns `200` and JSON field `status` equals `UP`.
- `GET /q/health/live` returns `200` and JSON field `status` equals `UP`.
- `GET /q/health/ready` returns `200` and JSON field `status` equals `UP`.
- `GET /hello` returns `404`.
- README contains `quarkus-smallrye-health`, health endpoint paths, and BCE convention text.

# Dependency and Version Policy

Current dependency set is not sufficient because `quarkus-smallrye-health` is required to provide standard health/readiness endpoints.

New dependency:

- `io.quarkus:quarkus-smallrye-health`
  - Needed for Quarkus-supported health, liveness, and readiness endpoints.
  - Version managed by the existing Quarkus BOM `3.36.1`; no explicit dependency version is pinned in `pom.xml`.
  - Runtime impact: exposes standard non-application health endpoints under `/q/health`.
  - Test impact: enables health endpoint integration tests.
  - Maintenance impact: future dependency health checks can be added by later workflows without changing endpoint paths.

No MongoDB, JSON-B, JWT, metrics, tracing, or business endpoint dependencies are added in this workflow.

# Ordered Implementation Plan

1. Write Red tests in `realworld-api` for SmallRye Health endpoints and removal of `/hello` behavior.
2. Run tests through Quarkus Dev MCP and confirm Red.
3. Add `quarkus-smallrye-health` using the Quarkus extension tool.
4. Stop/start Quarkus dev mode after the dependency change.
5. Remove generated `GreetingResource` and generated integration-test inheritance.
6. Update `realworld-api/README.md` with shell purpose, health endpoints, extension list, BCE convention, and guide links.
7. Run Quarkus tests until Green.
8. Run repository baseline verification scripts.
9. Save Step 06 verification report.
