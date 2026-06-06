# Repository Structure Overview

- O repositório ainda está essencialmente no estado de template RealWorld.
- Não há módulos Quarkus, `pom.xml`, código Java de produção ou aplicação de testes standalone criados neste momento.
- A estrutura SLDD atual vive em `.sldd/specs/` e contém o workflow-set pai e workflows filhos scaffoldados.
- `README.md` ainda contém placeholders do template RealWorld.

# Architecture Summary

- Nenhuma arquitetura de aplicação foi implementada ainda.
- A arquitetura-alvo será definida em workflows posteriores, especialmente `workspace-quarkus-baseline`, `realworld-architecture-baseline`, `realworld-api-application-shell` e `realworld-api-st-shell`.
- Para este workflow, o artefato arquitetural relevante é apenas a baseline contratual da API RealWorld, usada como fonte comum para decisões futuras.

# Conventions to Preserve

- Preservar o fluxo SLDD: intenção, contexto, design, testes red, implementação green e verificação.
- Preservar separação entre definição de contrato e implementação.
- Preservar o objetivo do template RealWorld: backend compatível com a API pública RealWorld/Conduit.
- Não introduzir código, módulos, dependências ou estrutura física de workspace neste workflow.

# Integration Points

- Downstream `workspace-quarkus-baseline` usará esta baseline para criar a estrutura física do workspace.
- Downstream `realworld-system-test-strategy` usará esta baseline para definir cenários HTTP standalone.
- Workflows de implementação (`auth/user`, `profiles`, `articles`) usarão esta baseline para alinhar endpoints, payloads, autenticação e erros.

# Risks and Unknowns

- A especificação pública RealWorld pode conter ambiguidades ou exemplos incompletos.
- A normalização local precisa ser suficiente para orientar próximos workflows sem antecipar detalhes de implementação.
- Como ainda não existe código de aplicação, não há restrições técnicas concretas de compatibilidade interna a preservar.

# Context to Carry Into Steps 02-06

- Este é um workflow de definição contratual, não de implementação.
- O design deve produzir uma baseline local e rastreável do contrato RealWorld.
- Testes do Step 04 devem validar artefatos de contrato/documentação, não comportamento de uma aplicação inexistente.
- Implementação do Step 05, se necessária, deve limitar-se a artefatos de contrato/documentação aprovados neste workflow.
