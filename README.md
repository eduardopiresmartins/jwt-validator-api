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

O projeto possui testes unitários e testes de integração, incluindo os 4 casos oficiais do desafio.

## Como executar com Docker

```bash
docker build -t jwt-validator-api .
docker run --rm -p 8080:8080 jwt-validator-api
```

O `Dockerfile` utiliza multi-stage build para compilar a aplicação em uma etapa e executar apenas o `.jar` na imagem final.

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

A aplicação utiliza SpringDoc OpenAPI para disponibilizar documentação interativa do endpoint.

Com a aplicação em execução, acesse:

`http://localhost:8080/swagger-ui.html`

Também é possível acessar a especificação OpenAPI em:

`http://localhost:8080/v3/api-docs`

## Health check

A aplicação utiliza Spring Boot Actuator para expor verificações básicas de saúde.

Com a aplicação em execução, o health check pode ser acessado em:

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

Caso uma chave ou secret fosse fornecida, a validação criptográfica poderia ser adicionada como etapa anterior às validações de negócio.

## Decisões técnicas

- Separação em `controller`, `dto`, `service`, `validator` e `exception`
- Validators pequenos e coesos, cada um responsável por uma etapa da validação
- O service orquestra o fluxo completo de validação
- Falhas de regra de negócio retornam HTTP `200` com `valid: false`
- Erros de request inválido retornam HTTP `400` com payload padronizado
- Código orientado à simplicidade, testabilidade e baixo acoplamento

## Observabilidade

Foram adicionados logs no fluxo de validação para facilitar rastreabilidade operacional.

- Cada validação recebe um identificador curto
- O JWT completo não é registrado em log para evitar exposição de dados sensíveis
- O Actuator está disponível para health check

Exemplo:

```text
[0f4252c1] Iniciando validacao de JWT
[0f4252c1] Validacao de JWT concluida com sucesso
```

## CI/CD

O projeto possui workflow de CI com GitHub Actions em `.github/workflows/ci.yml`.

O workflow executa build e testes automatizados em `push` e `pull_request` para a branch `main`.

## Arquitetura sugerida para AWS

### Opção simples para deploy demonstrativo

Uma opção objetiva para demonstração da aplicação é executá-la em uma instância AWS Lightsail com Docker instalado.

Fluxo:

```text
Cliente -> IP publico Lightsail -> Container Docker -> Spring Boot API
```

Essa opção foi considerada pela simplicidade operacional e pelo custo previsível para o contexto do desafio.

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

A aplicação já está preparada para containerização via Docker, o que facilita uma evolução para ECS Fargate sem necessidade de gerenciar servidores diretamente.

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
- Collection do Insomnia ou Postman
