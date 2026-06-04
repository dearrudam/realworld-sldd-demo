# Workflow-Set Plan: RealWorld Capability Decomposition

## Workflow Kind

`workflow-set`

## Large Idea

Use BCE by RealWorld API capability so backend responsibilities are isolated, independently specified, and testable through API-level verification and black-box system tests.

The decomposition establishes capability-oriented business components for the RealWorld backend before feature implementation begins. Each future capability workflow can then define its own product intent, codebase context, technical design, Red tests, Green implementation, and verification under normal SLDD gates.

## Source Inputs

- SLDD exploration on 2026-06-04: use BCE by feature so each RealWorld capability stays isolated and testable.
- `.sldd/specs/realworld-quarkus-apps/`: completed Quarkus scaffold workflow.
- `.sldd/specs/realworld-domain-model/`: completed canonical RealWorld domain model reference.
- `realworld-api/docs/domain-model.md`: approved domain model reference for future domain-affecting implementation work.
- Repository architecture rule: `realworld-api-st` remains an independent black-box HTTP system-test application.

## Why Decomposition Is Recommended

- The idea affects multiple RealWorld capabilities rather than one endpoint or isolated behavior.
- Capability-level decomposition supports independent SLDD child workflows with clear predecessor relationships.
- BCE by API capability aligns source package ownership, test boundaries, and RealWorld endpoint groups.
- A parent workflow-set prevents later feature workflows from making inconsistent component-boundary decisions.

## Proposed Child Workflows

| Name | Title | Kind | Scope | Predecessors | Notes |
|---|---|---|---|---|---|
| `realworld-bce-architecture` | BCE Architecture Baseline | `feature` | Define backend component/package rules, cross-component interaction policy, boundary/control/entity responsibilities, and test isolation policy. | `realworld-domain-model` | No RealWorld behavior implementation by default; establishes architecture baseline for later child workflows. |
| `realworld-authentication` | Authentication Capability | `feature` | Define login, token issuance, authenticated principal handling, password verification policy, and authentication error behavior. | `realworld-bce-architecture` | Should decide how authentication is consumed by other capability boundaries. |
| `realworld-users` | Users Capability | `feature` | Define registration, current user retrieval, user update behavior, user response shape, uniqueness handling, and internal tests. | `realworld-authentication` | Builds on authentication decisions for token-bearing user responses. |
| `realworld-profiles` | Profiles Capability | `feature` | Define profile projection, profile lookup, follow/unfollow behavior, and viewer-relative `following` calculation. | `realworld-users` | May introduce relationship persistence for follows. |
| `realworld-articles` | Articles Capability | `feature` | Define article create/read/update/delete, slug behavior, article response projection, author projection, and article listing foundation. | `realworld-users`, `realworld-profiles` | Establishes article ownership and projection rules for later comments, favorites, and tags. |
| `realworld-comments` | Comments Capability | `feature` | Define create/list/delete comments for articles, author projection, comment ownership, and article-comment relationship behavior. | `realworld-articles` | Depends on article lookup and authenticated user behavior. |
| `realworld-favorites` | Favorites Capability | `feature` | Define favorite/unfavorite behavior, favorites count, and viewer-relative `favorited` calculation. | `realworld-articles` | May introduce relationship persistence for favorites. |
| `realworld-tags` | Tags Capability | `feature` | Define tag value capture, tag listing, tag response shape, and interaction with article tag lists. | `realworld-articles` | Should decide whether tags stay embedded on articles or become normalized persistence records. |

## Execution Guidance

- Execute `realworld-bce-architecture` first to establish package/component rules before feature code grows.
- Execute `realworld-authentication` before user-dependent write operations.
- Execute `realworld-users` before profiles and article authorship.
- Execute `realworld-profiles` before article response projection because articles embed author profiles.
- Execute `realworld-articles` before comments, favorites, and tags.
- After articles are verified, `realworld-comments`, `realworld-favorites`, and `realworld-tags` can proceed independently unless their Step 01 artifacts introduce additional dependencies.
- Keep `realworld-api-st` independent in every child workflow; system tests should validate only externally observable HTTP behavior.

## Out Of Scope

- This workflow-set does not implement production code.
- This workflow-set does not execute child workflows.
- This workflow-set does not create child workflows until scaffold is explicitly approved in Step `02-scaffold-children`.
- This workflow-set does not revise the approved domain model unless a later child workflow formally requires and approves a domain model change.
- This workflow-set does not choose detailed persistence document shapes for every capability.

## Scaffold Policy

If scaffold is approved, all proposed child workflows in this plan will be created as Step 01 pending drafts.

Partial scaffold selection is not part of the first version.

## Approval

Status: Approved for parent workflow-set plan materialization on 2026-06-04.
