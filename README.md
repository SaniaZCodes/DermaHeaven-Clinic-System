# DermaHeaven Aesthetic - Management System 🏥✨

**DermaHeaven** is a comprehensive GUI-based clinic management system built in Java using Swing, designed to streamline operations for aesthetic clinics. It handles patient and doctor interactions, appointment scheduling, feedback management, and data persistence.

---

## 🚀 Tech Stack
* **Frontend:** Java Swing (Panels, Tables, Dialogs, Custom UI Components)
* **Backend:** Java (Object-Oriented Logic)
* **Persistence:** Serialized objects (`.ser` files) for data storage
* **Architecture:** Manager-based pattern (Separation of Concerns)

---

## 🛠 Key Features

### 👤 User Management
* **Patient & Doctor Portals:** Secure Login/Signup with password validation (8+ chars, digits, special chars).
* **Doctor Profiles:** Specializations, working hours, and specific discount rates.
* **Patient Profiles:** Real-time viewing of personal details and booking history.

### 📅 Appointment Management
* **Smart Booking:** Dialog-based selection for doctors, dates, and times with automated availability checks.
* **Status Tracking:** Real-time updates for `BOOKED`, `COMPLETED`, and `CANCELLED` statuses.
* **Rescheduling:** Easy-to-use interface to change appointment timings.

### 💆 Services & Packages
* **Individual Services:** Categorized skin and laser treatments.
* **Special Packages:** Pre-defined bundles with automated 15% discounts.
* **Custom Package Builder:** A tool for patients to select specific services and receive a 30% discount.

### ⭐️ Feedback System
* **Rating Analytics:** Patients can rate (1-5 stars) and comment on services.
* **Doctor Insights:** Doctors can view their personal average ratings and feedback history.
* **Clinic Overview:** Public view of top 10 feedbacks and overall clinic rating.

---

## 🔄 Main Workflow
1. **Startup:** `MainApplication.java` initializes managers and loads saved data.
2. **Welcome Screen:** Beautiful gradient interface to choose between Doctor or Patient paths.
3. **Dashboards:** - **Patients:** Book appointments, manage profile, and submit feedback.
   - **Doctors:** View schedule, mark appointments as complete, and check performance.
4. **Data Saving:** All changes are automatically persisted via Serialization.

---

## 💻 How to Run
1. Clone this repository.
2. Ensure you have **Java JDK** installed.
3. Open the project in your favorite IDE (IntelliJ, Eclipse, or VS Code).
4. Run `MainApplication.java` to start the system.

---
*Developed as a demonstration of strong OOP principles, GUI design, and real-world software engineering.*
