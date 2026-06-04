# realworld-api

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## RealWorld Architecture

Generated starter resources are scaffold code only and are not part of the RealWorld API contract. Future capability workflows must follow `docs/domain-model.md` for domain decisions and `docs/bce-architecture.md` for BCE package layout, layer responsibilities, cross-component collaboration, and test isolation.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/realworld-api-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST JSON-B ([guide](https://quarkus.io/guides/rest#json-serialisation)): JSON-B serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- JNoSQL MongoDB ([guide](https://docs.quarkiverse.io/quarkus-jnosql/dev/)): Quarkus JNoSQL MongoDB Runtime provides runtime support for integrating MongoDB
        document-oriented databases with Quarkus applications. This extension facilitates seamless integration, enabling
        efficient storage, retrieval, and management of document-oriented data within Quarkus projects, enhancing
        developer productivity.

## Provided Code

### Quarkus JNoSQL MongoDB Codestart

Create your first Document NoSQL entity with Quarkus and JNoSQL MongoDB.

[Related guide section...](https://docs.quarkiverse.io/quarkus-jnosql/dev/)

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
