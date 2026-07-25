# Chat App

A real-time chat application MVP built with Spring Boot and React. Supports direct messaging, group chats, real-time WebSocket delivery, and offline message catch-up — similar to a minimal WhatsApp.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Architecture Overview](#architecture-overview)
- [Database Schema](#database-schema)
- [Backend Code Flow](#backend-code-flow)
  - [Registration & Login](#registration--login)
  - [JWT Authentication on HTTP Requests](#jwt-authentication-on-http-requests)
  - [Creating a Conversation](#creating-a-conversation)
  - [Sending a Message](#sending-a-message)
  - [WebSocket Connection & Real-time Delivery](#websocket-connection--real-time-delivery)
  - [Offline Message Delivery](#offline-message-delivery)
- [Frontend Code Flow](#frontend-code-flow)
- [Package Breakdown](#package-breakdown)
- [REST API Reference](#rest-api-reference)
- [Configuration](#configuration)
- [Design Decisions & Trade-offs](#design-decisions--trade-offs)
- [Scaling Path](#scaling-path)

---

## Tech Stack

| Layer      | Technology                                         |
|------------|----------------------------------------------------|
| Backend    | Java 21, Spring Boot 3.3, Spring Security, Spring WebSocket |
| Database   | PostgreSQL 16                                      |
| Migrations | Flyway                                             |
| ORM        | Spring Data JPA / Hibernate 6                      |
| Auth       | JWT (JJWT 0.12)                                    |
| Frontend   | React 18, TypeScript, Vite                         |
| Styling    | Tailwind CSS v4                                    |
| Data fetching | React Query (@tanstack/react-query)             |
| HTTP client | Axios                                             |
| Dev infra  | Docker Compose                                     |

---

## Project Structure

```
chat-app/
├── docker-compose.yml          # PostgreSQL container (port 5433)
│
├── backend/
│   ├── build.gradle
│   ├── src/main/
│   │   ├── java/com/chatapp/
│   │   │   ├── ChatApplication.java
│   │   │   ├── auth/               # Register, login, JWT response
│   │   │   ├── user/               # User entity, repository, search endpoint
│   │   │   ├── conversation/       # Conversation + member entity, service, controller
│   │   │   ├── message/            # Message entity, service, controller
│   │   │   ├── websocket/          # WS handler, session registry, delivery services
│   │   │   ├── security/           # JWT filter, entry point, UserDetailsService
│   │   │   ├── config/             # SecurityConfig, WebSocketConfig
│   │   │   ├── common/             # SecurityUtils (current user helper)
│   │   │   └── exception/          # Custom exceptions + global handler
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/
│   │           ├── V1__create_users_table.sql
│   │           ├── V2__create_conversations_tables.sql
│   │           └── V3__create_messages_table.sql
│
└── frontend/
    └── src/
        ├── types.ts                # Shared TypeScript interfaces
        ├── api.ts                  # All API calls + axios instance
        ├── App.tsx                 # Routes + global providers
        ├── context/
        │   └── AuthContext.tsx     # JWT state, login/register/logout
        ├── hooks/
        │   └── useWebSocket.ts     # WS connection with auto-reconnect
        ├── pages/
        │   ├── LoginPage.tsx
        │   ├── RegisterPage.tsx
        │   └── ChatPage.tsx        # Main layout, wires WS to React Query cache
        └── components/
            ├── ConversationList.tsx # Sidebar: DM search, group creation, list
            ├── ChatWindow.tsx       # Message thread + input + optimistic updates
            └── PrivateRoute.tsx     # Redirects unauthenticated users to /login
```

---

## Getting Started

### Prerequisites

- Docker Desktop
- Java 21
- Node.js 20+

### Run

```bash
# 1. Start the database
cd chat-app
docker compose up -d

# 2. Start the backend (first run downloads Gradle, runs Flyway migrations)
cd backend
./gradlew bootRun

# 3. Start the frontend
cd ../frontend
npm install
npm run dev
```

Open `http://localhost:5173` in two browser windows (one incognito) to test Alice ↔ Bob.

### Test Two Users

1. Register as `alice` in window 1
2. Register as `bob` in window 2 (incognito)
3. In Alice's window: type `bob` in the search box → click his name to open a DM
4. Send a message — it appears instantly in Bob's window
5. Close Bob's window, have Alice send more messages
6. Re-open Bob's window and log back in — the missed messages arrive immediately on reconnect

---

## Architecture Overview

```
  Browser (Alice)                 Browser (Bob)
     │                               │
     │ POST /api/messages            │  WebSocket push
     ▼                               ▼
┌──────────────────────────────────────────────┐
│               Spring Boot (port 8080)        │
│                                              │
│  JwtAuthenticationFilter (HTTP)              │
│  ChatWebSocketHandler    (WS)                │
│                                              │
│  AuthController      → AuthService           │
│  ConversationController → ConversationService│
│  MessageController   → MessageService        │
│                    → WebSocketDeliveryService│
│                                              │
│  OnlineUserRegistry  (ConcurrentHashMap)     │
│  OfflineMessageDeliveryService               │
└──────────────────────────────────────────────┘
                    │
                    ▼
            PostgreSQL (port 5433)
```

The architecture is a **modular monolith**. Each package owns its own entities, repositories, services, and controllers. Packages communicate by calling each other's service methods — never by crossing directly into another package's repository.

---

## Database Schema

### users
| Column        | Type         | Notes                    |
|---------------|--------------|--------------------------|
| id            | UUID (PK)    | `gen_random_uuid()`      |
| username      | VARCHAR(50)  | unique                   |
| email         | VARCHAR(255) | unique                   |
| password_hash | VARCHAR(255) | BCrypt                   |
| created_at    | TIMESTAMPTZ  | auto-set by DB           |

### conversations
| Column     | Type        | Notes                              |
|------------|-------------|------------------------------------|
| id         | UUID (PK)   |                                    |
| type       | VARCHAR(10) | `'DIRECT'` or `'GROUP'`            |
| name       | VARCHAR(100)| null for DIRECT conversations      |
| created_by | UUID (FK)   | references users                   |
| created_at | TIMESTAMPTZ |                                    |

### conversation_members
| Column          | Type        | Notes                        |
|-----------------|-------------|------------------------------|
| conversation_id | UUID (PK, FK)| composite primary key       |
| user_id         | UUID (PK, FK)| composite primary key       |
| joined_at       | TIMESTAMPTZ |                              |

### messages
| Column          | Type        | Notes                                       |
|-----------------|-------------|---------------------------------------------|
| id              | UUID (PK)   |                                             |
| conversation_id | UUID (FK)   | cascades on delete                          |
| sender_id       | UUID (FK)   |                                             |
| content         | TEXT        |                                             |
| created_at      | TIMESTAMPTZ |                                             |
| delivered       | BOOLEAN     | `false` until pushed over WebSocket         |

---

## Backend Code Flow

### Registration & Login

```
POST /api/auth/register  or  POST /api/auth/login
        │
        ▼
AuthController.register() / .login()
        │
        ▼
AuthService
  register:
    1. Check username/email uniqueness → throw ConflictException if taken
    2. BCrypt.encode(password) → save User entity
    3. JwtService.generateToken(userId, username) → return token + userId

  login:
    1. AuthenticationManager.authenticate() → validates credentials via
       UserDetailsServiceImpl (loads user from DB) + BCryptPasswordEncoder
    2. Load User from DB
    3. JwtService.generateToken() → return token + userId
```

The token payload contains `sub` (username) and a custom `userId` claim. Expiry defaults to 24 hours, configurable in `application.yml`.

---

### JWT Authentication on HTTP Requests

Every protected request passes through `JwtAuthenticationFilter` before reaching any controller:

```
Incoming HTTP request
        │
        ▼
JwtAuthenticationFilter.doFilterInternal()
  1. Read "Authorization: Bearer <token>" header
  2. JwtService.extractUsername(token)
  3. UserDetailsServiceImpl.loadUserByUsername() → load from DB
  4. JwtService.isTokenValid() → verify signature + expiry
  5. Set UsernamePasswordAuthenticationToken into SecurityContextHolder
        │
        ▼
Controller method runs
  SecurityUtils.currentUser()
    → SecurityContextHolder.getContext().getAuthentication().getName()
    → UserRepository.findByUsername()
    → returns User entity
```

If the token is missing or invalid, `JwtAuthEntryPoint` returns a JSON `401` response. If the user is authenticated but not authorized for a resource, the service layer throws `ForbiddenException` → `GlobalExceptionHandler` returns `403`.

---

### Creating a Conversation

#### Direct (DM)

```
POST /api/conversations/direct  { "recipientId": "..." }
        │
        ▼
ConversationService.getOrCreateDirect()
  1. Guard: caller cannot chat with themselves
  2. Load recipient User or throw 404
  3. ConversationRepository.findDirectConversation(callerId, recipientId)
     → JPQL: find DIRECT conversation where BOTH users are members
  4a. EXISTS → return existing (idempotent — safe to call repeatedly)
  4b. NOT EXISTS →
        create Conversation(type=DIRECT)
        add ConversationMember for caller
        add ConversationMember for recipient
        saveAndFlush → re-fetch → return DTO
```

#### Group

```
POST /api/conversations/group  { "name": "...", "members": ["uuid", ...] }
        │
        ▼
ConversationService.createGroup()
  1. Ensure caller is included in members list
  2. Load all member Users (batch) — throw 404 if any missing
  3. Create Conversation(type=GROUP, name=...)
  4. Add ConversationMember for each user
  5. saveAndFlush → re-fetch → return DTO
```

---

### Sending a Message

This is the most important flow — two separate transactions, intentionally:

```
POST /api/messages  { "conversationId": "...", "content": "..." }
        │
        ▼
MessageController.send()
  │
  ├── 1. MessageService.sendMessage()   ← Transaction 1
  │         a. ConversationService.assertMember() → throw 403 if not a member
  │         b. Load Conversation or throw 404
  │         c. Save Message(delivered=false)
  │         d. COMMIT
  │
  └── 2. WebSocketDeliveryService.deliverNewMessage()   ← Transaction 2
            a. Load conversation members
            b. For each member (excluding sender):
               - OnlineUserRegistry.getSession(memberId)
               - If online: send JSON envelope over WebSocket
               - Track which sends succeeded
            c. MessageRepository.markDelivered(successfulIds)
            d. COMMIT

  3. Return 201 with MessageResponse to the HTTP caller
```

**Why two transactions?** If WS delivery fails (user just disconnected), the message is already safely committed in Transaction 1. The `delivered=false` flag ensures it gets retried on reconnect. Putting both in one transaction would mean a WS failure could roll back the persist.

---

### WebSocket Connection & Real-time Delivery

```
Frontend: new WebSocket("ws://localhost:8080/ws/chat?token=<jwt>")
        │
        ▼
ChatWebSocketHandler.afterConnectionEstablished()
  1. Extract "token" query parameter from URI
  2. JwtService.validateToken() + extractUserId()
  3. If invalid → session.close(POLICY_VIOLATION)  ← connection rejected
  4. OnlineUserRegistry.register(userId, session)
     → ConcurrentHashMap<UUID, WebSocketSession>
  5. OfflineMessageDeliveryService.deliverPendingMessages(userId)
     → see "Offline Delivery" below

ChatWebSocketHandler.afterConnectionClosed()
  1. OnlineUserRegistry.remove(userId)
```

The `OnlineUserRegistry` is an in-memory `ConcurrentHashMap`. It lives for the lifetime of the JVM. On restart, all users start "offline" and receive their missed messages when they reconnect.

**Thread safety:** `WebSocketMessageSender.sendToUser()` synchronizes on the session object before calling `session.sendMessage()`. This is necessary because Spring WebSocket sessions are not thread-safe and multiple threads can attempt to push to the same user concurrently.

---

### Offline Message Delivery

When a user reconnects, before anything else:

```
OfflineMessageDeliveryService.deliverPendingMessages(userId)
  1. MessageRepository.findUndeliveredForUser(userId)
     → JPQL JOIN on conversation_members
     → WHERE delivered = false AND sender_id != userId
     → ORDER BY created_at ASC  (chronological order)
  2. For each undelivered message:
     a. Build WsEnvelope { type: "NEW_MESSAGE", payload: MessageResponse }
     b. WebSocketMessageSender.sendToUser()
     c. Collect IDs of successfully sent messages
  3. MessageRepository.markDelivered(ids)
     → single bulk UPDATE WHERE id IN (...)
```

No message queue or scheduled job is needed. The DB is the queue. This works cleanly for the MVP scale (≤100 users).

---

## Frontend Code Flow

### Auth Flow

```
AuthContext (React Context)
  - Reads token + user from localStorage on mount
  - login() → POST /api/auth/login → stores token in localStorage
  - register() → POST /api/auth/register → stores token in localStorage
  - logout() → clears localStorage → state becomes null

PrivateRoute
  - Reads token from AuthContext
  - If null → <Navigate to="/login" />
  - If present → renders children

axios interceptor (api.ts)
  - Attaches "Authorization: Bearer <token>" to every request
  - On 401 response → clears localStorage → redirects to /login
```

### Message Send Flow (with Optimistic Update)

```
User types message → hits Enter
        │
        ▼
ChatWindow → useMutation(sendMessage)
  onMutate (before request):
    1. Cancel in-flight queries for this conversation
    2. Inject optimistic Message into React Query cache
       (rendered immediately with opacity-60 styling)
  
  API call:  POST /api/messages
  
  onSuccess:
    Replace optimistic message with real server response
    (correct id, createdAt, delivered flag)
  
  onError:
    Remove optimistic message from cache (roll back)
```

### Real-time Receive Flow

```
useWebSocket hook (in ChatPage)
  → new WebSocket("ws://localhost:8080/ws/chat?token=...")
  → auto-reconnects after 3 seconds on close

On WS message received:
  handleWsMessage(envelope)
    if envelope.type === "NEW_MESSAGE":
      1. qc.setQueryData(['messages', conversationId], (old) => [...old, msg])
         → appends to the existing cache → ChatWindow re-renders with new message
      2. qc.invalidateQueries(['conversations'])
         → refreshes conversation list order
```

The WebSocket **never sends messages** — it only receives. Sending always goes through `POST /api/messages` for reliable persistence, validation, and membership checks.

---

## Package Breakdown

| Package | Responsibility |
|---|---|
| `auth` | `AuthController`, `AuthService`, `AuthDtos` — register/login, returns JWT |
| `user` | `User` entity, `UserRepository`, `UserController` (search + /me), `UserResponse` DTO |
| `conversation` | `Conversation`, `ConversationMember` entities, service (create/list), controller, DTOs |
| `message` | `Message` entity, `MessageService` (persist + history), `MessageController`, DTOs |
| `websocket` | `ChatWebSocketHandler`, `OnlineUserRegistry`, `WebSocketDeliveryService`, `OfflineMessageDeliveryService`, `WebSocketMessageSender`, `WsEnvelope` |
| `security` | `JwtService`, `JwtAuthenticationFilter`, `JwtAuthEntryPoint`, `JwtProperties`, `UserDetailsServiceImpl` |
| `config` | `SecurityConfig` (filter chain, CORS, BCrypt), `WebSocketConfig` (handler registration) |
| `common` | `SecurityUtils.currentUser()` — resolves the authenticated User entity from SecurityContext |
| `exception` | `ResourceNotFoundException`, `ConflictException`, `ForbiddenException`, `GlobalExceptionHandler` |

---

## REST API Reference

All endpoints except `/api/auth/**` require `Authorization: Bearer <token>`.

### Auth

| Method | Path | Body | Response |
|--------|------|------|----------|
| POST | `/api/auth/register` | `{ username, email, password }` | `{ token, username, userId }` |
| POST | `/api/auth/login` | `{ username, password }` | `{ token, username, userId }` |

### Users

| Method | Path | Notes |
|--------|------|-------|
| GET | `/api/users/me` | Returns current user |
| GET | `/api/users/search?q=alice` | Search users by username prefix, excludes self |

### Conversations

| Method | Path | Body | Notes |
|--------|------|------|-------|
| POST | `/api/conversations/direct` | `{ recipientId }` | Idempotent — returns existing if already exists |
| POST | `/api/conversations/group` | `{ name, members: [uuid] }` | Caller auto-added as member |
| GET | `/api/conversations` | — | Lists all conversations for current user |

### Messages

| Method | Path | Notes |
|--------|------|-------|
| POST | `/api/messages` | `{ conversationId, content }` — caller must be a member |
| GET | `/api/messages?conversationId=...` | Returns history, caller must be a member |

### WebSocket

```
ws://localhost:8080/ws/chat?token=<jwt>
```

Server → Client envelope:

```json
{
  "type": "NEW_MESSAGE",
  "payload": {
    "id": "uuid",
    "conversationId": "uuid",
    "sender": { "id": "uuid", "username": "alice" },
    "content": "Hello!",
    "createdAt": "2026-07-24T15:52:09Z",
    "delivered": false
  },
  "timestamp": "2026-07-24T15:52:09Z"
}
```

Other envelope types defined (not yet emitted): `USER_ONLINE`, `USER_OFFLINE`.

### Error Responses

All errors return consistent JSON:

```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "You are not a member of this conversation",
  "timestamp": "2026-07-24T15:52:09Z"
}
```

Validation errors return a map of field → message:

```json
{
  "content": "Message content is required",
  "conversationId": "conversationId is required"
}
```

---

## Configuration

All configuration lives in `backend/src/main/resources/application.yml`.

| Key | Default | Description |
|-----|---------|-------------|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5433/chatapp` | Postgres URL (port 5433 to avoid conflicts) |
| `spring.datasource.username` | `chatapp` | |
| `spring.datasource.password` | `chatapp_secret` | |
| `app.jwt.secret` | (hex string) | HMAC-SHA384 signing key — change in production |
| `app.jwt.expiration-ms` | `86400000` (24h) | Token lifetime in milliseconds |
| `server.port` | `8080` | |

For production, inject secrets via environment variables:

```yaml
app:
  jwt:
    secret: ${JWT_SECRET}
```

---

## Design Decisions & Trade-offs

**REST for send, WebSocket for receive only**
Sending over REST gives reliable auth, validation, and transactional persistence. WebSocket is push-only from the server. This makes the flow simpler to reason about than a bidirectional message protocol.

**Two transactions on message send**
`MessageService.sendMessage()` commits first, then `WebSocketDeliveryService.deliverNewMessage()` runs in a separate transaction. If delivery fails, the message is safe in the DB and will arrive on the next reconnect. A single transaction would risk rolling back a successful persist just because a WebSocket push failed.

**No message queue (no Redis, no Kafka)**
For ≤100 concurrent users, the DB is the queue. `delivered = false` rows are the pending message backlog. This is simple, observable, and correct. The query that loads offline messages uses an index on `(sender_id, delivered)` with a partial index `WHERE delivered = false` to stay fast even as the table grows.

**In-memory `OnlineUserRegistry`**
`ConcurrentHashMap<UUID, WebSocketSession>` — simple, zero dependencies, correct for a single node. On restart, everyone starts offline and catches up via offline delivery. To scale horizontally, replace this with Redis pub/sub; the rest of the code doesn't change.

**`VARCHAR + CHECK` instead of PostgreSQL native ENUM for conversation type**
Native Postgres enums require `ALTER TYPE` to add values, which is a pain in migration-managed schemas. `VARCHAR(10) CHECK (type IN ('DIRECT', 'GROUP'))` is simpler, works with `@Enumerated(EnumType.STRING)` natively, and is easy to extend.

**Idempotent direct conversation creation**
`POST /api/conversations/direct` always returns the same conversation if one already exists between the two users. This prevents duplicate DMs and makes the frontend safe to call this endpoint repeatedly without defensive checks.

---

## Scaling Path

The codebase is designed so each scaling upgrade is a localized change:

| When you need | Change |
|---|---|
| Multiple backend nodes | Replace `OnlineUserRegistry` with Redis pub/sub. The interface (`register`, `remove`, `getSession`) stays the same |
| Message fan-out at scale | Move `WebSocketDeliveryService.deliverNewMessage()` to a Kafka consumer. The `MessageController` publishes an event instead of calling the service directly |
| Token refresh | Add `POST /api/auth/refresh` + short-lived access tokens. `JwtService` already separates generation from validation |
| Typing indicators | `handleTextMessage()` in `ChatWebSocketHandler` already exists as an extension point. Add a `TYPING` envelope type to `WsEnvelope` |
| Message pagination | `MessageRepository.findByConversationId()` currently returns all messages. Add a `before` cursor parameter and change to `LIMIT 50` |
| Read receipts | Add a `read_at` column to `conversation_members`. The delivered flag already shows the first step of this pattern |
