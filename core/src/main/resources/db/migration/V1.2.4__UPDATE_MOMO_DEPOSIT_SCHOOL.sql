ALTER TABLE t_momo_deposit DROP COLUMN is_school;
ALTER TABLE t_momo_deposit ADD COLUMN school_id INTEGER REFERENCES t_school(id);