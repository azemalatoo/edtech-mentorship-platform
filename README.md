# EdTech Mentorship Platform

A robust EdTech mentorship platform built with **Spring Boot** that connects **mentees** with experienced **mentors** based on personalized preferences, interests, and goals.
The platform offers intelligent mentor-matching, paid mentorship packages, session scheduling, feedback systems, and much more.

## 🚀 Features

- 🔐 User Registration and Authentication (JWT)  
- 👤 Mentor & Mentee Profile Management  
- 🧠 Intelligent Mentor-Mentee Matching  
- 📆 Session Booking and Scheduling  
- 💬 In-App Messaging (Planned)  
- 🎯 Learning Goals & Progress Tracking  
- ⭐ Session Reviews & Feedback  
- 📄 Certificate Generation (Planned)  
- 🧾 Paid Mentorship Packages with Role-Based Access  
- 🛠 Admin Dashboard & Moderation Tools  
- 📚 Swagger/OpenAPI Documentation  

---

## 📦 Tech Stack

- **Backend**: Spring Boot (Java)  
- **Database**: PostgreSQL / H2 (for testing)  
- **Security**: Spring Security with JWT  
- **Documentation**: Swagger (Springdoc OpenAPI)  
- **Build Tool**: Maven  
- **Testing**: JUnit, MockMvc  

---

## 📁 Project Structure

```
src/
├── controller/        # REST API controllers
├── dto/               # Data Transfer Objects (DTOs)
├── entity/            # JPA entities
├── enums/             # Enum definitions
├── exception/         # Custom exceptions and handlers
├── mapper/            # Mapper classes (Entity <-> DTO)
├── repo/              # Spring Data JPA repositories
├── security/          # JWT and Spring Security config
├── service/           # Service interfaces and business logic
├── services/impl/     # Service implementations
└── config/            # Swagger & CORS configurations
```

---

## 🧪 Running Tests

- **Unit Tests**: Isolated component testing using JUnit and Mockito  
- **Integration Tests**: End-to-end testing with full Spring context using `@SpringBootTest` and `MockMvc`  

To run all tests:

```
./mvnw test
```

---

## 🔧 Getting Started

### Prerequisites

- Java 17+  
- Maven 3.8+  
- PostgreSQL (or use H2 for dev/testing)  

### Clone the Project

```
git clone https://github.com/yourusername/mentorconnect-edtech-platform.git
cd mentorconnect-edtech-platform
```

### Configure the Database

Edit your `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/mentorconnect
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

Or use H2 for testing:

```
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```

### Run the Application

```
./mvnw spring-boot:run
```

### Swagger UI

Access full API documentation at:  
http://localhost:8080/swagger-ui.html

---

## 🔐 Authentication

All secured endpoints require a **JWT token** in the `Authorization` header:

```
Authorization: Bearer <your_token>
```

Use the `/api/auth/register` and `/api/auth/login` endpoints to get your token.

---

## 📌 API Endpoints Overview

| Endpoint                    | Method | Description                         |
|-----------------------------|--------|-------------------------------------|
| `/api/auth/register`        | POST   | Register as a mentor/mentee         |
| `/api/auth/login`           | POST   | Login and receive JWT token         |
| `/api/mentee-profiles`      | CRUD   | Manage mentee profiles              |
| `/api/mentor-profiles`      | CRUD   | Manage mentor profiles              |
| `/api/match/recommendation` | GET    | Get matched mentors for a mentee    |
| `/api/packages`             | CRUD   | Manage mentorship packages          |
| `/api/sessions`             | CRUD   | Schedule and manage sessions        |
| `/api/reviews`              | CRUD   | Submit and view reviews             |

_(More APIs available in Swagger UI)_
