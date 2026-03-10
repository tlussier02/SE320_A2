# SE 320 Assignment 2 Implementation Roadmap (Detailed)

## Purpose
Use this as the authoritative build order for the Digital Therapy Assistant skeleton and implementation so everyone works in the same sequence without stepping on each other.

## Ground Rules
- Work only in `dta-skeleton` and commit in small, reviewable chunks.
- Prefer implementing one service layer component at a time.
- Keep API contracts stable until controller + tests are done.
- Update TODO markers as they are replaced by concrete implementation.
- After each checkpoint: run at least a quick build mentally by scanning for obvious compile blockers in your own modified files.

## Repository / Branch Strategy
1. Use a branch per subsystem:
   - `josh/core-security`
   - `trevor/ai-rag`
   - `timmy/api-db-tests`
2. Merge order by dependency:
   - Core config + entities/repositories first
   - Auth/session services next
   - API endpoints and DTO contracts
   - AI/RAG wiring
   - Tests + CI

## Prerequisites and Setup
1. Verify Java + Maven working and project boots from skeleton:
   - `mvn -q -DskipTests compile`
2. Verify files exist under expected package paths from class map:
   - `com.dta.*` packages for config, controller, service, repository, entity, ai, cli, dto, exception
3. Confirm `.yml` has:
   - H2 DB URL
   - JPA auto DDL preference or migration path
   - JWT secret and expiry

## Dependency Map (what must exist before what)
1. Security foundation before `AuthService` and controller auth endpoints.
2. Entities/repositories before session/chat/diary services that persist state.
3. DTO contracts before controller response assertions.
4. AI interfaces before controllers invoking response generation.
5. Tests after interfaces and service behavior are stable.

## Phase 1 — Core skeleton hardening (Josh)
### 1.1 Application bootstrap
- [ ] Confirm `DigitalTherapyAssistantApplication` starts and CLI bootstraps.
- [ ] Validate bean scan includes all package roots.
- [ ] Add startup checks/logging for missing beans (optional but useful).

### 1.2 Security layer
- [ ] Implement `JwtUtil`:
  - token creation
  - token parsing/validation
  - username extraction
- [ ] Implement `PasswordEncoderAdapter` wrapper around BCrypt usage.
- [ ] Implement `UserDetailsServiceImpl` lookup from repository.
- [ ] Implement `JwtAuthFilter` to:
  - read bearer token
  - validate and populate SecurityContext
- [ ] Implement `SecurityConfig`:
  - public auth endpoints (`/auth/*`, docs, h2)
  - JWT filter ordering
  - stateless session policy

### 1.3 Global exception behavior
- [ ] Implement exception classes shape and payload structure.
- [ ] Implement a global handler mapping:
  - validation errors
  - auth failures
  - resource not found
  - generic API errors

## Phase 2 — Data layer (Timmy)
### 2.1 Domain entities
- [ ] Implement fields + JPA annotations:
  - `User`
  - `UserSession`
  - `CbtSession`
  - `ChatMessage`
  - `DiaryEntry`
  - `CognitiveDistortion`
  - `TrustedContact`
- [ ] Define relationships and fetch strategy:
  - `User -> UserSession`
  - `User -> DiaryEntry`
  - `CbtSession -> ChatMessage`
  - `DiaryEntry -> CognitiveDistortion`
- [ ] Add `@CreationTimestamp` / updated fields where appropriate.

### 2.2 Repository methods
- [ ] Add query methods for required access patterns:
  - session list by user and status
  - diary entries by user/date range
  - progress aggregates by date/time window
  - safety plan fetch by user
- [ ] Keep methods named semantically and return optional or list appropriately.

### 2.3 DB scripts
- [ ] Finalize `schema.sql` for tables, keys, indexes, FK constraints.
- [ ] Finalize `data.sql` for seed users / lookup rows if required.
- [ ] Ensure H2 console works with expected JDBC URL.

## Phase 3 — Auth + session services (Josh + Timmy)
### 3.1 `AuthService` implementation
- [ ] Implement `register`:
  - duplicate check
  - password policy
  - password hashing
  - persist user
  - return `AuthResponse`
- [ ] Implement `login`:
  - credentials validation
  - issue access + refresh tokens
- [ ] Implement `refresh` and `logout`:
  - token rotation rules
  - response with new token pair
  - invalidate old refresh/session if tracked

### 3.2 `SessionService` implementation
- [ ] Implement `startSession` with mood/mode from request.
- [ ] Implement `chat` to append message and request AI response path.
- [ ] Implement `endSession` state transition + timestamp persist.
- [ ] Make service return `SessionResponse` fields expected by team (sessionId/state and lifecycle metadata).

## Phase 4 — Diary + progress services (Timmy)
### 4.1 `DiaryService`
- [ ] Implement `createDiaryEntry` with validation and persistence.
- [ ] Implement listing with optional paging/filter semantics.
- [ ] Implement delete with ownership validation.
- [ ] Implement distortion suggestion hook to AI service.

### 4.2 `ProgressService`
- [ ] Implement `getWeeklyProgress` windowed aggregation.
- [ ] Implement `getMonthlyProgress` aggregation.
- [ ] Implement `getBurnoutScore` from available signals.
- [ ] Return typed values matching `ProgressResponse`.

## Phase 5 — AI + RAG services (Trevor)
### 5.1 AI service contract
- [ ] Implement `AiService` methods:
  - `generateResponse`
  - `analyzeThought`
  - `generateReframingPrompts`
  - `detectCrisis`
  - `generateInsights`
  - `summarizeSession`
- [ ] Keep method boundaries pure where possible for easier test coverage.

### 5.2 AI helper classes
- [ ] Implement `KnowledgeBaseLoader` (load local CBT knowledge resources).
- [ ] Implement `RagContextBuilder`:
  - retrieve relevant knowledge
  - include recent session context
  - return compact context payload
- [ ] Implement `CrisisDetector` with deterministic rules and escalation signals.

### 5.3 Safety handling
- [ ] Make sure crisis detection returns:
  - `isCrisis`
  - action string/recommendation text
  - trigger fields needed by controller response

## Phase 6 — Controller layer wiring (Timmy)
### 6.1 API endpoints
- [ ] Implement `AuthController` for:
  - `POST /auth/register`
  - `POST /auth/login`
  - `POST /auth/refresh`
  - `POST /auth/logout`
- [ ] Implement `SessionController` for:
  - `GET /sessions`
  - `POST /sessions/{sessionId}/start`
  - `POST /sessions/{sessionId}/chat`
  - `POST /sessions/{sessionId}/end`
- [ ] Implement `DiaryController` for diary CRUD and distortion suggestion.
- [ ] Implement `ProgressController` for weekly/monthly/burnout endpoints.
- [ ] Implement `CrisisController` for crisis detection and safety plan endpoints.

### 6.2 DTO cleanup
- [ ] Remove placeholder DTO-only comments after implementation.
- [ ] Ensure request validation annotations are present.
- [ ] Standardize response fields and HTTP status codes.

## Phase 7 — Crisis plan and CLI touchpoints
### 7.1 Crisis workflow
- [ ] Wire GET/PUT `/crisis/safety-plan` to persistence.
- [ ] Return crisis action + escalation guidance.
- [ ] Add trusted contact usage path.

### 7.2 CLI
- [ ] Ensure `DtaCli` and `StartupBanner` either launch with minimal path or stay no-op-safe.
- [ ] Keep startup non-blocking for API tests.

## Phase 8 — Testing (Timmy ownership, all review)
### 8.1 Unit tests
- [ ] Services: auth/session/diary/progress/crisis/ai helpers.
- [ ] Mock repositories and dependencies first, no network calls.

### 8.2 Integration tests
- [ ] Controller tests with mocked auth and JWT as needed.
- [ ] Validate response payloads and status codes.
- [ ] Add DB integration tests for repository relationships.

### 8.3 CI and coverage
- [ ] Add/maintain workflow with:
  - `mvn test`
  - JaCoCo report generation
  - minimum coverage gate (set realistic threshold for initial milestone)
- [ ] Add badge/readme note for pipeline URL and last run.

## Definition of Done (for each component)
- Controller endpoint works end-to-end.
- Service logic documented with TODO removed or replaced.
- Repository methods used and tested.
- Error paths return meaningful HTTP codes.
- No uncaught null ownership/security flow bugs.
- Tests updated in same scope.

## Suggested sprint split
### Day 1
- Josh: security/config + auth service stubs implementation
- Timmy: entities/repositories/schema + diary/progress foundations
- Trevor: AI interface + data loader pipeline skeleton

### Day 2
- Josh: session lifecycle + integration with controller
- Timmy: controllers for session + diary + crisis safety endpoints
- Trevor: AI response/reframe + crisis detector

### Day 3
- Timmy: tests + cleanup + CI + final bug bash
- All: merge reviews, conflict resolution, final runbook check

## Common pitfalls to avoid
- Don’t expose sensitive token payloads in response DTOs.
- Don’t skip validation on AI-triggering endpoints.
- Don’t assume repository data exists without ownership checks.
- Don’t return session state only; include lifecycle metadata for downstream clients.
- Don’t bypass crisis escalation logic in chat responses.

## Final validation checklist before submit
- `mvn clean`
- `mvn test`
- `mvn exec:java`
- Verify these endpoints locally:
  - `/swagger-ui/index.html`
  - `/h2-console/login.jsp`
- Confirm README includes run instructions and endpoint list.
