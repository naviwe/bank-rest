# Тестовое задание

🚀 **Bank Cards REST API** — твой личный банковский симулятор в кармане!
Выпуск карт, переводы между ними, блокировка, админ-магия — всё через удобный REST + JWT + Swagger.

### Что понадобится (минимум)

- Java 21
- Maven
- Docker (самый простой путь для базы)

### Самый быстрый и красивый способ (с Docker) — 3 команды

1. Запускаем PostgreSQL в один клик→ База уже ждёт на порту 5433 (postgres/postgres, база card_management)
    
    Bash
    
    `docker-compose up -d`
    
2. Собираем проект
    
    Bash
    
    `mvn clean package`
    
3. Взлетаем!или ещё проще:
    
    Bash
    
    `java -jar target/bank_rest-0.0.1-SNAPSHOT.jar`
    
    Bash
    
    `mvn spring-boot:run`
    

Через 5–10 секунд сервер уже живёт на:
🌐 [**http://localhost:8080**](http://localhost:8080/)

### Куда бежать первым делом?

- 📖 **Swagger UI** — самая красивая документация:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
(там можно сразу регистрироваться, логиниться и тыкать все эндпоинты)
- 🔑 Зарегистрироваться: POST /auth/register
- 🔐 Войти и получить токен: POST /auth/login

### Ключевые эндпоинты одним взглядом

🔓 **Открытые**

- POST /auth/register
- POST /auth/login

👤 **Обычный пользователь (ROLE_USER)**

- GET /cards → мои карты
- GET /cards/{id}/balance → баланс
- POST /cards/transfer → перевод между своими картами
- PATCH /cards/{id}/request-block → прошу заблокировать

🛡️ **Админ (ROLE_ADMIN)**

- POST /admin/cards/{userId} → выпустить карту пользователю
- PATCH /admin/cards/{id}/block → заблокировать
- PATCH /admin/cards/{id}/activate → активировать
- GET /admin/users → все юзеры
- PATCH /admin/users/{id}/role → поменять роль

### Без Docker? Легко!

1. Запусти локальный PostgreSQL
2. Создай базу: card_management
3. В application.yml подправь порт, если нужно (обычно 5432)
4. И просто: mvn spring-boot:run

### Быстрые команды на каждый день

Bash

`mvn test                # проверить, что ничего не сломалось
mvn clean install       # свежая сборка
docker-compose down     # выключить базу, когда надоело`

Готово!
Запускай, регистрируйся, создавай карты, переводи деньги и наслаждайся → твой мини-банк уже работает! 💳✨