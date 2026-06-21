# realworld-api-st

Standalone Quarkus REST Client application shell for HTTP system tests targeting `realworld-api`.

The module owns reusable system-test strategy contracts: target API URL configuration, REST Client config-key conventions, authentication defaults, data-isolation defaults, and verification boundaries. Endpoint-specific workflows add their own scenario coverage later.

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

- Authentication mode is `NONE` until a security workflow introduces credentials.
- Data isolation is scenario-owned unique data; tests must not assume a global reset endpoint.
- This baseline verifies reusable contracts only. It does not implement endpoint-specific RealWorld scenarios.
- Initial live smoke coverage is intentionally skipped until `realworld-api` exposes a health or RealWorld business endpoint.

## Quarkus guides

- [Quarkus REST Client](https://quarkus.io/guides/rest-client)
- [Quarkus REST](https://quarkus.io/guides/rest)

## Auth/user system-test client

The system-test module includes a `TargetApiClient` contract for RealWorld registration, login, current-user retrieval, and current-user update endpoints. The client keeps the shared `service_uri` REST client config key and supports bearer-token scenarios through `AuthenticationMode.BEARER_TOKEN`.
