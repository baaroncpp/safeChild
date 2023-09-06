create table t_country (
    id BIGSERIAL primary key,
    name VARCHAR(50) not null,
    iso_alpha_2 CHARACTER(2) unique not null,
    iso_alpha_3 CHARACTER(3) unique not null,
    iso_numeric INTEGER unique not null,
    created_on timestamp not null default now(),
    modified_on timestamp
);

create table t_district (
    id BIGSERIAL primary key,
    country_id SERIAL references t_country(id),
    name VARCHAR(50),
    region VARCHAR(50),
    created_on timestamp not null default now(),
    modified_on timestamp
);