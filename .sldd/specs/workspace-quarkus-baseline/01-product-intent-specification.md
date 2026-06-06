# Problem Statement

The repository needs a minimal, explicit Quarkus workspace layout before architecture, application shell, system-test shell, and endpoint workflows can proceed. The current repository is still a RealWorld template plus SLDD artifacts, so downstream work needs agreed module boundaries, build/run conventions, and local development expectations.

# Target Users

- Developers creating and running `realworld-api`.
- Developers creating and running `realworld-api-st` as a standalone HTTP system-test application.
- SLDD reviewers verifying that later workflows use a consistent workspace structure.

# Formalized Exploration Decisions

- The workspace will contain two Quarkus applications: `realworld-api` and `realworld-api-st`.
- This workflow owns only the physical workspace baseline: project/module layout, build/run conventions, minimal generated application structure, and reserved local ports/profiles.
- The workflow must remain compatible with the approved `realworld-api-contract-baseline` and must not implement RealWorld business endpoints.
- Quarkus extension selection and exact generated structure must be decided in design before implementation, following Quarkus tooling guidance.

# Success Metrics

- Repository layout for both Quarkus apps is documented and created with minimal runnable structure.
- Build/run/test commands are clear for local development.
- Later architecture and feature workflows can add BCE structure, persistence, security, endpoints, and HTTP system tests without reorganizing the workspace.
- The baseline avoids premature business logic.

# Out of Scope

- RealWorld endpoint implementation, persistence model, authentication internals, BCE package details, and system-test scenario implementation.
- Production deployment, cloud infrastructure, frontend, and advanced observability.
- Detailed API behavior beyond referencing the approved contract baseline.

# Risks and Assumptions

- Quarkus project generation may require choosing extensions before code is written.
- A poor module layout would force later churn; this workflow should keep the structure simple and explicit.
- The standalone system-test app may need target URL/port conventions without depending on endpoint implementation yet.

# Acceptance Criteria (Given/When/Then)

- Given a developer clones the repository, When they inspect the workspace baseline, Then they can identify where `realworld-api` and `realworld-api-st` live and how each is built/run.
- Given later workflows need to add API architecture or endpoint slices, When they use this baseline, Then no workspace reorganization is required.
- Given `realworld-api-st` is intended to test over HTTP, When conventions are inspected, Then target API URL/port assumptions are documented.
- Given this workflow completes, When reviewing changes, Then no RealWorld business endpoint behavior has been implemented.
