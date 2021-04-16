<<<<<<< HEAD
INSERT INTO login(email,password)
VALUES('john.doe@gmail.com','secret'),
    ('xXedgelordXx@hotmail.com','swordfish'),
    ('ådun@æøå.no','æøåæøå');

INSERT INTO level(description)
VALUES('Lavt nivå'),
    ('Medium nivå'),
    ('Høyt nivå');

INSERT INTO user_info(imageUrl,firstname,surname,profile_description,points)
VALUES(NULL,'John','Doe',NULL,150),
    (NULL,NULL,NULL,NULL,0),
    ('resources/images/face.jpg','Ådne','Æøå','Æ ælskæ bokstavan æøå',2500);

INSERT INTO activity_type(type,point_factor)
VALUES('Jogging',2.5),
    ('Svømming',1.5),
    ('Ski',4),
    ('Klattring',5),
    ('Gåtur',1),
    ('Telttur',1.75),
    ('Fotball',2.25);

INSERT INTO activity(creator_id,level_id,type_id,longitude,latitude,start_time,end_time, title,description,max_participants)
VALUES (1,2,7,10.40658,63.41893,'2021-04-24 16:00:00','2021-04-24 19:00:00','Ukentlig fredags fotball','Ballen må sparkes',25),
    (1,2,7,10.40658,63.41893,'2021-05-01 16:00:00','2021-05-01 19:00:00','Ukentlig fredags fotball','Ballen må sparkes',25),
    (1,2,7,10.40658,63.41893,'2021-05-08 16:00:00','2021-05-08 19:00:00','Ukentlig fredags fotball','Ballen må sparkes',25),
    (3,3,6,11.46387,63.29912,'2021-05-01 18:00:00','2021-05-03 12:00:00','Campingtur til stormdal','Stormdal er en tøff rute, og annbefales derfor for bare erfarne haikere -æøåæøå',8),
    (3,1,1,10.39490,63.42254,'2021-04-30 06:00:00','2021-04-30 07:00:00','Morgensjoggen',NULL,4),
    (2,1,5,10.36838,63.43040,'2021-04-12 12:00:00',NULL,'Ikke en shady aktivitet, men ta med penger',NULL ,1),
    (3,2,2,NULL,NULL,'2021-04-22 12:00:00',NULL,'Felles badetur til Theisdammen','Vi kan være så mange som mulig. Jeg sitter badevakt så lenge det er folk',NULL);

INSERT INTO participant(user_id, activity_id, queue_nr)
VALUES (3,1,NULL),
    (3,2,NULL),
    (2,2,NULL),
    (2,3,NULL),
    (3,3,NULL),
    (1,6,NULL),
    (3,6,1),
    (1,7,NULL),
    (2,7,NULL);

INSERT INTO equipment(activity_id,description)
VALUES (1,'fotball'),(1,'keeper hansker'),
    (2,'fotball'),(2,'Keeper hansker'),
    (3,'fotball'),(3,'Keeper hansker'),
    (4,'Telt x1'),(4,'Gass ovn x2'),(4,'Gryte x2'),(4,'tekanne x1'),(4,'Badninton set x1'),(4,'Klatre utstyr x2'),(4,'Førstehjelps utstyr x1'),(4,'Spikke kniv x1'),
    (6,'kjeks'),
    (7,'grill'),(7,'Førstehjelps utstyr');