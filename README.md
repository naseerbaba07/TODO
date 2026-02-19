# Todo Management System

A full-stack Todo Management System built using React, Spring Boot, and PostgreSQL.  
This project demonstrates CRUD operations, REST API integration, and modern UI features.

---

## ✨ Features

• Add new tasks  
• Edit existing tasks  
• Delete tasks  
• Mark tasks as completed / pending  
• Due date support  
• Task priority (High / Medium / Low)  
• Filter tasks (All / Completed / Pending)  
• Dark mode UI  
• Responsive modern design  

---

/frontend   -> React (Vite) app
/backend    -> Spring Boot app
/database   -> SQL scripts

---
## 🛠 Tech Stack

### Frontend
- React (Vite)
- Axios
- CSS

### Backend
- Spring Boot
- Spring Data JPA
- REST APIs

### Database
- PostgreSQL

---


---

## ⚙️ Database Setup (PostgreSQL with Neon DB)
This project uses Neon DB, a cloud-native PostgreSQL platform, instead of a locally hosted database. Neon provides serverless scaling, branching, and secure connections, making it ideal for modern applications.
Setup Instructions
- Create a Neon Project
- Sign up at Neon and create a new project.
- Copy the provided connection string.
- Configure Environment Variables 
- Add your Neon DB connection string to application.properties


```application.properties
spring.datasource.url=jdbc:postgresql://<host>/<database>?sslmode=require
spring.datasource.username=<user>
spring.datasource.password=<password>
spring.datasource.driver-class-name=org.postgresql.Driver
```
⚠️ Keep sensitive values (username/password) out of version control. Use environment variables or a secrets manager in production.


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

## ▶️ Run Backend (Spring Boot)

Navigate to backend folder: 
 cd myapp
 mvn spring-boot:run

 Backend runs at: http://localhost:8080/todos


 ## ▶️ Run Frontend (React)

Navigate to frontend folder:
cd frontend
npm install
npm run dev

Frontend runs at: http://localhost:5173

---
---
## 🔌 API Endpoints

| Method | Endpoint    | Description   |
| ------ | ----------- | ------------- |
| GET    | /todos      | Get all tasks |
| POST   | /todos/save | Create task   |
| PUT    | /todos/{id} | Update task   |
| DELETE | /todos/{id} | Delete task   |

---
