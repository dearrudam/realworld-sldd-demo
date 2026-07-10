# service

Quarkus application module structured with the BCE (Boundary-Control-Entity) pattern. Business components organize code by domain responsibilities, with boundary layers for external interaction (JAX-RS resources), control for procedural logic, and entity for domain objects.

Persistence uses the Quarkus JNoSQL MongoDB extension, enabling Jakarta NoSQL Document access and Jakarta Data repositories for MongoDB-backed components.

The `users` business component declares and implements RealWorld account, authentication identity, public profile, and following behavior. Its package document is the boundary contract. Accepted passwords are retained as salted credential verification material instead of plaintext, accepted identity operations return JWT-shaped tokens, and accepted user state is retained through MongoDB-backed Jakarta Data repositories.

**Note:** "qmp" is a placeholder for the application name and should be replaced throughout the codebase with your actual application name.

## Build

```bash
mvn clean package
```

## Run

Development mode:

```bash
mvn quarkus:dev
```

Production build:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```
