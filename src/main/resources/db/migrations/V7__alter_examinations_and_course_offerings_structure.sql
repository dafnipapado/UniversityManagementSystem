ALTER TABLE course_offerings
DROP CONSTRAINT fk_course_offerings_examination_id;

ALTER TABLE course_offerings
DROP COLUMN examination_id;

ALTER TABLE examinations
ADD COLUMN course_offering_id BIGINT NOT NULL;

ALTER TABLE examinations
ADD CONSTRAINT fk_examinations_course_offering_id FOREIGN KEY (course_offering_id) REFERENCES course_offerings(id)
    ON DELETE RESTRICT;