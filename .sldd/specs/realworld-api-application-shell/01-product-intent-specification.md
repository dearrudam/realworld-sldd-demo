# Product Intent: RealWorld API Application Shell

## Workflow Kind

`feature`

## Parent Workflow-Set

`realworld-quarkus-sldd-workspace`

## Origin

This Step 01 draft was scaffolded from:

- Parent journal: `../realworld-quarkus-sldd-workspace/_spec-journal.json`
- Parent artifact: `../realworld-quarkus-sldd-workspace/01-workflow-set-plan.md`

## Scope

Included:

- Implement the minimal `realworld-api` Quarkus shell, configuration baseline, health or readiness basics, and BCE skeleton.

Excluded:

- RealWorld business endpoint behavior.
- Standalone system-test application implementation.
- Production deployment configuration.

## Workflow Precedence

Required predecessors:

- `../realworld-architecture-baseline/_spec-journal.json`

Approval gate:

- This Step 01 must not be marked complete until required predecessors have completed Step 06 verification.

## Product Intent

Create the minimal runnable Quarkus API application shell that later business API workflows can extend safely.

## Formalized Decisions

- Health/readiness baseline: add SmallRye Health and expose the standard Quarkus health endpoints `/q/health`, `/q/health/live`, and `/q/health/ready`.
- Initial readiness behavior verifies only that the application shell is up; it must not depend on MongoDB, JWT, or future business integrations yet.
- BCE structure baseline: document the convention only and do not create BCE packages or classes before the first business slice.
- Future business slices introduce their own packages following `dev.realworld.<business-component>.<boundary|control|entity>`.

## Acceptance Criteria

- The workflow creates a minimal `realworld-api` Quarkus application following the approved workspace and architecture baselines.
- The shell includes baseline configuration and SmallRye Health-based operational endpoints.
- The shell exposes `/q/health`, `/q/health/live`, and `/q/health/ready`.
- The initial readiness behavior verifies only application-shell availability and does not require MongoDB, JWT, or future business integrations.
- The shell documents the BCE package convention without creating fake or empty business-component packages/classes.
- The shell can be built and run locally.

## Approval Status

Approved and saved.
