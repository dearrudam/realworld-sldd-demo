# RealWorld API Architecture Baseline

This baseline records architecture decisions for downstream RealWorld API implementation workflows. It does not add endpoint behavior, persistence code, security code, or runtime dependencies.

## Source Baselines

- API contract: `../realworld-api-contract-baseline/realworld-api-contract-baseline.md`
- Workspace baseline: `../workspace-quarkus-baseline/workspace-quarkus-baseline.md`

## Application Boundary

`realworld-api` owns production API behavior. Future endpoint workflows add REST resources, business components, MongoDB/JNoSQL persistence integration, JWT authentication integration, JSON-B payload mapping, and runtime configuration to this application.

`realworld-api-st` owns standalone HTTP system tests. realworld-api-st calls realworld-api over HTTP and must not depend on production Java classes from `realworld-api`.

No RealWorld endpoint behavior is implemented by this baseline.

## BCE Package Convention

Future production code in `realworld-api` uses this package shape:

```text
dev.realworld.<business-component>.<boundary|control|entity>
```

Business components are direct children of `dev.realworld` and are named after RealWorld domain responsibilities, such as users, profiles, articles, comments, tags, authentication, or shared concerns that carry domain or protocol semantics.

Boundary classes adapt HTTP and JSON envelopes to internal operations. JAX-RS resources belong in boundary packages. Cross-cutting request concerns such as transactions and authorization checks belong in boundary classes.

Control classes contain procedural business logic. They are stateless where possible and are called by the owning component boundary or by another component when no boundary facade is justified.

Entity classes contain domain state and behavior. Future MongoDB documents, JNoSQL entities, value objects, and domain records belong in entity packages owned by the corresponding business component.

## Persistence Direction

MongoDB is the target database for future persistence workflows. `quarkus-jnosql-mongodb` is the approved extension direction for MongoDB/JNoSQL persistence.

This workflow does not add `quarkus-jnosql-mongodb`, MongoDB configuration, entities, repositories, indexes, seed data, or persistence implementation. Future persistence work must validate extension availability and compatibility before changing Maven dependencies.

Future MongoDB designs must decide document boundaries, identifier strategy, indexes, relation/reference shape, consistency rules, and query patterns before implementation.

## JSON Direction

JSON-B is the approved JSON serialization direction for RealWorld REST payloads. `quarkus-rest-jsonb` is the approved extension direction for JSON request and response mapping in `realworld-api`.

Future API workflows should map RealWorld JSON envelopes in boundary classes. Domain entities should not depend on transport-only envelope structure unless a later low-level design explicitly approves that coupling.

## Security Direction

JWT bearer-token authentication is the approved security direction. `quarkus-smallrye-jwt` is the approved extension direction for future protected endpoints and must remain compatible with the contract baseline's `Authorization: Bearer <token>` convention.

This workflow does not implement JWT signing, verification, claims, roles, expiration, key material, authorization policy, or secret management. Those decisions belong to future security and endpoint workflows.

## Configuration Convention

Use profile-scoped Quarkus configuration for environment-specific values. Do not hardcode external service URLs, database URLs, credentials, JWT secrets, keys, or deployment-specific settings without a `%dev`, `%test`, or other explicit profile when the value is profile-specific.

Preserve the workspace baseline ports unless a future approved workflow changes them:

- `realworld-api`: `8080`
- `realworld-api-st`: `8081`
- `realworld-api-st` target API URL: `http://localhost:8080`

## Test Layering

Future workflows use three test layers:

- Unit tests validate isolated domain/control behavior without Quarkus startup when practical.
- Quarkus integration tests in `realworld-api` use `@QuarkusTest` for in-process HTTP/resource and CDI behavior.
- Standalone HTTP system tests live in `realworld-api-st`, use REST client interfaces, and exercise a running `realworld-api` over HTTP.

System-test REST client interfaces belong under `realworld-api-st/src/main/java`; system-test classes belong under `realworld-api-st/src/test/java` and end with `IT`.

## Current Quarkus Extension Baseline

`realworld-api` currently includes:

- `quarkus-rest`
- `quarkus-arc`

`realworld-api-st` currently includes:

- `quarkus-rest`
- `quarkus-arc`
- `quarkus-rest-client`
- `quarkus-rest-client-jackson`

Future `realworld-api` extension direction:

- `quarkus-jnosql-mongodb`
- `quarkus-rest-jsonb`
- `quarkus-smallrye-jwt`

Future extension additions require a separate approved workflow. JSON serialization, JWT/security, persistence, OpenAPI, metrics, tracing, and system-test JSON client changes must be selected through dynamic Quarkus extension discovery before code, tests, or Maven changes are written for those capabilities.

## Downstream Workflow Rules

- Trace endpoint behavior to the API contract baseline.
- Keep JSON envelope mapping in boundary classes.
- Keep business decisions out of JAX-RS resources; delegate to controls.
- Add MongoDB persistence, JSON-B support, JWT security, validation, and observability only in workflows that explicitly need them.
- Keep `realworld-api-st` black-box from the production Java codebase.
