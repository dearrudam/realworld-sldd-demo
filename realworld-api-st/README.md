# realworld-api-st

Standalone Quarkus REST Client application root for future HTTP system tests targeting `realworld-api`.

This application currently contains only generated/minimal Quarkus scaffolding. No RealWorld system-test scenarios are implemented by the workspace baseline workflow.

## Local development

```bash
./mvnw quarkus:dev
./mvnw test
./mvnw install
```

Default HTTP port when run as an application: `8081`.

Default target API URL: `http://localhost:8080`.

## Quarkus guide

- [Quarkus REST Client](https://quarkus.io/guides/rest-client)
