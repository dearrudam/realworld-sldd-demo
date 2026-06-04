# Repository Structure Overview

- The repository contains a Quarkus RealWorld backend workspace with two Maven projects: `realworld-api/` and `realworld-api-st/`.
- `realworld-api/` is the backend API application. It currently contains Quarkus starter code plus the approved JNoSQL MongoDB extension.
- `realworld-api-st/` is a standalone black-box system-test application. It must call the API over HTTP and must not depend on `realworld-api` internals.
- `.sldd/specs/` contains SLDD journals and artifacts. `realworld-quarkus-apps` is complete and `realworld-domain-model` is the active workflow.

# Architecture Summary

- The implementation is still a scaffold. Generated greeting and JNoSQL sample classes are explicitly not part of the RealWorld API contract.
- The backend stack is Quarkus 3.36.1 with Java 25, Quarkus REST JSON-B, ArC CDI, and Quarkiverse JNoSQL MongoDB.
- The root `README.md` states that RealWorld behavior is deferred to later SLDD-scoped changes.
- The domain-model workflow is documentation-first: it creates a canonical model reference, not domain entities, repositories, API behavior, or persistence mappings.

# Conventions to Preserve

- Keep SLDD artifacts under `.sldd/specs/<feature-name>/` and use `_spec-journal.json` only for progress, evidence, and artifact links.
- Keep `realworld-api-st` black-box and independent from `realworld-api` classes and Maven artifacts.
- Prefer Quarkus extensions over custom infrastructure when adding capabilities.
- Keep project documentation current after structural or behavioral changes.
- Build each Quarkus project from its own directory with Maven.

# Integration Points

- The canonical domain model reference will be `realworld-api/docs/domain-model.md`.
- Root documentation should link to that reference after implementation.
- `realworld-api/AGENTS.md` should require domain-affecting implementation work to follow `docs/domain-model.md`.
- RealWorld backend documentation is available at `https://realworld-docs.netlify.app/specifications/backend/endpoints/` and `https://realworld-docs.netlify.app/specifications/backend/api-response-format/`.
- Direct OpenAPI artifact URLs attempted during Step 99 were not reachable, so downstream artifacts must record that the reachable evidence is the RealWorld backend documentation pages rather than a fetched OpenAPI file.

# Risks and Unknowns

- The reachable RealWorld docs describe API shapes and endpoint behavior, but they do not fully prove persistence structure, cascade rules, MongoDB document layout, or all uniqueness constraints.
- `Profile` appears API-facing and can be modeled as a projection of `User` unless later design explicitly stores it separately.
- `following`, `favorited`, and `favoritesCount` are viewer-relative or computed response fields, not automatically persisted fields.
- Tags are API strings; persistence may normalize them into a `Tag` concept or store article tag values directly.
- JNoSQL MongoDB choices should remain deferred to future implementation workflows unless this documentation workflow needs to mention candidate implications.
- The Quarkus update dry run reported the project as up to date, despite the tool report text comparing 3.36.1 to 3.34.1 inconsistently.

# Context to Carry Into Steps 02-06

- Step 02 should design a project-owned domain reference document, not production domain code.
- Step 03 should define the exact markdown sections, model version policy, source evidence notes, and documentation link updates.
- Step 04 should add tests that fail only because `realworld-api/docs/domain-model.md` and required documentation/rule links do not yet exist.
- Step 05 should make minimal documentation and metadata changes to satisfy the Red tests without implementing RealWorld runtime behavior.
- Step 06 should verify the API project build and scaffold verification remain valid.
