# AGENTS.md

## Project Overview

The project intents to be a RealWorld API implementation for the [RealWorld](https://github.com/gothinkster/realworld) project.
The project demonstrates clean separation of concerns with JAX-RS resources, Jakarta NoSQL/Jakarta Data persistence concepts, MicroProfile Config, CDI, and Health checks by using Boundary-Control-Entity (BCE) architectural pattern.

## Architecture

Follows the [BCE pattern](https://bce.design).

### Package Structure

```
dearrudam.[app-name].[component-name].[boundary|control|entity]
```

Example: `dearrudam.realworld.greetings.boundary.GreetingResource`

### Modules

- `service/` - Main Quarkus application with BCE structure
- `service-st/` - System tests using MicroProfile REST Client

## Build & Test

### Development Mode

```bash
cd service
mvn quarkus:dev
```

### Build

```bash
mvn clean package
```

### Run System Tests

System tests require the service to be running:

```bash
# Terminal 1: Start service
cd service
mvn quarkus:dev

# Terminal 2: Run system tests
cd service-st
mvn clean test-compile failsafe:integration-test

```

### Package

```bash
mvn package
```

## Dependencies

**IMPORTANT**: Always ask before adding new dependencies to `pom.xml`. This project minimizes external dependencies and relies on Java SE APIs and MicroProfile standards.
