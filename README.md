# Spring Boot DDD - E-Commerce API

Este projeto é uma demonstração de uma API de E-commerce desenvolvida utilizando **Spring Boot** e seguindo os princípios do **Domain-Driven Design (DDD)**. O objetivo é fornecer uma base sólida, escalável e de fácil manutenção, separando claramente as regras de negócio da infraestrutura técnica.

## 🚀 Tecnologias Utilizadas

- **Java 25**
- **Spring Boot 4.0.2**
- **Spring Data JPA** (Persistência de dados)
- **Bean Validation** (Validação de dados com Hibernate Validator)
- **Flyway** (Migrações de banco de dados)
- **PostgreSQL** (Banco de dados relacional)
- **Lombok** (Redução de código boilerplate)
- **SpringDoc OpenAPI** (Documentação Swagger)
- **Docker & Docker Compose** (Containerização)
- **Gradle 9.3.0** (Gerenciador de dependências)
- **Eclipse Temurin JDK 25** (Imagem Docker oficial)

## 🏗️ Arquitetura (DDD)

O projeto está estruturado seguindo os padrões do DDD, dividido em contextos delimitados (Bounded Contexts) e camadas:

- **Domain**: Contém o coração do negócio. Aqui residem as Entidades, Objetos de Valor (Value Objects), Agregados (Aggregate Roots), Repositórios (Interfaces) e Eventos de Domínio.
- **Application**: Camada de orquestração que contém os Casos de Uso (Use Cases) e Application Services. Ela coordena a execução das tarefas e validações.
- **Infrastructure**: Implementações técnicas e detalhes de persistência, como repositórios JPA, entidades de banco de dados e configurações de infraestrutura (Swagger, Eventos).
- **Web**: Camada de entrada da aplicação, contendo os Controllers REST enriquecidos com documentação OpenAPI.
- **Shared**: Componentes e abstrações que são compartilhados entre os diferentes domínios da aplicação.

### Estrutura de Pastas

```text
src/main/java/com/ecommerce/
├── customer/        # Domínio de Clientes (Agregados, Entidades, Repositórios)
├── order/           # Domínio de Pedidos (Lógica de criação e itens)
├── product/         # Domínio de Produtos (Catálogo e Preços)
├── shared/          # Código compartilhado e infraestrutura base
└── web/             # Controllers REST (Interface da API)
```

## 📋 Pré-requisitos

- Java 25 instalado
- Docker e Docker Compose (recomendado para banco e aplicação)
- PostgreSQL 15+ (caso opte por não usar Docker)

## ⚙️ Como Executar

### 1. Clonar o repositório
```bash
git clone <url-do-repositorio>
cd spring-boot-ddd
```

### 2. Configuração do Ambiente
O projeto utiliza um arquivo `.env` na raiz para gerenciar as credenciais do banco de dados de forma centralizada. Certifique-se de que ele existe:

```env
DB_HOST=postgres
DB_PORT=5432
DB_NAME=ecommerce_db
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

### 3. Executando com Docker (Recomendado)
A maneira mais fácil de subir o ambiente completo:

```bash
docker compose up --build
```

> **Nota**: O comando `docker compose` (sem hífen) é a versão moderna. O projeto usa **Eclipse Temurin JDK 25** como imagem base, garantindo compatibilidade total com Java 25 e todas as ferramentas necessárias para o Gradle.

A API estará disponível em `http://localhost:8080`.

### 4. Executando Localmente (Gradle)
Se preferir rodar apenas o banco no Docker:

1. Suba o banco: `docker compose up -d postgres`
2. Execute: `./gradlew bootRun`

## 🛣️ Endpoints e Documentação

### Swagger UI (OpenAPI)
A documentação é gerada automaticamente e pode ser personalizada via `application.yml`.
- **Interface Visual**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Principais Endpoints
- `POST /customers`: Cadastro de novos clientes.
- `POST /products`: Cadastro de produtos no catálogo.
- `GET /products/{id}`: Consulta de produto por ID.
- `POST /orders`: Criação de pedidos com validação de cliente e itens.
- `GET /orders/{id}`: Consulta detalhada de um pedido.

## 🗄️ Banco de Dados e Migrações

### Flyway
As migrações são aplicadas automaticamente pelo Spring Boot no startup (`src/main/resources/db/migration`). 

> **Nota sobre o Plugin Gradle**: O plugin do Flyway para Gradle foi removido para garantir compatibilidade total com o **Gradle 9** e **Java 25**. A gestão recomendada é via integração nativa do Spring Boot.

### Reset Completo do Banco
Para limpar todos os dados e recriar as tabelas:
```bash
docker compose down -v
docker compose up --build
```

### Boas Práticas Aplicadas
- **OSIV Desativado**: `spring.jpa.open-in-view` configurado como `false` para evitar problemas de performance.
- **Strict Validation**: O Hibernate está configurado para `validate`, garantindo que o código Java e o banco SQL estejam sempre em sincronia.
- **Bean Validation**: Uso de `spring-boot-starter-validation` para garantir a integridade dos dados de entrada.

---
Desenvolvido seguindo as melhores práticas de DDD e as versões mais recentes do ecossistema Spring.
