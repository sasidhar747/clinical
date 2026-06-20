# Smart Clinic Management System — User Stories

## Roles / Personas
| Persona | Description |
|---------|-------------|
| **Admin** | Clinic administrator managing staff, reports, and system configuration |
| **Doctor** | Physician who views schedules, writes prescriptions |
| **Receptionist** | Front-desk staff handling bookings and billing |
| **Patient** | End-user visiting the clinic |

---

## Epic 1 — Patient Management

| ID | User Story | Acceptance Criteria | Priority |
|----|-----------|---------------------|----------|
| US-01 | As a **Receptionist**, I want to register a new patient so that their records are stored in the system. | Patient profile created with name, DOB, contact, address; duplicate NIC check enforced. | High |
| US-02 | As a **Patient**, I want to view and update my personal profile so that my information is current. | Patient can edit contact details; DOB and NIC are read-only after registration. | High |
| US-03 | As an **Admin**, I want to search patients by name or ID so that I can quickly retrieve records. | Search returns results in < 1 second; supports partial name match. | Medium |

## Epic 2 — Doctor & Schedule Management

| ID | User Story | Acceptance Criteria | Priority |
|----|-----------|---------------------|----------|
| US-04 | As an **Admin**, I want to add/update doctor profiles so that their specialisations and availability are visible. | Doctor profile includes name, specialisation, contact, and weekly schedule slots. | High |
| US-05 | As a **Doctor**, I want to view my appointments for the day so that I can prepare in advance. | Dashboard shows a sorted list of today's confirmed appointments. | High |
| US-06 | As a **Receptionist**, I want to see available doctors by specialisation so that I can book the right doctor. | Filter by specialisation returns available doctors with next open slot. | Medium |

## Epic 3 — Appointment Scheduling

| ID | User Story | Acceptance Criteria | Priority |
|----|-----------|---------------------|----------|
| US-07 | As a **Receptionist**, I want to book an appointment for a patient so that their visit is confirmed. | Appointment created with patient, doctor, date/time, status = SCHEDULED; conflict check prevents double-booking. | High |
| US-08 | As a **Patient**, I want to reschedule my appointment so that I can change to a convenient time. | Existing slot freed; new slot confirmed; notification sent. | Medium |
| US-09 | As a **Patient**, I want to cancel my appointment so that the slot is freed for others. | Status updated to CANCELLED; slot released; notification sent. | Medium |
| US-10 | As an **Admin**, I want to view all appointments by date range so that I can monitor clinic load. | Filterable table with date, patient, doctor, status columns; exportable to CSV. | Low |

## Epic 4 — Prescriptions

| ID | User Story | Acceptance Criteria | Priority |
|----|-----------|---------------------|----------|
| US-11 | As a **Doctor**, I want to create a digital prescription linked to an appointment so that medication history is tracked. | Prescription includes medicines, dosage, duration; linked to appointment ID. | High |
| US-12 | As a **Patient**, I want to view my prescription history so that I can reference past medications. | List of past prescriptions with date, doctor name, and download option. | Medium |

## Epic 5 — Billing

| ID | User Story | Acceptance Criteria | Priority |
|----|-----------|---------------------|----------|
| US-13 | As a **Receptionist**, I want to generate an invoice after a consultation so that the patient can pay. | Invoice auto-calculates consultation fee + extras; shows total due. | High |
| US-14 | As an **Admin**, I want to view daily/monthly revenue reports so that I can track clinic income. | Report shows total collections per day/month; generated via stored procedure. | High |

## Epic 6 — Notifications

| ID | User Story | Acceptance Criteria | Priority |
|----|-----------|---------------------|----------|
| US-15 | As a **Patient**, I want to receive an email confirmation when my appointment is booked so that I have a record. | Email sent within 1 minute of booking; includes date, time, and doctor name. | Medium |
| US-16 | As a **Patient**, I want to receive a reminder 24 hours before my appointment so that I don't forget. | Automated reminder sent 24 h before appointment time. | Low |

## Epic 7 — Reporting & Admin

| ID | User Story | Acceptance Criteria | Priority |
|----|-----------|---------------------|----------|
| US-17 | As an **Admin**, I want to generate a patient visit report for a date range so that I can analyse trends. | Report returns visit count, top doctors, and diagnosis categories for the period. | Medium |
| US-18 | As an **Admin**, I want to manage user roles so that only authorised staff access sensitive areas. | Role assignment (ADMIN/DOCTOR/RECEPTIONIST/PATIENT) persisted; changes take effect on next login. | High |
