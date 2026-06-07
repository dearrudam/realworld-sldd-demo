# realworld-api-st

Standalone Quarkus REST Client application shell for HTTP system tests targeting `realworld-api`.

The module owns reusable system-test strategy contracts: target API URL configuration, REST Client config-key conventions, authentication defaults, data-isolation defaults, and verification boundaries. It now includes black-box auth/current-user scenarios against `realworld-api`.

Execution is JUnit/Quarkus test-suite based. The shell does not expose test-control endpoints, does not run tests at application startup, and currently performs no live smoke call because `realworld-api` has no exposed endpoint for this workflow.

## Local development

```bash
./mvnw quarkus:dev
./mvnw test
./mvnw install
```

Default application HTTP port: `8081`.

Default target API URL: `http://localhost:8080`.

Override the target API URL with:

```properties
realworld-api.base-url=http://localhost:8080
```

REST Clients targeting `realworld-api` use config key `service_uri`, bridged by:

```properties
quarkus.rest-client.service_uri.url=${realworld-api.base-url}
```

## System-test strategy

- Authentication mode is still represented by the baseline enum, while auth/current-user scenarios obtain Bearer tokens through registration/login calls.
- Data isolation is scenario-owned unique data; tests must not assume a global reset endpoint.
- Auth/current-user system tests exercise registration, login, current user retrieval/update, and invalid token behavior over HTTP.

## Quarkus guides

- [Quarkus REST Client](https://quarkus.io/guides/rest-client)
- [Quarkus REST](https://quarkus.io/guides/rest)
