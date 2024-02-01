WITH country AS (
    SELECT id FROM t_country WHERE name = 'UGANDA'
)

INSERT INTO t_district (id, name, region, country_id)
VALUES
    (113,'kagadi','Western',(select id from country)),
    (114,'Kakumiro','Western',(select id from country)),
    (115,'Omoro','Northern',(select id from country)),
    (116,'Rubanda','Western',(select id from country)),
    (117,'Namisindwa','Eastern',(select id from country)),
    (118,'Pakwach','Northern',(select id from country)),
    (119,'Butebo','Eastern',(select id from country)),
    (120,'Rukiga','Western',(select id from country)),
    (121,'Kyotera','Central',(select id from country)),
    (122,'Bunyangabu','Western',(select id from country)),
    (123,'Nabilatuk','Northern',(select id from country)),
    (124,'Bugweri','Eastern',(select id from country)),
    (125,'Kasanda','Central',(select id from country)),
    (126,'Kwania','Northern',(select id from country)),
    (127,'Kapelebyong','Eastern',(select id from country)),
    (128,'Kikuube','Western',(select id from country)),
    (129,'Obongi','Northern',(select id from country)),
    (130,'Kazo','Western',(select id from country)),
    (131,'Rwampara','Western',(select id from country)),
    (132,'Kitagwenda','Western',(select id from country)),
    (133,'Madi-Okollo','West Nile',(select id from country)),
    (134,'Karenga','Northern',(select id from country)),
    (135,'Terego','Northern',(select id from country)),
    (136,'Kalaki','Eastern',(select id from country));
