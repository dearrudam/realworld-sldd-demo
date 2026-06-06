# Problem Statement

O projeto precisa de uma linha de base explícita do contrato externo da RealWorld API antes de criar workspace, arquitetura, testes de sistema ou implementação. Sem essa linha de base, os workflows posteriores podem divergir sobre endpoints, payloads, autenticação, códigos HTTP, erros e limites de aceite.

# Target Users

- Desenvolvedores implementando `realworld-api` em Quarkus.
- Desenvolvedores criando testes HTTP standalone em `realworld-api-st`.
- Revisores usando SLDD para validar se design, testes e implementação seguem o contrato RealWorld.

# Formalized Exploration Decisions

- A fonte de comportamento é a especificação pública RealWorld/Conduit API, normalizada localmente neste workflow para uso pelos demais workflows.
- Este workflow produz decisões contratuais; não implementa aplicação nem testes de endpoint.
- A linha de base deve cobrir inventário de endpoints, autenticação Bearer/JWT, envelopes JSON, convenções de validação/erro e fronteiras iniciais de aceite.

# Success Metrics

- Workflows posteriores conseguem referenciar um contrato local único.
- Endpoint inventory cobre auth/user, profiles, articles, comments, favorites, feed e tags.
- Convenções de request/response/error/auth são claras o suficiente para orientar design e testes.
- Questões abertas ficam registradas sem bloquear indevidamente o próximo workflow.

# Out of Scope

- Código de produção, Quarkus workspace, persistência, arquitetura BCE, system tests e frontend.
- Decisões de deploy, observabilidade avançada ou otimizações não exigidas pelo contrato.

# Risks and Assumptions

- A especificação RealWorld pública pode ter ambiguidades; decisões locais devem resolver apenas o necessário.
- JWT/token behavior será detalhado o bastante para contratos HTTP, mas implementação criptográfica fica para workflows posteriores.
- Validações específicas podem ser refinadas durante workflows de endpoint, desde que não quebrem esta baseline.

# Acceptance Criteria (Given/When/Then)

- Given um workflow posterior precisa de endpoints RealWorld, When ele consulta esta baseline, Then encontra endpoint inventory e convenções HTTP/JSON/auth aplicáveis.
- Given há ambiguidade na especificação pública, When a baseline normaliza uma decisão, Then essa decisão fica documentada como fonte local para downstream.
- Given um endpoint exige usuário autenticado, When o contrato é consultado, Then o uso de Bearer token e resposta de erro esperada estão definidos em alto nível.
- Given este workflow termina, When o próximo workflow inicia, Then não há código de aplicação produzido por este Step 01.
