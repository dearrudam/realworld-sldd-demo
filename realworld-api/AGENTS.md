# realworld-api Instructions

Scope: `realworld-api/`.

- This project owns the RealWorld REST API implementation.
- Keep generated starter endpoints separate from future RealWorld API contracts.
- Do not add users, profiles, articles, comments, favorites, tags, authentication, or persistence behavior without an approved SLDD workflow for that capability.
- Use Quarkus REST JSON-B for HTTP JSON payload support.
- Keep MongoDB persistence work behind the approved JNoSQL MongoDB extension unless a later SLDD design changes the persistence choice.
- Do not depend on classes from `realworld-api-st`.
