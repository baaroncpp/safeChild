ALTER TABLE t_student_travel DROP COLUMN is_deleted;
ALTER TABLE t_student_travel ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;
ALTER TABLE t_student_day DROP COLUMN is_deleted;
ALTER TABLE t_student_day ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;