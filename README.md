# RealWorld Quarkus SLDD Backend

This repository is a spec-led implementation workspace for the [RealWorld](https://github.com/gothinkster/realworld) backend API. It uses Quarkus and follows the local SLDD workflow to move changes through product intent, design, tests, implementation, and verification.

## Applications

- [`realworld-api`](realworld-api/) contains the Quarkus REST API application that will implement RealWorld-compatible backend behavior.
- [`realworld-api-st`](realworld-api-st/) contains a standalone Quarkus system-test application that validates `realworld-api` externally over HTTP.

The initial scaffold does not implement RealWorld endpoints yet. Authentication, users, profiles, articles, comments, favorites, feeds, tags, and pagination behavior will be added through later SLDD-scoped changes.

## Development workflow

SLDD artifacts live under `.sldd/specs/`. Start or resume feature work through the SLDD workflow before changing product behavior, API contracts, persistence models, or system-test coverage.

## Common commands

Run scaffold verification from the repository root:

```bash
python3 scripts/verify-realworld-scaffold.py
```

Build and test the API application:

```bash
cd realworld-api
./mvnw test
```

Build and test the standalone system-test application:

```bash
cd realworld-api-st
./mvnw test
```

Run the API in development mode:

```bash
cd realworld-api
./mvnw quarkus:dev
```

Run the system-test application in development mode:

```bash
cd realworld-api-st
./mvnw quarkus:dev
```

`realworld-api-st` uses the `service_uri` REST client configuration key to target the API over HTTP. The scaffold default points to `http://localhost:8080`.
