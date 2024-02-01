CREATE TABLE IF NOT EXISTS t_notification(
     id BIGSERIAL primary key,
     created_on timestamp not null default now(),
     receiver VARCHAR(15),
     sender VARCHAR(50),
     message TEXT NOT NULL,
     status VARCHAR(20) NOT NULL,
     status_note TEXT,
     transaction_id TEXT,
     external_transaction_id VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS t_user_student_status_count(
      id BIGSERIAL primary key,
      username VARCHAR(50),
      created_on timestamp not null default now(),
      modified_on timestamp,
      date timestamp not null,
      user_type VARCHAR(32) NOT NULL,
      student_status VARCHAR(32) NOT NULL,
      date_count INT
);

CREATE TABLE IF NOT EXISTS t_trip(
     id BIGSERIAL primary key,
     created_on timestamp not null default now(),
     modified_on timestamp,
     staff_username VARCHAR(50) NOT NULL,
     school_id VARCHAR(20) NOT NULL,
     trip_type VARCHAR(32) NOT NULL,
     trip_status VARCHAR(32) NOT NULL,
     note TEXT
);

CREATE TABLE IF NOT EXISTS t_student_travel(
   id BIGSERIAL primary key,
   created_on timestamp not null default now(),
   modified_on timestamp,
   trip_id INTEGER REFERENCES t_trip(id) NOT NULL,
   student_username VARCHAR(50) NOT NULL,
   school_id VARCHAR(20) NOT NULL,
   STUDENT_status VARCHAR(32) NOT NULL,
   location_id INTEGER NOT NULL REFERENCES t_location(id)
);

CREATE TABLE IF NOT EXISTS t_student_day(
    id BIGSERIAL primary key,
    created_on timestamp not null default now(),
    modified_on timestamp,
    school_date timestamp NOT NULL,
    student_username VARCHAR(50) NOT NULL,
    staff_username VARCHAR(50) NOT NULL,
    school_id VARCHAR(20) NOT NULL,
    student_status VARCHAR(32) NOT NULL,
    on_trip BOOLEAN DEFAULT false,
    location_id INTEGER NOT NULL REFERENCES t_location(id)
);

ALTER TABLE t_student_travel ADD full_name VARCHAR(100);
ALTER TABLE t_student_day ADD full_name VARCHAR(100);