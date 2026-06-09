# JWT Validator API

## Objetivo

Esta aplicação expõe uma API REST para validar um JWT de acordo com regras específicas de negócio. O fluxo valida a estrutura do token, o payload decodificado e o conteúdo das claims exigidas pelo desafio.

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

Por esse motivo, a solução valida:

- a estrutura do JWT
- a decodificação do payload
- as regras de negócio das claims

A assinatura é mantida como parte estrutural do token, mas não é validada criptograficamente.

Caso uma chave/secret fosse fornecida, a validação criptográfica poderia ser adicionada como uma etapa anterior às validações de negócio.

## Decisões técnicas

- Separação em `controller`, `dto`, `service`, `validator` e `exception`
- Validators pequenos e coesos, cada um responsável por uma etapa da validação
- O service orquestra o fluxo completo de validação
- Falhas de regra de negócio retornam HTTP `200` com `valid: false`
- Erros de request inválido retornam HTTP `400` com payload padronizado
- O código prioriza simplicidade, testabilidade e baixo acoplamento

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

Uma evolução natural e simples para execução em AWS pode considerar:

- Amazon ECR para armazenar a imagem Docker
- Amazon ECS Fargate para executar o container
- Application Load Balancer para distribuir requisições
- Amazon CloudWatch Logs para centralização dos logs
- AWS X-Ray como evolução para tracing distribuído
- GitHub Actions para build, testes e publicação da imagem
- API Gateway como camada opcional de exposição, autenticação e controle de tráfego

Como a aplicação já está preparada para containerização via Docker, uma evolução para ECS Fargate se torna um próximo passo natural, sem necessidade de gerenciar servidores diretamente.

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

## Melhorias futuras

- Validação criptográfica da assinatura caso secret/chave pública seja fornecida
- Publicação da imagem no Amazon ECR
- Deploy em ECS Fargate
- Métricas customizadas com Micrometer/Prometheus
- Tracing distribuído com AWS X-Ray ou OpenTelemetry
- Terraform/OpenTofu para provisionamento de infraestrutura
- Collection do Insomnia/Postman
