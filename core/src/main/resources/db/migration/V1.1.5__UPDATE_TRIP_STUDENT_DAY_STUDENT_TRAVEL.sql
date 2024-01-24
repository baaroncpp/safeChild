ALTER TABLE t_notification ADD COLUMN modified_on timestamp;

ALTER TABLE t_user_student_status_count DROP COLUMN username;
ALTER TABLE t_user_student_status_count ADD COLUMN staff_id INTEGER REFERENCES t_user(id);

ALTER TABLE t_trip DROP COLUMN staff_username;
ALTER TABLE t_trip ADD COLUMN staff_id INTEGER REFERENCES t_user(id);
ALTER TABLE t_trip ADD COLUMN created_by INTEGER REFERENCES t_user(id);
ALTER TABLE t_trip ADD COLUMN modified_by INTEGER REFERENCES t_user(id);
ALTER TABLE t_trip ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;
ALTER TABLE t_trip ALTER COLUMN school_id TYPE INTEGER USING (school_id::INTEGER);
ALTER TABLE t_trip ADD CONSTRAINT fk_school_id FOREIGN KEY(school_id) REFERENCES t_school(id);

ALTER TABLE t_student_travel DROP COLUMN student_username;
ALTER TABLE t_student_travel ADD COLUMN created_by INTEGER REFERENCES t_user(id);
ALTER TABLE t_student_travel ADD COLUMN modified_by INTEGER REFERENCES t_user(id);
ALTER TABLE t_student_travel ADD COLUMN is_deleted INTEGER REFERENCES t_user(id);
ALTER TABLE t_student_travel ALTER COLUMN school_id TYPE INTEGER USING (school_id::INTEGER);
ALTER TABLE t_student_travel ADD CONSTRAINT fk_school_id FOREIGN KEY(school_id) REFERENCES t_school(id);
ALTER TABLE t_student_travel ADD COLUMN student_id INTEGER REFERENCES t_student(id);

ALTER TABLE t_student_day DROP COLUMN student_username;
ALTER TABLE t_student_day DROP COLUMN staff_username;
ALTER TABLE t_student_day ADD COLUMN created_by INTEGER REFERENCES t_user(id);
ALTER TABLE t_student_day ADD COLUMN modified_by INTEGER REFERENCES t_user(id);
ALTER TABLE t_student_day ADD COLUMN is_deleted INTEGER REFERENCES t_user(id);
ALTER TABLE t_student_day ALTER COLUMN school_id TYPE INTEGER USING (school_id::INTEGER);
ALTER TABLE t_student_day ADD CONSTRAINT fk_school_id FOREIGN KEY(school_id) REFERENCES t_school(id);
ALTER TABLE t_student_day ADD COLUMN student_id INTEGER REFERENCES t_student(id);
ALTER TABLE t_student_day ADD COLUMN staff_id INTEGER REFERENCES t_user(id);