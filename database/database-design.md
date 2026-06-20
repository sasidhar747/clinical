# Smart Clinic Management System — Database Design

## Part A: Relational Database Design (MySQL)

### ER Diagram (Text Representation)

```
PATIENTS ──< APPOINTMENTS >── DOCTORS
                 │
                 ├──< PRESCRIPTIONS >── PRESCRIPTION_ITEMS
                 │
                 └──< INVOICES >── INVOICE_ITEMS

USERS ──< USER_ROLES
```

### Table Definitions

#### 1. patients
```sql
CREATE TABLE patients (
    patient_id    INT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender        ENUM('MALE','FEMALE','OTHER') NOT NULL,
    nic           VARCHAR(20) UNIQUE NOT NULL,
    email         VARCHAR(150) UNIQUE,
    phone         VARCHAR(15) NOT NULL,
    address       TEXT,
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 2. doctors
```sql
CREATE TABLE doctors (
    doctor_id      INT AUTO_INCREMENT PRIMARY KEY,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    specialisation VARCHAR(150) NOT NULL,
    email          VARCHAR(150) UNIQUE NOT NULL,
    phone          VARCHAR(15) NOT NULL,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    is_active      BOOLEAN DEFAULT TRUE,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

#### 3. appointments
```sql
CREATE TABLE appointments (
    appointment_id   INT AUTO_INCREMENT PRIMARY KEY,
    patient_id       INT NOT NULL,
    doctor_id        INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status           ENUM('SCHEDULED','COMPLETED','CANCELLED','NO_SHOW') DEFAULT 'SCHEDULED',
    notes            TEXT,
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id)  REFERENCES doctors(doctor_id),
    UNIQUE KEY uq_doctor_slot (doctor_id, appointment_date, appointment_time)
);
```

#### 4. prescriptions
```sql
CREATE TABLE prescriptions (
    prescription_id  INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id   INT NOT NULL UNIQUE,
    issued_date      DATE NOT NULL,
    notes            TEXT,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);
```

#### 5. prescription_items
```sql
CREATE TABLE prescription_items (
    item_id         INT AUTO_INCREMENT PRIMARY KEY,
    prescription_id INT NOT NULL,
    medicine_name   VARCHAR(200) NOT NULL,
    dosage          VARCHAR(100) NOT NULL,
    frequency       VARCHAR(100) NOT NULL,
    duration_days   INT NOT NULL,
    FOREIGN KEY (prescription_id) REFERENCES prescriptions(prescription_id)
);
```

#### 6. invoices
```sql
CREATE TABLE invoices (
    invoice_id       INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id   INT NOT NULL UNIQUE,
    issued_date      DATE NOT NULL,
    total_amount     DECIMAL(10,2) NOT NULL,
    paid_amount      DECIMAL(10,2) DEFAULT 0.00,
    status           ENUM('PENDING','PAID','PARTIALLY_PAID','CANCELLED') DEFAULT 'PENDING',
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);
```

#### 7. invoice_items
```sql
CREATE TABLE invoice_items (
    item_id     INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id  INT NOT NULL,
    description VARCHAR(200) NOT NULL,
    quantity    INT DEFAULT 1,
    unit_price  DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id)
);
```

#### 8. users
```sql
CREATE TABLE users (
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(80) UNIQUE NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       ENUM('ADMIN','DOCTOR','RECEPTIONIST','PATIENT') NOT NULL,
    is_active  BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

---

## Part B: NoSQL Database Design (MongoDB)

### Collections

#### 1. audit_logs
Stores every significant action for compliance and traceability.
```json
{
  "_id": "ObjectId",
  "timestamp": "ISODate",
  "action": "APPOINTMENT_CREATED",
  "performed_by": "receptionist_user_id",
  "entity": "appointments",
  "entity_id": 42,
  "details": {
    "patient_id": 10,
    "doctor_id": 3,
    "slot": "2026-06-25T10:00:00"
  },
  "ip_address": "192.168.1.5"
}
```

#### 2. notifications
```json
{
  "_id": "ObjectId",
  "patient_id": 10,
  "type": "APPOINTMENT_REMINDER",
  "channel": "EMAIL",
  "recipient": "patient@email.com",
  "subject": "Appointment Reminder — Dr. Smith",
  "body": "Your appointment is scheduled for 2026-06-25 at 10:00 AM.",
  "status": "SENT",
  "sent_at": "ISODate",
  "created_at": "ISODate"
}
```

#### 3. medical_documents
```json
{
  "_id": "ObjectId",
  "patient_id": 10,
  "document_type": "LAB_REPORT",
  "uploaded_by": "doctor_user_id",
  "upload_date": "ISODate",
  "filename": "blood_test_2026.pdf",
  "content_base64": "<base64_encoded_content>",
  "tags": ["blood", "CBC", "2026"]
}
```

### Indexes (MongoDB)
```js
db.audit_logs.createIndex({ "timestamp": -1 });
db.audit_logs.createIndex({ "entity": 1, "entity_id": 1 });
db.notifications.createIndex({ "patient_id": 1, "status": 1 });
db.medical_documents.createIndex({ "patient_id": 1, "document_type": 1 });
```
