const API_BASE = "/api/students";
const FETCH_OPTIONS = { credentials: "same-origin" };

const studentForm = document.getElementById("studentForm");
const studentTableBody = document.getElementById("studentTableBody");
const tableMessage = document.getElementById("tableMessage");
const formTitle = document.getElementById("formTitle");
const tableTitle = document.getElementById("tableTitle");
const saveBtn = document.getElementById("saveBtn");
const cancelBtn = document.getElementById("cancelBtn");
const refreshBtn = document.getElementById("refreshBtn");
const logoutBtn = document.getElementById("logoutBtn");
const professorInfo = document.getElementById("professorInfo");

const studentIdInput = document.getElementById("studentId");
const firstNameInput = document.getElementById("firstName");
const lastNameInput = document.getElementById("lastName");
const emailInput = document.getElementById("email");
const branchInput = document.getElementById("branch");
const yearInput = document.getElementById("year");

let editingId = null;
let professorBranch = "";

window.onload = () => {
    initPage();
};

async function initPage() {
    try {
        const response = await fetch("/api/me", FETCH_OPTIONS);

        if (!response.ok) {
            window.location.href = "/login";
            return;
        }

        const professor = await response.json();
        professorBranch = professor.branch;
        branchInput.value = professorBranch;
        professorInfo.textContent = `${professor.username} | Branch: ${professor.branch}`;
        tableTitle.textContent = `${professor.branch} Students`;

        loadStudents();
    } catch (error) {
        window.location.href = "/login";
    }
}

refreshBtn.addEventListener("click", loadStudents);

logoutBtn.addEventListener("click", async () => {
    await fetch("/api/logout", { method: "POST", ...FETCH_OPTIONS });
    window.location.href = "/login";
});

cancelBtn.addEventListener("click", resetForm);

studentForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const payload = {
        firstName: firstNameInput.value.trim(),
        lastName: lastNameInput.value.trim(),
        email: emailInput.value.trim(),
        branch: professorBranch,
        year: Number(yearInput.value)
    };

    try {
        const options = {
            method: editingId ? "PUT" : "POST",
            ...FETCH_OPTIONS,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        };

        const url = editingId ? `${API_BASE}/${editingId}` : API_BASE;
        const response = await fetch(url, options);

        if (!response.ok) {
            throw new Error("Failed to save student.");
        }

        resetForm();
        loadStudents();
    } catch (error) {
        tableMessage.textContent = error.message;
        tableMessage.classList.remove("success");
    }
});

async function loadStudents() {
    tableMessage.textContent = "";
    studentTableBody.innerHTML = `
        <tr>
            <td colspan="7" class="empty-row">Loading students...</td>
        </tr>
    `;

    try {
        const response = await fetch(API_BASE, FETCH_OPTIONS);

        if (response.status === 401) {
            window.location.href = "/login";
            return;
        }

        if (!response.ok) {
            throw new Error("Failed to load students.");
        }

        const students = await response.json();
        renderTable(students);
    } catch (error) {
        studentTableBody.innerHTML = `
            <tr>
                <td colspan="7" class="empty-row">Could not load students.</td>
            </tr>
        `;
        tableMessage.textContent = error.message;
    }
}

function renderTable(students) {
    if (!students.length) {
        studentTableBody.innerHTML = `
            <tr>
                <td colspan="7" class="empty-row">No Records Found</td>
            </tr>
        `;
        return;
    }

    studentTableBody.innerHTML = students.map((student) => `
        <tr>
            <td>${student.id}</td>
            <td>${student.firstName}</td>
            <td>${student.lastName}</td>
            <td>${student.email}</td>
            <td>${student.branch}</td>
            <td>${student.year}</td>
            <td class="action-cell">
                <button class="btn btn-primary btn-small" onclick="editStudent(${student.id})">Edit</button>
                <button class="btn btn-danger btn-small" onclick="deleteStudent(${student.id})">Delete</button>
            </td>
        </tr>
    `).join("");
}

async function editStudent(id) {
    try {
        const response = await fetch(`${API_BASE}/${id}`, FETCH_OPTIONS);

        if (!response.ok) {
            throw new Error("Failed to load student.");
        }

        const student = await response.json();

        editingId = student.id;
        studentIdInput.value = student.id;
        firstNameInput.value = student.firstName;
        lastNameInput.value = student.lastName;
        emailInput.value = student.email;
        branchInput.value = student.branch;
        yearInput.value = student.year;

        formTitle.textContent = "Edit Student";
        saveBtn.textContent = "Update";
        cancelBtn.hidden = false;
    } catch (error) {
        tableMessage.textContent = error.message;
    }
}

async function deleteStudent(id) {
    if (!confirm("Delete this student?")) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/${id}`, {
            method: "DELETE",
            ...FETCH_OPTIONS
        });

        if (!response.ok) {
            throw new Error("Failed to delete student.");
        }

        loadStudents();
    } catch (error) {
        tableMessage.textContent = error.message;
    }
}

function resetForm() {
    editingId = null;
    studentForm.reset();
    studentIdInput.value = "";
    branchInput.value = professorBranch;
    formTitle.textContent = "Add Student";
    saveBtn.textContent = "Save";
    cancelBtn.hidden = true;
}

window.editStudent = editStudent;
window.deleteStudent = deleteStudent;
