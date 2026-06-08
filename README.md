# Ares API

API REST desenvolvida com Spring Boot para o aplicativo **Ares Training**, responsável pelo gerenciamento de usuários, autenticação e registro de treinos de musculação.

## Tecnologias Utilizadas

* Java
* Spring Boot
* Spring Security
* JWT (JSON Web Token)
* Spring Data JPA
* Maven
* Banco de Dados Relacional
* Swagger / OpenAPI

---

## Funcionalidades

### Autenticação

* Cadastro de usuários
* Login com e-mail e senha
* Geração de token JWT
* Proteção de rotas autenticadas

### Treinos

* Criação de treinos
* Consulta de treinos cadastrados
* Gerenciamento de exercícios
* Gerenciamento de séries
* Associação de treinos ao usuário autenticado

### Documentação

* Documentação automática via Swagger/OpenAPI

---

## Estrutura do Projeto

```text
src/main/java/com/example/demo
│
├── config
├── controller
│   ├── AuthController
│   └── WorkoutController
│
├── dto
├── entity
├── exception
├── repository
├── security
└── service
```

### Camadas

| Camada     | Responsabilidade               |
| ---------- | ------------------------------ |
| Controller | Exposição dos endpoints REST   |
| Service    | Regras de negócio              |
| Repository | Comunicação com banco de dados |
| Entity     | Representação das tabelas      |
| DTO        | Transferência de dados         |
| Security   | Autenticação e autorização     |
| Exception  | Tratamento global de erros     |

---

## Configuração do Ambiente

### Pré-requisitos

* Java 17+ (ou versão utilizada no projeto)
* Maven
* Banco de dados configurado

### Clone do Repositório

```bash
git clone <url-do-repositorio>
cd ares-api
```

### Configuração

Configure o arquivo:

```properties
src/main/resources/application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ares
spring.datasource.username=usuario
spring.datasource.password=senha

jwt.secret=sua-chave-secreta
jwt.expiration=86400000
```

---

## Executando o Projeto

### Via Maven Wrapper

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
mvnw.cmd spring-boot:run
```

### Via Maven

```bash
mvn spring-boot:run
```

---

## Endpoints Principais

### Autenticação

| Método | Endpoint       | Descrição           |
| ------ | -------------- | ------------------- |
| POST   | /auth/register | Cadastro de usuário |
| POST   | /auth/login    | Login               |

### Treinos

| Método | Endpoint       | Descrição           |
| ------ | -------------- | ------------------- |
| GET    | /workouts      | Lista os treinos    |
| GET    | /workouts/{id} | Busca treino por ID |
| POST   | /workouts      | Cria treino         |
| PUT    | /workouts/{id} | Atualiza treino     |
| DELETE | /workouts/{id} | Remove treino       |

> Os endpoints acima podem variar conforme a implementação final.

---

## Autenticação JWT

Após realizar login, utilize o token retornado no cabeçalho:

```http
Authorization: Bearer SEU_TOKEN
```

---

## Documentação Swagger

Após iniciar a aplicação, acesse:

```text
http://localhost:8080/swagger-ui.html
```

ou

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Tratamento de Erros

A API possui tratamento global de exceções para:

* Recurso não encontrado
* Usuário já cadastrado
* Erros de validação
* Falhas de autenticação

---

## Futuras Melhorias

* Histórico de evolução de cargas
* Divisão de treinos por grupos musculares
* Metas de treino
* Dashboard de desempenho
* Upload de imagens
* Integração com aplicativo mobile

---

## Autor

Desenvolvido para o projeto **Ares Training**, plataforma de acompanhamento e registro de treinos de musculação.
