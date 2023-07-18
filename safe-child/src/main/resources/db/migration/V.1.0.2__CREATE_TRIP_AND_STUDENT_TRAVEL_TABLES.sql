CREATE TABLE core.t_trip(
    id BIGSERIAL primary key,
    created_on timestamp not null default now(),
    modified_on timestamp,
    driver_username VARCHAR(50) NOT NULL,
    school_id VARCHAR(20) NOT NULL,
    trip_type VARCHAR(32) NOT NULL,
    trip_status VARCHAR(32) NOT NULL,
    note TEXT
);

CREATE TABLE core.t_student_travel(
    id BIGSERIAL primary key,
    created_on timestamp not null default now(),
    modified_on timestamp,
    trip_id INTEGER REFERENCES t_trip(id) NOT NULL,
    student_username VARCHAR(50) NOT NULL,
    driver_username VARCHAR(50) NOT NULL,
    school_id VARCHAR(20) NOT NULL,
    STUDENT_status VARCHAR(32) NOT NULL
);