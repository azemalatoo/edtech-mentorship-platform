# MentorConnect — EdTech Mentorship Platform

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 🚀 Overview

**MentorConnect** is a centralized, locally‑adapted EdTech mentorship platform built with **Java 17** and **Spring Boot**. It connects learners across Kyrgyzstan—urban or rural—with experienced mentors tailored to their interests and career goals. By combining mentor-mentee matching, real‑time communication, and progress tracking, MentorConnect empowers students to gain real‑world insights and clear pathways for academic and professional growth.

## 📋 Table of Contents

1. [Key Features](#key-features)
2. [Architecture & Tech Stack](#architecture--tech-stack)
3. [Data Model](#data-model)
4. [Getting Started](#getting-started)

   * [Prerequisites](#prerequisites)
   * [Clone & Run](#clone--run)
   * [API Documentation](#api-documentation)
5. [Usage Examples](#usage-examples)
6. [Testing](#testing)
7. [Contributing](#contributing)

---

## ⭐ Key Features

* **User Onboarding & Security**: JWT‑powered registration, login, and role‑based access control.
* **Profile Management**: Create detailed mentor & mentee profiles with expertise tags and goals.
* **Smart Matching**: Algorithmic pairing of mentors and learners by interests, availability, and experience.
* **Session Scheduling**: Calendar sync, availability slots, and automated reminders.
* **Real‑Time Communication**: In‑app messaging via WebSockets and hooks for video calls.
* **Progress Tracking**: Learning goals, milestones, ratings, and feedback loops.
* **Administration Tools**: Dashboard for approval workflows, moderation, and analytics.
* **Interactive API**: Swagger‑documented endpoints for easy integration with web and mobile frontends.

---

## 🏗️ Architecture & Tech Stack

| Layer             | Technology                                     | Purpose                                      |
| ----------------- | ---------------------------------------------- | -------------------------------------------- |
| **Core API**      | Java 17, Spring Boot 3.0.4                     | High‑performance, modular REST services      |
| **Security**      | Spring Security, JWT (Access & Refresh Tokens) | Stateless authentication & role‑based access |
| **Persistence**   | PostgreSQL                                     | ACID‑compliant relational storage, JSON data |
| **Realtime Chat** | WebSockets                                     | Low‑latency messaging                        |
| **Documentation** | Springdoc OpenAPI (Swagger UI)                 | Interactive API docs & endpoint testing      |
| **Testing**       | JUnit, MockMvc, Postman                        | Unit, integration, and API validation        |
| **Deployment**    | Heroku                                         | Cloud hosting with automated deploys         |
| **Dev Tools**     | IntelliJ IDEA, Maven                           | IDE support and build automation             |

---

## 📊 Data Model

![ER Diagram](https://raw.githubusercontent.com/azemalatoo/mentorconnect-edtech-platform/main/diagrams/er-diagram.png)

---

## 🔧 Getting Started

### Prerequisites

* **Java 17** or higher
* **Maven 3.8+**
* **PostgreSQL** instance
* (Optional) **Heroku CLI** for cloud deployment

### Clone & Run

```bash
git clone https://github.com/azemalatoo/edtech-mentorship-platform.git
cd edtech-mentorship-platform
./mvnw spring-boot:run
```


### API Documentation

Once running, access interactive docs at:

```
http://localhost:8080/swagger-ui/index.html
```

or (Heroku deployment)

```
https://mentor-connect-03d663062b56.herokuapp.com/swagger-ui/index.html
```

---

## 💡 Usage Examples

1. **Register a new user**

   ```bash
   POST /api/auth/register
   {
     "username": "julia",
     "email": "julia@example.com",
     "password": "P@ssw0rd"
   }
   ```

2. **Login & Retrieve JWT**

   ```bash
   POST /api/auth/login
   {
     "username": "julia",
     "password": "P@ssw0rd"
   }
   ```

3. **Create a Mentorship Session**

   ```bash
   POST /api/sessions
   Authorization: Bearer <ACCESS_TOKEN>
   {
     "mentorId": "<UUID>",
     "startTime": "2025-06-01T10:00:00",
     "endTime": "2025-06-01T11:00:00"
   }
   ```

---

## 🧪 Testing

* Run all unit and integration tests:

  ```bash
  ./mvnw test
  ```
* Use Postman collection at `/docs/postman_collection.json` for end‑to‑end API workflows.

---

## 🤝 Contributing

Contributions are welcome! Please open issues or create pull requests for bug fixes, enhancements, or new features.

1. Fork the repo
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m 'Add my feature'`)
4. Push to your branch (`git push origin feature/my-feature`)
5. Open a Pull Request
