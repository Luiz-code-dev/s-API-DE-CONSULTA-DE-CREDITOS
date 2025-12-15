# API de Consulta de Créditos Constituídos

API RESTful desenvolvida com Spring Boot para consulta de créditos constituídos, acompanhada de um frontend Angular para visualização dos dados.

## 📋 Índice

- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Endpoints da API](#-endpoints-da-api)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Testes](#-testes)
- [Docker](#-docker)

## 🚀 Tecnologias

### Backend
- **Java 21 LTS**
- **Spring Boot 3.2.x**
- **Spring Data JPA**
- **Hibernate 6.x**
- **PostgreSQL 15+**
- **Apache Kafka** (mensageria)
- **Lombok 1.18.30** (redução de boilerplate)
- **JUnit 5 & Mockito** (testes)
- **SpringDoc OpenAPI 2.x** (documentação)

### Frontend
- **Angular 17.x**
- **TypeScript 5.4**
- **SCSS**
- **RxJS 7.8**

### Infraestrutura
- **Docker & Docker Compose**
- **Nginx 1.25** (servidor web frontend)
- **Eclipse Temurin 21** (JDK/JRE)

## 🏗 Arquitetura

O projeto segue os princípios de **Clean Architecture** e padrões de projeto como:

- **MVC** (Model-View-Controller)
- **Repository Pattern**
- **DTO Pattern**
- **Dependency Injection**

### Estrutura de Camadas (Backend)

```
src/main/java/com/creditoapi/
├── domain/              # Entidades e Repositórios
│   ├── entity/
│   └── repository/
├── application/         # Lógica de Negócio
│   ├── dto/
│   ├── mapper/
│   └── service/
├── infrastructure/      # Configurações e Integrações
│   ├── config/
│   └── messaging/
└── presentation/        # Controllers e Handlers
    ├── controller/
    └── exception/
```

## 📦 Pré-requisitos

- **Java 21 LTS** (JDK)
- **Maven 3.9+**
- **Node.js 20+**
- **npm 10+**
- **Docker & Docker Compose** (opcional, para execução containerizada)

## 🔧 Instalação e Execução

### Opção 1: Docker Compose (Recomendado)

Execute todos os serviços com um único comando:

```bash
# Subir toda a stack (PostgreSQL, Kafka, Backend, Frontend)
docker-compose up -d

# Verificar logs
docker-compose logs -f

# Parar os serviços
docker-compose down
```

Acesse:
- **Frontend:** http://localhost:4200
- **Backend API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html

### Opção 2: Execução Local (Desenvolvimento)

#### 1. Subir infraestrutura (PostgreSQL e Kafka)

```bash
docker-compose -f docker-compose.dev.yml up -d
```

#### 2. Backend

```bash
cd backend

# Instalar dependências e compilar
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A API estará disponível em: http://localhost:8080

#### 3. Frontend

```bash
cd frontend

# Instalar dependências
npm install

# Executar em modo desenvolvimento
npm start
```

O frontend estará disponível em: http://localhost:4200

## 📡 Endpoints da API

### Buscar créditos por NFS-e

```http
GET /api/creditos/{numeroNfse}
```

**Parâmetros:**
| Nome | Tipo | Descrição |
|------|------|-----------|
| `numeroNfse` | `string` | Número identificador da NFS-e |

**Exemplo de resposta:**
```json
[
  {
    "numeroCredito": "123456",
    "numeroNfse": "7891011",
    "dataConstituicao": "2024-02-25",
    "valorIssqn": 1500.75,
    "tipoCredito": "ISSQN",
    "simplesNacional": "Sim",
    "aliquota": 5.0,
    "valorFaturado": 30000.00,
    "valorDeducao": 5000.00,
    "baseCalculo": 25000.00
  }
]
```

### Buscar crédito por número

```http
GET /api/creditos/credito/{numeroCredito}
```

**Parâmetros:**
| Nome | Tipo | Descrição |
|------|------|-----------|
| `numeroCredito` | `string` | Número identificador do crédito |

**Exemplo de resposta:**
```json
{
  "numeroCredito": "123456",
  "numeroNfse": "7891011",
  "dataConstituicao": "2024-02-25",
  "valorIssqn": 1500.75,
  "tipoCredito": "ISSQN",
  "simplesNacional": "Sim",
  "aliquota": 5.0,
  "valorFaturado": 30000.00,
  "valorDeducao": 5000.00,
  "baseCalculo": 25000.00
}
```

## 📁 Estrutura do Projeto

```
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/creditoapi/
│   │   │   └── resources/
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   ├── models/
│   │   │   └── services/
│   │   ├── environments/
│   │   └── styles.scss
│   ├── Dockerfile
│   └── package.json
├── docker-compose.yml
├── docker-compose.dev.yml
└── README.md
```

## 🧪 Testes

### Testes Visuais da API

#### Swagger UI (Recomendado)

Acesse a documentação interativa no navegador:
```
http://localhost:8080/swagger-ui.html
```

No Swagger você pode:
- Visualizar todos os endpoints disponíveis
- Testar as requisições diretamente no browser
- Ver os schemas de request/response

#### Postman / cURL

**Buscar créditos por NFS-e:**
```bash
curl http://localhost:8080/api/creditos/7891011
```

**Buscar crédito por número:**
```bash
curl http://localhost:8080/api/creditos/credito/123456
```

#### PowerShell
```powershell
# Buscar por NFS-e (retorna 2 créditos)
Invoke-RestMethod -Uri "http://localhost:8080/api/creditos/7891011"

# Buscar crédito específico
Invoke-RestMethod -Uri "http://localhost:8080/api/creditos/credito/123456"
```

### Testes Unitários (Backend)

```bash
cd backend

# Executar todos os testes
mvn test

# Executar testes com relatório de cobertura
mvn test jacoco:report
```

O relatório de cobertura será gerado em: `target/site/jacoco/index.html`

### Testes Implementados

- **CreditoServiceTest** - Testes unitários do serviço
- **CreditoControllerTest** - Testes do controller (MockMvc)
- **CreditoMapperTest** - Testes do mapper
- **CreditoRepositoryTest** - Testes de integração do repositório

## 🐳 Docker

### Build das imagens

```bash
# Build do backend
docker build -t credito-api:latest ./backend

# Build do frontend
docker build -t credito-frontend:latest ./frontend
```

### Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `SPRING_DATASOURCE_URL` | URL do banco de dados | `jdbc:postgresql://localhost:5433/credito_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `postgres` |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Servidores Kafka | `localhost:9092` |
| `APP_KAFKA_ENABLED` | Habilitar Kafka | `true` |

## 📊 Dados de Exemplo

O banco de dados é populado automaticamente com os seguintes registros:

| Nº Crédito | Nº NFS-e | Data | Valor ISSQN | Tipo |
|------------|----------|------|-------------|------|
| 123456 | 7891011 | 2024-02-25 | R$ 1.500,75 | ISSQN |
| 789012 | 7891011 | 2024-02-26 | R$ 1.200,50 | ISSQN |
| 654321 | 1122334 | 2024-01-15 | R$ 800,50 | Outros |

## 🔄 Mensageria (Kafka)

Como desafio adicional, foi implementada integração com **Apache Kafka** para auditoria de consultas.

Toda vez que uma consulta é realizada, um evento é publicado no tópico `consulta-credito-topic` contendo:

```json
{
  "tipoConsulta": "NFSE",
  "parametroConsulta": "7891011",
  "dataHoraConsulta": "2024-02-25T10:30:00",
  "quantidadeResultados": 2,
  "sucesso": true
}
```

### Monitorar Eventos no Kafka

**Ver mensagens em tempo real:**
```bash
docker exec -it credito-kafka-dev kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic consulta-credito-topic \
  --from-beginning
```

**Listar tópicos disponíveis:**
```bash
docker exec credito-kafka-dev kafka-topics \
  --bootstrap-server localhost:9092 \
  --list
```

**Ver detalhes do tópico:**
```bash
docker exec credito-kafka-dev kafka-topics \
  --bootstrap-server localhost:9092 \
  --describe \
  --topic consulta-credito-topic
```

## 📝 Documentação da API

A documentação interativa da API está disponível via Swagger UI:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

## 👤 Autor

Desenvolvido como desafio técnico para vaga de Desenvolvedor Java Senior.

## 📄 Licença

Este projeto está sob a licença Apache 2.0.
