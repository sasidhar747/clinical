-- ============================================================
-- Smart Clinic Management System — Stored Procedures
-- Database: MySQL 8.0
-- ============================================================

DELIMITER $$

-- ──────────────────────────────────────────
-- 1. Daily Revenue Report
--    Returns total billed and collected per day for a date range
-- ──────────────────────────────────────────
CREATE PROCEDURE sp_daily_revenue_report(
    IN p_start_date DATE,
    IN p_end_date   DATE
)
BEGIN
    SELECT
        i.issued_date                          AS report_date,
        COUNT(i.invoice_id)                    AS total_invoices,
        SUM(i.total_amount)                    AS total_billed,
        SUM(i.paid_amount)                     AS total_collected,
        SUM(i.total_amount - i.paid_amount)    AS outstanding_balance
    FROM invoices i
    WHERE i.issued_date BETWEEN p_start_date AND p_end_date
      AND i.status != 'CANCELLED'
    GROUP BY i.issued_date
    ORDER BY i.issued_date ASC;
END$$

-- ──────────────────────────────────────────
-- 2. Doctor Appointment Summary
--    Returns appointment count and completion rate per doctor
-- ──────────────────────────────────────────
CREATE PROCEDURE sp_doctor_appointment_summary(
    IN p_start_date DATE,
    IN p_end_date   DATE
)
BEGIN
    SELECT
        d.doctor_id,
        CONCAT(d.first_name, ' ', d.last_name)  AS doctor_name,
        d.specialisation,
        COUNT(a.appointment_id)                  AS total_appointments,
        SUM(CASE WHEN a.status = 'COMPLETED'  THEN 1 ELSE 0 END) AS completed,
        SUM(CASE WHEN a.status = 'CANCELLED'  THEN 1 ELSE 0 END) AS cancelled,
        SUM(CASE WHEN a.status = 'NO_SHOW'    THEN 1 ELSE 0 END) AS no_shows,
        ROUND(
            100.0 * SUM(CASE WHEN a.status = 'COMPLETED' THEN 1 ELSE 0 END)
            / NULLIF(COUNT(a.appointment_id), 0), 2
        ) AS completion_rate_pct
    FROM doctors d
    LEFT JOIN appointments a
        ON d.doctor_id = a.doctor_id
        AND a.appointment_date BETWEEN p_start_date AND p_end_date
    WHERE d.is_active = TRUE
    GROUP BY d.doctor_id, doctor_name, d.specialisation
    ORDER BY total_appointments DESC;
END$$

-- ──────────────────────────────────────────
-- 3. Patient Visit History
--    Returns full visit timeline for a single patient
-- ──────────────────────────────────────────
CREATE PROCEDURE sp_patient_visit_history(
    IN p_patient_id INT
)
BEGIN
    SELECT
        a.appointment_id,
        a.appointment_date,
        a.appointment_time,
        CONCAT(d.first_name, ' ', d.last_name) AS doctor_name,
        d.specialisation,
        a.status,
        a.notes                                AS visit_notes,
        COALESCE(i.total_amount, 0)            AS invoice_amount,
        COALESCE(i.status, 'N/A')             AS invoice_status
    FROM appointments a
    JOIN doctors  d ON a.doctor_id  = d.doctor_id
    LEFT JOIN invoices i ON a.appointment_id = i.appointment_id
    WHERE a.patient_id = p_patient_id
    ORDER BY a.appointment_date DESC, a.appointment_time DESC;
END$$

-- ──────────────────────────────────────────
-- 4. Monthly Appointment Statistics
-- ──────────────────────────────────────────
CREATE PROCEDURE sp_monthly_appointment_stats(
    IN p_year INT
)
BEGIN
    SELECT
        MONTH(a.appointment_date)                AS month_num,
        MONTHNAME(a.appointment_date)            AS month_name,
        COUNT(a.appointment_id)                  AS total_appointments,
        COUNT(DISTINCT a.patient_id)             AS unique_patients,
        SUM(CASE WHEN a.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed,
        SUM(CASE WHEN a.status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled
    FROM appointments a
    WHERE YEAR(a.appointment_date) = p_year
    GROUP BY MONTH(a.appointment_date), MONTHNAME(a.appointment_date)
    ORDER BY month_num ASC;
END$$

-- ──────────────────────────────────────────
-- 5. Outstanding Payments Report
-- ──────────────────────────────────────────
CREATE PROCEDURE sp_outstanding_payments()
BEGIN
    SELECT
        i.invoice_id,
        CONCAT(p.first_name, ' ', p.last_name)  AS patient_name,
        p.phone                                  AS patient_phone,
        i.issued_date,
        i.total_amount,
        i.paid_amount,
        (i.total_amount - i.paid_amount)         AS balance_due,
        DATEDIFF(CURDATE(), i.issued_date)       AS days_outstanding,
        i.status
    FROM invoices i
    JOIN appointments a ON i.appointment_id = a.appointment_id
    JOIN patients p     ON a.patient_id     = p.patient_id
    WHERE i.status IN ('PENDING', 'PARTIALLY_PAID')
    ORDER BY days_outstanding DESC;
END$$

DELIMITER ;

-- ──────────────────────────────────────────
-- Sample Usage
-- ──────────────────────────────────────────
-- CALL sp_daily_revenue_report('2026-06-01', '2026-06-30');
-- CALL sp_doctor_appointment_summary('2026-06-01', '2026-06-30');
-- CALL sp_patient_visit_history(1);
-- CALL sp_monthly_appointment_stats(2026);
-- CALL sp_outstanding_payments();
