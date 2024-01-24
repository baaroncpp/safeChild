CREATE TABLE t_school(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user(id),
    modified_on TIMESTAMP,
    modified_by INTEGER REFERENCES t_user(id),
    is_deleted BOOLEAN DEFAULT FALSE,
    school_name TEXT NOT NULL,
    group_id INTEGER,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    country_id INTEGER NOT NULL REFERENCES t_country(id),
    district_id INTEGER NOT NULL REFERENCES t_district(id),
    sms_cost BIGINT NOT NULL,
    reference_school_id VARCHAR(100) UNIQUE,
    school_category VARCHAR(50),
    is_assigned BOOLEAN DEFAULT FALSE,
    location_id INTEGER NOT NULL REFERENCES t_location(id)
);

CREATE TABLE t_school_user(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user(id),
    modified_on TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    modified_by INTEGER REFERENCES t_user(id),
    school_id INTEGER REFERENCES t_school(id) NOT NULL,
    user_id INTEGER REFERENCES t_user(id) NOT NULL
);
