CREATE TABLE IF NOT EXISTS t_student_day(
    id BIGSERIAL primary key,
    created_on timestamp not null default now(),
    modified_on timestamp,
    school_date timestamp NOT NULL,
    student_username VARCHAR(50) NOT NULL,
    staff_username VARCHAR(50) NOT NULL,
    school_id VARCHAR(20) NOT NULL,
    STUDENT_status VARCHAR(32) NOT NULL
);