CREATE SCHEMA IF NOT EXISTS gidddb;
use gidddb;


CREATE TABLE user(
	user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    mail VARCHAR(16) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE level(
	level_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(16) NOT NULL,
    point_scalar decimal(5,2)#DECIMAL (Numbers of digits, numbers after decimal)
);

CREATE TABLE user_info(
	user_id INT NOT NULL PRIMARY KEY,
    
    image_url VARCHAR(32),
    firstname VARCHAR(32),
    lastname VARCHAR(32),
    description VARCHAR(180),
    points INT NOT NULL,
    activity_level INT NOT NULL,
    
    CONSTRAINT FK_user FOREIGN KEY (user_id)
    REFERENCES user(user_id),
    CONSTRAINT FK_level_user_info FOREIGN KEY(activity_level)
    REFERENCES level(level_id)
);

CREATE TABLE activity_type(
	type_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(12),
	points_per_minute DECIMAL(5,2) #Points-per-minute
);

CREATE TABLE activity(
	activity_id INT	NOT NULL AUTO_INCREMENT PRIMARY KEY,
    creator_id INT NOT NULL,
    level_id INT NOT NULL,
    type_id INT NOT NULL,
    
    longitude DECIMAL(7,2),#FLOAT(Numbers of digits, numbers after decimal)
    latitude DECIMAL(7,2),
    start_time TIMESTAMP NOT NULL,#TIMESTAMP deals with time zones, DATETIME just stores the numbers
    end_time TIMESTAMP,
    place_name VARCHAR(32),
	description VARCHAR(180),
    
    CONSTRAINT FK_creator FOREIGN KEY (creator_id)
    REFERENCES user(user_id),
    CONSTRAINT FK_level_activity FOREIGN KEY(level_id)
    REFERENCES level(level_id),
    CONSTRAINT FK_activity_type FOREIGN KEY(type_id)
    REFERENCES activity_type(type_id)
);

CREATE TABLE participant(
	user_id INT NOT NULL,
    activity_id INT NOT NULL,
    queue_nr INT,
    
    CONSTRAINT PK_user_activity PRIMARY KEY(user_id,activity_id),
    
    CONSTRAINT FK_m2m_user FOREIGN KEY(user_id)
    REFERENCES user(user_id),
    CONSTRAINT FK_m2m_activity FOREIGN KEY(activity_id)
    REFERENCES activity(activity_id)
);

CREATE TABLE equipment(
	equipment_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    activity_id INT NOT NULL,
    description VARCHAR(64) NOT NULL,
    
    CONSTRAINT FK_activity FOREIGN KEY(activity_id)
    REFERENCES activity(activity_id)
);
