# realworld-api-st

Standalone Quarkus REST Client application shell for HTTP system tests targeting `realworld-api`.

The module owns reusable system-test strategy contracts: target API URL configuration, REST Client config-key conventions, authentication defaults, data-isolation defaults, and verification boundaries. It also contains auth/user system tests for the first RealWorld business slice.

Execution is JUnit/Quarkus test-suite based. The target API is expected at `realworld-api.base-url` when endpoint-specific system tests are run.

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

- Authentication modes include unauthenticated calls and bearer-token calls.
- Data isolation is scenario-owned unique data; tests must not assume a global reset endpoint.
- Auth/user system tests verify registration, login failure, and protected update behavior against a running target API.

## Quarkus guides

- [Quarkus REST Client](https://quarkus.io/guides/rest-client)
- [Quarkus REST](https://quarkus.io/guides/rest)
