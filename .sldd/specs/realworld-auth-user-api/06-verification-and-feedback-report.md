# Compliance Matrix

- Replaced the previous demo-only persistence with Quarkus JNoSQL MongoDB: `User` is a JNoSQL document entity and `UserRepository` is a Jakarta Data / JNoSQL repository.
- Replaced the previous opaque token with SmallRye JWT Build token issuance and SmallRye JWT bearer-token verification on protected endpoints.
- Preserved the RealWorld `user` JSON envelope for registration, login, current-user retrieval, and current-user update.

# Version and Dependency Validation

- `realworld-api` keeps the approved Quarkus platform version and adds `quarkus-smallrye-jwt`, `quarkus-smallrye-jwt-build`, and `quarkus-jnosql-mongodb`.
- JNoSQL annotation processing is enabled for Java 25 via `maven.compiler.proc=full`.
- JWT signing and verification key locations, issuer, and token lifespan are configured in `application.properties`.

# Test Convention Compliance

- `realworld-api` compiles with the strict JNoSQL and SmallRye JWT dependencies.
- Full `realworld-api` tests require a MongoDB test service. In this container, Testcontainers cannot access Docker (`/var/run/docker.sock` is unavailable), so HTTP tests that require MongoDB cannot complete here.
- `realworld-api-st` tests verify the shared `service_uri` REST client contract and system-test strategy defaults.

# Risks by Severity

- Medium: Local execution of MongoDB-backed tests depends on Docker/Testcontainers or an externally configured MongoDB instance.
- Low: The generated RSA key pair is demo key material and must be replaced by environment-managed secrets for non-demo deployments.

# Remediation Steps

1. Configure CI with Docker/Testcontainers or an explicit MongoDB service URL before running the full API integration suite.
2. Replace demo key files with deployment-managed key material for non-demo environments.
3. Add MongoDB unique index creation for `email` as a follow-up hardening step if it is not provided by the deployment database.

# Go/No-Go Decision and Rationale

Go for architecture alignment: the implementation now follows the approved SmallRye JWT and Quarkus JNoSQL MongoDB decisions. Local full-test execution is environment-blocked by the absence of Docker/MongoDB, not by an in-memory persistence shortcut.
