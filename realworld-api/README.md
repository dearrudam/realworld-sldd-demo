# realworld-api

Quarkus REST backend for the RealWorld API demo.

This module provides the runnable API shell, operational health/readiness baseline, and the first RealWorld business slice for authentication and current-user behavior. The auth/user slice follows the approved architecture by using Quarkus JNoSQL with MongoDB for user persistence.

## Current capabilities

- Quarkus REST API on port `8080`.
- SmallRye Health endpoints under `/q/health`.
- Auth/user JSON envelope endpoints under `/api`.
- MongoDB document persistence through Quarkus JNoSQL for registered users.
- PBKDF2 password hashing for registered users.
- SmallRye JWT bearer tokens for protected current-user calls.
- RealWorld-style error envelopes for validation and domain errors.

## BCE convention

RealWorld business slices use BCE packages:

```text
dev.realworld.<business-component>.boundary
dev.realworld.<business-component>.control
dev.realworld.<business-component>.entity
```

The auth/user slice lives under `dev.realworld.authuser`.

## Local development

```bash
./mvnw quarkus:dev
./mvnw test
./mvnw install
```

Default HTTP port: `8080`.

## Quarkus guides

- [Quarkus REST](https://quarkus.io/guides/rest)
- [Quarkus REST JSON-B](https://quarkus.io/guides/rest-json)
- [SmallRye JWT](https://quarkus.io/guides/security-jwt)
- [Quarkus JNoSQL](https://docs.quarkiverse.io/quarkus-jnosql/dev/index.html)
- [Hibernate Validator](https://quarkus.io/guides/validation)
- [SmallRye Health](https://quarkus.io/guides/smallrye-health)
