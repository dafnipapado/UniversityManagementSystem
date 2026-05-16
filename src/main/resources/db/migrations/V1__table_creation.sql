CREATE TABLE regions(
	id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    
    CONSTRAINT pk_regions PRIMARY KEY (id),
    CONSTRAINT uk_regions_name UNIQUE (name) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE departments(
	id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    
    CONSTRAINT pk_departments PRIMARY KEY (id),
    CONSTRAINT uk_departments_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE roles(
	id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    
    CONSTRAINT pk_roles PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE capabilities(
	id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    
    CONSTRAINT pk_capabilities PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE roles_capabilities(
	role_id BIGINT NOT NULL ,
    capability_id BIGINT NOT NULL,
    PRIMARY KEY(role_id, capability_id),
    
    CONSTRAINT fk_roles_capabilities_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
      ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_roles_capabilities_capability_id FOREIGN KEY (capability_id) REFERENCES capabilities (id)
      ON UPDATE CASCADE ON DELETE CASCADE,
	INDEX idx_roles_capabilities_capability_id (capability_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE users(
	id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    deleted_at DATETIME NULL,
    
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT fk_users_role_id FOREIGN KEY (role_id) REFERENCES roles (id)
      ON UPDATE CASCADE ON DELETE SET NULL,
	INDEX idx_users_role_id (role_id),
    INDEX idx_users_deleted (deleted),
    INDEX idx_users_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE users_info(
	id BIGINT NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    afm VARCHAR(9) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telephone VARCHAR(10),
    zipcode VARCHAR(5),
    user_id BIGINT NOT NULL,
	region_id BIGINT,
    
    CONSTRAINT pk_users_info PRIMARY KEY (id),
    CONSTRAINT uk_users_info_afm UNIQUE (afm),
    CONSTRAINT uk_users_info_email UNIQUE (email),
    CONSTRAINT fk_users_info_user_id FOREIGN KEY (user_id) REFERENCES users (id)
      ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_users_info_region_id FOREIGN KEY (region_id) REFERENCES regions (id)
      ON UPDATE CASCADE ON DELETE SET NULL,
	INDEX idx_users_info_lastname (lastname),
    INDEX idx_users_info_region_id (region_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE teachers(
	id BIGINT NOT NULL AUTO_INCREMENT,
    teacherAM VARCHAR(5) NOT NULL,
    uuid BINARY(16) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    deleted_at DATETIME NULL,
    
    CONSTRAINT pk_teachers PRIMARY KEY (id),
    CONSTRAINT uk_teachers_teacherAm UNIQUE (teacherAM),
    CONSTRAINT uk_teachers_uuid UNIQUE (uuid),
    CONSTRAINT fk_teachers_user_id FOREIGN KEY (user_id) REFERENCES users (id) 
      ON UPDATE CASCADE ON DELETE CASCADE,
    INDEX idx_teachers_deleted (deleted),
    INDEX idx_teachers_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE students(
	id BIGINT NOT NULL AUTO_INCREMENT,
    studentAM VARCHAR(5) NOT NULL,
    uuid BINARY(16) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    deleted_at DATETIME NULL,
    
    CONSTRAINT pk_students PRIMARY KEY (id),
    CONSTRAINT uk_students_studentAm UNIQUE (studentAM),
    CONSTRAINT uk_students_uuid UNIQUE (uuid),
    CONSTRAINT fk_students_user_id FOREIGN KEY (user_id) REFERENCES users (id)
      ON UPDATE CASCADE ON DELETE CASCADE,
	INDEX idx_students_deleted (deleted),
    INDEX idx_students_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE examinations(
	id BIGINT NOT NULL AUTO_INCREMENT,
    examination_date DATE NOT NULL,
    
    CONSTRAINT pk_examination PRIMARY KEY (id),
    INDEX idx_examinations_examination_date (examination_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE exam_results(
	id BIGINT NOT NULL AUTO_INCREMENT,
    grade INT,
    graded TINYINT(1) NOT NULL DEFAULT 0,
    examination_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    
    CONSTRAINT pk_exam_results PRIMARY KEY (id),
	CONSTRAINT uk_exam_results_examination_student UNIQUE (examination_id, student_id),
    CONSTRAINT fk_exam_results_examination_id FOREIGN KEY (examination_id) REFERENCES examinations (id)
      ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_exam_results_student_id FOREIGN KEY (student_id) REFERENCES students (id)
      ON UPDATE CASCADE ON DELETE CASCADE,
    INDEX idx_exam_results_graded (graded),
	INDEX idx_exam_results_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE semesters(
	id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    year YEAR NOT NULL,
    
    CONSTRAINT pk_semesters PRIMARY KEY (id),
    CONSTRAINT uk_semesters_name_year UNIQUE (name, year),
	INDEX idx_semesters_year (year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE courses(
	id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    ects INT NOT NULL,
    department_id BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    deleted_at DATETIME NULL,
    
    CONSTRAINT pk_courses PRIMARY KEY (id),
    CONSTRAINT uk_courses_code UNIQUE (code),
    CONSTRAINT uk_courses_name UNIQUE (name),
    CONSTRAINT fk_courses_department_id FOREIGN KEY (department_id) REFERENCES departments (id)
      ON UPDATE CASCADE ON DELETE RESTRICT,
	INDEX idx_courses_ects (ects),
    INDEX idx_courses_department_id (department_id),
    INDEX idx_courses_deleted (deleted),
    INDEX idx_courses_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE course_offerings(
	id BIGINT NOT NULL AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    semester_id BIGINT NOT NULL,
    examination_id BIGINT NOT NULL,
    
    CONSTRAINT pk_course_offerings_id PRIMARY KEY (id),
	CONSTRAINT uk_course_offerings_course_teacher_semester UNIQUE (course_id, teacher_id, semester_id),
    CONSTRAINT fk_course_offerings_course_id FOREIGN KEY (course_id) REFERENCES courses (id)
      ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT fk_course_offerings_teacher_id FOREIGN KEY (teacher_id) REFERENCES teachers (id)
      ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT fk_course_offerings_semester_id FOREIGN KEY (semester_id) REFERENCES semesters (id)
      ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT fk_course_offerings_examination_id FOREIGN KEY (examination_id) REFERENCES examinations (id)
      ON UPDATE CASCADE ON DELETE RESTRICT,
	INDEX idx_course_offerings_course_id (course_id),
	INDEX idx_course_offerings_teacher_id (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE enrollments(
	id BIGINT NOT NULL AUTO_INCREMENT,
    enrollment_date DATETIME NOT NULL,
    course_offering_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    
    CONSTRAINT pk_enrollments PRIMARY KEY (id),
	CONSTRAINT uk_enrollments_course_offering_student UNIQUE (course_offering_id, student_id),
    CONSTRAINT fk_enrollments_course_offering_id FOREIGN KEY (course_offering_id) REFERENCES course_offerings (id)
      ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_enrollments_student_id FOREIGN KEY (student_id) REFERENCES students (id)
      ON UPDATE CASCADE ON DELETE CASCADE,
    INDEX idx_enrollments_course_offering (course_offering_id),
    INDEX idx_enrollments_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;








