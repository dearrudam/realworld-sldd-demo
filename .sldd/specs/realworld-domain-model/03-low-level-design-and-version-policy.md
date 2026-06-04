# Requirement-to-Design Traceability

- Canonical reference requirement: create `realworld-api/docs/domain-model.md` with model version, source evidence, entity catalog, relationship cardinality, field restrictions, Mermaid ER diagram, persistence implications, and evolution policy.
- Source evidence requirement: cite reachable RealWorld backend documentation pages for endpoints and API response formats, and record that direct OpenAPI URLs were not reachable during this workflow.
- Entity coverage requirement: document User, Profile, Article, Comment, Tag, Follow, and Favorite concepts.
- Classification requirement: explicitly distinguish stored entities, API projections, relationship entities, and computed/viewer-relative fields.
- Linkage requirement: update root `README.md` and `realworld-api/AGENTS.md` so future domain-affecting implementation work uses `docs/domain-model.md`.
- Scope control requirement: do not add domain runtime entities, repositories, migrations, endpoints, or persistence mappings in this workflow.

# API Contracts

- No runtime API contracts are added or changed.
- The domain reference will summarize documented RealWorld API concepts from:
  - `https://realworld-docs.netlify.app/specifications/backend/endpoints/`
  - `https://realworld-docs.netlify.app/specifications/backend/api-response-format/`
  - `https://realworld-docs.netlify.app/specifications/backend/error-handling/`
- The reference will identify these API-facing shapes: authenticated User, Profile, Article, Article list item, Comment, and Tags list.
- The reference will avoid treating response wrappers such as `user`, `profile`, `article`, `articles`, `comment`, `comments`, and `tags` as persisted domain entities.

# Data Models

- `User`: stored aggregate candidate with identity fields `username` and `email`, authentication secret material, optional `bio`, optional `image`, and response-only `token`.
- `Profile`: API projection of `User` with `username`, `bio`, `image`, and viewer-relative `following`.
- `Article`: stored aggregate candidate with `slug`, `title`, `description`, `body`, `tagList`, timestamps, author relationship, and computed favorite fields.
- `Comment`: stored aggregate candidate owned by an article and authored by a user.
- `Tag`: domain concept represented by strings in the API; storage normalization remains a future design decision.
- `Follow`: relationship concept from follower user to followed user.
- `Favorite`: relationship concept from user to article.
- `favorited`, `favoritesCount`, and `following` are computed or viewer-relative fields.

# Error Model

- No runtime error behavior changes are introduced.
- The domain reference will cite documented RealWorld error conventions: validation errors use HTTP 422 with an `errors.body` array, while authentication/authorization/not-found concerns use 401, 403, and 404.
- Future implementation workflows must define concrete validation and exception mapping behavior before changing runtime code.

# Test Strategy

- Add a JUnit test in `realworld-api/src/test/java/org/soujava/demo/sldd/DomainModelDocumentationTest.java`.
- The test will inspect repository files using `java.nio.file.Files` and `Path`.
- It will initially fail in Step 04 because `realworld-api/docs/domain-model.md` does not exist and required README/AGENTS links are absent.
- Step 05 will satisfy the tests with documentation and rule updates only.
- Existing Quarkus tests remain unchanged.

# Test Scenario Catalog

- `domainModelReferenceExists`: `docs/domain-model.md` exists under `realworld-api/`.
- `domainModelReferenceContainsRequiredSections`: the reference includes model version, source evidence, entity catalog, field restrictions, relationship cardinality, Mermaid ER diagram, API projection/computed fields, persistence implications, and evolution policy.
- `domainModelReferenceCoversApprovedConcepts`: the reference names User, Profile, Article, Comment, Tag, Follow, and Favorite.
- `domainModelReferenceRecordsApprovedClassifications`: the reference identifies Profile as an API projection and following/favorited/favoritesCount as computed or viewer-relative fields.
- `workspaceDocumentationLinksDomainModel`: root `README.md` links to `realworld-api/docs/domain-model.md`.
- `agentRulesRequireDomainModelReference`: `realworld-api/AGENTS.md` requires domain-affecting work to follow `docs/domain-model.md`.

# Dependency and Version Policy

- No new production dependencies are required.
- No new test dependencies are required; JUnit is already available through `quarkus-junit`.
- Quarkus remains on the current project-managed platform version.
- The domain model document starts at model version `0.1.0` because it is a pre-runtime reference for future implementation workflows.
- Future incompatible domain reference changes must increment the model version and record the related SLDD workflow.

# Ordered Implementation Plan

1. Add the Red documentation test under `realworld-api/src/test/java/org/soujava/demo/sldd/`.
2. Run the targeted Maven test and confirm failure for missing domain reference or required links.
3. Create `realworld-api/docs/domain-model.md` with the approved sections, Mermaid ER diagram, entity catalog, field restrictions, relationship cardinality, computed/API projection classifications, source evidence, and evolution policy.
4. Update root `README.md` with a link to the canonical domain model reference.
5. Update `realworld-api/AGENTS.md` to require domain-affecting implementation work to follow `docs/domain-model.md`.
6. Rerun the targeted Maven test and `mvn verify` in `realworld-api/`.
7. Run `sh scripts/verify-scaffold.sh` from the repository root because documentation/rule integration touches scaffold-level expectations.
