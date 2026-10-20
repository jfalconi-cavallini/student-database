# Student Database API — project context

Portfolio project for junior backend roles. A REST API that stores per-student
tutoring data (tutors, students, session notes, homework, practice test results),
modeled on the owner's production Supabase/Postgres schema for his tutoring business.

## How to work with me (teaching mode)
- I'm a CS grad, strong in Python/TypeScript, beginner-intermediate in Java.
- Act as a senior Java backend mentor. One small step at a time:
  what we're building and why → exact code/commands → how to run and verify → STOP and wait
  for me to say "done" or paste errors.
- Explain Java/Spring concepts briefly the first time they appear.
- I want to learn: show me the code and explain it rather than silently editing many files.
- Keep it simple: no microservices or extra features until the MVP works.
- Use FAKE seed data only (example.com emails). Never real student data — the records are minors'.

## Stack
- Java 21 (Temurin), Spring Boot 4.1.x (3.x is EOL), Maven wrapper (`.\mvnw.cmd`)
- Spring Web, Spring Data JPA (Hibernate 7), PostgreSQL 17, Flyway, Validation,
  Spring Security, Actuator, Testcontainers
- Docker Compose (`compose.yaml`) runs Postgres locally: db `studentvault`, user/pass `vault`/`vault`, port 5432

## Environment
- Windows + PowerShell. Project path: `C:\Software Engineering\metaminds\student-database`
- Docker Desktop (WSL2) must be running for `docker compose` and tests.
- Repo: github.com/jfalconi-cavallini/student-database
- Manual API testing: `requests.http` with the VS Code REST Client extension (uses `# @name` chaining).

## Conventions (keep following these)
- Base package `com.studentvault`; main class at the root. Package by feature:
  `tutor/`, `student/`, `note/`, `homework/`, `common/`, `common/error/`, `config/`.
- Layers: Controller (HTTP only) → Service (business rules, `@Transactional`) → Repository.
- DTOs are Java records (`XRequest`, `XResponse` with `static from(entity)`). Never expose entities.
- Constructor injection with `final` fields. No Lombok.
- Entities: UUID ids (`GenerationType.UUID`), `@CreationTimestamp Instant createdAt`,
  `@ManyToOne(fetch = LAZY)`, no bidirectional `@OneToMany`. Responses expose related ids only (avoid N+1).
- Students and tutors are soft-deleted (`archived`); leaf records (notes, homework) are hard-deleted.
- Emails normalized via `common.Emails.normalize`.
- Services throw domain exceptions (`NotFoundException`, `ConflictException`, `BadRequestException`);
  `GlobalExceptionHandler` (`@RestControllerAdvice`, extends `ResponseEntityExceptionHandler`)
  returns RFC 9457 `ProblemDetail`; validation errors include an `errors` field map. Never leak stack traces.
- Nested child resources are scoped by parent: `findByIdAndStudentId` → 404 if mismatched (IDOR protection).
- Lists are paginated: `Pageable` + `@PageableDefault`, returned as `PagedModel`.
- State transitions live in the entity (e.g. `Homework.submit()/grade()`, no `setStatus`); invalid transition → 409.
- `spring.jpa.hibernate.ddl-auto=validate`. Flyway owns the schema. NEVER edit an applied migration;
  add a new `V<n>__description.sql`.
- DB config via env vars with local defaults: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
- `SecurityConfig` currently permits all requests and disables CSRF — TEMPORARY until JWT step.
- `tutorId` in note/homework request bodies is TEMPORARY — becomes the logged-in tutor once auth exists.

## Git workflow
- One feature branch per step (`feat/...`), conventional commit messages, PR + merge on GitHub.
- Rhythm: code → run & verify → commit/push on branch → merge PR → `git checkout main && git pull`
  → `git checkout -b` next branch.

## Progress
- [x] 1 Project setup (Initializr, Boot 4.1, Java 21)
- [x] 2 Docker Compose Postgres + V1 migration (tutors, students)
- [x] 3 Tutor/Student entities + repositories + Testcontainers repository test
- [x] 4 Tutor CRUD API (DTOs, validation, pagination)
- [x] 5 Student CRUD + tutor assignment + `GET /api/tutors/{id}/students`
- [x] 6 Global error handling (ProblemDetail, domain exceptions)
- [~] 7 Session notes (V2 migration, `/api/students/{studentId}/notes`) — code written; confirm committed/merged
- [ ] 8 Homework with status workflow (V3 migration, assign/submit/grade) — code was provided in chat
- [ ] 9 Practice test results (V4)
- [ ] 10 Spring Security + JWT (users table, login, BCrypt)
- [ ] 11 Role-based access: ADMIN all; TUTOR only assigned students' rows; STUDENT own rows + submit homework;
        PARENT read-only on their child. Add 403 handler for AccessDeniedException.
- [ ] 12 Unit tests (JUnit 5 + Mockito for services/entities) + Testcontainers integration test incl. authorization
- [ ] 13 OpenAPI/Swagger (springdoc), Dockerfile, README
- [ ] 14 Deploy (Render or Railway) + hosted Postgres; CORS for the frontend
- [ ] End: 3–4 honest resume bullets

## Later (not MVP)
sessions table (+ `session_id` on notes), parent_updates, hour packages/booking, tutor availability,
skill graph/mastery, curriculum catalog, file uploads (report cards, transcripts), audit log.
