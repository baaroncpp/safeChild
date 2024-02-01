insert into t_role(name, note)
values
    ('TRIP_ROLE', 'trip role');

insert into t_permission(role_id, permission_name)
values
    (6, 'TRIP_ROLE.WRITE'),
    (6, 'TRIP_ROLE.READ'),
    (6, 'TRIP_ROLE.UPDATE'),
    (6, 'TRIP_ROLE.DELETE');