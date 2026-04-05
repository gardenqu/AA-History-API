# AA History Crossword API

A RESTful API for an **African American History crossword puzzle application**. Users can play crossword puzzles built from a curated word bank of African American history terms, figures, and events. The backend handles puzzle generation, user authentication, progress tracking, and administrative management.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Features](#features)
- [Architecture](#architecture)
- [API Reference](#api-reference)
- [Security](#security)
- [Database Schema](#database-schema)
- [Hosting](#hosting)
- [License](#license)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.1 |
| Database | PostgreSQL 16 |
| ORM | Hibernate / Spring Data JPA |
| Security | Spring Security 7, JWT (jjwt 0.12.6) |
| Password Hashing | BCrypt |
| Email | Spring Mail (Gmail SMTP) |
| Build Tool | Maven |

---

## Features

- **Crossword puzzle generation** — algorithmic grid builder that places words with valid intersections, assigns clue numbers top-left to bottom-right, and fills empty cells with black squares
- **Difficulty-based grid sizing** — Easy (10x10, 6 words), Medium (15x15, 10 words), Hard (20x20, 15 words)
- **Word bank seeding** — auto-seeds categories, words, and clues from `blackHistory.json` on startup
- **JWT authentication** — stateless token-based auth with role claims embedded in the token
- **Google OAuth 2.0** — sign in with Google (in progress)
- **Role-based access control** — `USER` and `ADMIN` roles enforced at the method level via `@PreAuthorize`
- **Password reset via email** — time-limited secure reset tokens sent via Gmail
- **Login attempt logging** — tracks successful and failed login attempts with failure reasons
- **Security audit log** — logs sensitive actions like bans, role assignments, and password changes
- **User profile management** — profile completion tracking with name, birthday, and phone number
- **Puzzle solve tracking** — records time taken, mistakes, hint usage, and completion status
- **Admin panel endpoints** — full CRUD for categories, word bank entries, clues, users, and puzzles

---

## Architecture

```
src/
├── Controller/       # REST endpoints (AuthController, PuzzleController, AdminController, UserController)
├── DTO/              # Request/Response objects and Mappers
├── Entity/           # JPA entities (AppUser, Puzzle, Clue, WordBankEntry, Role, etc.)
├── Exception/        # GlobalExceptionHandler, AuthException, ErrorResponse
├── Repository/       # Spring Data JPA repositories
├── Security/         # JwtFilter, JwtService, SecurityConfig
├── Service/          # Business logic layer
└── Utility/          # CrosswordGenerator, WordBankSeeder, JsonMapConverter
```

### Crossword Generation Algorithm
The algorithm:

1. Shuffles selected words randomly
2. Places the first word horizontally in the center of the grid
3. For each remaining word, finds intersecting letters with already-placed words
4. Attempts vertical then horizontal placement through each intersection
5. Validates placement rules (no adjacent parallel words, no boundary violations)
6. Numbers clues top-left to bottom-right
7. Fills unused cells with `#` (black squares)

---

## API Reference

### Auth — `/api/auth`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/register` | Public | Register a new user |
| POST | `/login` | Public | Login with email and password |
| POST | `/google` | Public | Login or register with Google OAuth |
| POST | `/forgot-password` | Public | Request a password reset email |
| POST | `/reset-password` | Public | Reset password using a token |

### Users — `/api/users`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/me` | User | Get current user's profile |
| PATCH | `/me` | User | Update name, phone number, birth date |

### Puzzles — `/api/puzzles`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/` | User | Generate a new crossword puzzle |
| GET | `/` | User | Get all puzzles |
| GET | `/{id}` | User | Get a puzzle with its grid and clues |
| GET | `/{id}/clues` | User | Get clues for a specific puzzle |
| POST | `/{id}/solve` | User | Submit a puzzle solve |
| GET | `/solves/me` | User | Get current user's solve history |
| GET | `/{id}/solves` | User | Get all solves for a puzzle |

### Admin — `/api/admin` *(ADMIN role required)*

**Categories:**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/categories` | Create a category |
| GET | `/categories` | Get all categories |
| DELETE | `/categories/{id}` | Delete a category |

**Word Bank:**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/word-bank/entries` | Add a word to the word bank |
| GET | `/word-bank/entries/{categoryId}` | Get entries by category |
| DELETE | `/word-bank/entries/{id}` | Delete a word bank entry |
| POST | `/word-bank/clues` | Add a clue for a word |
| GET | `/word-bank/clues/{entryId}` | Get clues for an entry |
| DELETE | `/word-bank/clues/{id}` | Delete a clue |

**Users:**

| Method | Endpoint | Description |
|---|---|---|
| GET | `/users` | Get all users |
| GET | `/users/{id}` | Get user by ID |
| PATCH | `/users/{id}/deactivate` | Deactivate a user |
| PATCH | `/users/{id}/ban` | Ban a user |
| PATCH | `/users/{id}/unban` | Unban a user |
| PATCH | `/users/{id}/roles` | Assign roles to a user |

**Puzzles:**

| Method | Endpoint | Description |
|---|---|---|
| PATCH | `/puzzles/{id}/approve` | Approve a puzzle |
| PATCH | `/puzzles/{id}/reject` | Reject a puzzle |
| DELETE | `/puzzles/{id}` | Delete a puzzle |

---

## Security

- **JWT tokens** are signed with HMAC-SHA256 and include the user ID and roles as claims
- **Passwords** are hashed with BCrypt before storage — never stored in plain text
- **Spring Security's `AuthenticationManager`** handles credential verification
- **`CredentialsContainer`** erases the password hash from memory after authentication
- **Method-level authorization** via `@PreAuthorize` — all admin endpoints require `ADMIN` role
- **JWT filter** validates tokens on every request, rejects expired/malformed tokens, and blocks banned/deactivated users
- **Login attempts** are logged with success/failure status and failure reason
- **Security audit log** tracks sensitive actions (bans, role changes, password resets)
- **Password reset tokens** are time-limited (24 hours) and deleted after use
- **Sessions** are stateless

---

## Database Schema

14 tables across 5 categories:

| Category | Tables |
|---|---|
| Auth / User | `app_user`, `role`, `permission`, `user_role`, `role_permission` |
| Security | `password_reset_token`, `login_attempt`, `security_log` |
| Puzzle | `puzzle`, `clue`, `puzzle_solve` |
| Word Bank | `category`, `word_bank_entry`, `word_bank_clue` |

---

## License

MIT — see [LICENSE](LICENSE) for details.
