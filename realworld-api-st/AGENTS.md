# realworld-api-st Instructions

Scope: `realworld-api-st/`.

- This project owns standalone black-box system tests for `realworld-api`.
- Exercise `realworld-api` only over HTTP through REST clients or equivalent external calls.
- Do not add a Maven dependency on `realworld-api` or import API implementation classes.
- Keep test DTOs local to this project when future tests need payload shapes.
- Use the `service_uri` REST client configuration key for the API base URL.
