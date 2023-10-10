ALTER TABLE t_guardian ADD COLUMN is_notified BOOLEAN DEFAULT FALSE;
ALTER TABLE t_student ADD COLUMN student_username VARCHAR(7);
ALTER TABLE t_user ADD COLUMN core_banking_id BIGINT;
