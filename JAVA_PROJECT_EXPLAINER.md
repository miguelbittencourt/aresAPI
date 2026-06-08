# Guia de Aprendizado do Projeto Java Spring Boot

Este documento descreve a arquitetura e os componentes do projeto para que você avance de OOP básico em Java para aplicações profissionais.

## 1. Visão Geral da Arquitetura

O projeto segue a arquitetura em camadas (Layered):

- Controller (API REST)
- Service (Lógica de negócio)
- Repository (Persistência com JPA)
- Entity (Modelos de domínio)

### 1.1 Estrutura do projeto

```
src/main/java/com/example/demo/
├── controller/        # REST endpoints e rotas HTTP
├── service/           # regras de negócio
├── repository/        # acesso a dados/JPA
├── entity/            # modelos JPA (tabelas)
├── dto/               # objetos de transferência de dados
├── security/          # configurações JWT / Spring Security
├── exception/         # tratamento de erros
└── DemoApplication.java
```

## 2. Entity (Modelos de Dados)

### User Example

- `@Entity`: mapeia classe para tabela do banco.
- `@Id`, `@GeneratedValue`: chave primária auto increment.
- `@OneToMany`: relacionamento user -> workouts.

```java
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
}
```

## 3. Repository (Spring Data JPA)

Exemplo de interface:

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

- `JpaRepository` fornece CRUD pronto.
- Nome de método se converte em query automaticamente.

## 4. Service (Lógica de Negócio)

Exemplo de registro de usuário, criptografando senha e gerando JWT:

```java
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponseDTO register(LoginRegisterDTO loginRegisterDTO) {
        if (userRepository.existsByEmail(loginRegisterDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email já está registrado");
        }

        User user = new User();
        user.setEmail(loginRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(loginRegisterDTO.getPassword()));

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser);

        return new AuthResponseDTO(token, savedUser.getEmail(), savedUser.getId());
    }
}
```

## 5. Controller (API REST)

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody LoginRegisterDTO loginRegisterDTO) {
        AuthResponseDTO response = authService.register(loginRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

- `@RestController` retorna JSON.
- `@RequestBody` converte JSON para objeto Java.
- `@Valid` aplica validação automática.

## 6. Segurança - JWT

Fluxo resumido:

1. `AuthService` gera token com `JwtTokenProvider`.
2. `JwtAuthenticationFilter` inspeciona cabeçalho `Authorization: Bearer <token>`.
3. Valida token e injeta `Authentication` no `SecurityContext`.
4. `SecurityConfig` protege rotas e permite `/api/auth/**` sem login.

## 7. Teste local rápido

1. Execute: `mvn clean compile`
2. Execute: `mvn spring-boot:run`
3. Teste endpoints:
   - `POST /api/auth/register`
   - `POST /api/auth/login`
   - `POST /api/workouts` (com token)
   - `GET /api/workouts` (com token)

## 8. Endpoints importantes

- `POST /api/auth/register` → `LoginRegisterDTO`
- `POST /api/auth/login` → `LoginRegisterDTO`
- `POST /api/workouts` → `CreateWorkoutDTO`
- `GET /api/workouts` → lista de treinos
- `GET /api/workouts/{id}` → treino específico
- `PUT /api/workouts/{id}` → atualiza treino
- `DELETE /api/workouts/{id}` → apaga treino

## 9. Dicas para aprender Java profissionalmente

- Aprenda **maven** e `pom.xml`:
  - dependências, plugins, build
- Pratique **Spring Boot** com projetos pequenos
- Refatore com padrões: `Service`, `Repository`, `DTO`, `Mapper`
- Escreva **testes**: JUnit + Mockito
- Domine **SQL** e mapeamento JPA
- Use IDE proficiente: VS Code / IntelliJ

---

### Como converter para PDF

Se `pandoc` estiver instalado no seu ambiente:

```bash
cd c:/dev/springBootProject
pandoc JAVA_PROJECT_EXPLAINER.md -o JAVA_PROJECT_EXPLAINER.pdf
```

Se não, posso tentar gerar com Python (`reportlab` ou `pdfkit`), ou você pode usar editores como Typora / GitHub repo colab.
