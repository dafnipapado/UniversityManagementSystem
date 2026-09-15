ALTER TABLE semesters
ADD starts_at DATE NOT NULL,
ADD ends_at DATE NOT NULL,
ADD registration_deadline DATETIME NOT NULL,
ADD is_active TINYINT(1) NOT NULL DEFAULT 0;
