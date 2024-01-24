CREATE TABLE t_log(
    id BIGSERIAL PRIMARY KEY ,
    created_on TIMESTAMP NOT NULL DEFAULT now(),
    modified_on TIMESTAMP,
    resource_url TEXT,
    http_status VARCHAR(5),
    log_level VARCHAR(30),
    note TEXT,
    entity_name VARCHAR(200)
);

INSERT INTO t_app_client (name,secret,grant_types,token_validity,scope) VALUES
    ('mobileApp','$2y$12$4boE8QoLm/f6fFKbN8Ai4eagkQ2C6s7aZQXc/8262e8BpHzK.te9K','password,refresh_token',120,'read');

INSERT INTO t_user_group(user_group_name, group_note) VALUES('MOBILE_APP_GROUP', 'Group for mobile app users');

INSERT INTO t_role(name, note) VALUES('MOBILE_APP_ROLE', 'Access mobile app APIs');

WITH role AS (SELECT id FROM t_role WHERE name = 'MOBILE_APP_ROLE')
INSERT INTO t_permission(role_id, permission_name)
VALUES
    ((select id from role), 'MOBILE_APP_ROLE.WRITE'),
    ((select id from role), 'MOBILE_APP_ROLE.READ'),
    ((select id from role), 'MOBILE_APP_ROLE.UPDATE'),
    ((select id from role), 'MOBILE_APP_ROLE.DELETE');

WITH
    userGroup AS (SELECT id FROM t_user_group WHERE user_group_name = 'MOBILE_APP_GROUP'),
    read AS (SELECT id FROM t_permission WHERE permission_name = 'MOBILE_APP_ROLE.READ'),
    write AS (SELECT id FROM t_permission WHERE permission_name = 'MOBILE_APP_ROLE.WRITE'),
    update AS (SELECT id FROM t_permission WHERE permission_name = 'MOBILE_APP_ROLE.UPDATE'),
    delete AS (SELECT id FROM t_permission WHERE permission_name = 'MOBILE_APP_ROLE.DELETE')

INSERT INTO t_group_authority(user_group_id, permission_id)
VALUES
    ((select id from userGroup), (select id from read)),
    ((select id from userGroup), (select id from write)),
    ((select id from userGroup), (select id from update)),
    ((select id from userGroup), (select id from delete));

WITH userGroup AS (SELECT id FROM t_user_group WHERE user_group_name = 'MOBILE_APP_GROUP')
INSERT INTO t_user(username, password, user_group_id, user_type, account_locked, account_expired, cred_expired)
VALUES('appAdmin', '$2y$12$R2gix.Nr/E4j9pmKVQHA4u/x.oyWf/wEPUBcQwxYerQSwqQXMcWZO', (select id from userGroup), 'DRIVER', FALSE, FALSE, FALSE);