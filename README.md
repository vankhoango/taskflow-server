# TaskFlow Server

TaskFlow Server is the companion backend for the **TaskFlow Android** reference application.

It is a small REST API built with **Kotlin and Spring Boot** that provides real networking, persistence, authentication, and authorization scenarios for the Android application.

The backend is intentionally kept relatively small. The primary focus of the TaskFlow project is Android architecture; this server exists so the Android application can interact with a real backend rather than mocked APIs.

> **TaskFlow Android:** [taskflow-android](https://github.com/YOUR_USERNAME/taskflow-android)

---

## Purpose

TaskFlow Server provides the backend infrastructure needed to explore Android concepts such as:

- REST API integration
- Retrofit and OkHttp
- Authentication
- JWT access tokens
- Refresh tokens
- Automatic token refresh
- HTTP error handling
- Network error handling
- Local/remote data synchronization
- Offline-first architecture
- Repository coordination between Room and a remote API

The server is therefore part of the larger TaskFlow architecture:

```text
Android Application
        │
        │ HTTP / JSON
        ▼
TaskFlow Server
        │
        ▼
      SQLite
```

On the Android side:

```text
Compose UI
    │
    ▼
ViewModel
    │
    ▼
Use Cases
    │
    ▼
Repository Interface
    ▲
    │ implements
Repository Implementation
    │
    ├────────────► Room
    │
    └────────────► Retrofit / OkHttp
                         │
                         ▼
                  TaskFlow Server
```

The repository implementation depends on the repository abstraction defined by the domain layer. The diagram above represents architectural dependencies rather than simply runtime call order.

---

## Features

The server currently supports:

- User registration
- User login
- Password hashing
- JWT access tokens
- JWT refresh tokens
- Stateless authentication
- Project CRUD operations
- Task CRUD operations
- Project ownership
- Task ownership
- Clearing completed tasks
- Structured API errors
- SQLite persistence

Projects belong to authenticated users, and tasks belong to projects.

Authorization checks ensure that users can only access their own projects and tasks.

---

## Tech Stack

- Kotlin
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- Hibernate
- SQLite
- JJWT
- Gradle

---

## Project Structure

A simplified view of the project:

```text
taskflow-server/
│
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/example/taskflow/server/
│   │   │       ├── auth/
│   │   │       ├── project/
│   │   │       └── task/
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── LICENSE
└── README.md
```

The base package is:

```text
com.example.taskflow.server
```

---

## API Overview

### Authentication

```http
POST /auth/register
POST /auth/login
POST /auth/refresh
```

Registration creates a new user with a securely hashed password.

Login returns an authentication session containing an access token and refresh token.

The refresh endpoint accepts a valid refresh token and returns a new access token.

---

## Projects

```http
GET    /projects
POST   /projects
GET    /projects/{id}
PATCH  /projects/{id}
DELETE /projects/{id}
```

Project operations require authentication.

Users can only access projects they own.

Deleting a project also removes its associated tasks.

---

## Tasks

Project-scoped operations:

```http
GET    /projects/{projectId}/tasks
POST   /projects/{projectId}/tasks
DELETE /projects/{projectId}/tasks/completed
```

Individual task operations:

```http
GET    /tasks/{id}
PATCH  /tasks/{id}
DELETE /tasks/{id}
```

Tasks are authorized through their owning project.

For example:

```text
User
 │
 └── Project
       │
       └── Task
```

A user cannot access a task belonging to another user's project.

---

## Authentication

TaskFlow uses JWT-based authentication.

Two token types are used:

```text
Access Token
    │
    └── Used to access protected API endpoints

Refresh Token
    │
    └── Used only to obtain a new access token
```

A typical authentication flow is:

```text
POST /auth/login
       │
       ▼
Access Token + Refresh Token
       │
       ▼
Android stores session
       │
       ▼
Authorization: Bearer <access-token>
       │
       ▼
Protected API
```

When the access token expires:

```text
Protected request
       │
       ▼
401 Unauthorized
       │
       ▼
POST /auth/refresh
       │
       ▼
New Access Token
       │
       ▼
Retry original request
```

Access tokens and refresh tokens have different purposes and are validated accordingly.

An access token cannot be used as a refresh token, and a refresh token cannot be used to access normal protected endpoints.

---

## Authorization

Authentication answers:

> Who is making this request?

Authorization answers:

> Is this user allowed to access this resource?

TaskFlow performs resource queries using the authenticated user's identity.

Conceptually:

```text
GET /projects/42

Authenticated User
       │
       ▼
Find project where:
    project.id = 42
        AND
    project.user.id = currentUser.id
```

The same principle applies to tasks through their owning project.

Resources that do not exist or do not belong to the authenticated user are treated as unavailable to that user.

---

## Error Handling

The server uses structured API errors rather than returning arbitrary exception messages to clients.

Conceptually, an error response looks like:

```json
{
  "code": "ERROR_CODE",
  "message": "Human-readable description"
}
```

The Android application maps server error codes into application-specific domain/UI errors rather than displaying arbitrary backend messages directly.

---

## Database

TaskFlow currently uses SQLite.

The database is configured through Spring Data JPA and Hibernate.

During development, the SQLite database is created locally as:

```text
taskflow.db
```

The current project uses Hibernate schema management for development.

For a production application, explicit database migrations using a tool such as Flyway or Liquibase would generally be preferable.

---

## Configuration

Application configuration is located in:

```text
src/main/resources/application.properties
```

Example database configuration:

```properties
spring.datasource.url=jdbc:sqlite:taskflow.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

JWT expiration settings can also be configured:

```properties
auth.jwt.access-token-expiration=900
auth.jwt.refresh-token-expiration=2592000
```

These values are expressed in seconds.

The example above gives the access token a relatively short lifetime while allowing the refresh token to maintain a longer-lived session.

---

## JWT Secret

The JWT signing secret must **not** be committed to the repository.

Configure it through an environment variable rather than putting the actual secret into source control.

For example:

```properties
auth.jwt.secret=your-development-secret
```

Before starting the application, provide the environment variable.

### macOS / Linux

```bash
export TASKFLOW_JWT_SECRET="your-development-secret"
```

### Windows PowerShell

```powershell
$env:TASKFLOW_JWT_SECRET="your-development-secret"
```

Use an appropriately strong secret for your environment.

Never commit real JWT secrets, passwords, access tokens, or refresh tokens.

---

## Running the Server

### Requirements

You will need:

- JDK compatible with the project's Gradle/Spring Boot configuration
- Git

The project includes the Gradle Wrapper, so a separate Gradle installation is not required.

Clone the repository:

```bash
git clone git@github.com:YOUR_USERNAME/taskflow-server.git
cd taskflow-server
```

Configure the JWT secret:

```bash
export TASKFLOW_JWT_SECRET="your-development-secret"
```

Then run:

```bash
./gradlew bootRun
```

On Windows:

```powershell
.\gradlew.bat bootRun
```

By default, the server runs on:

```text
http://localhost:8080
```

---

## Building

Run:

```bash
./gradlew clean build
```

To build the Spring Boot executable JAR:

```bash
./gradlew bootJar
```

The resulting artifact is created under:

```text
build/libs/
```

It can then be run with:

```bash
java -jar build/libs/<generated-jar-name>.jar
```

The SQLite database path is relative to the working directory from which the application is started.

---

## Connecting the Android Application

When using an Android emulator, the development machine can normally be reached through:

```text
http://10.0.2.2:8080/
```

For a physical Android device, use the development machine's LAN address, for example:

```text
http://192.168.x.x:8080/
```

The phone and development machine must be able to communicate over the network.

The current development setup uses HTTP for local development.

A production deployment should use HTTPS.

---

## Security Notes

TaskFlow Server is an educational/reference backend and should not be considered a production-ready authentication system.

A production system would require additional work, including:

- HTTPS
- Production secret management
- JWT signing-key rotation
- Refresh-token rotation
- Refresh-token revocation
- Server-side logout/session invalidation
- Rate limiting
- Abuse protection
- Production database infrastructure
- Explicit database migrations
- Monitoring and auditing
- Production deployment configuration

Sensitive authentication information should never be written to application logs.

---

## Relationship to TaskFlow Android

TaskFlow Server intentionally exists as a **separate repository** from the Android application.

```text
taskflow-android
       │
       │ REST / JSON
       ▼
taskflow-server
```

This separation reflects the actual system boundary:

- Android owns client-side UI and application architecture.
- Spring Boot owns server-side authentication, authorization, persistence, and API behavior.
- HTTP/JSON is the contract between them.

The Android project is the primary architecture showcase.

> **Android repository:** [taskflow-android](https://github.com/YOUR_USERNAME/taskflow-android)

---

## Project Status

TaskFlow Server is being developed alongside the TaskFlow Android application.

Currently implemented:

```text
✓ REST API
✓ SQLite persistence
✓ Project CRUD
✓ Task CRUD
✓ User registration
✓ Password hashing
✓ Login
✓ Spring Security
✓ JWT access tokens
✓ JWT refresh tokens
✓ Token-type validation
✓ Resource ownership
✓ Authentication
✓ Authorization
✓ Structured error handling
✓ Clear-completed-tasks bulk operation
```

The server intentionally remains smaller than the Android application because its primary role is to provide realistic backend behavior for the Android architecture project.

---

## License

Copyright 2026 Khoa Ngo

Licensed under the Apache License, Version 2.0.

See the [LICENSE](LICENSE) file for details.