# Money Talks Project — Transaction Service

Микросервис транзакций **Money Talks Project**. Принимает операции пользователя,
проверяет связанные категории и счета, сохраняет транзакцию и доводит исполнение
ордера до конца через сервис счетов.

## Ответственность

- создание транзакций трёх типов: доход, расход, перевод;
- валидация счетов и категорий через `mtp-backend-ms-account` и
  `mtp-backend-ms-cathegories`;
- создание ордера и запуск его исполнения;
- повторные попытки и контроль статуса ордера;
- выдача истории транзакций с названиями счетов и категорий.

## Стек

- Java 21, Spring Boot 4.0.1;
- Spring Web MVC, Spring Data JPA, Liquibase;
- PostgreSQL;
- OpenFeign + OkHttp, Resilience4j CircuitBreaker;
- Lombok, MapStruct, Maven.

## API

| Метод | Путь | Назначение |
|---|---|---|
| `GET` | `/transactions/types` | Справочник типов транзакций |
| `POST` | `/transactions/create` | Создать транзакцию |
| `GET` | `/transactions` | Транзакции текущего пользователя |

Тело создания:

```json
{
  "description": "Перевод на накопительный счёт",
  "type": "TRANSFER",
  "amount": 30,
  "source": "be290141-8062-4aa0-820f-9a6a334df227",
  "target": "b4d54291-ac2b-44bb-bfea-bb531de08668"
}
```

Пользователь определяется заголовком `X-User-ID`, который проставляет gateway.
`amount` должен быть больше нуля.

### Семантика типов

| Тип | `source` | `target` |
|---|---|---|
| `REVENUE` | категория дохода | счёт зачисления |
| `EXPENSE` | счёт списания | категория расхода |
| `TRANSFER` | счёт списания | счёт зачисления |

Перед сохранением сервис проверяет существование и принадлежность пользователю
каждого ресурса. Неизвестный ресурс даёт `ACCOUNT_NOT_FOUND` или
`CATEGORY_NOT_FOUND`.

## Как исполняется операция

1. `TransactionServiceImpl` создаёт `TransactionEntity` и связанный
   `OrderEntity` в статусе `CREATED` и сохраняет их одной транзакцией.
2. `OrderExecutorServiceImpl` переводит ордер в `EXECUTING` и вызывает
   `POST /orders/execute` сервиса счетов.
3. Сервис счетов в транзакции `SERIALIZABLE` меняет балансы и переводит ордер в
   `EXECUTED`.
4. Если вызов не удался, сервис до `ms.order-executor.max-retry-counts` раз
   (по умолчанию 3) опрашивает `POST /orders/status` с паузой
   `ms.order-executor.retry-delay-ms` (по умолчанию 1000 мс):
   - `EXECUTED` — транзакция завершается успешно;
   - `UNREGISTERED` — ордер отправляется на исполнение повторно;
   - `CREATED` — ожидание и повторный опрос.
5. При успехе транзакция и ордер переводятся в `EXECUTED`.

Повторный запуск для транзакции, которая уже в `EXECUTED`, отсекается по статусу.
Важно, что это защита только на этом уровне: сервис счетов не проверяет статус
ордера и при повторном вызове снова изменит балансы.

`GET /transactions` собирает UUID счетов и категорий из всех транзакций
пользователя и запрашивает их названия через `POST /accounts/resources` и
`POST /categories/resources`, после чего отдаёт клиенту объединённую модель.

## Межсервисное взаимодействие

| Feign-клиент | Сервис | Переменная адреса |
|---|---|---|
| `accounts-client` | `mtp-backend-ms-account` | `MS_ACCOUNTS_BASE_URL` |
| `categories-client` | `mtp-backend-ms-cathegories` | `MS_CATEGORIES_BASE_URL` |

Для клиентов включён CircuitBreaker и заданы таймауты (3000 мс на соединение,
6000 мс на чтение). Fallback-фабрики возвращают доменную ошибку с заголовком
`X-Error-Code`, чтобы вызывающий код мог отличить недоступность сервиса от
бизнес-ошибки.

## Данные

Схема по умолчанию — `mtp_transes`, база — `mtp_dev`. Миграции:
`src/main/resources/db/dev/changelog.yml`.

| Таблица | Назначение |
|---|---|
| `trans` | `id` (UUID), `description`, `owner_id`, `source`, `target`, `amount`, `created_at`, `status`, `type`, `order_id` |
| `order` | `id` (UUID), `source`, `target`, `amount`, `status` |

Схема `mtp_transes` должна существовать до первого запуска.

## Конфигурация

| Переменная | Назначение | По умолчанию |
|---|---|---|
| `POSTGRES_URL` | Хост PostgreSQL | `localhost` |
| `POSTGRES_PORT` | Порт PostgreSQL | `5432` |
| `POSTGRES_DB_NAME` | Имя базы | `mtp_dev` |
| `POSTGRES_USER` | Пользователь БД | `postgres` |
| `POSTGRES_PASSWORD` | Пароль БД | dev-значение в `application.yml` |
| `DB_SCHEME_NAME` | Схема сервиса | `mtp_transes` |
| `LIQUIBASE_ENABLED` | Запускать миграции при старте | `true` |
| `MS_CATEGORIES_BASE_URL` | Адрес сервиса категорий | `http://localhost:8081` |
| `MS_ACCOUNTS_BASE_URL` | Адрес сервиса счетов | `http://localhost:8082` |
| `LOGGING_LEVEL` | Уровень логирования | `debug` |

Параметры повторов (`ms.order-executor.*`) заданы в `application.yml`; при
необходимости их можно переопределить средствами Spring Boot.

## Запуск

```bash
cd ../mtp-backend-root && ./mvnw install
cd ../mtp-backend-commons && ./mvnw install
./mvnw spring-boot:run
```

Сервис слушает порт `8084`. Для полноценной работы должны быть доступны
PostgreSQL, сервис счетов и сервис категорий.

Тесты:

```bash
./mvnw test
```

Каталог `http-requests` содержит примеры ручных запросов.

## Связанные репозитории

- [Money-Talks-Project](https://github.com/Sun1tAr/Money-Talks-Project) — обзор и сценарий транзакции;
- [mtp-backend-ms-gateway](https://github.com/Sun1tAr/mtp-backend-ms-gateway);
- [mtp-backend-ms-account](https://github.com/Sun1tAr/mtp-backend-ms-account);
- [mtp-backend-ms-cathegories](https://github.com/Sun1tAr/mtp-backend-ms-cathegories);
- [mtp-backend-commons](https://github.com/Sun1tAr/mtp-backend-commons).
