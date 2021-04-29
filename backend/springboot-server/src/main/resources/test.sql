INSERT INTO accountinfo (id, firstname, imageurl, points, profile_description, surname, level_id)
VALUES (3, 'admin', null, 0, null, 'adminson', null);
INSERT INTO accountinfo (id, firstname, imageurl, points, profile_description, surname, level_id)
VALUES (4, 'user', null, 0, null, 'userson', null);

INSERT INTO account (id, email, password, account_info)
VALUES (1, 'admin@admin.no', 'admin', 3);
INSERT INTO account (id, email, password, account_info)
VALUES (2, 'user@user.no', 'user', 4);


INSERT INTO activity_type (id, activity_type, point_factor)
VALUES (1001, 'Fotball', 3);
INSERT INTO activity_type (id, activity_type, point_factor)
VALUES (1002, 'Svømming', 6);
INSERT INTO activity_type (id, activity_type, point_factor)
VALUES (1003, 'Tur', 4);