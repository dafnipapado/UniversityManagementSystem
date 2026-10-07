# University Management System

### 📋 Project Overview
University Management System is a university portal offering role-based access to various university operations. 
This server-side application is built with Java/Spring Boot, using Thymeleaf templates for rendering and MySQL (managed via 
Flyway migrations) for the database. It supports features such as role-based access control, user management, course and 
course offering management, and enrollments - with examinations planned for a future release.

##
### 🚀 Features
- Role-based access control and operations
- Teacher, student, and user creation and management
- Course and course offering management
- Semester management (including activation of the current semester)
- Student enrollment in course offerings

##
### 🛠️ Technologies
#### 💻 Backend
- Java 21
- Spring Boot 3.5
- Spring Data JPA
- Spring Security
- Lombok
- Bean Validation
- Gradle 8.14

#### 🎨 Frontend/View
- Thymeleaf
- Bootstrap 5
- Font Awesome

#### 🗄️ Database
- MySQL 8.0
- Flyway

#### 🔧 Tools
- Git / GitHub
- IntelliJ IDEA
- MySQL Workbench

##
### ⚙️ Setup
#### 📌 Prerequisites
Ensure you have the following installed on your system
- Java 21
- MySQL 8.0
- Git

#### 📝 Steps
1. Open a command prompt and navigate to the directory where you want to store the project
2. Clone the repository:
```
git clone https://github.com/dafnipapado/UniversityManagementSystem
```
3. Open the repository in IntelliJ (or your preferred IDE). 
4. **Database Setup** <br>
If you already have a MySQL database and user you'd like to use, update the credentials in `application-dev.yml`
to match them, and skip to step 4.
Otherwise, open MySQL Workbench (or your preferred MySQL client) and create a MySQL instance (if you don't already have one) 
using the port specified in application-dev.yml (default:3306).Then run the following commands to create the database and the user, 
and grant all the database privileges to the user:
```
CREATE SCHEMA IF NOT EXISTS `database_name` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `database_name`;
```
```
CREATE USER 'username'@'%' IDENTIFIED WITH caching_sha2_password BY 'password';
GRANT ALL PRIVILEGES ON database_name.* TO 'username'@'%';
FLUSH PRIVILEGES;
```
Make sure the database name, username and password used above match the values configured in `application-dev.yml`.<br>
5. Start the application:
```
./gradlew bootRun
```
You can access the application by opening http://localhost:8080 in your web browser.