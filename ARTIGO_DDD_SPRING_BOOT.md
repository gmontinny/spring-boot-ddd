# Arquitetura DDD com Spring Boot 4.0.2 e Java 25: Guia Completo

## 📚 Introdução

Este artigo apresenta uma implementação prática de **Domain-Driven Design (DDD)** utilizando as versões mais recentes do ecossistema Java: **Spring Boot 4.0.2** e **Java 25**. O projeto demonstra como construir uma API de E-commerce escalável, mantível e alinhada com os princípios do DDD.

## 🎯 O que é Domain-Driven Design?

Domain-Driven Design é uma abordagem de desenvolvimento de software que coloca o **domínio do negócio** no centro da arquitetura. Criado por Eric Evans, o DDD propõe:

- **Linguagem Ubíqua**: Vocabulário comum entre desenvolvedores e especialistas do domínio
- **Bounded Contexts**: Delimitação clara de contextos de negócio
- **Agregados**: Agrupamento de entidades relacionadas com consistência transacional
- **Value Objects**: Objetos imutáveis que representam conceitos do domínio
- **Domain Events**: Comunicação entre agregados através de eventos

## 🏗️ Arquitetura em Camadas

O projeto segue uma arquitetura em camadas bem definida:

```
┌─────────────────────────────────────┐
│         WEB LAYER (Controllers)      │  ← Interface REST
├─────────────────────────────────────┤
│    APPLICATION LAYER (Use Cases)     │  ← Orquestração
├─────────────────────────────────────┤
│      DOMAIN LAYER (Business Logic)   │  ← Coração do Sistema
├─────────────────────────────────────┤
│  INFRASTRUCTURE LAYER (Persistence)  │  ← Detalhes Técnicos
└─────────────────────────────────────┘
```

### 1. Domain Layer (Camada de Domínio)

O coração da aplicação, onde residem as regras de negócio puras, sem dependências externas.

#### Entidades e Agregados

```java
// Classe base para todas as entidades
public abstract class Entity<ID> {
    protected final ID id;

    protected Entity(ID id) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
    }

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

**Princípios aplicados:**
- Identidade única através do ID
- Igualdade baseada em identidade, não em atributos
- Imutabilidade do identificador

#### Aggregate Root

```java
public abstract class AggregateRoot<ID> extends Entity<ID> {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot(ID id) {
        super(id);
    }

    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
```

**Características:**
- Raiz de consistência transacional
- Gerenciamento de eventos de domínio
- Ponto de entrada para operações no agregado

#### Value Objects

```java
public class Email extends ValueObject {
    private final String address;

    public Email(String address) {
        if (address == null || !address.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
```

```java
public class Money extends ValueObject {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
```

**Vantagens dos Value Objects:**
- Imutabilidade garantida
- Validação no construtor
- Encapsulamento de regras de negócio
- Sem identidade própria

#### Exemplo Completo: Agregado Customer

```java
public class Customer extends AggregateRoot<CustomerId> {
    private String name;
    private Email email;

    public Customer(CustomerId id, String name, Email email) {
        super(id);
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }
}
```

#### Exemplo Completo: Agregado Order

```java
public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(OrderId id, CustomerId customerId) {
        super(id);
        this.customerId = customerId;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
```

**Regras de Negócio no Agregado:**
- Order é o Aggregate Root
- OrderItem é uma entidade interna
- Apenas Order pode adicionar itens
- Status inicial sempre PENDING

### 2. Application Layer (Camada de Aplicação)

Orquestra os casos de uso e coordena as operações do domínio.

#### Use Case Pattern

```java
@Service
@RequiredArgsConstructor
public class CreateOrderUseCase implements UseCase<CreateOrderRequest, OrderId> {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public OrderId execute(CreateOrderRequest request) {
        // 1. Validar cliente
        var customerId = new CustomerId(request.customerId());
        customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Criar pedido
        var order = new Order(OrderId.generate(), customerId);

        // 3. Adicionar itens validando produtos
        for (var itemRequest : request.items()) {
            var productId = new ProductId(itemRequest.productId());
            var product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            var orderItem = new OrderItem(
                    UUID.randomUUID(),
                    productId,
                    itemRequest.quantity(),
                    product.getPrice().getAmount()
            );
            order.addItem(orderItem);
        }

        // 4. Persistir
        orderRepository.save(order);

        // 5. Publicar evento
        var event = new OrderCreatedEvent(order.getId());
        eventPublisher.publish(event);

        return order.getId();
    }
}
```

**Responsabilidades:**
- Orquestração de múltiplos agregados
- Validação de regras de aplicação
- Gerenciamento transacional
- Publicação de eventos

### 3. Infrastructure Layer (Camada de Infraestrutura)

Implementa os detalhes técnicos de persistência e integrações.

#### Padrão Repository

```java
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    @Override
    public Optional<Order> findById(OrderId id) {
        return jpaRepository.findById(id.getValue())
                .map(this::toDomain);
    }

    @Override
    public void save(Order order) {
        var entity = toEntity(order);
        jpaRepository.save(entity);
    }

    private Order toDomain(OrderEntity entity) {
        // Conversão de entidade JPA para domínio
    }

    private OrderEntity toEntity(Order order) {
        // Conversão de domínio para entidade JPA
    }
}
```

**Separação de Responsabilidades:**
- Interface do repositório no domínio
- Implementação na infraestrutura
- Mapeamento entre modelos de domínio e persistência

### 4. Web Layer (Camada de Apresentação)

Expõe a API REST com documentação OpenAPI.

```java
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Endpoints para gestão de pedidos")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Realizar um novo pedido")
    public ResponseEntity<OrderId> create(@RequestBody CreateOrderRequest request) {
        OrderId id = createOrderUseCase.execute(request);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de um pedido")
    public ResponseEntity<OrderResponse> get(@PathVariable UUID id) {
        return orderService.getOrder(new OrderId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
```

## 🚀 Novidades do Spring Boot 4.0.2

### 1. Suporte Nativo ao Java 25

```gradle
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
```

### 2. Gradle 9.3.0

Compatibilidade total com a versão mais recente do Gradle:

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '4.0.2'
    id 'io.spring.dependency-management' version '1.1.7'
}
```

### 3. Melhorias no Spring Data JPA

```yaml
spring:
  jpa:
    open-in-view: false  # Desabilita OSIV para melhor performance
    hibernate:
      ddl-auto: validate  # Validação estrita do schema
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

**Por que desabilitar OSIV?**
- Evita lazy loading fora de transações
- Melhora performance
- Força design explícito de queries

### 4. Integração com Flyway

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
```

Migrações versionadas e automáticas no startup.

## 🔧 Configuração do Projeto

### Dependencies (build.gradle)

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-flyway'
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5'
    implementation 'org.flywaydb:flyway-database-postgresql'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'org.postgresql:postgresql'
    annotationProcessor 'org.projectlombok:lombok'
}
```

### Docker Compose

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_USER: ${DB_USER}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
      POSTGRES_DB: ${DB_NAME}
    ports:
      - "${DB_PORT}:5432"

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    depends_on:
      - postgres
```

## 📊 Bounded Contexts

O projeto está dividido em três contextos delimitados:

### 1. Customer Context
- Gerenciamento de clientes
- Validação de email
- Cadastro e consulta

### 2. Product Context
- Catálogo de produtos
- Gestão de preços (Money)
- Controle de estoque

### 3. Order Context
- Criação de pedidos
- Validação de itens
- Eventos de domínio

## 🎯 Padrões e Boas Práticas

### 1. Dependency Inversion
```java
// Domain define a interface
public interface OrderRepository {
    Optional<Order> findById(OrderId id);
    void save(Order order);
}

// Infrastructure implementa
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    // Implementação com JPA
}
```

### 2. Domain Events

```java
public class OrderCreatedEvent implements DomainEvent {
    private final OrderId orderId;
    private final Instant occurredOn;

    public OrderCreatedEvent(OrderId orderId) {
        this.orderId = orderId;
        this.occurredOn = Instant.now();
    }
}
```

### 3. Validação no Domínio

```java
public Email(String address) {
    if (address == null || !address.contains("@")) {
        throw new IllegalArgumentException("Invalid email address");
    }
    this.address = address;
}
```

## 📈 Vantagens da Arquitetura

### 1. Testabilidade
- Domínio puro sem dependências
- Mocks apenas na infraestrutura
- Testes unitários rápidos

### 2. Manutenibilidade
- Separação clara de responsabilidades
- Código autodocumentado
- Fácil localização de bugs

### 3. Escalabilidade
- Bounded contexts independentes
- Possibilidade de microsserviços
- Event-driven architecture

### 4. Evolução
- Mudanças de infraestrutura não afetam domínio
- Novos casos de uso facilmente adicionados
- Refatoração segura

## 🔍 Documentação Automática com OpenAPI

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: alpha
    tagsSorter: alpha
    display-request-duration: true
```

Acesse: `http://localhost:8080/swagger-ui.html`

## 🎓 Conclusão

Este projeto demonstra como implementar uma arquitetura DDD robusta utilizando as tecnologias mais modernas do ecossistema Java. A combinação de **Spring Boot 4.0.2** e **Java 25** oferece:

- Performance otimizada
- Sintaxe moderna
- Ferramentas maduras
- Comunidade ativa

A arquitetura DDD garante que o código permaneça alinhado com o negócio, facilitando a comunicação entre desenvolvedores e especialistas do domínio, além de proporcionar uma base sólida para crescimento e evolução do sistema.

## 📚 Referências

- Evans, Eric. "Domain-Driven Design: Tackling Complexity in the Heart of Software"
- Vernon, Vaughn. "Implementing Domain-Driven Design"
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Java 25 Release Notes: https://openjdk.org/projects/jdk/25/

---

**Autor**: Projeto Spring Boot DDD  
**Versão**: 1.0.0  
**Data**: 2024
