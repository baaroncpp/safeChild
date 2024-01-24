ALTER TABLE t_school ADD COLUMN physical_address TEXT;
ALTER TABLE t_school ADD COLUMN core_banking_id BIGINT;

ALTER TABLE t_student ADD COLUMN core_banking_id BIGINT;
ALTER TABLE t_student ALTER COLUMN email DROP NOT NULL ;

ALTER TABLE t_user_meta ADD COLUMN pin VARCHAR(5);
ALTER TABLE t_user_meta ADD COLUMN physical_address TEXT;