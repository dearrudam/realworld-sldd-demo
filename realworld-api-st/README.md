# realworld-api-st

Standalone Quarkus REST Client application for HTTP system tests targeting `realworld-api`.

The module owns reusable system-test strategy contracts: target API URL configuration, REST Client config-key conventions, authentication defaults, data-isolation defaults, and verification boundaries. Endpoint-specific workflows add their own scenario coverage later.

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

## Quarkus guides

- [Quarkus REST Client](https://quarkus.io/guides/rest-client)
- [Quarkus REST](https://quarkus.io/guides/rest)
