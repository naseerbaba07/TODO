# Todo Management System

A full-stack Todo Management System built using React, Spring Boot, and PostgreSQL (Neon DB).  
This project demonstrates full-stack development, REST API design, cloud database integration, and a polished modern UI.
---

# 🤖 BabaList AI — AI-Powered Task Assistant

BabaList is evolving from a traditional Todo Management System into an **AI-powered personal productivity assistant**.

With **Baba AI**, users will be able to interact with their tasks using natural language instead of manually navigating through multiple screens, filters, and forms.

Instead of asking users to remember specific commands, Baba AI understands what they mean and uses the appropriate task-management capabilities.

### 💬 Example Questions

```text
What tasks do I have today?

Show me my pending tasks.

Which tasks are overdue?

What do I need to finish this week?

What should I work on first?

How productive was I this week?

Show me all my Java-related tasks.

What tasks are due tomorrow?

Create a task to study Java tomorrow.

Make my DBMS assignment high priority.

Change my Java project deadline to Monday.

Mark my assignment as completed.

Delete my shopping task.

Break my project into smaller tasks.

---

## 🧠 How Baba AI Works

Baba AI uses a combination of **Google Gemini, Spring Boot, and PostgreSQL**.

The AI understands the user's natural-language request and determines which BabaList capability is required.

```text
                    User
                      │
                      ▼
               ┌─────────────┐
               │  Baba AI    │
               │   Gemini    │
               └──────┬──────┘
                      │
                Understand Intent
                      │
        ┌─────────────┼─────────────┐
        │             │             │
        ▼             ▼             ▼
   Task Tools    Analytics Tools  Planning
        │             │             │
        └─────────────┼─────────────┘
                      ▼
               ┌─────────────┐
               │ Spring Boot │
               │   Backend   │
               └──────┬──────┘
                      ▼
               ┌─────────────┐
               │ PostgreSQL  │
               └─────────────┘

User
 ↓
Gemini
 ↓
Tool / Function
 ↓
Spring Boot
 ↓
Service Layer
 ↓
Repository
 ↓
PostgreSQL

## 🚀 Baba AI Capabilities

### 📋 Task Management

Baba AI will understand natural-language requests for:

- Creating tasks
- Updating tasks
- Completing tasks
- Deleting tasks
- Changing deadlines
- Changing priorities
- Searching tasks
- Finding specific tasks

Example:

> "Create a task to prepare for my Java exam tomorrow."

Baba AI can understand the request and create the appropriate task.

---

### 🔎 Intelligent Task Search

Users don't need to use complicated filters.

They can simply ask:

```text
Show my Java tasks.

Do I have anything related to DBMS?

Show assignments that are still pending.

What tasks are due this week?


## 📌 Project Overview

This application allows to manage tasks with priority levels, due dates, filtering, and status tracking. It follows a clean frontend-backend separation using REST APIs.

The goal of this project was to:
 • Build a complete CRUD system
 • Connect React frontend with Spring Boot backend
 • Use a cloud-hosted PostgreSQL database
 • Design a modern UI using Material UI
 • Implement real-world UX improvements

---
### 🧠 Architecture

**Frontend (React)**  
⬇ Axios HTTP requests  
**Backend (Spring Boot REST API)**  
⬇ Spring Data JPA  
**Database (PostgreSQL on Neon Cloud)**  

### Layered Design
- **Controller Layer** → Handles API endpoints  
- **Service Layer** → Business logic  
- **Repository Layer** → Database interaction  
- **Database Layer** → PostgreSQL  
---



## ✨ Features
- Add, edit, delete tasks  
- Mark tasks as completed or pending  
- Due date support with overdue indicator  
- Task priority (High / Medium / Low)  
- Filter tasks (All / Completed / Pending)  
- Clear all tasks (with smart empty-state message)  
- Responsive modern design with dark mode  

## 📅 Productivity Enhancements
- Overdue task indicator  
- Priority tagging with color codes  
- Quick filters (All / Completed / Pending)  
- Undo option for completed tasks  

## 🎨 Modern UI

• Material UI components
• Custom gradient cards
• Priority color strip on each card
• Segmented filter bar
• Toast notifications
• Responsive layout
• Hover animations
• Dark mode toggle


---
### 🤖 AI

- Google Gemini API
- Gemini Function Calling
- AI-powered task understanding
- Natural-language task management
- AI productivity analysis
- AI task planning

## 🧱 Project Structure

- /frontend   -> React (Vite) client

- /backend    -> Spring Boot app

- /database   -> SQL scripts -->

---

## 🛠 Tech Stack

### Frontend
- React (Vite)
- Axios
- CSS
- Material UI
- React Toastify
- React Icons
- DayJS

### Backend
- Spring Boot
- Maven
- Spring Data JPA
- Lombok
- web
- Devtools
- REST APIs

### Database
- PostgreSQL (Neon Cloud DB)

### AI

- Google Gemini API
- Gemini Function Calling
- AI Task Agent
- Natural Language Processing
- AI Productivity Analysis

---


## 🛠 Database Setup (PostgreSQL with Neon DB)

1. Sign up at [Neon](https://neon.tech) and create a new project.  
2. Copy the provided connection string.  
3. Configure environment variables in `application.properties`.  
4. Run SQL migration scripts to create tables.  



```application.properties
spring.datasource.url=jdbc:postgresql://<host>/<database>?sslmode=require
spring.datasource.username=<user>
spring.datasource.password=<password>
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
```
⚠️ Keep sensitive values (username/password) out of version control. Use environment variables or a secrets manager in production.

---

## ⚙️ Environment Setup

  ### 1️⃣ Clone Repository
  ```
  git clone <repo-url>
  cd project-folder 
  ```
---
  ### 2️⃣ Backend Configuration

Add database configuration in:

``` backend/src/main/resources/application.properties ```

```application.properties
spring.datasource.url=jdbc:postgresql://<host>/<database>?sslmode=require
spring.datasource.username=<user>
spring.datasource.password=<password>
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
```
---
  ### 3️⃣ Database Table
```sql
CREATE TABLE todos (
    id BIGSERIAL PRIMARY KEY,
    workname VARCHAR(255) NOT NULL,
    work BOOLEAN NOT NULL DEFAULT FALSE,
    work_date DATE,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    CONSTRAINT priority_check
        CHECK (priority IN ('HIGH','MEDIUM','LOW'))
);
```
---

  ### ▶️ Run Backend (Spring Boot)

Navigate to backend folder: 
```
 cd myapp
 mvn spring-boot:run
```
 Backend runs at: http://localhost:8080/todos


 ## ▶️ Run Frontend (React)

Navigate to frontend folder:
```
cd frontend
npm install
npm run dev
```

Frontend runs at: http://localhost:5173

---


## 🔌 API Endpoints

| Method | Endpoint       | Description        |
|--------|----------------|--------------------|
| GET    | /todos         | Get all tasks      |
| POST   | /todos/save    | Create a new task  |
| PUT    | /todos/{id}    | Update a task      |
| DELETE | /todos/{id}    | Delete a task      |
| DELETE | /todos/clear   | Delete all tasks   |

---


## 🎯 What This Project Demonstrates

- Full-stack architecture
- REST API design
- Cloud database integration
- Modern React UI patterns
- Production-style project structure

---

## 🛡 Error Handling
- Toast notifications for API failures  
- Validation prevents empty task submission  
- Safe checks before clearing tasks  
- Graceful UI updates after API calls 

---

## 📸 Screenshots
### Dashboard View
![Dashboard Screenshot](./screenshots/dashboard.png)

### Dark Mode
![Dark Mode Screenshot](./screenshots/darkmode.png)

---
