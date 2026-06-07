# Repository Structure Overview

- Repository root contains two Quarkus Maven applications:
  - `realworld-api/` — production API application shell target for this workflow.
  - `realworld-api-st/` — standalone system-test application reserved for future black-box HTTP tests.
- `realworld-api` currently has Quarkus REST and ArC dependencies, Java 25, Quarkus platform `3.36.1`, and HTTP port `8080` configured in `src/main/resources/application.properties`.
- `realworld-api` still contains generated greeting scaffolding:
  - `src/main/java/dev/realworld/GreetingResource.java`
  - `src/test/java/dev/realworld/GreetingResourceTest.java`
  - `src/test/java/dev/realworld/GreetingResourceIT.java`
- Root `README.md` links the contract and architecture baseline artifacts and repository baseline verification scripts.

# Architecture Summary

- The approved architecture baseline defines future BCE packages as `dev.realworld.<business-component>.<boundary|control|entity>`.
- This application-shell workflow should not create fake or empty RealWorld business-component packages before a real business slice exists.
- The shell may replace generated greeting scaffolding with shell-appropriate health/readiness verification through Quarkus SmallRye Health.
- Production API code remains in `realworld-api`; standalone black-box system tests remain in `realworld-api-st` and should not depend on production Java classes.

# Conventions to Preserve

- Use Quarkus extension-first development and Quarkus Dev MCP tooling for extension changes and tests.
- Keep Quarkus configuration profile-aware for environment-specific values.
- Keep default local ports from the workspace baseline: `realworld-api` on `8080`, `realworld-api-st` on `8081`.
- Update `README.md` files when structural application capabilities or extensions change.
- Keep business endpoint behavior out of this workflow.

# Integration Points

- SmallRye Health is the approved operational endpoint capability for this shell.
- The standard Quarkus non-application health endpoints are expected at `/q/health`, `/q/health/live`, and `/q/health/ready` after adding `quarkus-smallrye-health`.
- No MongoDB, JWT, RealWorld JSON envelope, or business endpoint integration is implemented in this workflow.

# Risks and Unknowns

- The current generated `/hello` resource does not represent RealWorld API behavior and should be removed or replaced by shell-oriented verification.
- Adding `quarkus-smallrye-health` changes the Maven dependency set and requires a full Quarkus dev-mode stop/start cycle before relying on Dev MCP tests.
- SmallRye Health default readiness is shell-only unless future checks are added; future MongoDB/JWT workflows must decide whether they affect readiness.

# Context to Carry Into Steps 02-06

- Step 02/03 should design a minimal shell: add `quarkus-smallrye-health`, remove generated greeting endpoint/tests, verify health endpoints, document BCE convention only, and avoid business-component packages/classes.
- Step 04 should write tests first for health endpoint availability and for absence of generated `/hello` behavior.
- Step 05 should perform the minimum changes: add the health extension, remove greeting scaffolding, update README, and make the tests green.
- Step 06 should verify Quarkus tests and repository baseline scripts.
