# MentorConnect — EdTech Mentorship Platform

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.4-brightgreen.svg)](https://spring.io/projects/spring-boot)

---

## Overview

**MentorConnect** is a centralized, locally‑adapted EdTech mentorship platform built with **Java 17** and **Spring Boot**. It connects learners across Kyrgyzstan with experienced mentors tailored to their interests and career goals. By combining mentor-mentee matching, real‑time communication, and progress tracking, MentorConnect empowers students to gain real‑world insights and clear pathways for academic and professional growth.

## Table of Contents

1. [Key Features](#key-features)
2. [Architecture & Tech Stack](#architecture--tech-stack)
3. [Data Model](#data-model)
4. [Getting Started](#getting-started)

   * [Prerequisites](#prerequisites)
   * [Clone & Run](#clone--run)
   * [API Documentation](#api-documentation)
5. [Testing](#testing)
6. [Contributing](#contributing)

---

## Key Features

* **User Onboarding & Security**: JWT‑powered registration, login, and role‑based access control.
* **Profile Management**: Create detailed mentor & mentee profiles with expertise tags and goals.
* **Smart Matching**: Algorithmic pairing of mentors and learners by interests, availability, and experience.
* **Session Scheduling**: Book mentorship sessions and schedule. 
* **Real‑Time Communication**: In‑app messaging via WebSockets.
* **Progress Tracking**: Learning goals, milestones, ratings, and feedback loops.
* **Administration Tools**: Dashboard for mentors aproval and moderation tools.
* **Interactive API**: Swagger‑documented endpoints for easy integration with web and mobile frontends.

---

## Architecture & Tech Stack

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

## Data Model
<img src="https://github.com/user-attachments/assets/c4f854c0-6152-414b-8bbb-b9b728a928a3" alt="tables UML" width="400" />



---

## Getting Started

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

## Testing

* Run all unit and integration tests:

  ```bash
  ./mvnw test
  ```
* Use Postman collection at `/docs/postman_collection.json` for end‑to‑end API workflows.

---

## Contributing

Contributions are welcome! Please open issues or create pull requests for bug fixes, enhancements, or new features.

1. Fork the repo
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m 'Add my feature'`)
4. Push to your branch (`git push origin feature/my-feature`)
5. Open a Pull Request
