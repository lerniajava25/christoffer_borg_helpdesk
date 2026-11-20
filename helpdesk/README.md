# Helpdesk

A web-based helpdesk application developed with Java, Spring Boot, Thymeleaf and PostgreSQL.

The application provides a platform for users to submit support requests and communicate with support agents. Agents can manage incoming tickets, assign tickets to themselves, update ticket statuses and respond to users.

The application uses a layered architecture, database-backed authentication and a responsive user interface built with Tailwind CSS and daisyUI.

## Features

### User

- Log in and log out.
- Create support tickets.
- View personal support tickets.
- View ticket details and status.
- Read and write comments.
- Track ticket creation and modification dates.

### Support Agent

- Access all user functionality.
- View all support tickets.
- View tickets assigned to themselves.
- Assign unassigned tickets to themselves.
- Update ticket statuses.
- View ticket creators and assigned agents.
- Read and respond to comments.

### User Interface

- Responsive layout.
- Custom login page.
- Shared navigation using Thymeleaf Fragments.
- Role-based navigation.
- Colour-coded ticket statuses.
- Consistent styling across application pages.
- Form validation.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Application framework |
| Spring MVC | Web application architecture |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database access |
| Hibernate | Object-relational mapping |
| PostgreSQL 18 | Relational database |
| Flyway | Database migrations |
| Thymeleaf | Server-side HTML rendering |
| Tailwind CSS 4 | CSS framework |
| daisyUI 5 | UI component library |
| Docker Compose | Database environment |
| Maven | Build and dependency management |
| JUnit | Automated testing |

## Architecture

The application follows a layered architecture with separation of concerns.

Controller → Service → Repository → PostgreSQL

Thymeleaf is used to render server-side HTML views, while Spring Security manages authentication and authorization.

| Layer | Responsibility |
|---|---|
| Controller | Handles HTTP requests, responses and view models |
| Service | Handles application logic and data operations |
| Repository | Provides access to the database |
| Model | Defines entities, relationships and enumerations |
| Security | Handles user authentication |
| Config | Contains application and security configuration |
| Templates | Contains HTML views and reusable UI fragments |

## Database

The application uses PostgreSQL as its relational database.

### Entities

**User**

Represents a registered user or support agent.

- ID
- Username
- Password
- Email
- Role
- Enabled status

**Ticket**

Represents a support request.

- ID
- Title
- Description
- Status
- Creator
- Assigned agent
- Creation timestamp
- Last modification timestamp

**Comment**

Represents a message associated with a support ticket.

- ID
- Content
- Author
- Associated ticket

### Relationships

- A user can create multiple tickets.
- An agent can be assigned multiple tickets.
- A ticket can contain multiple comments.
- A user or agent can write multiple comments.
- A ticket may exist without an assigned agent.

Database schema changes are managed through versioned Flyway migrations. Hibernate validates the database schema on application startup.

## Ticket Statuses

| Status | Description |
|---|---|
| NEW | A newly created support request |
| IN_PROGRESS | A support request currently being processed |
| CLOSED | A completed support request |

## Security

The application implements authentication and authorization using Spring Security.

Security features include:

- Database-backed authentication.
- BCrypt password hashing.
- Role-based access control.
- Ownership-based access control.
- CSRF protection.
- Server-side input validation.
- Session-based authentication and logout.

Regular users can only access and comment on tickets they have created.

Support agents can access all tickets, assign tickets to themselves, update ticket statuses and respond to comments.

Authorization is enforced on the server and does not rely solely on the visibility of interface elements.

### HTTP Error Handling

| Status code | Description |
|---|---|
| 400 Bad Request | Invalid input |
| 403 Forbidden | Insufficient permissions |
| 404 Not Found | Requested ticket does not exist |
| 409 Conflict | Ticket has already been assigned |

## Getting Started

### Prerequisites

Before running the application, make sure the following software is installed:

- Java JDK 21 or later.
- Docker with Docker Compose support.
- Git.

An internet connection is required to download dependencies and load the CDN-based frontend styling.

Maven Wrapper is included in the project.

### 1. Clone the Repository

Clone the Git repository and navigate to the Spring Boot project directory:

```bash
cd christoffer_borg_helpdesk/helpdesk
```

### 2. Start Docker

Make sure Docker is running before starting the application.

The project includes a Docker Compose configuration that provides the PostgreSQL database environment.

### 3. Start the Application

Run the application using the development profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Alternatively, open the project in IntelliJ IDEA and run the main application class with `dev` configured as the active Spring profile.

The development profile is required to initialize the demonstration accounts.

### 4. Open the Application

Open the following address in a web browser:

http://localhost:8080

The application will redirect unauthenticated users to the login page.

## Development Accounts

The development profile automatically creates the following demonstration accounts on a fresh database:

| Username | Password | Role |
|---|---|---|
| devuser | secret | USER |
| devuser2 | secret | USER |
| devagent | secret | AGENT |

All passwords are hashed using BCrypt before being stored in the database.

No manual SQL configuration is required when starting with a fresh database.

These accounts are intended exclusively for local development, testing and demonstration.

## Database Initialization

Docker Compose provides a PostgreSQL 18 database instance.

| Configuration | Value |
|---|---|
| Host | localhost |
| Port | 5433 |
| Application database | mydatabase |
| Test database | helpdesk_test |
| Username | myuser |
| Password | secret |

The project includes an initialization script, `init.sql`, which automatically creates the dedicated test database when PostgreSQL initializes an empty data directory.

Flyway automatically applies the database migrations when the application or tests are started.

The development initializer creates the demonstration accounts if they do not already exist.

## Application Routes

### General Routes

| Method | Route | Description |
|---|---|---|
| GET | / | Home page |
| GET | /login | Login page |
| POST | /login | Authenticate user |
| POST | /logout | Log out |

### User Routes

| Method | Route | Description |
|---|---|---|
| GET | /tickets | View personally created tickets |
| GET | /tickets/new | Display ticket creation form |
| POST | /tickets/new | Create a ticket |
| GET | /tickets/{id} | View ticket details |
| POST | /tickets/{id}/comments | Create a comment |

### Agent Routes

| Method | Route | Description |
|---|---|---|
| GET | /agent/tickets | View all tickets |
| GET | /agent/tickets/assigned | View personally assigned tickets |
| GET | /agent/tickets/{id} | View ticket details |
| POST | /agent/tickets/{id}/assign | Assign a ticket |
| POST | /agent/tickets/{id}/status | Update ticket status |
| POST | /agent/tickets/{id}/comments | Create a comment |

## Testing

### Automated Tests

The project includes automated repository tests and an application context test.

- `UserRepositoryTest`
- `TicketRepositoryTest`
- `HelpdeskApplicationTests`

Make sure the PostgreSQL container is running:

```bash
docker compose up -d
```

Run all automated tests:

```bash
./mvnw clean test
```

The tests use a separate database named `helpdesk_test` to avoid modifying application data.

### Manual Testing

The following functionality has been manually tested:

- User and agent authentication.
- Logout and session handling.
- Ticket creation.
- Personal ticket lists.
- Agent ticket lists.
- Ticket details.
- Ticket assignment.
- Duplicate assignment prevention.
- Ticket status updates.
- Comment creation and retrieval.
- Role-based access restrictions.
- Ownership-based read and write restrictions.
- Input validation.
- HTTP error handling.
- Navigation and user interface functionality.

Application startup, automatic development account creation and automated tests have also been verified against a clean PostgreSQL installation.

## Frontend

The frontend is rendered server-side using Thymeleaf.

Tailwind CSS and daisyUI provide the visual styling and UI components.

Thymeleaf Fragments are used to provide reusable navigation across application pages.

The interface adapts its navigation to the authenticated user's role.

The frontend does not require a separate Node.js application or frontend build process.

## Known Limitations

- Administrator functionality is not implemented.
- Automated tests provide basic coverage of repository functionality and application context initialization. Additional functionality has been tested manually.
- Frontend styling is loaded through external CDNs.
- Development credentials are intended for local demonstration only.
- Simultaneous ticket assignment requests are not protected by database-level concurrency control.