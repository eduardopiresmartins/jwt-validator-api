# JWT Validator API

## Objetivo

Esta aplicação expõe uma API REST para validar um JWT de acordo com regras específicas de negócio. O fluxo valida a estrutura do token, o payload decodificado e o conteúdo das claims exigidas pelo desafio técnico.

## Status da solução

Atualmente o projeto possui:

- API REST funcional
- Validações de JWT implementadas
- Testes unitários
- Testes de integração com os 4 casos oficiais
- Tratamento global de erros
- Logs de observabilidade
- Swagger/OpenAPI
- Dockerfile multi-stage
- Workflow de CI com GitHub Actions
- Deploy demonstrativo em AWS Lightsail com Docker
- Workflow simples de CD para AWS Lightsail via GitHub Actions

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Maven
- Spring Web
- Bean Validation
- Jackson
- JUnit 5
- MockMvc
- Lombok
- SpringDoc OpenAPI
- Spring Boot Actuator
- Docker
- GitHub Actions

## Como executar localmente

```bash
./mvnw spring-boot:run
```

A aplicação sobe por padrão na porta `8080`.

## Como executar os testes

```bash
./mvnw clean test
```

O projeto possui testes unitarios e testes de integracao, incluindo os 4 casos oficiais do desafio.

## Como executar com Docker

```bash
docker build -t jwt-validator-api .
docker run --rm -p 8080:8080 jwt-validator-api
```

O `Dockerfile` utiliza multi-stage build para compilar a aplicação em uma etapa e executar apenas o `.jar` na imagem final.

## Execucao em cloud

Foi realizado um deploy demonstrativo da aplicacao em uma instancia AWS Lightsail com Ubuntu e Docker.

A aplicacao foi:

- clonada na instancia
- empacotada em imagem Docker a partir do `Dockerfile`
- executada em container com exposicao da porta `8080`
- configurada para envio de logs do container ao Amazon CloudWatch Logs
- validada externamente via `curl`, incluindo:
  - `GET /actuator/health`
- `POST /api/v1/jwt/validate` com token valido
- `POST /api/v1/jwt/validate` com token invalido
- `POST /api/v1/jwt/validate` com request invalido

Endpoints publicos do ambiente demonstrativo atual:

- API: `http://32.192.177.12:8080/api/v1/jwt/validate`
- Swagger UI: `http://32.192.177.12:8080/swagger-ui.html`
- OpenAPI JSON: `http://32.192.177.12:8080/v3/api-docs`
- Health check: `http://32.192.177.12:8080/actuator/health`

Por se tratar de um ambiente demonstrativo, esses enderecos podem mudar caso a instancia seja recriada ou substituida.

## Endpoint

`POST /api/v1/jwt/validate`

Request:

```json
{
  "token": "jwt"
}
```

Response:

```json
{
  "valid": true
}
```

ou

```json
{
  "valid": false
}
```

## Documentação da API com Swagger

A aplicacao utiliza SpringDoc OpenAPI para disponibilizar documentacao interativa do endpoint.

Ambiente local:

`http://localhost:8080/swagger-ui.html`

Ambiente demonstrativo em cloud:

`http://32.192.177.12:8080/swagger-ui.html`

Especificacao OpenAPI:

`http://localhost:8080/v3/api-docs`

Especificacao OpenAPI no ambiente demonstrativo:

`http://32.192.177.12:8080/v3/api-docs`

## Health check

A aplicacao utiliza Spring Boot Actuator para expor verificacoes basicas de saude:

`http://localhost:8080/actuator/health`

## Exemplos de uso com curl

Exemplo com o caso válido oficial:

```bash
curl -X POST http://localhost:8080/api/v1/jwt/validate \
  -H "Content-Type: application/json" \
  -d '{"token":"eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg"}'
```

Resposta esperada:

```json
{
  "valid": true
}
```

Exemplo com token inválido:

```bash
curl -X POST http://localhost:8080/api/v1/jwt/validate \
  -H "Content-Type: application/json" \
  -d '{"token":"abc.def.ghi"}'
```

Resposta esperada:

```json
{
  "valid": false
}
```

## Tratamento de erros

A API diferencia falhas de regra de negócio de falhas de request:

- JWT inválido por regra de negócio retorna HTTP `200` com `{"valid": false}`
- Request inválido retorna HTTP `400` com payload padronizado

Exemplo para token vazio:

```json
{
  "message": "Requisicao invalida",
  "details": [
    "Token is required"
  ]
}
```

Exemplo para body malformado:

```json
{
  "message": "Corpo da requisicao malformado",
  "details": [
    "Corpo da requisicao ausente ou invalido"
  ]
}
```

## Regras de validação

- JWT deve possuir 3 partes
- Payload deve ser Base64Url válido
- Payload deve ser JSON válido
- Payload deve conter exatamente `Name`, `Role` e `Seed`
- `Name` deve ser texto, não vazio, sem números e com no máximo 256 caracteres
- `Role` deve ser `Admin`, `Member` ou `External`
- `Seed` deve ser número inteiro positivo e primo

## Premissas assumidas

O desafio fornece tokens, mas não fornece secret, chave pública ou algoritmo esperado para validação criptográfica da assinatura.

Por esse motivo, a aplicação valida:

- a estrutura do JWT
- a decodificação do payload
- as regras de negócio das claims

A assinatura é mantida como parte estrutural do token, mas não é validada criptograficamente.

Essa decisão evita assumir um segredo ou algoritmo não informado no enunciado e mantém a solução aderente à massa de teste fornecida.

Caso uma chave ou secret fossem fornecidos, a validacao criptografica poderia ser adicionada antes das validacoes de negocio.

## Decisões técnicas

- Separação em `controller`, `dto`, `service`, `validator` e `exception`
- Validators pequenos e coesos, cada um responsável por uma etapa da validação
- O service orquestra o fluxo completo de validação
- Falhas de regra de negócio retornam HTTP `200` com `valid: false`
- Erros de request inválido retornam HTTP `400` com payload padronizado
- Código orientado à simplicidade, testabilidade e baixo acoplamento

## Observabilidade

Foram adicionados logs no fluxo de validacao para facilitar rastreabilidade operacional.

- Cada validação recebe um identificador curto
- O JWT completo não é registrado em log para evitar exposição de dados sensíveis
- O Actuator está disponível para health check
- No ambiente demonstrativo em AWS Lightsail, os logs do container sao enviados para o Amazon CloudWatch Logs

Exemplo:

```text
[0f4252c1] Iniciando validacao de JWT
[0f4252c1] Validacao de JWT concluida com sucesso
```

Exemplo observado no CloudWatch:

```text
[a27885e2] Iniciando validacao de JWT
[a27885e2] Falha na validacao do JWT: payload invalido
```

## CI/CD

O projeto possui workflow de CI com GitHub Actions em `.github/workflows/ci.yml`.

O workflow executa build e testes automatizados em `push` e `pull_request` para a branch `main`.

Para `push` na branch `main`, o mesmo workflow tambem pode executar um deploy simples na instancia AWS Lightsail via SSH, mantendo o projeto aderente ao ambiente atual da solucao.

Fluxo resumido:

```text
Push na main -> GitHub Actions -> ./mvnw clean test -> SSH na instancia Lightsail -> docker build -> restart do container -> health check
```

Secrets esperados no GitHub para a etapa de deploy:

- `LIGHTSAIL_HOST`
- `LIGHTSAIL_USER`
- `LIGHTSAIL_SSH_KEY`
- `LIGHTSAIL_APP_PATH`
- `LIGHTSAIL_LOG_GROUP`
- `LIGHTSAIL_LOG_STREAM`
- `AWS_REGION`

## Arquitetura sugerida para AWS

### Opcao simples para deploy demonstrativo

Uma opcao objetiva para demonstracao da aplicacao e executa-la em uma instancia AWS Lightsail com Docker instalado. Essa foi a abordagem utilizada para disponibilizar a API em um ambiente cloud simples durante a avaliacao da solucao.

Fluxo:

```text
Cliente -> IP publico Lightsail -> Container Docker -> Spring Boot API -> CloudWatch Logs
```

Essa opcao foi considerada pela simplicidade operacional e pelo custo previsivel para o contexto do desafio.

### Evolução para ambiente produtivo

Uma evolução natural para um ambiente mais robusto pode considerar:

- Amazon ECR para armazenar a imagem Docker
- Amazon ECS Fargate para executar o container
- Application Load Balancer para roteamento
- Amazon CloudWatch Logs para centralização dos logs
- AWS X-Ray ou OpenTelemetry para tracing distribuído
- GitHub Actions para build, testes e publicação da imagem
- API Gateway como camada opcional de exposição, autenticação e controle de tráfego

Fluxo:

```text
Cliente -> API Gateway ou ALB -> ECS Fargate -> Container da aplicacao -> CloudWatch Logs
```

A aplicacao ja esta preparada para containerizacao via Docker, o que facilita uma evolucao para ECS Fargate sem necessidade de gerenciar servidores diretamente.

## Estrutura do projeto

```text
src
├── main
│   ├── java/br/com/eduardo/jwtvalidator
│   │   ├── controller
│   │   ├── dto
│   │   ├── exception
│   │   ├── service
│   │   └── validator
│   └── resources
└── test
    └── java/br/com/eduardo/jwtvalidator
```

## Historico de evolucao

A solução foi construída incrementalmente, com etapas separadas para:

- setup inicial
- contrato da API
- validações
- testes
- observabilidade
- tratamento de erros
- Docker
- CI
- documentação

## Melhorias futuras

- Validação criptográfica da assinatura caso secret ou chave pública sejam fornecidos
- Publicação da imagem no Amazon ECR
- Deploy em ECS Fargate
- Métricas customizadas com Micrometer e Prometheus
- Tracing distribuído com AWS X-Ray ou OpenTelemetry
- Terraform ou OpenTofu para provisionamento de infraestrutura
