# Requirement-to-Design Traceability

Requirement: establish a Quarkus + SLDD RealWorld backend workspace.
Coverage: scaffold two independent sibling Quarkus Maven projects and update root documentation/guidance.

Requirement: create `realworld-api`.
Coverage: generate a Quarkus app with groupId `org.soujava.demo.sldd`, artifactId `realworld-api`, REST JSON-B support, and the requested `jnosql-mongodb` extension. If generation or dependency resolution fails for `jnosql-mongodb`, stop and ask the user for direction.

Requirement: create `realworld-api-st`.
Coverage: generate a Quarkus app with groupId `org.soujava.demo.sldd`, artifactId `realworld-api-st`, REST client support, and no dependency on `realworld-api` internals.

Requirement: preserve black-box system-test boundaries.
Coverage: `realworld-api-st` communicates with `realworld-api` only over HTTP. No Maven dependency from `realworld-api-st` to `realworld-api`.

Requirement: introduce workspace guidance through SLDD.
Coverage: add root `AGENTS.md` during scaffold implementation and add subproject `AGENTS.md` files after app directories exist.

Requirement: keep README current.
Coverage: replace placeholder RealWorld template text with Quarkus + SLDD workspace documentation during implementation.

# API Contracts

Initial scaffold contracts:

- No RealWorld endpoint behavior is implemented in this change.
- Generated Quarkus starter endpoints may exist only as scaffold code.
- Any starter endpoint is not considered part of the RealWorld API contract.
- Future RealWorld API contracts will be specified in later SLDD workflows.

Workspace contracts:

- Root contains `realworld-api/` and `realworld-api-st/`.
- Each subproject is independently buildable with Maven.
- `realworld-api-st` uses HTTP configuration to target `realworld-api`.
- Root README documents the workspace purpose, project layout, and basic commands.
- Root `AGENTS.md` documents workspace-wide development rules.
- Subproject `AGENTS.md` files document app-specific boundaries.

# Data Models

Initial scaffold data models:

- No RealWorld domain model is introduced in this scaffold change.
- No users, profiles, articles, comments, tags, or authentication models are introduced yet.
- No MongoDB document schema is introduced yet.

Generated project model:

- `realworld-api` owns future API resources, controls, entities, DTOs, persistence adapters, and configuration.
- `realworld-api-st` owns future system-test client DTOs and test fixtures.
- Shared DTOs are not introduced in this change because system tests must stay black-box.

Future model policy:

- API DTOs and persistence documents must be designed in later SLDD changes.
- System-test DTOs may duplicate HTTP payload shapes when needed to avoid coupling to API internals.

# Error Model

Initial scaffold error model:

- No RealWorld error response contract is implemented in this change.
- Generated Quarkus default errors are acceptable during scaffold verification.
- Future RealWorld error responses must follow the RealWorld API spec and be designed in later SLDD changes.

Expected scaffold errors:

- Build failure if an approved extension cannot be resolved. For `jnosql-mongodb`, this is a blocking condition and must not be handled by substituting a fallback extension.
- Build failure if generated project configuration is invalid.
- Verification failure if required files, dependencies, or documentation are missing.

# Test Strategy

Step 04 Red phase:

- Create scaffold verification tests/checks that fail before generation.
- Tests should verify expected directories, Maven project files, dependencies, README content, and `AGENTS.md` files.
- Red evidence must show these checks fail because the projects and files do not exist yet.

Step 05 Green phase:

- Generate `realworld-api`.
- Generate `realworld-api-st`.
- Add root and subproject `AGENTS.md` files.
- Update README.
- Update `.gitignore` for Java/Quarkus/Maven outputs.
- Run the same checks until they pass.
- Build both generated Quarkus projects.

Step 06 verification:

- Re-run scaffold checks.
- Re-run Maven builds for both projects.
- Confirm no forbidden dependency from `realworld-api-st` to `realworld-api`.
- Confirm documentation reflects the final layout.

# Test Scenario Catalog

Scaffold structure scenarios:

- Given the repository root, when scaffold verification runs, then `realworld-api/pom.xml` exists.
- Given the repository root, when scaffold verification runs, then `realworld-api-st/pom.xml` exists.
- Given the repository root, when scaffold verification runs, then root `AGENTS.md` exists.
- Given generated subprojects, when scaffold verification runs, then `realworld-api/AGENTS.md` and `realworld-api-st/AGENTS.md` exist.

Dependency scenarios:

- Given `realworld-api/pom.xml`, when dependencies are inspected, then Quarkus REST JSON-B is present.
- Given `realworld-api/pom.xml`, when dependencies are inspected, then the requested JNoSQL MongoDB extension is present.
- Given `realworld-api-st/pom.xml`, when dependencies are inspected, then Quarkus REST Client is present.
- Given `realworld-api-st/pom.xml`, when dependencies are inspected, then it does not declare a dependency on `realworld-api`.

Documentation scenarios:

- Given `README.md`, when content is inspected, then `[YOUR_FRAMEWORK]` placeholders are removed.
- Given `README.md`, when content is inspected, then it describes Quarkus, SLDD, `realworld-api`, and `realworld-api-st`.
- Given `.gitignore`, when content is inspected, then Java/Quarkus/Maven outputs are ignored.

Build scenarios:

- Given `realworld-api`, when Maven tests/build run, then the project builds successfully.
- Given `realworld-api-st`, when Maven tests/build run, then the project builds successfully.

# Dependency and Version Policy

Build tool:

- Use Maven for both Quarkus applications.
- Generate Maven wrappers for both applications unless Quarkus generation fails or the user explicitly changes this decision.
- Keep apps as independent sibling Maven projects, not a root multi-module build.

Quarkus platform:

- Use the current Quarkus platform version available from Quarkus tooling at generation time.
- Discovery currently reports Quarkus `3.36.0` in documentation and extension registry results.
- Do not pin an older version unless generation requires it.

`realworld-api` dependencies:

- Required: `io.quarkus:quarkus-rest-jsonb`
- Needed for: JSON-B serialization support for Quarkus REST.
- Runtime impact: enables JSON request/response body serialization.
- Test impact: project dependency checks and future HTTP payload tests.
- Maintenance impact: standard Quarkus platform-managed extension.

- Required: `jnosql-mongodb` / `io.quarkiverse.jnosql:quarkus-jnosql-mongodb`
- Discovery result: may not be present in queried Quarkus extension registry.
- Policy: attempt to generate with the requested JNoSQL MongoDB extension and do not use a fallback.
- Failure policy: if Quarkus CLI generation or dependency resolution fails for `jnosql-mongodb`, stop and ask the user for direction before changing the extension choice.
- Needed for: requested MongoDB persistence capability through JNoSQL.
- Runtime impact: enables future MongoDB integration through the requested JNoSQL extension.
- Test impact: dependency verification only in this scaffold; no MongoDB behavior tests yet.
- Maintenance impact: may require explicit user direction if the extension is not available through the active Quarkus platform or registry.

Alternatives not selected now:

- `io.quarkus:quarkus-mongodb-client`
- `io.quarkus:quarkus-mongodb-panache`
- Reason: the user explicitly requested no fallback for `jnosql-mongodb`; any alternative requires explicit user approval through the SLDD flow.

`realworld-api-st` dependencies:

- Required: `io.quarkus:quarkus-rest-client`
- Needed for: standalone HTTP client behavior against `realworld-api`.
- Runtime impact: supports future black-box REST calls.
- Test impact: dependency verification only in this scaffold.
- Maintenance impact: Quarkus platform-managed extension.

Alternative not selected now:

- `io.quarkus:quarkus-rest-client-jsonb`
- Reason: JSON-B typed client DTO serialization is likely useful later, but plain `rest-client` directly matches the requested extension and is sufficient for scaffold creation. Add `rest-client-jsonb` in a later SLDD change if typed JSON payload tests require it.

Documentation and guidance files:

- Add root `AGENTS.md`.
- Add `realworld-api/AGENTS.md`.
- Add `realworld-api-st/AGENTS.md`.
- Update root `README.md`.
- Update root `.gitignore`.

No external non-Quarkus dependencies are introduced by this scaffold design.

# Ordered Implementation Plan

Step 04 Red tests/checks:

1. Add a scaffold verification script or test artifact that checks required directories, Maven files, dependencies, README content, `.gitignore`, and `AGENTS.md` files.
2. Run the verification before generation.
3. Confirm Red failure because scaffold files do not exist yet.
4. Mark Step 04 complete only with `red_confirmed` evidence.

Step 05 Green implementation:

1. Generate `realworld-api` as a Quarkus Maven app with:
   - groupId `org.soujava.demo.sldd`
   - artifactId `realworld-api`
   - extensions `rest-jsonb,jnosql-mongodb`
   - stop and ask the user for direction if `jnosql-mongodb` cannot be resolved or generated
2. Generate `realworld-api-st` as a Quarkus Maven app with:
   - groupId `org.soujava.demo.sldd`
   - artifactId `realworld-api-st`
   - extension `rest-client`
3. Add root `AGENTS.md` with workspace-wide RealWorld + SLDD + Quarkus rules.
4. Add `realworld-api/AGENTS.md` with API-specific boundaries.
5. Add `realworld-api-st/AGENTS.md` with standalone black-box system-test boundaries.
6. Update root README to describe this Quarkus + SLDD RealWorld workspace.
7. Update `.gitignore` for Java, Maven, Quarkus, logs, and build outputs.
8. Run scaffold verification until it passes.
9. Build/test `realworld-api`.
10. Build/test `realworld-api-st`.
11. Mark Step 05 complete only with `green_confirmed` evidence.

Step 06 verification:

1. Re-run scaffold verification.
2. Re-run both Maven builds.
3. Inspect dependency boundaries.
4. Produce the verification and feedback report.
