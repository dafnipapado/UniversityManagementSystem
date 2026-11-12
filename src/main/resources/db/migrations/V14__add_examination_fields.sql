ALTER TABLE examinations
ADD COLUMN start_time TIME NULL,
ADD COLUMN duration INT NOT NULL,
ADD COLUMN passing_grade INT NOT NULL;