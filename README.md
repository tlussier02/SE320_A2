# Digital Therapy Assistant

Digital Therapy Assistant is a Spring Boot backend for burnout recovery workflows. It includes a startup CLI, JWT-based authentication, REST endpoints, H2 persistence, AI-assisted CBT features, Swagger/OpenAPI documentation, and JaCoCo coverage reporting.

## Requirements

- Java 17
- Maven 3.9+

## Build

```bash
mvn clean
mvn compile
```

## Run

Start the application with the submission command path:

```bash
mvn exec:java
```

This launches:

- the REST API on `http://localhost:8080`
- the startup CLI in the same terminal session
- the file-based H2 database at `./data/dta-db.mv.db`

## URLs

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- H2 Console: `http://localhost:8080/h2-console/login.jsp`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Submission copies:

- exported OpenAPI JSON: `docs/api/openapi.json`
- preserved JaCoCo report: `docs/reports/jacoco/index.html`
- design document PDF: `Design Document.pdf`

## Seeded Accounts

- `demo@dta.local` / `Password123!`
- `timmy@theraburn.com` / `Password123!`

## CLI Overview

The startup CLI supports:

- Authentication
- CBT Sessions
- Thought Diary
- Progress Dashboard
- Crisis Support
- Settings
- Exit

## Test And Coverage

Run the full test suite:

```bash
mvn test
```

Generated JaCoCo report:

- `target/site/jacoco/index.html`

Preserved submission copy:

- `docs/reports/jacoco/index.html`

The copy under `docs/` is kept outside `target/` because `mvn clean` removes generated build output.

## Important Paths

- controllers: `src/main/java/com/dta/controller`
- services: `src/main/java/com/dta/service` and `src/main/java/com/dta/service/impl`
- entities: `src/main/java/com/dta/entity`
- CLI: `src/main/java/com/dta/cli`
- SQL schema: `src/main/resources/schema.sql`
- SQL seed data: `src/main/resources/data.sql`
- C4 diagrams: `docs/diagrams/c4`
- exported OpenAPI spec: `docs/api/openapi.json`
- preserved JaCoCo report: `docs/reports/jacoco`

## Current Submission Scope

This repository contains the application code, CLI, H2 configuration, Swagger support, schema/data initialization, preserved coverage artifacts, C4 diagram source/exports, and the design document PDF.
