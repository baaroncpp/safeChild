CREATE TABLE IF NOT EXISTS t_trip(
    id BIGSERIAL primary key,
    created_on timestamp not null default now(),
    modified_on timestamp,
    created_by INTEGER REFERENCES t_user(id),
    modified_by INTEGER REFERENCES t_user(id),
    is_deleted BOOLEAN DEFAULT FALSE,
    staff_id INTEGER REFERENCES t_user(id),
    school_id INTEGER  REFERENCES t_school(id),
    trip_type VARCHAR(32) NOT NULL,
    trip_status VARCHAR(32) NOT NULL,
    note TEXT
);