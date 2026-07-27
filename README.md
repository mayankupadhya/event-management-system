# Event Management System

A backend REST API for managing events, user registrations, and role-based access — built with Spring Boot and secured using JWT authentication.

This project was built as a hands-on learning project to practice Spring Security, JWT-based stateless authentication, and clean REST API design.

## Tech Stack

- **Java**
- **Spring Boot 4**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **JWT (jjwt 0.12.7)**
- **Maven**

## Features

- User registration and login with BCrypt password encryption
- JWT-based stateless authentication
- Role-based authorization (`ADMIN`, `STUDENT`) enforced at the Spring Security config level
- Event CRUD operations (create, update, delete, search) — restricted to admins where appropriate
- Event registration module — students can register for events and view their registered events
- Centralized exception handling with clean, structured JSON error responses
- Request validation using `@NotBlank`, `@Email`, and `@Valid`
- Consistent use of `ResponseEntity` with appropriate HTTP status codes

## Project Structure

```
com.example.eventmanagement
├── controller
│   ├── AuthController
│   ├── EventController
│   ├── RegistrationController
│   └── UserController
├── dto
│   ├── LoginRequest
│   ├── RegisterRequest
│   └── UserDTO
├── entity
│   ├── User
│   ├── Event
│   └── Registration
├── exception
│   ├── GlobalExceptionHandler
│   └── ResourceNotFoundException
├── repository
│   ├── UserRepository
│   ├── EventRepository
│   └── RegistrationRepository
├── security
│   ├── SecurityConfig
│   ├── JwtFilter
│   ├── JwtService
│   ├── CustomUserDetails
│   └── CustomUserDetailService
└── service
    ├── UserService
    └── EventService
```

## API Endpoints

### Authentication

| Method | Endpoint         | Access | Description                  |
|--------|------------------|--------|-------------------------------|
| POST   | `/auth/register` | Public | Register a new user           |
| POST   | `/auth/login`    | Public | Login and receive a JWT token |

### Events

| Method | Endpoint                     | Access          | Description                    |
|--------|------------------------------|-----------------|---------------------------------|
| GET    | `/events/all`                | Authenticated   | Get all events                  |
| GET    | `/events/search?title=`      | Authenticated   | Search events by title          |
| GET    | `/events/{id}/participants`  | Authenticated   | Get participants of an event    |
| POST   | `/events/add/{adminId}`      | Admin only      | Create a new event              |
| PUT    | `/events/update/{id}`        | Admin only      | Update an existing event        |
| DELETE | `/events/delete/{id}`        | Admin only      | Delete an event                 |

### Registrations

| Method | Endpoint                             | Access        | Description                          |
|--------|---------------------------------------|---------------|----------------------------------------|
| POST   | `/registrations/register/{eventId}`  | Authenticated | Register the logged-in user for an event |
| GET    | `/registrations/my-events`           | Authenticated | View events the logged-in user registered for |

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- MySQL

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/mayankupadhya/event-management-system.git
   cd event-management-system
   ```

2. Create a MySQL database
   ```sql
   CREATE DATABASE eventdb;
   ```

3. Configure your database credentials
   - Copy `src/main/resources/application.properties.example` to `src/main/resources/application.properties`
   - Update the `spring.datasource.username`, `spring.datasource.password`, and `jwt.secret` values with your own

4. Run the application
   ```bash
   ./mvnw spring-boot:run
   ```

   The application will start on `http://localhost:8080`

### Testing the API

Use Postman (or any REST client) to test the endpoints:

1. Register a user via `POST /auth/register`
2. Log in via `POST /auth/login` to receive a JWT token
3. Add the token as a **Bearer Token** in the Authorization tab for all protected endpoints

## Security Notes

- Passwords are hashed using BCrypt before being stored — plaintext passwords are never persisted.
- JWT tokens are validated on every request via a custom `OncePerRequestFilter`.
- Role-based access is enforced centrally in `SecurityConfig` using `hasRole()`, rather than scattered manual checks inside controllers.
- Sensitive fields (like password hashes) are never included in API responses — endpoints like `/events/{id}/participants` return only safe user fields (`id`, `name`, `email`).

## Author

Built by Mayank as part of backend development and internship preparation.