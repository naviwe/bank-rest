# Bank Cards REST API

### 🏦   Secure & Modern Banking Backend

A production-ready RESTful API for managing users, bank cards and internal transfers.

## ✨ Core Features

Authentication
- Register new users → /auth/register
- Login & get JWT → /auth/login
- Stateless JWT authentication
- Role-based access: USER / ADMIN

User Zone (ROLE_USER)
- List all your cards (paginated) → GET /cards
- View masked card number (**** **** **** 1234)
- Check balance → GET /cards/{id}/balance
- Request card block → PATCH /cards/{id}/request-block
- Transfer money between your own cards → POST /cards/transfer
- Protected with pessimistic locking

Admin Zone (ROLE_ADMIN)
- Create card for any user → POST /admin/cards/{userId}
- Block / Activate / Delete card → PATCH/DELETE /admin/cards/{id}
- List all users → GET /admin/users
- List all cards → GET /admin/cards
- Change user role → PATCH /admin/users/{id}/role
- Delete user (except admins) → DELETE /admin/users/{id}

Security & Protection Highlights

✓ Card numbers encrypted in database (AES-256)

✓ Only last 4 digits exposed in responses

✓ Passwords stored with BCrypt

✓ JWT with expiration & signature validation

✓ Input validation (Jakarta Bean Validation)

✓ Custom exceptions & meaningful HTTP responses

✓ Pessimistic WRITE lock on concurrent transfers

✓ No plain card numbers ever logged or returned

### 🛠 Technology Stack

Core
- Java 21
- Spring Boot 3.2+
- Spring Security + JWT (jjwt 0.11.5)
- Spring Data JPA + Hibernate

Database
- PostgreSQL 16
- Liquibase (YAML migrations)

Mapping & Utilities
- MapStruct 1.5.5 (clean DTO ↔ Entity)
- Lombok
- Custom AES encryption utility
- Card number generator

API & Docs
- springdoc-openapi 2.7.0 → Swagger UI
- Jakarta Validation

Testing & Quality
- JUnit 5 + Spring Boot Test
- Proper exception handling
- Input sanitization

DevOps
- Multi-stage Dockerfile (Temurin 21)
- docker-compose with healthchecks
- Ready for CI/CD

### 🚀 Quick Start – Docker Way (Recommended)

git clone [https://github.com/naviwe/bank-rest.git](https://github.com/naviwe/bank-rest.git)

cd bank-rest-api

docker compose up --build

---

After start:
- Application      [http://localhost:8080](http://localhost:8080/)
- Swagger UI       [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- PostgreSQL       localhost:5433   (postgres / postgres)

---

First actions:

1. Register → POST /auth/register { "username": "...", "password": "..." }
2. Login → POST /auth/login → copy token
3. Use header: Authorization: Bearer {token}
4. Create admin manually if needed (or via DB)

### 📁 Project Layout

com.example.bankcards
├── config           → SecurityConfig, CorsConfig, beans

├── controller       → AuthController, CardController, AdminController

├── dto              → Request/Response DTOs & records

├── entity           → User, Card, Transaction, enums

├── exception        → Custom exceptions

├── repository       → JPA repositories with pessimistic locks

├── service          → AuthService, CardUserService, CardAdminService, UserService

├── security         → JwtFilter, JwtService, UserDetailsService

└── util             → CryptoUtil (AES), CardNumberGenerator

### 💭 Why I Built This

To deeply practice and showcase:

- Secure auth & role management in Spring Security
- Encryption of sensitive data at rest
- Safe concurrent money transfers
- Clean REST API design with OpenAPI
- Container-ready application
- Proper error handling & validation

Great for:
- Portfolio demonstration
- Job interviews
- Learning advanced Spring Boot patterns
- Code review discussions

Feedback — very welcome!

Let's make it even better together!