# realworld-api

Quarkus REST application shell for the future RealWorld backend API.

This module provides the runnable API shell, operational health/readiness baseline, and the RealWorld auth/current-user API slice.

## Current capabilities

- Quarkus REST application shell on port `8080`.
- SmallRye Health endpoints:
  - `/q/health`
  - `/q/health/live`
  - `/q/health/ready`
- Shell-only readiness: no MongoDB, JWT, or business dependency checks yet.
- Auth/current-user endpoints for registration, login, current-user retrieval, and current-user update.
- RealWorld JSON envelopes and validation/error responses.
- PBKDF2 salted password hashes and short-lived HS256 Bearer tokens for the demo scope.

## Configuration

`realworld.jwt.secret` defaults to a development placeholder and should be supplied from `REALWORLD_JWT_SECRET` outside local demo runs.

## BCE convention for future slices

Future RealWorld business slices create packages only when they introduce real behavior:

```text
dev.realworld.<business-component>.boundary
dev.realworld.<business-component>.control
dev.realworld.<business-component>.entity
```

This shell intentionally does not create placeholder business-component packages or classes.

## Local development

```bash
./mvnw quarkus:dev
./mvnw test
./mvnw install
```

Default HTTP port: `8080`.

## Quarkus guides

- [Quarkus REST](https://quarkus.io/guides/rest)
- [SmallRye Health](https://quarkus.io/guides/smallrye-health)
- [Writing JSON REST Services](https://quarkus.io/guides/rest-json)
- [Using JWT RBAC](https://quarkus.io/guides/security-jwt)
- [Build, Sign and Encrypt JSON Web Tokens](https://quarkus.io/guides/security-jwt-build)
- [Validation with Hibernate Validator](https://quarkus.io/guides/validation)
- [MongoDB Client](https://quarkus.io/guides/mongodb)
