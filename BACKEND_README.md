# AresApp Backend

Backend Spring Boot para a aplicação AresApp - um aplicativo para rastreamento de exercícios e progresso em treinos.

## Requisitos

- Java 17+
- Maven 3.6+

## Funcionalidades

### Autenticação

- Registro de novos usuários
- Login com geração de JWT token
- Autenticação baseada em Bearer token

### Gerenciamento de Treinos

- Criar novos treinos
- Listar todos os treinos do usuário
- Obter treino específico
- Filtrar treinos por data
- Atualizar treino
- Deletar treino

### Estrutura de Dados

- Usuário
- Treino (com data, academia, exercícios)
- Exercício (com nome, notas, séries)
- Série (com repetições, peso, unidade)

## Instalação

1. Compilar o projeto:

```bash
mvn clean compile
```

2. Executar a aplicação:

```bash
mvn spring-boot:run
```

A aplicação iniciará em `http://localhost:8080`

## Endpoints da API

### Autenticação

#### Registro

```
POST /api/auth/register
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "senha123"
}
```

**Resposta:**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "email": "usuario@example.com",
  "userId": 1
}
```

#### Login

```
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "senha123"
}
```

**Resposta:**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "email": "usuario@example.com",
  "userId": 1
}
```

### Treinos

#### Criar Treino

```
POST /api/workouts
Authorization: Bearer <token>
Content-Type: application/json

{
  "gymName": "Academia X",
  "date": "2024-01-15",
  "rawText": "texto bruto opcional",
  "exercises": [
    {
      "id": "ex1",
      "orderIndex": 1,
      "name": "Supino",
      "notes": "com halteres",
      "sets": [
        {
          "reps": 10,
          "weight": 20,
          "unit": "kg"
        },
        {
          "reps": 8,
          "weight": 25,
          "unit": "kg"
        }
      ]
    }
  ]
}
```

#### Listar Todos os Treinos

```
GET /api/workouts
Authorization: Bearer <token>
```

#### Obter Treino Específico

```
GET /api/workouts/{id}
Authorization: Bearer <token>
```

#### Treinos por Data

```
GET /api/workouts/date/{data}
Authorization: Bearer <token>
```

#### Atualizar Treino

```
PUT /api/workouts/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "gymName": "Academia Y",
  "date": "2024-01-16",
  "exercises": [...]
}
```

#### Deletar Treino

```
DELETE /api/workouts/{id}
Authorization: Bearer <token>
```

## Configuração

As configurações principais estão em `src/main/resources/application.properties`:

- `jwt.secret`: Chave secreta para geração de JWT (mude em produção!)
- `jwt.expiration`: Tempo de expiração do token em milissegundos (padrão: 24 horas)
- `spring.jpa.hibernate.ddl-auto`: Estratégia de DDL (create-drop para desenvolvimento)

## Banco de Dados

O projeto utiliza H2 (banco em memória) para desenvolvimento. Para acessar o console:

```
http://localhost:8080/h2-console
```

**Credenciais:**

- URL: `jdbc:h2:mem:testdb`
- Usuário: `sa`
- Senha: (deixar em branco)

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/        # REST Endpoints
│   │   ├── service/           # Lógica de negócio
│   │   ├── entity/            # Modelos JPA
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── repository/        # Acesso a dados
│   │   ├── security/          # Configuração de segurança e JWT
│   │   ├── exception/         # Tratamento de exceções
│   │   └── DemoApplication.java
│   └── resources/
│       └── application.properties
└── test/
```

## Testando a API

Use o Postman ou similar para testar:

1. Registre um novo usuário em `POST /api/auth/register`
2. Use o token retornado para autenticar nas requisições
3. Crie, liste e gerencie treinos

## Segurança

- Senhas são criptografadas com BCrypt
- Autenticação via JWT Bearer token
- Tokens expiram após 24 horas
- CORS habilitado para todos os domínios (ajuste em produção)

## Desenvolvimento Futuro

- [ ] Integração com banco de dados PostgreSQL
- [ ] Sistema de relatórios de progresso
- [ ] Gráficos de evolução
- [ ] Backup e restauração de dados
- [ ] Sistema de badges/conquistas
- [ ] Integração com wearables
