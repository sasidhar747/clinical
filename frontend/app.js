// ── Config ───────────────────────────────────
const BASE_URL = 'http://localhost:8080/api/v1';

// ── Navigation ────────────────────────────────
function showSection(id) {
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.getElementById(id).classList.add('active');
  if (id === 'patients')     loadPatients();
  if (id === 'appointments') loadAppointments();
  if (id === 'billing')      loadInvoices();
  if (id === 'dashboard')    loadDashboard();
}

// ── Modal ─────────────────────────────────────
function openModal(id)  { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }

// ── Toast ─────────────────────────────────────
function showToast(msg, type = 'info') {
  const t = document.getElementById('toast');
  t.textContent = msg;
  t.style.background = type === 'error' ? '#dc2626' : type === 'success' ? '#16a34a' : '#1e293b';
  t.classList.add('show');
  setTimeout(() => t.classList.remove('show'), 3000);
}

// ── API Helper ────────────────────────────────
async function apiFetch(endpoint, options = {}) {
  try {
    const token = localStorage.getItem('jwt_token');
    const headers = { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}) };
    const res = await fetch(`${BASE_URL}${endpoint}`, { ...options, headers });
    if (!res.ok) throw new Error(await res.text());
    return res.status === 204 ? null : await res.json();
  } catch (err) {
    showToast(err.message || 'Network error', 'error');
    return null;
  }
}

// ── Dashboard ─────────────────────────────────
async function loadDashboard() {
  const patients = await apiFetch('/patients') || [];
  const today = new Date().toISOString().split('T')[0];
  const appointments = await apiFetch(`/appointments?date=${today}`) || [];
  document.getElementById('totalPatients').textContent = patients.length;
  document.getElementById('todayAppointments').textContent = appointments.length;
  document.getElementById('totalDoctors').textContent = '—';
  document.getElementById('pendingInvoices').textContent = '—';
}

// ── Patients ──────────────────────────────────
async function loadPatients() {
  const data = await apiFetch('/patients') || [];
  renderPatients(data);
}

function renderPatients(data) {
  const tbody = document.getElementById('patientsBody');
  tbody.innerHTML = data.length === 0
    ? '<tr><td colspan="6" style="text-align:center;color:#94a3b8;padding:2rem">No patients found</td></tr>'
    : data.map(p => `
        <tr>
          <td>${p.patientId}</td>
          <td>${p.firstName} ${p.lastName}</td>
          <td>${p.dateOfBirth || '—'}</td>
          <td>${p.phone}</td>
          <td>${p.email || '—'}</td>
          <td>
            <button class="btn btn-sm" onclick="editPatient(${p.patientId})">Edit</button>
            <button class="btn btn-danger btn-sm" style="margin-left:.4rem" onclick="deletePatient(${p.patientId})">Delete</button>
          </td>
        </tr>`).join('');
}

async function searchPatients() {
  const q = document.getElementById('patientSearch').value.trim();
  const data = q ? await apiFetch(`/patients/search?name=${encodeURIComponent(q)}`) || [] : await apiFetch('/patients') || [];
  renderPatients(data);
}

async function submitPatient() {
  const patient = {
    firstName:   document.getElementById('p_firstName').value,
    lastName:    document.getElementById('p_lastName').value,
    dateOfBirth: document.getElementById('p_dob').value,
    gender:      document.getElementById('p_gender').value,
    nic:         document.getElementById('p_nic').value,
    email:       document.getElementById('p_email').value,
    phone:       document.getElementById('p_phone').value,
    address:     document.getElementById('p_address').value,
  };
  if (!patient.firstName || !patient.lastName || !patient.nic || !patient.phone) {
    showToast('Please fill in all required fields', 'error'); return;
  }
  const result = await apiFetch('/patients', { method: 'POST', body: JSON.stringify(patient) });
  if (result) {
    showToast('Patient registered successfully!', 'success');
    closeModal('addPatientModal');
    loadPatients();
  }
}

async function deletePatient(id) {
  if (!confirm('Delete this patient?')) return;
  const result = await apiFetch(`/patients/${id}`, { method: 'DELETE' });
  if (result !== undefined) { showToast('Patient deleted', 'success'); loadPatients(); }
}

// ── Appointments ──────────────────────────────
async function loadAppointments() {
  const data = await apiFetch('/appointments') || [];
  const tbody = document.getElementById('appointmentsBody');
  tbody.innerHTML = data.length === 0
    ? '<tr><td colspan="7" style="text-align:center;color:#94a3b8;padding:2rem">No appointments found</td></tr>'
    : data.map(a => `
        <tr>
          <td>${a.appointmentId}</td>
          <td>${a.patient?.firstName || ''} ${a.patient?.lastName || ''}</td>
          <td>${a.doctor?.firstName || ''} ${a.doctor?.lastName || ''}</td>
          <td>${a.appointmentDate}</td>
          <td>${a.appointmentTime}</td>
          <td><span class="badge badge-${a.status?.toLowerCase()}">${a.status}</span></td>
          <td>
            <button class="btn btn-danger btn-sm" onclick="cancelAppt(${a.appointmentId})">Cancel</button>
          </td>
        </tr>`).join('');
}

async function cancelAppt(id) {
  if (!confirm('Cancel this appointment?')) return;
  await apiFetch(`/appointments/${id}`, { method: 'DELETE' });
  showToast('Appointment cancelled', 'success');
  loadAppointments();
}

// ── Billing ───────────────────────────────────
async function loadInvoices() {
  showToast('Billing module — connect to /api/v1/invoices endpoint', 'info');
}

// ── Auth ──────────────────────────────────────
function logout() {
  localStorage.removeItem('jwt_token');
  showToast('Logged out', 'info');
  setTimeout(() => window.location.reload(), 1000);
}

// ── Init ──────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
  showSection('dashboard');
});
