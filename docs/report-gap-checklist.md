# Report Gap Checklist

This checklist is based on:

- `Assignment 2 - 320 Digital Therapy Assistant Requirements (1).pdf`
- `Assignment 2 - 320  Digital Therapy Assistant Solution Summary (3).pdf`

It is meant to show what is still needed in the final report document, not just what is left in the codebase.

## What Timmy's Draft Already Covers

- team members and ownership areas
- high-level section structure for diagrams, solution contracts, API, QA, and JaCoCo
- a short list of example endpoints
- a short list of example QA scenarios
- a starting point for security, persistence, AI/RAG, crisis safety, and progress contracts

## What Still Needs To Be Added To The Report

### 1. Replace Placeholder Diagram Sections With Real Artifacts

- insert the actual context diagram
- insert the actual container diagram
- insert the actual component diagram
- insert the actual class diagram
- insert the actual sequence diagram(s)
- insert the actual ER diagram
- add short captions explaining what each diagram shows

### 2. Expand The API Section To Cover The Full Assignment Scope

The current PDF only lists a small subset of required endpoints. The report should include the full REST surface:

- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`
- `GET /sessions`
- `GET /sessions/{sessionId}`
- `POST /sessions/{sessionId}/start`
- `POST /sessions/{sessionId}/chat`
- `POST /sessions/{sessionId}/end`
- `GET /diary/entries`
- `POST /diary/entries`
- `GET /diary/entries/{entryId}`
- `DELETE /diary/entries/{entryId}`
- `POST /diary/distortions/suggest`
- `GET /diary/insights`
- `GET /progress/weekly`
- `GET /progress/monthly`
- `GET /progress/burnout`
- `GET /progress/achievements`
- `GET /crisis`
- `GET /crisis/coping-strategies`
- `POST /crisis/detect`
- `GET /crisis/safety-plan`
- `PUT /crisis/safety-plan`

For each endpoint, the report should state:

- method and route
- purpose
- request fields or path/query parameters
- success response shape
- status codes
- validation or authorization notes
- JIRA mapping if the team wants traceability in the report

### 3. Make Solution Contracts Concrete

The current contract section is still too general. It should be revised to show:

- what the contract is
- which classes or modules satisfy it
- how the implementation behaves
- any important tradeoffs or limits

Contracts that should be explicitly tied to the implementation:

- JWT authentication and request filtering
- H2 persistence and schema initialization
- CLI command pattern and service injection
- AI/RAG context building and knowledge loading
- crisis detection and escalation behavior
- OpenAPI generation and Swagger UI exposure
- progress summary calculations

### 4. Add A Real QA Section Instead Of A Short Sample Table

The QA section should include more than five example tests. It should cover:

- unit tests
- integration tests
- smoke tests
- authentication failure cases
- validation errors
- crisis detection cases
- diary lifecycle cases
- session lifecycle cases
- progress summary cases

It should also state:

- what was mocked
- what used the database
- what pass criteria were used
- where coverage was measured from

### 5. Replace "JaCoCo Screenshot" Placeholder With Real Coverage Content

The report should include:

- actual line and branch coverage values
- a screenshot or exported figure from the generated JaCoCo report
- a short note on strongest-covered packages
- a short note on weakest-covered packages and planned follow-up

### 6. Add The Required Code-Level Detail For Part 6

The requirements call for more than a single generic class and sequence section. The report should explicitly include:

- class diagram for the AI service module
- sequence diagram for chat message flow
- sequence diagram for diary entry analysis or reframing flow

### 7. Add Database Coverage In The Report Body

The report should explain:

- main entities
- key relationships
- how UUID identifiers are used
- soft delete behavior for diary entries
- how seed data supports demo and testing

### 8. Add A Short "How To Run" Or Evidence Section

Even if the README exists separately, the report benefits from a short proof section covering:

- Maven run command
- Swagger URL
- H2 console URL
- example seeded login
- note that the CLI starts with the application

## Repo-Backed Items The Report Can Reference

These repo artifacts can be cited or embedded into the final report:

- `README.md`
- `src/main/resources/schema.sql`
- `src/main/resources/data.sql`
- `src/main/resources/application.yml`
- `docs/diagrams/c4/`
- `target/site/jacoco/index.html`

## Recommended Next Report Tasks

1. Embed the real diagrams and add captions.
2. Expand the API table to the full endpoint list.
3. Replace the contract bullets with implementation-mapped explanations.
4. Replace the QA sample table with a fuller test matrix.
5. Insert real JaCoCo metrics and screenshot evidence.
6. Add a short run/demo evidence section at the end.

## If You Give Me Edit Access

If you share the editable source document, I can:

- turn this checklist into final report wording
- expand the API section directly
- write the missing solution-contract text
- draft the QA and coverage sections from the repo state
- align the report wording with the actual implementation files
