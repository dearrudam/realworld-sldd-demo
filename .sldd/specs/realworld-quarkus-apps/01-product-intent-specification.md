# Problem Statement

This repository is a fork of the RealWorld example-app initiative template. Its purpose is to implement the RealWorld backend API using Quarkus and SLDD.

The workspace will contain two Quarkus applications:

- `realworld-api`: the RealWorld REST API implementation.
- `realworld-api-st`: a standalone Quarkus system-test application that validates `realworld-api` over HTTP.

The project should demonstrate a spec-driven development flow where SLDD guides intent, design, tests, implementation, and verification.

# Target Users

- Developers implementing a Quarkus backend compatible with the RealWorld API.
- Developers and maintainers validating the backend through standalone black-box system tests.
- Practitioners evaluating SLDD as a workflow for Java and Quarkus application development.

# Formalized Exploration Decisions

- This workspace is a spec-driven RealWorld backend demo repository.
- The inherited README is placeholder RealWorld template content and must be updated as the implementation becomes concrete.
- SLDD is part of the project identity, not only a temporary development process.
- The repository should eventually include workspace-level guidance, such as a root `AGENTS.md`, but that guidance must be introduced through the SLDD flow.
- Subproject-specific guidance files may be added after `realworld-api` and `realworld-api-st` exist.
- `realworld-api-st` is a standalone system-test application, not an internal unit-test module.
- The standalone system tests must validate the API externally over HTTP and must not depend on API internals.
- Maven groupId: `org.soujava.demo.sldd`
- API artifactId: `realworld-api`
- Standalone test artifactId: `realworld-api-st`
- `realworld-api` requested extensions: `rest-jsonb`, `quarkus-jnosql-mongodb`
- `realworld-api-st` requested extension: `rest-client` and add assertj-core for fluent assertions in system tests.

# Success Metrics

- The repository clearly documents its role as a Quarkus + SLDD RealWorld backend implementation.
- The SLDD workflow captures the intent, design, tests, implementation, and verification path.
- `realworld-api` is generated as a Quarkus REST API application.
- `realworld-api-st` is generated as a standalone Quarkus system-test application.
- The generated structure supports later SLDD steps for implementing RealWorld API behavior.
- README documentation is updated after structural changes.
- Workspace and subproject guidance files are created only when approved by the SLDD flow.

# Out of Scope

- Full RealWorld API behavior implementation is not part of the initial scaffolding step unless approved in later SLDD steps.
- Authentication, users, profiles, articles, comments, favorites, feeds, and pagination behavior are deferred to later SLDD-scoped implementation work.
- Deployment manifests, CI/CD, and production hardening are deferred unless added by later approved scope.
- Direct, non-SLDD implementation changes are out of scope.

# Risks and Assumptions

- `io.quarkiverse.jnosql:quarkus-jnosql-mongodb` matches the requested MongoDB persistence capability, though it may require additional configuration or code to integrate with the RealWorld domain model.
- `io.quarkus:quarkus-rest-jsonb` matches the requested REST JSON-B capability.
- `io.quarkus:quarkus-rest-client` matches the requested REST client capability, though `io.quarkus:quarkus-rest-client-jsonb` may be more appropriate if JSON-B payload handling is needed in the system-test app.
- The workspace will contain separate Quarkus projects rather than a single generated multi-module project unless changed during design.
- Maven is assumed as the build tool unless changed during design.

# Acceptance Criteria (Given/When/Then)

Given this repository is a fork of the RealWorld example-app template
When the SLDD workflow is established
Then the workspace purpose is documented as a Quarkus + SLDD RealWorld backend implementation.

Given Step 01 is approved
When the SLDD journal is created
Then `.sldd/specs/realworld-quarkus-apps/_spec-journal.json` records Step 01 as complete with its artifact link.

Given the design steps are later approved
When the Quarkus applications are generated
Then `realworld-api` exists with groupId `org.soujava.demo.sldd`.

Given the design steps are later approved
When the Quarkus applications are generated
Then `realworld-api-st` exists with groupId `org.soujava.demo.sldd`.

Given `realworld-api` is generated
When its dependencies are inspected
Then it includes the approved REST JSON-B extension and the approved MongoDB persistence extension.

Given `realworld-api-st` is generated
When its dependencies are inspected
Then it includes the approved REST client extension.

Given standalone system tests are implemented in later SLDD steps
When they validate API behavior
Then they interact with `realworld-api` over HTTP and do not import API internals.

Given workspace guidance is introduced
When `AGENTS.md` files are added
Then they are created through approved SLDD scope rather than direct ad hoc changes.
