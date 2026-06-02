# Agent Instructions

Scope: `realworld-api-st/`.

- This Quarkus application owns standalone black-box system tests for `realworld-api`.
- Test `realworld-api` only through HTTP and public API contracts.
- Do not import API implementation classes or add a Maven dependency on `realworld-api`.
- Place REST client interfaces under `src/main/java` and system-test classes under `src/test/java` when later SLDD workflows add scenarios.
- Name system tests with the `IT` suffix.
