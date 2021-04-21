INSERT INTO account(email, password)
VALUES ('john.doe@gmail.com', 'secret'),
       ('xXedgelordXx@hotmail.com', 'swordfish'),
       ('ådun@æøå.no', 'æøåæøå');

INSERT INTO level(id, description)
VALUES (1, 'Lavt nivå'),
       (2, 'Medium nivå'),
       (3, 'Høyt nivå');

INSERT INTO accountinfo(id, imageUrl, firstname, surname, profile_description, points)
VALUES (1, NULL, 'John', 'Doe', NULL, 150),
       (2, NULL, NULL, NULL, NULL, 0),
       (3, 'resources/images/face.jpg', 'Ådne', 'Æøå', 'Æ ælskæ bokstavan æøå', 2500);

INSERT INTO activity_type(id, activity_type, point_factor)
VALUES (1, 'Jogging', 2.5),
       (2, 'Svømming', 1.5),
       (3, 'Ski', 4),
       (4, 'Klattring', 5),
       (5, 'Gåtur', 1),
       (6, 'Telttur', 1.75),
       (7, 'Fotball', 2.25);

INSERT INTO activity(id, creator_id, level_id, type_id, longitude, latitude, start_time, end_time, title, description,
                     max_participants)
VALUES (1,1, 2, 7, 10.40658, 63.41893, '2021-04-24 16:00:00', '2021-04-24 19:00:00', 'Ukentlig fredags fotball',
        'Ballen må sparkes', 25),
       (2, 1, 2, 7, 10.40658, 63.41893, '2021-05-01 16:00:00', '2021-05-01 19:00:00', 'Ukentlig fredags fotball',
        'Ballen må sparkes', 25),
       (3, 1, 2, 7, 10.40658, 63.41893, '2021-05-08 16:00:00', '2021-05-08 19:00:00', 'Ukentlig fredags fotball',
        'Ballen må sparkes', 25),
       (4, 3, 3, 6, 11.46387, 63.29912, '2021-05-01 18:00:00', '2021-05-03 12:00:00', 'Campingtur til stormdal',
        'Stormdal er en tøff rute, og annbefales derfor for bare erfarne haikere -æøåæøå', 8),
       (5, 3, 1, 1, 10.39490, 63.42254, '2021-04-30 06:00:00', '2021-04-30 07:00:00', 'Morgensjoggen', NULL, 4),
       (6, 2, 1, 5, 10.36838, 63.43040, '2021-04-12 12:00:00', NULL, 'Ikke en shady aktivitet, men ta med penger', 10,
        1),
       (7, 3, 2, 2, 10.36838, 63.43040, '2021-04-22 12:00:00', NULL, 'Felles badetur til Theisdammen',
        'Vi kan være så mange som mulig. Jeg sitter badevakt så lenge det er folk', 20);

