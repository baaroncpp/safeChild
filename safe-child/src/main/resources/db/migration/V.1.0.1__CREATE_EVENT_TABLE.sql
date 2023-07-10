CREATE TABLE t_user_student_status_count(
    id BIGSERIAL primary key,
    username VARCHAR(50),
    created_on timestamp not null default now(),
    modified_on timestamp,
    date timestamp not null,
    user_type VARCHAR(32) NOT NULL,
    student_status VARCHAR(32) NOT NULL,
    date_count INT
);