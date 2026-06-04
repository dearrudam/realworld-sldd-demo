# Low-Level Design and Version Policy: BCE Architecture Baseline

## Requirement-to-Design Traceability

| Requirement | Low-Level Decision | Step 04/05 Constraint |
| --- | --- | --- |
| Document BCE package/component rules. | Add or update API-owned architecture documentation describing `org.soujava.demo.sldd.<business-component>.<boundary|control|entity>`. | Red tests must fail when the baseline document or required package policy text is absent. Green implementation must add only the missing baseline material. |
| Clarify layer responsibilities. | Document Quarkus-specific boundary, control, and entity responsibilities, including JAX-RS resources in boundary and domain state in entity. | Tests must check for project-specific responsibility statements rather than generic BCE explanation. |
| Define cross-component interaction policy. | Document allowed collaboration through boundary/control entry points and controlled entity references aligned with the domain model. | Tests must verify the presence of explicit cross-component policy text. |
| Define test isolation policy. | Document and verify that `realworld-api-st` remains black-box and has no Maven dependency on `realworld-api`. | Tests must not import API internals into `realworld-api-st`; verification may inspect the system-test POM or source tree. |
| Avoid endpoint behavior implementation. | Step 05 must not add RealWorld resources, DTOs, repositories, persistence mappings, or endpoint behavior. | Tests should target docs/architecture markers, not RealWorld runtime behavior. |

## API Contracts

This workflow does not introduce or change HTTP API contracts.

- No RealWorld endpoint paths are added.
- No request or response payloads are added.
- Existing generated starter endpoints remain scaffold-only and outside the RealWorld API contract.
- Later child workflows own authentication, users, profiles, articles, comments, favorites, feeds, and tags behavior.

## Data Models

This workflow does not add domain or persistence data models.

- `realworld-api/docs/domain-model.md` remains the source of truth for future domain implementation.
- No entity, DTO, repository, JNoSQL mapping, collection, index, or persistence schema is introduced here.
- If Step 05 adds package-level documentation files, they must describe responsibilities only and must not define runtime domain state.

## Error Model

This workflow does not introduce runtime error behavior.

- No JAX-RS exception mappers are added.
- No authentication, validation, persistence, or domain errors are added.
- Future child workflows must define their own error behavior through their SLDD Step 02 and Step 03 artifacts.

## Test Strategy

Step 04 should add Red tests that fail because the BCE baseline has not yet been implemented. Suitable tests are:

- API-side documentation tests that read the planned BCE architecture document and assert required project-specific rules.
- Repository or module-independence checks confirming `realworld-api-st` does not depend on `realworld-api` and does not import `org.soujava.demo.sldd` API internals.
- Tests must avoid RestAssured in `realworld-api-st`; any future black-box HTTP tests there should use Quarkus REST Client.

Step 05 should make the smallest production/documentation changes needed to satisfy those tests.

## Test Scenario Catalog

| ID | Scenario | Red Condition | Green Condition |
| --- | --- | --- | --- |
| BCE-DOC-001 | API BCE baseline document exists. | `realworld-api/docs/bce-architecture.md` or equivalent approved baseline is absent. | The document exists and references the approved root package and BC layout. |
| BCE-DOC-002 | Layer responsibilities are project-specific. | The baseline lacks Quarkus-specific boundary/control/entity responsibilities. | The baseline states JAX-RS resources and HTTP mapping belong in boundary, procedural operations in control, and domain state/behavior in entity. |
| BCE-DOC-003 | Cross-component policy is explicit. | The baseline lacks collaboration and entity-reference rules. | The baseline documents boundary/control entry points and domain-model-aligned entity references. |
| BCE-ST-001 | System tests remain black-box. | The system-test module depends on or imports API internals. | The POM and sources show no API Maven dependency and no API implementation imports. |
| BCE-SCAFFOLD-001 | Starter code is outside RealWorld contract. | The baseline does not classify greeting/garage generated code as scaffold-only. | The baseline states generated starter resources and codestart examples are not RealWorld API contract. |

## Dependency and Version Policy

The current dependency set is sufficient for this workflow.

- **New dependencies required:** none.
- **Reason:** Step 04 can use existing Maven, JUnit, AssertJ, and file-reading capabilities to verify architecture documentation and module independence.
- **Version pinning:** no new version pins are needed.
- **Runtime impact:** none; this workflow should not change runtime behavior.
- **Test impact:** only architecture/documentation tests should be added.
- **Maintenance impact:** future capability workflows must update the BCE baseline only through SLDD when they intentionally change package, layer, cross-component, or test-isolation rules.

Do not change `pom.xml` files in this workflow unless Step 03 is explicitly revised and approved to add a dependency.

## Ordered Implementation Plan

1. In Step 04, add Red tests that check for the planned BCE baseline document and module-independence rules.
2. Confirm the tests fail for the expected reason before production/documentation changes.
3. In Step 05, add the minimal BCE architecture baseline documentation under `realworld-api/docs/`.
4. If needed for test clarity, add package-level documentation markers without moving generated scaffold classes or adding RealWorld behavior.
5. Keep generated greeting and garage/codestart code classified as scaffold-only.
6. Run API tests from `realworld-api/` and any repository-level scaffold verification needed by changed files.
7. In Step 06, verify that the baseline is documented, tests pass, runtime behavior is unchanged, and downstream child workflows can use the approved BCE package policy.
