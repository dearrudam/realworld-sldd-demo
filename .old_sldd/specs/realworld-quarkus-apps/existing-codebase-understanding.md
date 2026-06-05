# Repository Structure Overview

The repository currently contains the inherited RealWorld example-app template files and local agent/SLDD support files.

Top-level project files:

- `README.md`: inherited RealWorld template README with `[YOUR_FRAMEWORK]` placeholders.
- `CODE_OF_CONDUCT.md`: standard project conduct document.
- `LICENSE`: repository license.
- `logo.png` and `favicon.ico`: inherited RealWorld assets.
- `.gitignore`: currently oriented toward Node/front-end style ignores plus IDE/system files.
- `.vscode/settings.json`: Java/Maven editor settings.
- `opencode.json`: configures the local Quarkus Agent MCP server through `jbang quarkus-agent-mcp@quarkusio`.

Workflow and agent files:

- `.sldd/specs/realworld-quarkus-apps/01-product-intent-specification.md`: approved Step 01 artifact.
- `.sldd/specs/realworld-quarkus-apps/_spec-journal.json`: SLDD journal for this workflow.
- `.agents/skills/**`: repository-local skills, including SLDD, Quarkus REST, Quarkus Arc, BCE, Java conventions, MicroProfile, and diagramming skills.

No Quarkus application directories exist yet.

# Architecture Summary

The repository has no implemented application architecture yet.

Current architecture state:

- The repo is a fork/template workspace, not yet an executable backend.
- The intended target architecture from Step 01 is a workspace containing two separate Quarkus applications:
  - `realworld-api`: RealWorld REST API implementation.
  - `realworld-api-st`: standalone black-box system-test application for `realworld-api`.
- The repository currently has SLDD workflow support and project-local skill definitions, but no generated Maven or Gradle projects.
- The existing README describes the generic RealWorld example-app intent but has not yet been adapted to Quarkus or SLDD.

# Conventions to Preserve

Preserve these existing conventions and constraints:

- Keep the RealWorld project identity and links to the RealWorld specification.
- Keep SLDD artifacts under `.sldd/specs/<feature-name>/`.
- Respect SLDD gates before implementation changes.
- Preserve repository-local agent/skill files unless a later approved SLDD step changes them.
- Keep the standalone system-test app decoupled from API internals.
- Prefer Quarkus-supported extensions over custom infrastructure.
- Keep README documentation current after structural changes.

Conventions that likely need updating later:

- `.gitignore` currently lacks typical Java/Quarkus build outputs such as `target/`, `.mvn/wrapper/maven-wrapper.jar`, and related generated files.
- `README.md` still contains `[YOUR_FRAMEWORK]` placeholders and generic setup instructions.
- No root `AGENTS.md` exists yet; Step 01 says workspace guidance may be introduced through SLDD.

# Integration Points

Known or planned integration points:

- Quarkus Agent MCP is configured in `opencode.json` for local Quarkus project creation, extension discovery, dev mode, tests, and logs.
- RealWorld API compatibility is the external behavioral integration target.
- `realworld-api-st` will integrate with `realworld-api` over HTTP.
- `realworld-api` is expected to integrate with MongoDB through an approved persistence extension.
- `realworld-api` is expected to expose JSON REST endpoints using Quarkus REST JSON-B.
- `realworld-api-st` is expected to use a Quarkus REST client extension.

# Risks and Unknowns

- `jnosql-mongodb` was requested but was not found in the current Quarkus extension registry results; Step 02/03 must resolve whether to use it, add it manually, or choose a supported MongoDB fallback.
- The correct REST client extension for `realworld-api-st` is still open: plain `rest-client` may be insufficient if JSON-B entity serialization is needed.
- The workspace structure is undecided: independent sibling Quarkus projects versus a Maven parent/multi-module layout.
- README and `.gitignore` need future updates once the generated structure is approved.
- `.codex` is currently deleted in the working tree; this appears unrelated to the SLDD workflow and should not be touched unless explicitly requested.
- The RealWorld API behavior scope is large and should be split into later SLDD changes after scaffolding.
- The repository contains project-local skill files, so future changes should avoid accidentally treating them as application code.

# Context to Carry Into Steps 02-06

- Treat this repository as a RealWorld + Quarkus + SLDD workspace, not a generic Quarkus sandbox.
- Generate `realworld-api` and `realworld-api-st` only after Step 02 and Step 03 are approved.
- Decide the workspace/build layout before generating Quarkus apps.
- Resolve the MongoDB/JNoSQL extension question before implementation.
- Resolve whether `realworld-api-st` needs `rest-client-jsonb` instead of plain `rest-client`.
- Include documentation and workspace guidance decisions in design, including whether to add root and subproject `AGENTS.md` files.
- Keep system tests black-box: they must use HTTP and must not depend on `realworld-api` classes or internals.
