# Repository Structure Overview

- The repository currently contains the RealWorld template `README.md`, SLDD workflow artifacts under `.sldd/specs/`, agent skills under `.agents/skills/`, and the contract verification script under `scripts/`.
- There is not yet a Quarkus workspace, Maven parent, Gradle build, `pom.xml`, Java source tree, or generated `realworld-api` / `realworld-api-st` application.
- The predecessor workflow `realworld-api-contract-baseline` is complete and provides the local downstream contract artifact at `.sldd/specs/realworld-api-contract-baseline/realworld-api-contract-baseline.md`.

# Architecture Summary

- No application architecture has been implemented yet.
- The workspace baseline should introduce only physical project boundaries and build/run conventions.
- Detailed BCE package rules, persistence, security internals, endpoint implementation, and HTTP system-test scenarios remain owned by later workflows.

# Conventions to Preserve

- Preserve the existing SLDD workflow structure and journal-only `_spec-journal.json` files.
- Preserve the completed RealWorld API contract baseline as the source for downstream API behavior.
- Use Quarkus-supported project generation and Quarkus build/run conventions.
- Do not introduce RealWorld business endpoint behavior in this workflow.
- Keep generated application shells minimal and defer business architecture to later workflows.

# Integration Points

- `realworld-api` will become the main Quarkus backend application.
- `realworld-api-st` will become a standalone Quarkus application for HTTP-level system tests against `realworld-api`.
- Later workflows will consume the workspace layout before adding BCE architecture, system-test strategy, application shells, and endpoint slices.
- Local run conventions need to reserve independent ports so both applications can run concurrently.

# Risks and Unknowns

- Quarkus extension selection must happen before project generation or code creation and should not be silently chosen.
- A premature multi-module or separate-root decision can create later churn if not documented.
- The system-test application needs target URL configuration, but no endpoint behavior exists yet.
- Generated starter code may need to be minimized or documented so it is not mistaken for RealWorld business behavior.

# Context to Carry Into Steps 02-06

- This workflow is brownfield only in the sense that it modifies an existing template repository; it is greenfield for Quarkus application code.
- Step 02 and Step 03 should define a minimal workspace baseline, not endpoint logic.
- Step 04 should test/check the expected workspace structure and conventions before implementation.
- Step 05 may create Quarkus project structure only after Quarkus extension discovery and user selection rules are satisfied.
