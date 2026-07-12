/* ── Lamp pull cord ── */
const scene = document.getElementById("scene");
const cordHandle = document.getElementById("cordHandle");
const cordLine = document.getElementById("cordLine");

const BASE_CORD = 50;
const PULL_THRESHOLD = 50;
const MAX_PULL = 75;

let dragging = false;
let startY = 0;
let pullDistance = 0;

function resetCord() {
    cordHandle.style.transform = "";
    cordLine.style.height = BASE_CORD + "px";
}

function turnOnLight() {
    scene.classList.add("lit");
    document.body.classList.add("lit");
    resetCord();
    cordHandle.classList.remove("dragging");
}

function onPullStart(clientY) {
    if (scene.classList.contains("lit")) return;
    dragging = true;
    startY = clientY;
    cordHandle.classList.add("dragging");
}

function onPullMove(clientY) {
    if (!dragging) return;
    pullDistance = Math.min(MAX_PULL, Math.max(0, clientY - startY));
    cordHandle.style.transform = `translateY(${pullDistance}px)`;
    cordLine.style.height = BASE_CORD + pullDistance + "px";
}

function onPullEnd() {
    if (!dragging) return;
    dragging = false;
    cordHandle.classList.remove("dragging");

    if (pullDistance >= PULL_THRESHOLD) {
        turnOnLight();
    } else {
        resetCord();
    }

    pullDistance = 0;
}

cordHandle.addEventListener("mousedown", (e) => {
    e.preventDefault();
    onPullStart(e.clientY);
});

document.addEventListener("mousemove", (e) => onPullMove(e.clientY));
document.addEventListener("mouseup", onPullEnd);

cordHandle.addEventListener("touchstart", (e) => {
    e.preventDefault();
    onPullStart(e.touches[0].clientY);
}, { passive: false });

document.addEventListener("touchmove", (e) => {
    if (dragging) onPullMove(e.touches[0].clientY);
}, { passive: true });

document.addEventListener("touchend", onPullEnd);

/* ── Login + register ── */
const loginForm = document.getElementById("loginForm");
const messageEl = document.getElementById("message");
const createAccountSection = document.getElementById("createAccountSection");
const createAccountBtn = document.getElementById("createAccountBtn");
const branchSelect = document.getElementById("branch");

function getCredentials() {
    return {
        username: document.getElementById("username").value.trim(),
        password: document.getElementById("password").value
    };
}

function showMessage(text, isSuccess) {
    messageEl.textContent = text;
    messageEl.classList.toggle("success", isSuccess);
}

function hideCreateAccount() {
    createAccountSection.hidden = true;
}

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    hideCreateAccount();
    showMessage("", false);

    const credentials = getCredentials();

    try {
        const response = await fetch("/api/login", {
            method: "POST",
            credentials: "same-origin",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(credentials)
        });

        if (!response.ok) {
            throw new Error("Login request failed.");
        }

        const data = await response.json();

        if (data.status) {
            showMessage(data.message, true);
            window.location.href = "/students";
        } else if (data.userExists === false) {
            showMessage(data.message, false);
            createAccountSection.hidden = false;
        } else {
            showMessage(data.message, false);
        }
    } catch (error) {
        showMessage(error.message, false);
    }
});

createAccountBtn.addEventListener("click", async () => {
    const credentials = getCredentials();
    const branch = branchSelect.value.trim();

    if (!credentials.username || !credentials.password) {
        showMessage("Email and password are required.", false);
        return;
    }

    if (!branch) {
        showMessage("Please select your branch.", false);
        return;
    }

    try {
        const response = await fetch("/api/register", {
            method: "POST",
            credentials: "same-origin",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                username: credentials.username,
                password: credentials.password,
                branch: branch
            })
        });

        if (!response.ok) {
            throw new Error("Create account request failed.");
        }

        const data = await response.json();

        if (data.status) {
            hideCreateAccount();
            showMessage(data.message, true);
            window.location.href = "/students";
        } else {
            showMessage(data.message, false);
        }
    } catch (error) {
        showMessage(error.message, false);
    }
});
