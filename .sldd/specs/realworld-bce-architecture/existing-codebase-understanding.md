# Existing Codebase Understanding: BCE Architecture Baseline

## Repository Structure Overview

This repository is a Quarkus RealWorld backend workspace managed through SLDD.

- `realworld-api/` is the backend API application. It owns future RealWorld REST resources, application services, domain entities, persistence adapters, and API configuration.
- `realworld-api-st/` is a standalone black-box system-test application. It validates the backend only through HTTP and must not import API internals or depend on the API Maven artifact.
- `.sldd/specs/` stores workflow journals and approved SLDD artifacts.
- `realworld-api/docs/domain-model.md` is the canonical domain-model reference approved by the predecessor `realworld-domain-model` workflow.

The current API source tree contains Quarkus generated or codestart classes under the root package `org.soujava.demo.sldd`, including greeting and garage examples. These classes are scaffold/codestart code only and are not part of the RealWorld API contract.

## Architecture Summary

The workspace currently has the application scaffolds and the approved domain reference, but it does not yet have a documented BCE package baseline for future RealWorld business components.

The current baseline decisions already in force are:

- The API module uses Quarkus REST JSON-B for HTTP endpoints.
- Future MongoDB persistence work should use the approved JNoSQL MongoDB extension unless a later SLDD workflow changes that decision.
- Domain-affecting implementation work must follow `realworld-api/docs/domain-model.md` before changing entities, DTOs, repositories, validation rules, persistence mappings, or relationship behavior.
- Generated starter resources are outside the RealWorld API contract.
- System tests must stay black-box and independent from API internals.

For the BCE baseline, the application needs documented package and dependency-direction rules before child capability workflows add authentication, users, profiles, articles, comments, favorites, or tags.

## Conventions to Preserve

- Keep `realworld-api-st` independent from `realworld-api` internals and Maven artifacts.
- Build each Quarkus project from its own directory with Maven.
- Prefer Quarkus extensions over custom infrastructure.
- Keep generated starter endpoints outside the RealWorld contract.
- Keep implementation changes minimal and traceable to approved SLDD artifacts.
- Use package names based on domain responsibilities rather than technical concerns.
- Use BCE package segments only inside a business component: `boundary`, `control`, and `entity`.
- Place JAX-RS resources, HTTP mapping, transaction demarcation, and cross-cutting request concerns in the boundary layer.
- Keep procedural business logic in control classes.
- Keep domain objects, value objects, and persistence candidates in entity packages.

## Integration Points

- `realworld-api` integrates with HTTP clients through Quarkus REST JSON-B endpoints.
- `realworld-api-st` integrates with `realworld-api` through HTTP using Quarkus REST Client.
- Future persistence integration belongs to API-owned adapters/entities and must align with JNoSQL MongoDB and the approved domain model.
- Future RealWorld capability workflows depend on this BCE baseline for package ownership, cross-component access, and test isolation.

## Risks and Unknowns

- The current generated/codestart classes are in the root application package and could be mistaken for RealWorld contract code unless the BCE baseline explicitly labels them as scaffold-only.
- Child workflows may create inconsistent package shapes if the BCE baseline does not specify where business components, boundaries, controls, and entities belong.
- Cross-component calls could become overly coupled if child workflows share implementation classes instead of using documented control or boundary entry points.
- System tests could drift into white-box tests if the independence rule is not repeated in the BCE baseline.
- No runtime RealWorld behavior should be introduced by this workflow; adding behavior here would bypass the child capability SLDD workflows.

## Context to Carry Into Steps 02-06

- This workflow should create an architecture baseline, not implement RealWorld endpoint behavior.
- The preferred implementation target for Step 05 is documentation and lightweight package-level architecture markers, not domain behavior or persistence schema changes.
- The baseline should resolve the Step 01 open questions as follows:
  - Structural changes are allowed only when they document or prepare the architecture baseline without implementing RealWorld behavior.
  - Generated greeting and garage/codestart code remains scaffold-only and outside the RealWorld API contract.
- Step 04 tests should verify the BCE baseline artifacts and guard against coupling `realworld-api-st` to `realworld-api` internals.
- Step 05 should not add new dependencies unless Step 03 is revised to approve them.
