# realworld-api

Quarkus REST application shell for the future RealWorld backend API.

This module currently provides the runnable API shell and operational health/readiness baseline. RealWorld business endpoint behavior is not implemented yet.

## Current capabilities

- Quarkus REST application shell on port `8080`.
- SmallRye Health endpoints:
  - `/q/health`
  - `/q/health/live`
  - `/q/health/ready`
- Shell-only readiness: no MongoDB, JWT, or business dependency checks yet.

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
