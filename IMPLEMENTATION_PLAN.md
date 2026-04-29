# OpSpot — Phase-wise Implementation Plan

**Stack:** Spring Boot 3.4.x · Gradle · Java 21 · Spring Web · Spring Data JPA · Thymeleaf · MySQL 8 · Lombok · Tailwind CDN  
**Mode:** Single-user, local. JSON import via UI textarea (no live Gemini call). `ddl-auto=create-drop` until project completion. Dockerization in final phase.

---

## Phase 1 — Project Bootstrap & Local DB
**Goal:** Spring Boot app boots, connects to MySQL running in Docker.

- [ ] Init Gradle Spring Boot 3.4.x project, package `com.opspot`
- [ ] Dependencies: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-thymeleaf`, `mysql-connector-j`, `lombok`, `spring-boot-starter-validation`
- [ ] `application.properties` — DB URL `jdbc:mysql://localhost:3306/opspot`, user/pass, `ddl-auto=create-drop`, `show-sql=true`, server port 8080
- [ ] `docker-compose.dev.yml` — MySQL 8 only (app runs locally via `./gradlew bootRun` for fast iteration)
- [ ] Health endpoint `GET /api/health` returning `{status: "ok"}`

**Acceptance:** `docker compose -f docker-compose.dev.yml up -d` → `./gradlew bootRun` → `/api/health` returns 200. ✅

---

## Phase 2 — Domain Layer (Enums, Entities, Repositories)
**Goal:** All 4 module tables auto-created on startup.

**Enums:** `EventType`, `ApplicationStatus`, `WorkMode`

**Entities** (all share: `id`, `status` (default `SAVED`), `createdAt`, `updatedAt`, unique link):

| Entity | Key fields |
|---|---|
| `Event` | title, description, organizer, eventType, workMode, city, theme, startDate, endDate, registrationDeadline, professionalAllowed, **registrationLink (unique)** |
| `Job` | title, company, location, workMode, skills (CSV), experienceMin, experienceMax, salaryRange, description, **jobLink (unique)** |
| `CourseOffer` | title, platform, topic, originalPrice, discountedPrice, isFree, validTill, description, **offerLink (unique)** |
| `AIToolOffer` | toolName, offerTitle, isFree, forProfessionals, validTill, description, **offerLink (unique)** |

**Repositories:** Spring Data JPA with `existsByXLink(...)`, `findByXLink(...)`, and Spring `Specification` support for dynamic filters.

**Acceptance:** App startup creates all 4 tables; verified via `SHOW TABLES` in MySQL. ✅

---

## Phase 3 — JSON Schemas & Import Service
**Goal:** Define the JSON contract for Gemini prompt templates; backend imports it.

- [ ] 4 JSON schema files in `docs/json-schemas/` (events.json, jobs.json, course-offers.json, ai-tool-offers.json)
- [ ] Generic `ImportService<T>` with per-module mappers
- [ ] Per item: check `existsByXLink` → skip if dupe, insert if new (status defaults to `SAVED`)
- [ ] Return `ImportSummary { inserted, skipped, total }`

**Generic JSON shape:**
```json
{ "items": [ { "...module-specific fields..." } ] }
```

**Acceptance:** Import same JSON twice — first run inserts N, second run skips N. ✅

---

## Phase 4 — Events Module (REST APIs)
**Goal:** Full CRUD-lite for events.

- [ ] `POST /api/events/import` — body: raw JSON → returns `ImportSummary`
- [ ] `GET /api/events` — query params: `city`, `eventType`, `workMode`, `theme`, `startDateFrom`, `startDateTo`, `professionalAllowed`, `showRejected` (default false) → `List<EventResponseDTO>`
- [ ] `PATCH /api/events/{id}/status` — body `{ "status": "APPLIED" | "REJECTED" }` → updated DTO
- [ ] DTOs: `EventResponseDTO`, `StatusUpdateRequest`, `ImportRequest`, `ImportSummary`
- [ ] Global `@RestControllerAdvice` for error responses (404, 400, 500)

**Acceptance:** Import sample JSON, GET with filters, PATCH status, GET with `showRejected=false` confirms rejected items hidden. ✅

---

## Phase 5 — Jobs Module
**Goal:** Full CRUD-lite for jobs.

- [ ] `POST /api/jobs/import`
- [ ] `GET /api/jobs` — filters: `location`, `workMode`, `skills` (any-match), `experienceMin`, `experienceMax`, `showRejected`
- [ ] `PATCH /api/jobs/{id}/status`

**Acceptance:** Mirror Phase 4 acceptance criteria for jobs. ✅

---

## Phase 6 — Course Offers Module
**Goal:** Full CRUD-lite for course offers.

- [ ] `POST /api/course-offers/import`
- [ ] `GET /api/course-offers` — filters: `platform`, `topic`, `maxPrice`, `isFree`, `showRejected`
- [ ] `PATCH /api/course-offers/{id}/status`

**Acceptance:** Mirror Phase 4 acceptance criteria for course offers. ✅

---

## Phase 7 — AI Tool Offers Module
**Goal:** Full CRUD-lite for AI tool offers.

- [ ] `POST /api/ai-offers/import`
- [ ] `GET /api/ai-offers` — filters: `toolName`, `isFree`, `forProfessionals`, `showRejected`
- [ ] `PATCH /api/ai-offers/{id}/status`

**Acceptance:** Mirror Phase 4 acceptance criteria for AI tool offers. ✅

---

## Phase 8 — UI Dashboard (Dark Theme)
**Goal:** Single-page dashboard served by Thymeleaf, all data via fetch/AJAX.

- [ ] `src/main/resources/templates/index.html` — Tailwind via CDN, dark palette
- [ ] 4 tabs: Events / Jobs / Course Offers / AI Tool Offers
- [ ] Per tab:
  - Filter panel (module-specific inputs) + `Show Rejected` toggle
  - **Fetch Results** button → `GET /api/{module}` with filter query params
  - **Import JSON** button → modal with textarea → `POST /api/{module}/import` → toast with summary
  - Cards grid — title, key fields, status badge (🟡 Saved / 🟢 Applied / 🔴 Rejected), Apply / Reject buttons → PATCH
  - Skeleton loader while fetching
  - Empty state message
  - Toast notifications on status change
- [ ] Static assets: `src/main/resources/static/js/{events,jobs,course-offers,ai-offers}.js` + shared `api.js`

**Acceptance:** Open `http://localhost:8080`, import sample JSON in each tab, filter, mark statuses — all work without page reload. ✅

---

## Phase 9 — Testing, Logging & Polish
**Goal:** Clean, production-ready code.

- [ ] Postman collection with 12 requests (3 per module × 4 modules)
- [ ] Manual UI test: import → filter → status change → showRejected toggle
- [ ] SLF4J logging across all services (info on import summaries, warn on dupes, error on parse failures)
- [ ] `@Valid` on all DTOs (`@NotBlank`, `@URL`, etc.)
- [ ] Proper error response shape for 404, 400, 500
- [ ] Remove dead code

**Acceptance:** All 12 API calls succeed in Postman; UI end-to-end works across all 4 tabs. ✅

---

## Phase 10 — Full Dockerization
**Goal:** Whole stack runs via single `docker compose up`.

- [ ] Multi-stage `Dockerfile` — Gradle build stage → JRE 21 runtime stage
- [ ] `docker-compose.yml` — `mysql` service + `app` service with `depends_on`, MySQL healthcheck, env vars for DB creds
- [ ] `application-docker.properties` — DB host = `mysql` (Docker service name)
- [ ] Verify: fresh `docker compose up --build` → `http://localhost:8080` works fully

**Acceptance:** Fresh clone → `docker compose up --build` → full dashboard with no local Java/MySQL needed. ✅

---

## Project Structure (target)

```
OpSpot/
├── src/main/java/com/opspot/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── enums/
│   ├── repository/
│   └── service/
├── src/main/resources/
│   ├── templates/index.html
│   ├── static/js/
│   ├── application.properties
│   └── application-docker.properties
├── docs/json-schemas/
│   ├── events.json
│   ├── jobs.json
│   ├── course-offers.json
│   └── ai-tool-offers.json
├── docker-compose.dev.yml
├── docker-compose.yml
├── Dockerfile
└── build.gradle
```
