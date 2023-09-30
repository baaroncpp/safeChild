CREATE TABLE t_vehicle(
    id SERIAL PRIMARY KEY,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    created_by INTEGER REFERENCES t_user (id),
    modified_on TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    modified_by INTEGER REFERENCES t_user(id),
    current_driver INTEGER REFERENCES t_user(id),
    plate_number VARCHAR(7) UNIQUE NOT NULL,
    vehicle_model VARCHAR(100),
    on_route BOOLEAN DEFAULT FALSE,
    school_id INTEGER REFERENCES t_school (id),
    maximum_capacity INTEGER
);

insert into t_role(name, note)
values
    ('SCHOOL_ROLE', 'school role'),
    ('STUDENT_ROLE', 'student role'),
    ('USER_ROLE', 'user role'),
    ('VEHICLE_ROLE', 'vehicle role');

insert into t_permission(role_id, permission_name)
values
    (2, 'SCHOOL_ROLE.WRITE'),
    (2, 'SCHOOL_ROLE.READ'),
    (2, 'SCHOOL_ROLE.UPDATE'),
    (2, 'SCHOOL_ROLE.DELETE'),

    (3, 'STUDENT_ROLE.WRITE'),
    (3, 'STUDENT_ROLE.READ'),
    (3, 'STUDENT_ROLE.UPDATE'),
    (3, 'STUDENT_ROLE.DELETE'),

    (4, 'USER_ROLE.WRITE'),
    (4, 'USER_ROLE.READ'),
    (4, 'USER_ROLE.UPDATE'),
    (4, 'USER_ROLE.DELETE'),

    (5, 'VEHICLE_ROLE.WRITE'),
    (5, 'VEHICLE_ROLE.READ'),
    (5, 'VEHICLE_ROLE.UPDATE'),
    (5, 'VEHICLE_ROLE.DELETE');

