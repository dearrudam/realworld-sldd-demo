# ![RealWorld Example App](logo.png)

# RealWorld API with Quarkus, MicroProfile, Quarkus JNoSQL MongoDB, and BCE architecture pattern.

The project goal is to be a RealWorld API implementation for the [RealWorld](https://github.com/gothinkster/realworld) project.

By following [Boundary-Control-Entity (BCE) architectural pattern](https://bce.design), this project demonstrates a clean separation of concerns with:
- JAX-RS resources: for HTTP REST endpoints.
- Jakarta NoSQL and Jakarta Data: for persistence/data access technologies by using the [Quarkus JNoSQL MongoDB extension](https://quarkus.io/extensions/io.quarkiverse.jnosql/quarkus-jnosql-mongodb/).
- MicroProfile Config: for configuration management.
- Jakarta Contexts and Dependency Injection (CDI): for context and dependency injection.
- MicroProfile Health Check: for endpoints or checks for service health monitoring.

## Getting Started

See [AGENTS.md](AGENTS.md#build--test) for build, dev mode, and system test instructions.

## Modules

- [service](service/README.md) - Quarkus application module with BCE structure
- [service-st](service-st/README.md) - System tests for the service module

## Architecture Overview

```mermaid
graph LR
    Client([API Client]) --> Service([service])
    SystemTests([service-st]) --> Service

    subgraph ServiceModule[service]
        Users([Users BC]) --> MongoDB([MongoDB])
        Greetings([Greetings BC])
        Health([Health BC])
    end

    Service --> Users
    Service --> Greetings
    Service --> Health

    classDef bc fill:#dae8fc,stroke:#6c8ebf,color:#000
    classDef ext fill:#fff2cc,stroke:#d6b656,color:#000,stroke-dasharray:5 5
    class Users,Greetings,Health,Service,SystemTests bc
    class Client,MongoDB ext
```

## User Registration Sequence

```mermaid
sequenceDiagram
    actor Client as API Client
    participant Resource as UsersResource<br/>Boundary
    participant Users as Users<br/>Control
    participant Records as UserRecords<br/>Repository
    participant Store as MongoDB

    Client->>Resource: POST /users
    Resource->>Users: register(username, email, password)
    Users->>Users: validate required values

    alt missing or blank username, email, or password
        Users-->>Resource: BadRequestException
        Resource-->>Client: 400 Bad Request
    else required values are present
        Users->>Records: findById(username)
        Records->>Store: lookup username
        Store-->>Records: existing user or empty
        Records-->>Users: username availability

        alt username already used
            Users-->>Resource: BadRequestException
            Resource-->>Client: 400 Bad Request
        else username is available
            Users->>Records: findByEmail(email)
            Records->>Store: lookup email
            Store-->>Records: existing user or empty
            Records-->>Users: email availability

            alt email already used
                Users-->>Resource: BadRequestException
                Resource-->>Client: 400 Bad Request
            else email is available
                Users->>Users: create User with credential material
                Users->>Records: save(user)
                Records->>Store: persist user document
                Store-->>Records: persisted
                Records-->>Users: saved user
                Users-->>Resource: User
                Resource->>Users: token(user)
                Users-->>Resource: JWT token
                Resource-->>Client: 201 Created user response
            end
        end
    end
```
