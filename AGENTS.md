# Workspace Instructions

Scope: the entire repository.

This repository is a Quarkus + SLDD implementation workspace for the RealWorld backend API.

## SLDD workflow

- Keep feature work aligned with the active SLDD workflow under `.sldd/specs/`.
- Do not add RealWorld API behavior unless an approved SLDD step covers that behavior.
- Keep SLDD journals journal-only; write implementation details in code, tests, or Markdown artifacts as directed by the workflow.

## Workspace structure

- `realworld-api` is the RealWorld REST API application.
- `realworld-api-st` is the standalone black-box system-test application.
- Keep the two Quarkus projects as independent sibling Maven projects unless a later SLDD workflow changes the build layout.
- Do not introduce a Maven dependency from `realworld-api-st` to `realworld-api`.

## Java and Quarkus conventions

- Prefer Java SE, Jakarta EE, MicroProfile, and Quarkus platform dependencies before adding other libraries.
- Keep REST resources thin; delegate business behavior to control classes when behavior is introduced.
- Keep future system tests external and HTTP-based.
- Keep README documentation current when changing the runnable workspace structure.
