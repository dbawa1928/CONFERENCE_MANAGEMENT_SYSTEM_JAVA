# 🚀 ConfSync — Smart Conference Management System
### Project Based Learning in Java (PBLJ) University Academic Project

ConfSync is a **complete, full-stack, enterprise-grade Conference Management System** built with **Java (Spring Boot, Hibernate, JPA, MySQL)** as the primary technology stack, accompanied by a clean, modern, responsive frontend running on `localhost:5173`.

---

## 📋 System Architecture

```text
┌────────────────────────────────────────────────────────┐
│                   Frontend (Client)                    │
│      HTML5 • CSS3 • Modern JavaScript ES6 Modules       │
│               http://localhost:5173                    │
└───────────────────────────┬────────────────────────────┘
                            │ REST API (JSON / Multipart)
                            │ Bearer JWT Authentication
                            ▼
┌────────────────────────────────────────────────────────┐
│            Java Spring Boot 3 Backend Server           │
│                http://localhost:8080                   │
│                                                        │
│  Controllers ──► Services (Impl) ──► Repositories      │
│       ▲                 ▲                 ▲            │
│   DTOs / Enums     Business Logic   Entities / JPA     │
│       ▲                 ▲                 ▲            │
│  Global Exceptions  File I/O (NIO)  Spring Security    │
└───────────────────────────┬────────────────────────────┘
                            │ JDBC Connection (Port 3306)
                            ▼
┌────────────────────────────────────────────────────────┐
│                     MySQL Database                     │
│                 confsync_db (Localhost)                │
└────────────────────────────────────────────────────────┘
```

---

## ☕ Key Java Concepts Demonstrated (PBLJ Evaluation)

| Java Concept | Implementation in ConfSync |
| :--- | :--- |
| **Object-Oriented Programming (OOP)** | **Encapsulation**: Private fields across all entities and DTOs with strictly typed getters, setters, and business methods.<br>**Polymorphism**: Clean Service interfaces (`ConferenceService`, `PaperService`, `UserService`, etc.) implemented by concrete `@Service` classes (`ConferenceServiceImpl`, etc.).<br>**Abstraction & Inheritance**: `BaseEntity` mapped superclass containing audit fields (`id`, `createdAt`, `updatedAt`, `@PrePersist`, `@PreUpdate`). |
| **Java Collections Framework** | `List`, `Set`, and `Map` used across services and analytics. Aggregations, groupings, and counts implemented using the Java 8+ Stream API (`Collectors.groupingBy`, `Collectors.counting`, `filter`, `map`). |
| **Exception Handling** | Custom typed runtime exceptions (`ResourceNotFoundException`, `DuplicateRegistrationException`, `ConferenceCapacityException`, `InvalidTimeRangeException`, `FileStorageException`) caught and formatted by a centralized `@RestControllerAdvice` (`GlobalExceptionHandler`). Standard HTTP status codes: `200`, `201`, `400`, `401`, `403`, `404`, `409`, `500`. |
| **Local File Handling (Java NIO)** | Local paper uploads and storage in `backend/uploads/papers/` using `java.nio.file.Path`, `Files`, and `StandardCopyOption`. Programmatic certificate creation with **Apache PDFBox** in `backend/uploads/certificates/`. |
| **Database Connectivity (JPA/Hibernate)** | 11 relational MySQL tables mapped with JPA annotations (`@Entity`, `@Table`, `@OneToMany`, `@ManyToOne`, `@OneToOne`, `@JoinColumn`, `@Lob`). Persistent state on localhost. |
| **Security & Cryptography** | Spring Security 6 with BCrypt password hashing, stateless JWT authentication token provider, and role-based route protection (`@PreAuthorize`). |
| **Java Enums** | `Role` (ADMIN, PARTICIPANT, SPEAKER, AUTHOR, REVIEWER), `ConferenceStatus`, `PaperStatus`, `RegistrationStatus`, `Recommendation`, `AssignmentStatus`. |

---

## 👥 System Roles & Pre-configured Demo Accounts

For immediate university demonstration, the database seeds the following user accounts upon first launch:

| Role | Email | Password | Primary Capabilities |
| :--- | :--- | :--- | :--- |
| **👑 ADMIN** | `admin@confsync.com` | `admin123` | Full dashboard, create/edit conferences, schedule sessions, assign reviewers, mark attendance, issue certificates. |
| **🎟️ PARTICIPANT** | `john.doe@university.edu` | `password123` | Browse conferences, register, view digital pass with QR code, build schedule, download verified certificates. |
| **📄 AUTHOR** | `alex.chen@stanford.edu` | `password123` | Submit research papers with PDF manuscript, track peer-review status pipeline, view reviewer scores & feedback. |
| **🔍 REVIEWER** | `david.miller@berkeley.edu` | `password123` | Access assigned papers, review 4 criteria (Originality, Technical, Relevance, Presentation), compute average, submit feedback. |
| **🎤 SPEAKER** | `alan.turing@cambridge.edu` | `password123` | Manage profile (Bio, Expertise, Organization), view assigned sessions & conference timeline. |

*(All sample data can be edited or deleted through the Admin portal).*

---

## 🛠️ Prerequisites

- **Java Development Kit (JDK 17 or JDK 21)**
- **MySQL Server (5.7+ or 8.0+)** running locally on port 3306
- **Node.js (v18+)** for the frontend server
- **Maven** (or Maven wrapper included)

---

## 🚀 Setup & Execution Guide

### Step 1: Set Up MySQL Database

Log in to MySQL (via MySQL Command Line Client, MySQL Workbench, or phpMyAdmin) and create the database:

```sql
CREATE DATABASE confsync_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

*(Optional: You can also execute the complete schema directly from `database_setup.sql` in the project root).*

---

### Step 2: Configure MySQL Credentials in Backend

Open `backend/src/main/resources/application.properties` and verify your local MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/confsync_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

*(Note: If testing on a machine without MySQL service active, you can optionally run the backend using H2 in-memory mode by passing `--spring.profiles.active=h2`).*

---

### Step 3: Run the Java Spring Boot Backend

Open your terminal or command prompt:

```bash
cd backend
mvn spring-boot:run
```

Once started, the backend runs at:
👉 **`http://localhost:8080`**

On initial startup, `DatabaseSeeder.java` will automatically populate MySQL with conferences, sessions, speakers, submitted papers, and test users.

---

### Step 4: Run the Frontend Server

Open a second terminal window:

```bash
cd frontend
npm install
npm run dev
```

Open your browser and navigate to:
👉 **`http://localhost:5173`**

You can click **"🚀 Quick Demo Login"** on the navbar to instantly authenticate into any role.

---

## 🌐 REST API Endpoints

### 🔐 Authentication (`/api/auth`)
- `POST /api/auth/register` — Register a new user account with role
- `POST /api/auth/login` — Authenticate and receive JWT token
- `GET  /api/auth/me` — Retrieve profile of currently authenticated user

### 📅 Conferences (`/api/conferences`)
- `GET    /api/conferences` — Search and filter conferences (by keyword, category, status)
- `GET    /api/conferences/upcoming` — List upcoming conferences
- `GET    /api/conferences/{id}` — Get conference details and capacity
- `POST   /api/conferences` — Create new conference *(Admin only)*
- `PUT    /api/conferences/{id}` — Update conference *(Admin only)*
- `DELETE /api/conferences/{id}` — Delete conference *(Admin only)*

### 🎟️ Registrations (`/api/registrations`)
- `POST   /api/registrations` — Register current participant for conference (checks capacity & duplicate)
- `GET    /api/registrations/my` — Get current participant's registrations and QR passes
- `GET    /api/registrations/conference/{id}` — List attendees for conference *(Admin only)*
- `DELETE /api/registrations/{id}` — Cancel registration

### 🗓️ Sessions (`/api/sessions`)
- `GET    /api/sessions` — List all conference sessions
- `GET    /api/sessions/conference/{id}` — List sessions for a conference
- `GET    /api/sessions/speaker/{id}` — List sessions assigned to a speaker
- `POST   /api/sessions` — Create session with time range validation *(Admin only)*
- `PUT    /api/sessions/{id}` — Update session *(Admin only)*
- `DELETE /api/sessions/{id}` — Delete session *(Admin only)*

### 🎤 Speakers (`/api/speakers`)
- `GET    /api/speakers` — List all speaker profiles
- `GET    /api/speakers/{id}` — Get speaker details
- `GET    /api/speakers/user/{userId}` — Get speaker profile for user
- `POST   /api/speakers` — Create speaker profile
- `PUT    /api/speakers/{id}` — Update speaker profile

### 📄 Research Papers (`/api/papers`)
- `POST   /api/papers` — Submit paper with abstract and local PDF file *(Author only)*
- `GET    /api/papers` — List all submitted papers *(Admin only)*
- `GET    /api/papers/my` — List author's submitted papers
- `GET    /api/papers/{id}` — View paper details and peer reviews
- `PUT    /api/papers/{id}/status` — Update paper status (ACCEPTED / REJECTED / REVISION)
- `GET    /api/papers/{id}/download` — Download uploaded PDF manuscript

### 🔍 Paper Reviews (`/api/reviews`)
- `POST   /api/reviews/assign` — Assign reviewer to paper *(Admin only)*
- `GET    /api/reviews/assigned` — List papers assigned to current reviewer
- `POST   /api/reviews/{paperId}` — Submit evaluation scores & feedback
- `GET    /api/reviews/paper/{paperId}` — Get evaluations for a paper
- `GET    /api/reviews/my` — Get reviewer's completed evaluations

### 🎟️ QR Attendance (`/api/attendance`)
- `POST   /api/attendance/check-in` — Verify QR code or token and mark attendance in MySQL *(Admin only)*

### 🏆 Certificates (`/api/certificates`)
- `POST   /api/certificates/generate` — Generate participation certificate (requires verified attendance)
- `GET    /api/certificates/my` — List certificates earned by current participant
- `GET    /api/certificates` — List all issued certificates *(Admin only)*
- `GET    /api/certificates/{id}/download` — Download generated PDF certificate document
- `GET    /api/certificates/verify/{code}` — Public verification of certificate authenticity

### 📊 Dashboard & Notifications (`/api/dashboard`, `/api/notifications`)
- `GET    /api/dashboard/stats` — Real-time MySQL aggregations (users, papers, conferences, attendance)
- `GET    /api/notifications` — Get in-app activity notifications
- `PUT    /api/notifications/{id}/read` — Mark notification as read
- `PUT    /api/notifications/read-all` — Mark all notifications as read

---

## 📁 Project Directory Structure

```text
CONFERENCE_MANAGEMENT_SYSTEM/
│
├── backend/
│   ├── pom.xml                                  # Maven project configuration
│   ├── uploads/
│   │   ├── papers/                              # Local storage for paper PDFs
│   │   └── certificates/                        # Local storage for generated certificates
│   ├── src/main/java/com/confsync/
│   │   ├── ConfSyncApplication.java             # Main Spring Boot Entrypoint
│   │   ├── config/
│   │   │   ├── SecurityConfig.java              # Spring Security, CORS, BCrypt
│   │   │   └── DatabaseSeeder.java              # Demo data initializer (CommandLineRunner)
│   │   ├── controller/                          # REST API Endpoints
│   │   ├── dto/                                 # Request & Response Data Transfer Objects
│   │   ├── entity/                              # JPA Entities (Inherit BaseEntity)
│   │   ├── enums/                               # Role, ConferenceStatus, PaperStatus, etc.
│   │   ├── exception/                           # Custom Exceptions & @RestControllerAdvice
│   │   ├── repository/                          # Spring Data JPA Repositories
│   │   ├── security/                            # JWT Filter, Token Provider, UserDetails
│   │   ├── service/                             # Service Interfaces (Polymorphism)
│   │   │   └── impl/                            # Service Implementations (Business Logic)
│   │   └── util/                                # QR Generator (ZXing), PDF Generator (PDFBox)
│   └── src/main/resources/
│       ├── application.properties               # MySQL Database & File Configuration
│       └── application-h2.properties            # In-memory test profile fallback
│
├── frontend/
│   ├── package.json                             # Node script configurations
│   ├── server.js                                # Native Node HTTP server on port 5173
│   ├── index.html                               # Responsive Single Page Application root
│   ├── css/
│   │   └── style.css                            # Professional Blue Theme & UI components
│   └── js/
│       ├── api.js                               # Centralized REST API fetch client
│       ├── auth.js                              # Authentication & Role Manager
│       ├── app.js                               # Router, Modals, Toasts & Navbar
│       └── pages/                               # Modular page views for all 5 roles
│           ├── public.js                        # Home, Conferences, About, Contact
│           ├── admin.js                         # Admin Dashboard & Management Tabs
│           ├── participant.js                   # Passes, QR Tickets, Schedules, Certificates
│           ├── author.js                        # Paper Submissions & Review Feedback
│           ├── reviewer.js                      # Evaluation Scoring & Sliders
│           └── speaker.js                       # Speaking Sessions & Profile
│
├── database_setup.sql                           # Standalone MySQL schema setup script
└── README.md                                    # Complete project documentation
```

---

## 🏆 Summary

ConfSync satisfies all university requirements for a Project Based Learning in Java (PBLJ) course:
- Pure Java enterprise architecture with clean separation of concerns
- Permanent local MySQL database persistence
- File upload handling and programmatic document generation
- Real QR code check-in attendance verification pipeline
- Role-based authorization across 5 distinct user types
- Professional, clean UI operating on `http://localhost:5173`

