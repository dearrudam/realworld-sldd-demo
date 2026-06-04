# Problem Statement

The RealWorld API implementation needs a canonical, project-owned domain model reference before domain entities, DTOs, persistence mappings, validation rules, and relationship behavior are implemented.

The external RealWorld OpenAPI contract defines API-facing schemas and operations involving users, profiles, articles, comments, tags, follows, and favorites. This project needs to analyze that contract and produce a local reference document for `realworld-api` that records the approved entities, relationship cardinality, field restrictions, uniqueness rules, computed fields, and model evolution policy.

The canonical implementation reference will be:

`realworld-api/docs/domain-model.md`

The SLDD workflow records the decision history and verification evidence, while `realworld-api/docs/domain-model.md` becomes the main documentation artifact used by implementation work.

# Target Users

- Developers implementing RealWorld domain behavior in `realworld-api`.
- Developers implementing persistence, validation, DTOs, repositories, and domain services.
- Developers writing system tests for RealWorld API behavior.
- Maintainers evolving the domain model through future SLDD workflows.
- Agents and automation that need a stable project-owned source of truth before modifying domain implementation.

# Formalized Exploration Decisions

- The current `realworld-quarkus-apps` workflow remains a scaffold workflow and should not be expanded with domain-model responsibilities.
- A new dependent workflow named `realworld-domain-model` will define the domain reference.
- The external OpenAPI contract is source evidence, not the canonical implementation source of truth.
- The canonical implementation reference will live in `realworld-api/docs/domain-model.md`.
- The SLDD artifacts under `.sldd/specs/realworld-domain-model/` provide the approval trail and audit history.
- The domain reference must include a Mermaid `erDiagram`.
- The domain reference must include relationship cardinality and field restrictions.
- The domain reference must distinguish stored entities, API projections, relationship entities, and computed response fields.
- `Profile` is treated as an API-facing projection candidate unless later design approves it as a stored entity.
- `favorited`, `favoritesCount`, and `following` are treated as computed or viewer-relative API fields unless later design approves persisted storage.
- Future domain model changes must update the canonical reference through SLDD before implementation diverges from it.
- The reference document must include an evolution policy and model version.

# Success Metrics

- `realworld-api/docs/domain-model.md` exists after implementation when `realworld-api/` exists.
- The domain model document is linked from project documentation, such as root `README.md` and `realworld-api/README.md` when present.
- `realworld-api/AGENTS.md` requires domain implementation to follow `docs/domain-model.md`.
- The document includes a Mermaid ER diagram derived from the OpenAPI contract and approved project decisions.
- The document includes an entity catalog covering at least User, Profile, Article, Comment, Tag, Follow, and Favorite concepts.
- The document includes relationship cardinality for authoring, commenting, following, favoriting, and tagging.
- The document includes field restrictions derived from OpenAPI schema evidence.
- The document identifies ambiguous or inferred persistence decisions separately from contract-proven API facts.
- Future implementation workflows can cite `realworld-api/docs/domain-model.md` as the required domain reference.

# Out of Scope

- Implementing domain entities, repositories, persistence adapters, API endpoints, authentication, comments, favorites, follows, feeds, or tags behavior.
- Creating database migrations or MongoDB collection schemas beyond documenting candidate persistence implications.
- Treating the external OpenAPI contract as automatically equivalent to the persistence model.
- Finalizing Quarkus/JNoSQL implementation details.
- Changing the currently approved `realworld-quarkus-apps` scaffold workflow unless explicitly requested later.
- Creating `realworld-api/docs/domain-model.md` before `realworld-api/` exists, unless the user explicitly approves a temporary location.

# Risks and Assumptions

- The OpenAPI contract gives strong API-facing evidence but cannot fully prove persistence cardinality, cascade behavior, join-table shape, embedded-document choices, or referential integrity strategy.
- Some API fields are likely projections or computed fields rather than stored entity attributes.
- `Profile` may be an API projection of `User`, but this must be explicitly approved during design.
- Tags appear as strings in the API, but persistence may model them as separate entities or normalized values.
- Favorites and follows are API behaviors that likely require relationship records, but the exact persistence representation needs design approval.
- The domain reference depends on `realworld-api/` existing; if the scaffold workflow is not complete, this workflow may need to stop before Step 05 or materialize only SLDD artifacts.
- The external OpenAPI URL may change, so the local domain reference must record the source URL and, ideally, the version observed.

# Acceptance Criteria (Given/When/Then)

Given the RealWorld OpenAPI contract is available
When the domain-model workflow analyzes source evidence
Then the analysis identifies API schemas, required fields, nullable fields, formats, arrays, path relationships, and operation-level relationship hints.

Given the OpenAPI evidence is analyzed
When the domain model reference is designed
Then it includes a Mermaid `erDiagram` showing approved entities and relationship cardinality.

Given the OpenAPI evidence is analyzed
When field restrictions are documented
Then each documented field records required status, nullable status, type, format, uniqueness or identity assumptions, and source evidence where available.

Given the domain model includes API response-only fields
When the reference document classifies fields
Then computed or viewer-relative fields such as `favorited`, `favoritesCount`, and `following` are not silently treated as stored fields.

Given the domain model includes `Profile`
When the reference document classifies model concepts
Then `Profile` is identified as an API projection unless design explicitly approves it as a stored entity.

Given `realworld-api/` exists
When Step 05 implementation runs
Then `realworld-api/docs/domain-model.md` is created as the canonical project-owned domain model reference.

Given the canonical domain model document exists
When project documentation is inspected
Then root documentation links to `realworld-api/docs/domain-model.md`.

Given `realworld-api/AGENTS.md` exists
When its project rules are inspected
Then it requires implementations affecting entities, DTOs, repositories, validation rules, persistence mappings, or relationship behavior to follow `docs/domain-model.md`.

Given a future implementation workflow changes domain behavior
When that change conflicts with `realworld-api/docs/domain-model.md`
Then the domain model must be updated through SLDD before implementation proceeds.

Given the domain model evolves
When the canonical document is updated
Then its model version and change history are updated with the related SLDD workflow reference.
