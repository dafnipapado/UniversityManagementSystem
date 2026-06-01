INSERT INTO capabilities (id, name, description) VALUES
(28, 'INSERT_USER', 'Create a new user'),
(29, 'VIEW_USER', 'View user list');

ALTER TABLE capabilities AUTO_INCREMENT=30;

INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
JOIN capabilities c
WHERE r.name = 'ADMIN'
AND c.name IN ( 'INSERT_USER', 'VIEW_USER' );

INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
JOIN capabilities c
WHERE r.name = 'TEACHER' AND c.name = 'INSERT_TEACHER';

INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
JOIN capabilities c
WHERE r.name = 'STUDENT' AND c.name = 'INSERT_STUDENT';