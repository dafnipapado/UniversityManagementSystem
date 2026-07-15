ALTER TABLE course_offerings
ADD COLUMN created_at DATETIME NOT NULL,
ADD COLUMN updated_at DATETIME NOT NULL,
ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0,
ADD COLUMN deleted_at DATETIME NULL,
ADD INDEX idx_course_offerings_deleted (deleted),
ADD INDEX idx_course_offerings_deleted_at (deleted_at);

ALTER TABLE examinations
ADD COLUMN created_at DATETIME NOT NULL,
ADD COLUMN updated_at DATETIME NOT NULL,
ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0,
ADD COLUMN deleted_at DATETIME NULL,
ADD INDEX idx_examinations_deleted (deleted),
ADD INDEX idx_examinations_deleted_at (deleted_at);

ALTER TABLE exam_results
ADD COLUMN created_at DATETIME NOT NULL,
ADD COLUMN updated_at DATETIME NOT NULL,
ADD COLUMN deleted TINYINT(1) NOT NULL DEFAULT 0,
ADD COLUMN deleted_at DATETIME NULL,
ADD INDEX idx_exam_results_deleted (deleted),
ADD INDEX idx_exam_results_deleted_at (deleted_at)