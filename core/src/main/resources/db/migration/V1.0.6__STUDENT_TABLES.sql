CREATE TABLE t_student(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user (id),
    modified_on TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    modified_by INTEGER REFERENCES t_user(id),
    first_name VARCHAR(50) NOT NULL,
    second_name VARCHAR(50) NOT NULL,
    school_id_number VARCHAR(20),
    national_id_number VARCHAR(20),
    email VARCHAR(100) NOT NULL,
    student_class VARCHAR(30) NOT NULL,
    school_id INTEGER REFERENCES t_school (id),
    profile_image_path_url TEXT,
    id_image_path_url TEXT,
    can_be_notified BOOLEAN DEFAULT FALSE
);

CREATE TABLE t_guardian(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user (id),
    modified_on TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    modified_by INTEGER REFERENCES t_user(id),
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    address VARCHAR(200) NOT NULL,
    relation VARCHAR(50) NOT NULL,
    identification_type VARCHAR(50),
    id_number VARCHAR(50)
);

CREATE TABLE t_guardian_student(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user (id),
    modified_on TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    modified_by INTEGER REFERENCES t_user(id),
    student_id INTEGER REFERENCES t_student (id),
    guardian_id INTEGER REFERENCES t_guardian (id)
);