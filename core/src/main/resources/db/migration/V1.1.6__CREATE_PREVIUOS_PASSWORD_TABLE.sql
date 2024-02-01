create table t_previous_password (
    id BIGSERIAL primary key,
    created_on timestamp not null default now(),
    modified_on timestamp,
    user_id INTEGER REFERENCES t_user(id),
    previous_password TEXT NOT NULL UNIQUE,
    password_change_count INTEGER
);