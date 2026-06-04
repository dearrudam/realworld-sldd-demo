# RealWorld BCE Architecture Baseline

## Scope

This document is the architecture baseline for future RealWorld backend capability workflows in `realworld-api`. It defines package ownership, BCE layer responsibilities, cross-component collaboration, and test isolation. It does not introduce RealWorld endpoint behavior, DTOs, repositories, persistence mappings, or runtime contracts.

Generated starter resources are scaffold-only. The current greeting and garage/codestart classes are not part of the RealWorld API contract and must not be treated as authentication, users, profiles, articles, comments, favorites, feeds, or tags behavior.

## Package Layout

The API root package is `org.soujava.demo.sldd`. Future RealWorld business components are direct children of that root package and use this package shape:

```text
org.soujava.demo.sldd.<business-component>.<boundary|control|entity>
```

Business component names must describe domain responsibilities rather than technical concerns. Expected RealWorld components include `authentication`, `users`, `profiles`, `articles`, `comments`, `favorites`, and `tags`.

The `boundary`, `control`, and `entity` package segments are only allowed inside a business component. A component may omit a layer when it has no approved responsibility for that layer.

## Layer Responsibilities

JAX-RS resources belong in boundary packages. Boundary classes own HTTP mapping, request and response translation, transaction demarcation, authentication or authorization checks, and other request-scoped cross-cutting concerns. Boundary classes delegate behavior instead of implementing business rules directly.

Control classes implement procedural business operations. Controls coordinate use cases, call entities, and expose explicit entry points for same-application component collaboration when a boundary facade is not the right abstraction.

Entity packages contain domain state and behavior. Entity classes, records, value objects, and persistence candidates must follow `docs/domain-model.md` before changing concepts, relationships, field restrictions, validation behavior, persistence mappings, or relationship behavior.

## Cross-Component Collaboration

Cross-component collaboration uses explicit boundary or control entry points. Components must not reach into another component's private implementation details or repository internals.

Entity references across components are allowed only when they reflect the approved domain model and preserve cohesion. Excessive cross-component references are a design signal to split, merge, or rebalance components through a later SLDD workflow.

Shared transport payloads do not define domain ownership. Boundary classes may map HTTP payloads to component-owned entities or control inputs, but response wrappers such as `user`, `profile`, `article`, `articles`, `comment`, `comments`, and `tags` remain transport shapes rather than business components.

## Test Isolation

realworld-api-st remains a black-box HTTP test harness. It validates externally observable API behavior over HTTP, owns its REST client interfaces and payload shapes, and must not import `realworld-api` implementation classes, domain classes, repositories, generated sources, or Maven artifacts.

API unit and integration tests stay inside `realworld-api`. System tests stay inside `realworld-api-st` and use Quarkus REST Client for future HTTP scenarios.

## Evolution Policy

Future capability workflows must follow this baseline unless their approved SLDD artifacts intentionally revise it. Changes to package layout, layer responsibilities, cross-component policy, or test isolation require an SLDD update before implementation diverges from this document.
