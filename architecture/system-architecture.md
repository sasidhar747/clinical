# Smart Clinic Management System — System Architecture & Microservices

## 1. System Overview
The Smart Clinic Management System (SCMS) is a cloud-native, microservices-based application designed to digitise and streamline clinic operations including patient registration, appointment scheduling, prescriptions, billing, and reporting.

## 2. Architecture Style
- **Pattern:** Microservices Architecture
- **Communication:** RESTful HTTP/JSON between services; asynchronous events via a message broker for notifications
- **API Gateway:** Single entry-point routing all client requests to appropriate microservices
- **Frontend:** Single Page Application (HTML/CSS/JavaScript)
- **Backend:** Java 17 + Spring Boot 3.x
- **Databases:** MySQL (relational) + MongoDB (NoSQL for logs/documents)
- **Containerisation:** Docker + Docker Compose
- **CI/CD:** GitHub Actions

## 3. Microservices Identified

| # | Service | Responsibility | Port |
|---|---------|---------------|------|
| 1 | **Patient Service** | Patient registration, profile management | 8081 |
| 2 | **Doctor Service** | Doctor profiles, specialisations, schedules | 8082 |
| 3 | **Appointment Service** | Booking, rescheduling, cancellation | 8083 |
| 4 | **Prescription Service** | Digital prescriptions linked to appointments | 8084 |
| 5 | **Billing Service** | Invoice generation, payment tracking | 8085 |
| 6 | **Notification Service** | Email/SMS alerts (async via broker) | 8086 |
| 7 | **Report Service** | Analytics and stored-procedure-based reports | 8087 |
| 8 | **API Gateway** | Routing, auth filter, rate limiting | 8080 |

## 4. Architecture Diagram (Text)

```
         ┌─────────────────────────────┐
         │        React/HTML Frontend   │
         └────────────┬────────────────┘
                      │ HTTP
         ┌────────────▼────────────────┐
         │         API Gateway          │  :8080
         └──┬──┬──┬──┬──┬──┬──┬───────┘
            │  │  │  │  │  │  │
    ┌───────┘  │  │  │  │  │  └─────────────┐
    ▼          ▼  │  │  │  ▼               ▼
Patient     Doctor│  │  │ Billing      Report
Service    Service│  │  │ Service      Service
 :8081      :8082 │  │  │  :8085        :8087
                  ▼  │  ▼
             Appt  │ Prescription
             Svc   │  Service
             :8083 │  :8084
                   ▼
             Notification
               Service
               :8086
         ┌──────────────┐  ┌──────────┐
         │  MySQL DB     │  │ MongoDB  │
         └──────────────┘  └──────────┘
```

## 5. Technology Stack

| Layer | Technology |
|-------|-----------|
| Frontend | HTML5, CSS3, JavaScript (Fetch API) |
| Backend | Java 17, Spring Boot 3.x, Spring Data JPA |
| REST API | Spring Web MVC |
| Auth | JWT (JSON Web Tokens) |
| Relational DB | MySQL 8.0 |
| NoSQL DB | MongoDB 6.0 (audit logs, documents) |
| ORM | Hibernate / Spring Data JPA |
| Build Tool | Maven |
| Containerisation | Docker, Docker Compose |
| CI/CD | GitHub Actions |

## 6. Security Design
- JWT-based stateless authentication issued by API Gateway
- Role-based access: `ADMIN`, `DOCTOR`, `RECEPTIONIST`, `PATIENT`
- HTTPS enforced in production
- Secrets managed via environment variables (not hardcoded)
