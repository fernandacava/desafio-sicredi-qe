<<<<<<< HEAD
# Desafio Sicredi QE — Automação de API DummyJSON

Projeto de automação de testes de API desenvolvido para o **Desafio Técnico Sicredi Quality Engineer**, cobrindo o gerenciamento de produtos eletrônicos na API pública [DummyJSON](https://dummyjson.com).

## Informações do projeto

| Item | Detalhe |
|------|---------|
| Nome | `desafio-sicredi-qe` |
| API alvo | https://dummyjson.com |
| Objetivo | Validar endpoints de health, usuários, autenticação e produtos |
| Tipo de testes | API (REST), positivos e negativos |
| Padrão arquitetural | Client Layer + POJOs + Factory + Base Test |

## Tecnologias utilizadas

- **Java 21**
- **Maven 3.9+**
- **Rest Assured 5.5**
- **JUnit 5**
- **AssertJ**
- **Jackson**
- **Allure Report**
- **Awaitility** (teste de token expirado)
- **GitLab CI**

## Estrutura do projeto

```
desafio-sicredi-qe/
├── .gitlab-ci.yml
├── pom.xml
├── README.md
└── src/test/java/br/com/sicredi/qe/
    ├── base/BaseTest.java
    ├── clients/
    │   ├── AuthClient.java
    │   ├── HealthClient.java
    │   ├── ProductClient.java
    │   └── UserClient.java
    ├── config/ConfigManager.java
    ├── factory/TestDataFactory.java
    ├── models/
    ├── tests/
    │   ├── AuthProductsTest.java
    │   ├── HealthTest.java
    │   ├── LoginTest.java
    │   ├── ProductByIdTest.java
    │   ├── ProductCreateTest.java
    │   ├── ProductsTest.java
    │   └── UsersTest.java
    └── utils/
        ├── JsonUtils.java
        └── TokenManager.java
```

## Como executar

### Pré-requisitos

- JDK 21
- Maven 3.9+
- Acesso à internet (API pública DummyJSON)

### Executar todos os testes

```bash
cd desafio-sicredi-qe
mvn clean test
```

### Executar incluindo teste lento (token expirado)

Por padrão, testes com tag `slow` são excluídos para manter o pipeline rápido.

```bash
mvn clean test -Pslow-tests
```

### Gerar relatório Allure

```bash
mvn clean test allure:report
mvn allure:serve
```

O relatório HTML é gerado em `target/site/allure-maven-plugin/`.

## Estratégia de testes

### Abordagem

1. **Camada Client**: encapsula endpoints REST, isolando Rest Assured dos testes.
2. **POJOs**: contratos tipados para request/response (Jackson).
3. **Test Data Factory**: centraliza massa de dados válida e inválida.
4. **BaseTest**: configuração única de base URI, filtros Allure e logging.
5. **Testes por domínio**: uma classe por recurso (health, login, users, products).
6. **Validação em camadas**:
   - Status HTTP
   - Contrato JSON (campos obrigatórios)
   - Regras funcionais (id > 0, token presente, paginação)

### Pirâmide aplicada

- 100% testes de API (sem UI)
- Cenários positivos para fluxo feliz
- Cenários negativos para robustez (401, 400, 404)
- Teste de token expirado marcado como `slow` (execução opcional)

## Plano de testes

| ID | Endpoint | Cenário | Tipo | Classe |
|----|----------|---------|------|--------|
| TC01 | GET /test | Status 200, status=ok, method=GET | Positivo | HealthTest |
| TC02 | GET /users | Lista com username, password e id>0 | Positivo | UsersTest |
| TC03 | GET /users | Paginação limit/skip | Positivo | UsersTest |
| TC04 | POST /auth/login | Login válido com token | Positivo | LoginTest |
| TC05 | POST /auth/login | Senha inválida | Negativo | LoginTest |
| TC06 | POST /auth/login | Usuário inexistente | Negativo | LoginTest |
| TC07 | POST /auth/login | Body vazio | Negativo | LoginTest |
| TC08 | POST /auth/login | Campos obrigatórios ausentes | Negativo | LoginTest |
| TC09 | GET /auth/products | Token válido | Positivo | AuthProductsTest |
| TC10 | GET /auth/products | Sem Authorization | Negativo | AuthProductsTest |
| TC11 | GET /auth/products | Token inválido | Negativo | AuthProductsTest |
| TC12 | GET /auth/products | Token expirado | Negativo | AuthProductsTest |
| TC13 | POST /products/add | Cadastro válido | Positivo | ProductCreateTest |
| TC14 | POST /products/add | Payload vazio | Negativo | ProductCreateTest |
| TC15 | POST /products/add | Preço negativo | Negativo* | ProductCreateTest |
| TC16 | POST /products/add | Payload com tipos inválidos | Negativo* | ProductCreateTest |
| TC17 | GET /products | Lista com total/skip/limit | Positivo | ProductsTest |
| TC18 | GET /products | Paginação limit/skip | Positivo | ProductsTest |
| TC19 | GET /products/{id} | Produto existente | Positivo | ProductByIdTest |
| TC20 | GET /products/{id} | Produto inexistente (404) | Negativo | ProductByIdTest |
| TC21 | GET /products/{id} | Id inválido (404) | Negativo | ProductByIdTest |

\* A API DummyJSON é simulada e pode não rejeitar o cenário conforme esperado em produção.

## Cenários cobertos

### GET /test
- Status 200
- Campo `status` = `ok`
- Campo `method` = `GET`

### GET /users
- Lista retornada com usuários
- `username` e `password` preenchidos
- `id` maior que zero
- Paginação com `limit` e `skip`

### POST /auth/login
- Login válido retorna `accessToken` e `refreshToken`
- Senha inválida → 400
- Usuário inexistente → 400
- Body vazio → 400
- Username ou password ausentes → 400

### GET /auth/products
- Token válido → 200 com lista de produtos
- Sem Authorization → 401
- Token inválido → 401
- Token expirado → 401 (teste `slow`)

### POST /products/add
- Cadastro válido de produto eletrônico
- Payload vazio (comportamento simulado)
- Preço negativo
- Payload com tipos inválidos

### GET /products
- Lista de produtos com `total`, `skip` e `limit`
- Paginação customizada

### GET /products/{id}
- Produto existente
- Produto inexistente → 404
- Id inválido → 404

## Bugs encontrados

Durante a execução contra a API DummyJSON (ambiente fake), foram observados comportamentos que seriam tratados como defeitos em uma API real:

| # | Endpoint | Comportamento observado | Severidade | Esperado em produção |
|---|----------|-------------------------|------------|---------------------|
| BUG-01 | POST /products/add | Aceita `price` negativo e retorna 200 | Média | 400 Bad Request |
| BUG-02 | POST /products/add | Aceita payload vazio e gera produto com novo id | Baixa | 400 com validação de campos obrigatórios |
| BUG-03 | POST /products/add | Aceita tipos inválidos (`title` numérico, `price` string) | Média | 400 com mensagem de validação |
| BUG-04 | GET /users | Expõe `password` em texto claro na listagem pública | Alta | Senha nunca deveria ser retornada |

> **Nota:** A DummyJSON é uma API de demonstração. Os cenários acima são documentados para evidenciar critério de QA e análise de risco.

## Melhorias sugeridas

1. **Contrato com JSON Schema**: validar responses com schemas versionados.
2. **Testes de contrato (Pact)**: garantir compatibilidade entre consumidor e provedor.
3. **Paralelização controlada**: JUnit 5 parallel para reduzir tempo de execução.
4. **Gestão de secrets**: credenciais via variáveis de ambiente/CI variables.
5. **Retry inteligente**: para instabilidade de rede em pipelines.
6. **Categorias Allure por severidade**: dashboard executivo para stakeholders.
7. **Integração com Postman/Newman**: reutilizar coleções em smoke tests.
8. **Ambiente mockável**: WireMock para cenários não cobertos pela API fake.

## Git e entrega

### Inicialização local

```bash
cd desafio-sicredi-qe
git init
git add .
git commit -m "Initial project setup"
git branch -M main
```

### Conectar ao GitLab (repositório privado)

```bash
git remote add origin git@gitlab.com:SEU_USUARIO/desafio-sicredi-qe.git
git push -u origin main
```

### Acesso para correção

No GitLab, adicionar como **Developer** no repositório privado:

| Usuário | E-mail |
|---------|--------|
| `sicredi_user` | correcaoprovaqa@sicredi.com.br |
| `sicredi_user_dbserver` | (usuário indicado pela empresa) |

**Settings → Members → Invite member**

## Pipeline CI/CD

O arquivo `.gitlab-ci.yml` executa:

1. `mvn clean test` com publicação de JUnit XML e Allure results
2. `mvn allure:report` para gerar relatório HTML

## Autor

Desafio técnico — Sicredi Quality Engineer
=======
# desafio-sicredi-qe
>>>>>>> 152777e92fb5cf47267d9ba8f543f45eb828f95f
