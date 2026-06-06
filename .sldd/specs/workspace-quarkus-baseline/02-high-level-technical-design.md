# Requirements Traceability

- Minimal physical workspace: introduce two explicit Quarkus application roots, `realworld-api` and `realworld-api-st`.
- Build/run conventions: document how each application is built, tested, and run locally using Quarkus-supported commands.
- Compatibility with API contract: keep the completed `realworld-api-contract-baseline` as downstream input without implementing endpoint behavior here.
- No premature business logic: generated or baseline code must not implement RealWorld auth, profiles, articles, comments, favorites, feed, or tags.

# Architecture Diagram

```text
repository root
├── .sldd/specs/                         # SLDD workflow state and approved artifacts
├── scripts/                             # repository-level verification helpers
├── realworld-api/                       # Quarkus backend application root
│   ├── pom.xml
│   └── src/...
└── realworld-api-st/                    # Quarkus standalone HTTP system-test app root
    ├── pom.xml
    └── src/...

realworld-api-st --HTTP target URL--> realworld-api
```

# Component Responsibilities

- Repository root: coordinates documentation, SLDD artifacts, and workspace-level helper scripts.
- `realworld-api`: owns the future RealWorld backend API runtime. In this workflow it should contain only a minimal generated Quarkus application baseline.
- `realworld-api-st`: owns future standalone HTTP system tests. In this workflow it should contain only a minimal generated Quarkus application baseline and target URL convention.
- `scripts/`: may contain checks that verify workspace structure and conventions.

# Data Flow

1. Developers work from the repository root.
2. Each Quarkus app is built and run from its own application directory.
3. `realworld-api` will expose HTTP behavior in later workflows.
4. `realworld-api-st` will target `realworld-api` over a configured URL in later workflows.
5. The approved contract baseline remains the behavior source for future tests and implementation.

# Security and Observability Requirements

- No application security implementation is introduced in this workflow.
- Future authentication behavior remains governed by `realworld-api-contract-baseline`.
- Baseline observability is limited to Quarkus default startup/runtime behavior.
- Ports should be reserved clearly: `realworld-api` defaults to `8080`; `realworld-api-st` should use a separate local port, such as `8081`, when run as an app.

# Trade-Offs and Alternatives

- Separate application roots are preferred over a parent multi-module build for this baseline because they keep the two Quarkus applications independently generated, built, and run while the repository is still being established.
- A Maven parent could reduce duplication later, but it adds coordination before architecture and dependency policy are known.
- Creating the system-test project now gives later workflows a stable location, but actual HTTP test scenarios remain deferred.
- Quarkus generated starter code is acceptable only as application-shell scaffolding and must not be treated as RealWorld business behavior.

# High-Level Test Scenario Map

- Verify that the workspace baseline documents `realworld-api` and `realworld-api-st` roots.
- Verify that the repository contains both Quarkus application directories after implementation.
- Verify each application has a build descriptor and standard source/resource structure.
- Verify local run conventions and ports are documented.
- Verify no RealWorld business endpoint inventory is implemented by this workflow.
- Verify the baseline references the completed API contract artifact for future behavior.
