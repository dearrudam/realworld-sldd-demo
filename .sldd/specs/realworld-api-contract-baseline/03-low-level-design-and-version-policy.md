# Requirement-to-Design Traceability

- Fonte local única: criar/usar artefatos aprovados em `.sldd/specs/realworld-api-contract-baseline/` como baseline consumível.
- Inventário completo: registrar endpoints RealWorld por área funcional.
- Convenções HTTP/JSON/auth: definir envelopes esperados, autenticação Bearer/JWT, status HTTP e formato de erro em alto nível.
- Escopo documental: Step 04 deve testar presença e consistência dos artefatos; Step 05 deve limitar-se a documentação/contrato.

# API Contracts

Inventário base esperado:

## User and Authentication

- `POST /api/users` — registrar usuário.
- `POST /api/users/login` — autenticar usuário.
- `GET /api/user` — obter usuário atual; requer Bearer token.
- `PUT /api/user` — atualizar usuário atual; requer Bearer token.

## Profiles

- `GET /api/profiles/{username}` — obter perfil.
- `POST /api/profiles/{username}/follow` — seguir perfil; requer Bearer token.
- `DELETE /api/profiles/{username}/follow` — deixar de seguir perfil; requer Bearer token.

## Articles, Feed, Favorites, Comments and Tags

- `GET /api/articles` — listar artigos com filtros/paginação.
- `GET /api/articles/feed` — feed de artigos; requer Bearer token.
- `POST /api/articles` — criar artigo; requer Bearer token.
- `GET /api/articles/{slug}` — obter artigo.
- `PUT /api/articles/{slug}` — atualizar artigo; requer Bearer token e autorização de autor.
- `DELETE /api/articles/{slug}` — remover artigo; requer Bearer token e autorização de autor.
- `POST /api/articles/{slug}/favorite` — favoritar artigo; requer Bearer token.
- `DELETE /api/articles/{slug}/favorite` — desfavoritar artigo; requer Bearer token.
- `GET /api/articles/{slug}/comments` — listar comentários.
- `POST /api/articles/{slug}/comments` — criar comentário; requer Bearer token.
- `DELETE /api/articles/{slug}/comments/{id}` — remover comentário; requer Bearer token e autorização.
- `GET /api/tags` — listar tags.

# Data Models

Envelopes JSON de contrato:

- `user`: `email`, `token`, `username`, `bio`, `image`.
- `profile`: `username`, `bio`, `image`, `following`.
- `article`: `slug`, `title`, `description`, `body`, `tagList`, `createdAt`, `updatedAt`, `favorited`, `favoritesCount`, `author`.
- `comment`: `id`, `createdAt`, `updatedAt`, `body`, `author`.
- `tags`: lista de strings.
- Listas usam envelopes como `articles` + `articlesCount`, `comments`, ou `tags`.

# Error Model

- Erros devem usar resposta JSON consistente com a convenção RealWorld, contendo `errors` e mensagens por campo/categoria quando aplicável.
- Validação inválida: status 422 quando o contrato RealWorld exigir validação de entrada.
- Não autenticado: status 401 para ausência/token inválido em endpoint protegido.
- Não autorizado: status 403 quando usuário autenticado não pode executar a ação.
- Não encontrado: status 404 para recursos inexistentes quando aplicável.

# Test Strategy

Step 04 deve ser Red-only e validar a baseline contratual como artefato, por exemplo:

- teste que falha enquanto não existir artefato de baseline final com inventário de endpoints;
- teste que falha se áreas funcionais obrigatórias não forem mencionadas;
- teste que falha se autenticação Bearer/JWT e modelo de erro não forem documentados;
- teste que falha se o workflow tentar exigir código de aplicação neste escopo.

# Test Scenario Catalog

- Contract artifact presence and required headings.
- Endpoint inventory includes all mandatory user/auth endpoints.
- Endpoint inventory includes profile follow/unfollow behavior.
- Endpoint inventory includes article CRUD, feed, favorites, comments and tags.
- Security convention documents Bearer token for protected endpoints.
- Error convention documents validation, authentication, authorization and not-found categories.

# Dependency and Version Policy

- Current dependency set is sufficient for this workflow because it produces documentation/contract artifacts only.
- No new runtime dependencies are required.
- No Maven/Gradle/Quarkus version pinning is introduced here.
- Future workflows may add Quarkus extensions, test libraries or OpenAPI tooling, but those decisions are out of scope for this baseline.

# Ordered Implementation Plan

1. Step 04: add Red tests/checks for expected contract artifact structure and required content.
2. Step 05: create or finalize the local contract baseline artifact that satisfies the tests.
3. Step 06: verify tests pass and downstream workflows can reference the approved baseline.
4. Do not create Quarkus modules, production endpoints, persistence, JWT implementation or system-test app in this workflow.
