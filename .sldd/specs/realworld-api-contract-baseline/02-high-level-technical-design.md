# Requirements Traceability

- Contrato local único: criar uma baseline textual versionada neste workflow para orientar todos os workflows posteriores.
- Inventário de endpoints: cobrir autenticação/usuário, perfis, artigos, comentários, favoritos, feed e tags.
- Convenções HTTP/JSON/auth: definir envelopes, autenticação Bearer/JWT em alto nível, respostas comuns e erros.
- Sem implementação: restringir este workflow a documentação/contrato e decisões de aceite.

# Architecture Diagram

```text
RealWorld public API expectations
        |
        v
realworld-api-contract-baseline
  - endpoint inventory
  - request/response envelopes
  - auth conventions
  - error conventions
  - acceptance boundaries
        |
        +--> workspace-quarkus-baseline
        +--> realworld-system-test-strategy
        +--> realworld-auth-user-api
        +--> realworld-profiles-api
        +--> realworld-articles-api
```

# Component Responsibilities

- Contract Baseline: consolidar o comportamento HTTP externo esperado da API RealWorld.
- Downstream Workspace: criar módulos e convenções físicas sem redefinir o contrato.
- Downstream Architecture: traduzir o contrato em estrutura BCE, persistência e camadas.
- Downstream System Tests: transformar a baseline em cenários HTTP executáveis.
- Downstream Feature Workflows: implementar fatias de negócio aderentes à baseline.

# Data Flow

1. O workflow consulta expectativas públicas RealWorld/Conduit e decisões do Step 01.
2. A baseline normaliza endpoints, payloads, autenticação e erros em artefatos locais.
3. Workflows posteriores consomem a baseline como entrada aprovada.
4. Testes e implementação futuros validam comportamento HTTP contra essa baseline.

# Security and Observability Requirements

- Segurança contratual: endpoints autenticados devem exigir `Authorization: Bearer <token>`.
- Tokens são tratados como JWT/Bearer em nível de contrato; algoritmo, segredo e claims ficam para workflows posteriores.
- Erros de autenticação/autorização devem ter convenção HTTP/JSON documentada.
- Observabilidade detalhada não faz parte desta baseline; futuros workflows podem adicionar logs/metrics sem alterar contrato externo.

# Trade-Offs and Alternatives

- Usar apenas link externo da especificação RealWorld seria simples, mas frágil para SLDD downstream; a decisão é manter baseline local.
- Criar OpenAPI completo agora seria útil, mas pode antecipar detalhes e aumentar escopo; a decisão inicial é baseline textual estruturada, podendo gerar OpenAPI em workflow posterior se aprovado.
- Detalhar validações exaustivamente agora reduziria ambiguidades, mas pode travar implementação; a decisão é documentar regras essenciais e permitir refinamento compatível.

# High-Level Test Scenario Map

- Verificar que o artefato de contrato local existe e possui seções para endpoints, auth, envelopes, erros e aceite.
- Verificar que cada área RealWorld principal está representada: user/auth, profiles, articles, comments, favorites, feed e tags.
- Verificar que endpoints autenticados possuem indicação de Bearer token.
- Verificar que a baseline declara explicitamente que não introduz código de aplicação.
