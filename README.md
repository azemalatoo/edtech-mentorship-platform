# EdTech Mentorship Platform

## Description
EdTech mentorship platform built with **Spring Boot** that connects **mentees** with experienced **mentors** based on personalized preferences, interests, and goals.
The platform offers mentor-matching, session scheduling, learning goals tracking and feedback systems, and much more.

## Motivation
Students need personalized guidance to succeed. This Spring Boot backend powers a platform that makes it easy for students to find and schedule sessions with expert mentors—handling profiles, matching, booking, and communication—to boost academic and career growth. 

## Core Features

- User Registration and Authentication (JWT)  
- Mentor & Mentee Profile Management  
- Mentor-Mentee Matching  
- Session Booking and Scheduling
- In-App Messaging using WebSockets 
- Learning Goals & Progress Tracking  
- Session Reviews & Feedback  
- Admin Dashboard & Moderation Tools  
- Swagger/OpenAPI Documentation

---

## Technology  Stack

- **Backend**: Java 17 | Spring Boot 3.0.4
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT (acess and refresh tokens)
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
└── config/            # Configurations
└── util/              # Utility classes
test/
├── mapper/            # Tests for mapper classes (Entity <-> DTO)
├── security/          # Tests for security classes
├── service/           # Tests for service classes


---

## 🔧 Getting Started

### Prerequisites

- Java 17+  
- Maven 3.8+  
- PostgreSQL 

### Clone the Project

```
git clone https://github.com/azemalatoo/mentorconnect-edtech-platform.git
cd edtech-mentorship-platform
```

### Run the Application

```
./mvnw spring-boot:run
```

### Swagger UI

Access full API documentation at:  
https://mentor-connect-03d663062b56.herokuapp.com/swagger-ui/index.html

---

## 🔐 Authentication

All secured endpoints require a **JWT token** in the `Authorization` header:

```
Authorization: Bearer <your_token>
```
Use the `/api/auth/register` and `/api/auth/login`, `/api/auth/refresh` endpoints to get your token.
