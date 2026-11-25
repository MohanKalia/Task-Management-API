# Mohan Task App

Mohan Task App is a JSON REST API for tracking tasks. Create tasks with a priority and an optional due date, list them in creation order, and mark them complete. The API runs locally with an in-memory database and includes Swagger UI for trying requests in a browser.

**Stack** · Java 25 · Spring Boot 4.2 · Spring Data JPA · H2 or MySQL

[Quick start](#quick-start) · [Try the API](#try-the-api) · [API reference](#api-reference) · [Database configuration](#database-configuration) · [Development](#development)

## Quick start

Install **JDK 25** and check that `java -version` reports version 25. The Maven Wrapper downloads Maven and the project dependencies on first use.

```sh
git clone https://github.com/MohanKalia/Task-Management-API.git
cd Task-Management-API
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run` in Command Prompt or `.\mvnw.cmd spring-boot:run` in PowerShell.

| Local URL | What it serves |
| --- | --- |
| [Swagger UI](http://localhost:8080/swagger-ui/index.html) | Browse the endpoints and send requests. |
| [OpenAPI JSON](http://localhost:8080/v3/api-docs) | Import the API description into an HTTP client. |
| [Tasks](http://localhost:8080/api/v1/tasks) | List tasks. A fresh instance returns `[]`. |

H2 starts automatically. Tasks are cleared when the app stops.

## Try the API

Enable **Try it out** for `POST /api/v1/tasks` in Swagger UI, or run the following commands in a second terminal with `curl`.

### Create a task

```sh
curl -i http://localhost:8080/api/v1/tasks \
  -H 'Content-Type: application/json' \
  -d '{"title":"Outline the release notes","description":"Summarize the API changes","priority":"HIGH"}'
```

The API returns `201 Created` and a task like this one.

```json
{
  "id": "95103fc9-4d2c-428a-a1f2-0718271de27d",
  "title": "Outline the release notes",
  "description": "Summarize the API changes",
  "dueDate": null,
  "status": "OPEN",
  "priority": "HIGH",
  "created": "2026-11-25T08:30:00Z",
  "updated": "2026-11-25T08:30:00Z"
}
```

### Mark it complete

Paste the `id` from your create response into `task_id`. The update returns `200 OK` with `status` set to `COMPLETE`. The list request shows the saved task.

```sh
task_id='paste-your-task-id-here'
curl -i -X PUT "http://localhost:8080/api/v1/tasks/${task_id}" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Outline the release notes","description":"Summarize the API changes","priority":"HIGH","status":"COMPLETE"}'

curl -i http://localhost:8080/api/v1/tasks
```

## API reference

| Method | Path | Response |
| --- | --- | --- |
| `POST` | `/api/v1/tasks` | `201 Created` with the new task. Its initial status is `OPEN`. |
| `GET` | `/api/v1/tasks` | `200 OK` with a JSON array ordered by `created`, oldest first. |
| `PUT` | `/api/v1/tasks/{taskId}` | `200 OK` with the task after replacing its editable fields. |
| `DELETE` | `/api/v1/tasks/{taskId}` | Currently returns `500 Internal Server Error` because the handler fails to bind `taskId`. |

### Request fields

Send POST and PUT bodies as JSON. Use the task UUID for `{taskId}`.

| Field | POST | PUT | Accepted value |
| --- | --- | --- | --- |
| `title` | Required | Required | Nonblank string, up to 255 characters. |
| `description` | Optional | Optional | String up to 1,000 characters, or `null`. |
| `dueDate` | Optional | Optional | Today or a future date in `YYYY-MM-DD` format, or `null`. |
| `priority` | Required | Required | `LOW`, `MEDIUM`, or `HIGH`. |
| `status` | Set by server | Required | `OPEN` or `COMPLETE`. |

`PUT` replaces the editable fields. Resend any optional values you want to keep. Omitting `description` or `dueDate` clears it. The due date rule also applies when updating an existing task.

The server generates the UUID and sets both UTC timestamps at creation. `updated` currently stays at its initial value after edits.

### Errors

Field validation returns `400 Bad Request` with the first field error in an `error` string. A blank title, for example, produces the following body.

```json
{
  "error": "Title must be between 1 and 255 characters."
}
```

An update for an unknown UUID also returns `400 Bad Request`, with `Task with id <uuid> not found` as the error message.

## Database configuration

Use the default in-memory H2 database for a quick local run. To keep tasks across restarts, run the following SQL as a local MySQL administrator. Replace the example password in both snippets.

```sql
CREATE DATABASE task_app;
CREATE USER 'task_user'@'localhost' IDENTIFIED BY 'your-local-password';
GRANT ALL PRIVILEGES ON task_app.* TO 'task_user'@'localhost';
```

Set the datasource variables and start the app from the same terminal.

```sh
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/task_app'
export SPRING_DATASOURCE_USERNAME='task_user'
export SPRING_DATASOURCE_PASSWORD='your-local-password'
export SPRING_JPA_HIBERNATE_DDL_AUTO='update'
./mvnw spring-boot:run
```

MySQL Connector/J is included. With `SPRING_JPA_HIBERNATE_DDL_AUTO=update`, Hibernate manages the `tasks` table inside the existing database. To switch back to H2, clear the four variables before starting the app.

## Development

```sh
./mvnw test
./mvnw package
java -jar target/task-0.0.1-SNAPSHOT.jar
```

`test` runs the Spring context smoke test. `package` also runs the test and builds an executable JAR. If a development server is already listening on port `8080`, stop it before launching the JAR or set `SERVER_PORT=8081`.

### Project layout

Application code lives under [`src/main/java/org/mohan/task`](src/main/java/org/mohan/task). Requests enter the controller, which maps JSON DTOs to service requests. The service applies the task defaults and calls the JPA repository.

| Path | Responsibility |
| --- | --- |
| [`controller`](src/main/java/org/mohan/task/controller) | HTTP endpoints and error responses. |
| [`service`](src/main/java/org/mohan/task/service) | Task creation, ordering, updates, and persistence coordination. |
| [`repository`](src/main/java/org/mohan/task/repository) | Spring Data JPA access to the `tasks` table. |
| [`domain`](src/main/java/org/mohan/task/domain) and [`mapper`](src/main/java/org/mohan/task/mapper) | Task entity, enums, request and response types, and conversions. |
| [`application.properties`](src/main/resources/application.properties) | Application configuration. |
