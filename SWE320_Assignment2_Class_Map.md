# SWE 320 Assignment 2 - Class Map

## Package Layout
- `com.dta` - bootstrap application and CLI
- `com.dta.config` - Spring Boot/security/app configuration
- `com.dta.controller` - API endpoints
- `com.dta.dto.request` - request payloads
- `com.dta.dto.response` - response payloads
- `com.dta.entity` - persistence models
- `com.dta.repository` - repository interfaces
- `com.dta.service` - service contracts (Josh + Trevor + Timmy scope)
- `com.dta.service.impl` - service implementations
- `com.dta.ai` - RAG / AI pipeline components
- `com.dta.security` - auth helpers
- `com.dta.security.filter` - JWT filter
- `com.dta.exception` - shared exceptions
- `com.dta.cli` - CLI startup entry

## Endpoints and Owner Mapping
### Josh (core backend/security)
- `POST /auth/register` -> `AuthController` -> `AuthService`
- `POST /auth/login` -> `AuthController` -> `AuthService`
- `POST /auth/refresh` -> `AuthController` -> `AuthService`
- `POST /auth/logout` -> `AuthController` -> `AuthService`
- Security/filter setup -> `SecurityConfig`, `JwtUtil`, `JwtAuthFilter`

### Timmy (API + DB)
- `GET /sessions` -> `SessionController` -> `SessionService`
- `POST /sessions/{sessionId}/start` -> `SessionController` -> `SessionService`
- `POST /sessions/{sessionId}/chat` -> `SessionController` -> `SessionService`
- `POST /sessions/{sessionId}/end` -> `SessionController` -> `SessionService`
- `POST /diary/entries` -> `DiaryController` -> `DiaryService`
- `GET /diary/entries` -> `DiaryController` -> `DiaryService`
- `DELETE /diary/entries/{id}` -> `DiaryController` -> `DiaryService`
- `POST /diary/distortions/suggest` -> `DiaryController` -> `DiaryService`
- `GET /progress/weekly` -> `ProgressController` -> `ProgressService`
- `GET /progress/monthly` -> `ProgressController` -> `ProgressService`
- `GET /progress/burnout` -> `ProgressController` -> `ProgressService`
- `GET /crisis` -> `CrisisController` -> `CrisisService`
- `POST /crisis/detect` -> `CrisisController` -> `CrisisService`
- `GET /crisis/safety-plan` -> `CrisisController` -> `CrisisService`
- `PUT /crisis/safety-plan` -> `CrisisController` -> `CrisisService`

### Trevor (AI + RAG)
- AI orchestration methods in `AiService`
- RAG helpers in `RagContextBuilder`, `KnowledgeBaseLoader`, `CrisisDetector`
- AI flow: `sessionService.chat` -> `AiServiceImpl.generateResponse` -> RAG components -> response

## Data model (minimum entities)
- `User` 1-* `UserSession`
- `User` 1-* `DiaryEntry`
- `UserSession` 1-* `ChatMessage`
- `DiaryEntry` *-* `CognitiveDistortion`
- `TrustedContact` tied to system/user context

## Delivery Checklist
- Add full implementations for each service method.
- Replace in-memory skeletons with JPA repositories/entities.
- Add tests (CLI, service, API), JaCoCo config, Swagger config.
