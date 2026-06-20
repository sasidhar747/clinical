# 🏥 Smart Clinic Management System (SCMS)

A cloud-native, microservices-based clinic management system developed as a capstone project for the IBM Full Stack Software Developer Professional Certificate on Coursera / Skills Network.

---

## 📁 Repository Structure

```
clinical/
├── architecture/
│   └── system-architecture.md      # System design & microservices overview
├── user-stories/
│   └── user-stories.md             # User stories for all epics
├── database/
│   ├── database-design.md          # Relational (MySQL) + NoSQL (MongoDB) schemas
│   └── stored-procedures.sql       # 5 stored procedures for reporting
├── backend/
│   ├── Dockerfile                  # Multi-stage Docker build
│   ├── pom.xml                     # Maven dependencies (Spring Boot 3.x)
│   └── src/main/java/com/clinic/scms/
│       ├── entity/                 # JPA models with validations
│       │   ├── Patient.java
│       │   ├── Doctor.java
│       │   ├── Appointment.java
│       │   └── Invoice.java
│       ├── controller/             # REST API controllers
│       │   ├── PatientController.java
│       │   └── AppointmentController.java
│       └── resources/
│           └── application.properties
├── frontend/
│   ├── index.html                  # SPA — Patients, Appointments, Billing, Dashboard
│   ├── style.css                   # Responsive CSS with custom design system
│   └── app.js                      # Fetch API integration with backend
├── .github/workflows/
│   └── ci.yml                      # GitHub Actions CI pipeline
└── docker-compose.yml              # Full stack: MySQL + MongoDB + Backend + Frontend
```

---

## 🚀 Quick Start (Docker Compose)

```bash
git clone https://github.com/sasidhar747/clinical.git
cd clinical
docker-compose up --build
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost |
| Backend API | http://localhost:8080/api/v1 |
| MySQL | localhost:3306 |
| MongoDB | localhost:27017 |

---

## 🛠️ Technology Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA + Hibernate |
| Auth | JWT (jjwt 0.12) |
| Relational DB | MySQL 8.0 |
| NoSQL DB | MongoDB 6.0 |
| Frontend | HTML5, CSS3, Vanilla JavaScript |
| Build | Maven |
| Containers | Docker, Docker Compose |
| CI/CD | GitHub Actions |

---

## 📋 Key Features

- ✅ Patient registration & search
- ✅ Doctor profile management
- ✅ Appointment booking with conflict detection
- ✅ Digital prescriptions linked to appointments
- ✅ Invoice generation & payment tracking
- ✅ 5 stored procedures for reporting
- ✅ Audit logs in MongoDB
- ✅ JWT authentication & RBAC
- ✅ Fully containerised with Docker Compose
- ✅ GitHub Actions CI pipeline

---

## 📌 API Endpoints

| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/v1/patients` | Register a new patient |
| GET | `/api/v1/patients` | List all patients |
| GET | `/api/v1/patients/{id}` | Get patient by ID |
| GET | `/api/v1/patients/search?name=` | Search patients |
| PUT | `/api/v1/patients/{id}` | Update patient |
| DELETE | `/api/v1/patients/{id}` | Delete patient |
| POST | `/api/v1/appointments` | Book appointment |
| GET | `/api/v1/appointments` | List appointments |
| PATCH | `/api/v1/appointments/{id}/status` | Update status |
| DELETE | `/api/v1/appointments/{id}` | Cancel appointment |

---

## 👨‍💻 Author

**Sasidhar Sai Varma Gamini**  
B.Tech Information Technology — Vel Tech R&D Institute of Science and Technology (2023–2027)  
GitHub: [@sasidhar747](https://github.com/sasidhar747)  
LinkedIn: [linkedin.com/in/sasidhar-sai-varma-gamini](https://linkedin.com/in/sasidhar-sai-varma-gamini)
