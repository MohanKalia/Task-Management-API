# Mohan Task App

A JSON REST API for managing tasks. Create a task with a priority and an optional due date, list tasks in creation order, and mark them complete as work gets done.

Built with Java 25, Spring Boot 4.2, Spring MVC, Spring Data JPA, and Jakarta Bean Validation.

## Quick start

Install **JDK 25** and make sure `java -version` reports Java 25. The included Maven Wrapper manages Maven.

```sh
git clone https://github.com/MohanKalia/Task-Management-API.git
cd Task-Management-API
./mvnw spring-boot:run
```

On Windows, run `mvnw.cmd spring-boot:run` from Command Prompt or `.\mvnw.cmd spring-boot:run` from PowerShell.

The API runs at `http://localhost:8080/api/v1/tasks`. Spring Boot starts an in-memory H2 database automatically. Tasks are cleared when the app stops.

## Try the API

In another terminal, create a task and then list all tasks.

```sh
curl -i http://localhost:8080/api/v1/tasks \
  -H 'Content-Type: application/json' \
  -d '{"title":"Outline the release notes","description":"Summarize the API changes","priority":"HIGH"}'

curl -i http://localhost:8080/api/v1/tasks
```

The create request returns `201 Created` with the new task.

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

Copy the generated `id` from your response into the URL below to mark your task complete.

```sh
curl -i -X PUT 'http://localhost:8080/api/v1/tasks/<task-id>' \
  -H 'Content-Type: application/json' \
  -d '{"title":"Outline the release notes","description":"Summarize the API changes","priority":"HIGH","status":"COMPLETE"}'
```

## API reference

| Method | Path | Behavior |
| --- | --- | --- |
| `POST` | `/api/v1/tasks` | Creates an `OPEN` task and returns it with `201 Created`. |
| `GET` | `/api/v1/tasks` | Returns `200 OK` and a JSON array ordered from oldest to newest. An empty list returns `[]`. |
| `PUT` | `/api/v1/tasks/{taskId}` | Replaces the editable fields and returns the task with `200 OK`. `taskId` is a UUID. |
| `DELETE` | `/api/v1/tasks/{taskId}` | Currently returns `500 Internal Server Error` because the controller fails to bind `taskId`. |

### Request fields

| Field | Required | Rules |
| --- | --- | --- |
| `title` | `POST` and `PUT` | Nonblank string, up to 255 characters. |
| `description` | Optional | String, up to 1,000 characters. |
| `dueDate` | Optional | Date in `YYYY-MM-DD` format. Use today or a future date. |
| `priority` | `POST` and `PUT` | `LOW`, `MEDIUM`, or `HIGH`. |
| `status` | `PUT` only | `OPEN` or `COMPLETE`. |

On `PUT`, omitting `description` or `dueDate` clears that field. The current implementation initializes both timestamps at creation and leaves `updated` at its original value after edits.

Request field validation failures and updates for an unknown UUID return `400 Bad Request` with an `error` string. For example, a request with a blank title can return this body.

```json
{
  "error": "Title must be between 1 and 255 characters."
}
```

## MySQL for local development

The project includes MySQL Connector/J. To keep tasks across restarts, create a `task_app` database in MySQL and use a development account with permission to manage its schema. Start the app with the following environment variables, replacing the example credentials. Hibernate creates or updates the `tasks` table in the existing database.

```sh
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/task_app'
export SPRING_DATASOURCE_USERNAME='task_user'
export SPRING_DATASOURCE_PASSWORD='replace-with-your-password'
export SPRING_JPA_HIBERNATE_DDL_AUTO='update'
./mvnw spring-boot:run
```

## Test and build

```sh
./mvnw test
./mvnw package
java -jar target/task-0.0.1-SNAPSHOT.jar
```

The included test checks that the Spring application context starts. Run the JAR after stopping any development server using port `8080`.

## Project layout

The application code lives under [`src/main/java/org/mohan/task`](src/main/java/org/mohan/task).

| Package | Responsibility |
| --- | --- |
| [`controller`](src/main/java/org/mohan/task/controller) | Maps HTTP requests and handles validation and task lookup errors. |
| [`service`](src/main/java/org/mohan/task/service) | Applies task defaults and coordinates persistence. |
| [`repository`](src/main/java/org/mohan/task/repository) | Accesses tasks through Spring Data JPA. |
| [`domain`](src/main/java/org/mohan/task/domain) and [`mapper`](src/main/java/org/mohan/task/mapper) | Define task entities, enums, and request and response types, and map between them. |
