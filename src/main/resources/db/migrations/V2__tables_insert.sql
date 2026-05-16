INSERT INTO regions (id, name) VALUES 
(1, 'Attica'),
(2, 'Central Greece'),
(3, 'Central Macedonia'),
(4, 'Crete'),
(5, 'Eastern Macedonia and Thrace'),
(6, 'Epirus'),
(7, 'Ionian Islands'),
(8, 'North Aegean'),
(9, 'Peloponnese'),
(10, 'South Aegean'),
(11, 'Thessaly'),
(12, 'Western Greece'),
(13, 'Western Macedonia');

ALTER TABLE regions AUTO_INCREMENT = 14;

INSERT INTO departments (id, name) VALUES
(1, 'Architecture'),
(2, 'Archeology'),
(3, 'Biology'),
(4, 'Business Administration'),
(5, 'Chemistry'),
(6, 'Civil Engineering'),
(7, 'Computer Science'),
(8, 'Economics'),
(9, 'Education'),
(10, 'Geology'),
(11, 'Journalism'),
(12, 'Law'),
(13, 'Mathematics'),
(14, 'Medicine'),
(15, 'Physics');

ALTER TABLE departments AUTO_INCREMENT = 16;

INSERT INTO roles VALUES
(1, 'ADMIN'),
(2, 'TEACHER'),
(3, 'STUDENT');

ALTER TABLE roles AUTO_INCREMENT = 4;

INSERT INTO capabilities (id, name, description) VALUES
(1, 'INSERT_TEACHER', 'Create a new teacher'),
(2, 'VIEW_TEACHER', 'View teacher list'),
(3, 'EDIT_TEACHER', 'Update a teacher'),
(4, 'DELETE_TEACHER', 'Delete a teacher'),
(5, 'INSERT_STUDENT', 'Create a new student'),
(6, 'VIEW_STUDENT', 'View student list'),
(7, 'EDIT_STUDENT', 'Update a student'),
(8, 'DELETE_STUDENT', 'Delete a student'),
(9, 'INSERT_EXAMINATION', 'Create an examination'),
(10, 'VIEW_EXAMINATION', 'View examination list'),
(11, 'EDIT_EXAMINATION', 'Update an examination'),
(12, 'INSERT_EXAM_RESULT', 'Create an exam result'),
(13, 'VIEW_EXAM_RESULT', 'View exam result list'),
(14, 'EDIT_EXAM_RESULT', 'Update an exam result'),
(15, 'DELETE_EXAM_RESULT', 'Delete an exam result'),
(16, 'INSERT_SEMESTER', 'Create a semester'),
(17, 'INSERT_COURSE', 'Create a course'),
(18, 'VIEW_COURSE', 'View course list'),
(19, 'EDIT_COURSE', 'Update a course'),
(20, 'DELETE_COURSE', 'Delete a course'),
(21, 'INSERT_COURSE_OFFERING', 'Create a course offering'),
(22, 'VIEW_COURSE_OFFERING', 'View course offering list'),
(23, 'EDIT_COURSE_OFFERING', 'Update a course offering'),
(24, 'DELETE_COURSE_OFFERING', 'Delete a course offering'),
(25, 'INSERT_ENROLLMENT', 'Create an enrollment'),
(26, 'VIEW_ENROLLMENT', 'View enrollment list'),
(27, 'DELETE_ENROLLMENT', 'Delete an enrollment');

ALTER TABLE capabilities AUTO_INCREMENT = 28;

INSERT INTO roles_capabilities (role_id, capability_id) 
SELECT r.id, c.id
FROM roles r
JOIN capabilities c
WHERE r.name = 'ADMIN';

INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
JOIN capabilities c
WHERE r.name = 'TEACHER'
  AND c.name IN (
    'VIEW_TEACHER',
    'EDIT_TEACHER',
    'VIEW_STUDENT',
    'INSERT_EXAMINATION',
    'VIEW_EXAMINATION',
    'EDIT_EXAMINATION',
    'INSERT_EXAM_RESULT',
    'VIEW_EXAM_RESULT',
    'EDIT_EXAM_RESULT',
    'DELETE_EXAM_RESULT',
    'VIEW_COURSE',
    'VIEW_COURSE_OFFERING',
    'VIEW_ENROLLMENT'
    );
    
INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
JOIN capabilities c
WHERE r.name = 'STUDENT'
  AND c.name IN (
	'VIEW_TEACHER',
    'VIEW_STUDENT',
    'EDIT_STUDENT',
    'VIEW_EXAMINATION',
    'VIEW_EXAM_RESULT',
    'VIEW_COURSE',
    'VIEW_COURSE_OFFERING',
    'INSERT_ENROLLMENT',
    'VIEW_ENROLLMENT',
    'DELETE_ENROLLMENT'
  );









