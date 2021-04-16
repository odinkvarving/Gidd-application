CREATE SCHEMA IF NOT EXISTS gidddb;
use gidddb;

SET SQL_MODE='ALLOW_INVALID_DATES';

CREATE TABLE login(
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(32) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE level(
    id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(16) NOT NULL

);

CREATE TABLE user_info(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,

    imageUrl VARCHAR(32),
    firstname VARCHAR(32),
    surname VARCHAR(32),
    profile_description VARCHAR(360),
    points INT NOT NULL,

    CONSTRAINT FK_info_to_user FOREIGN KEY (id)
    REFERENCES login(id)
);

CREATE TABLE activity_type(
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(12),
    point_factor DECIMAL(5,2)
);

CREATE TABLE activity(
    id INT AUTO_INCREMENT PRIMARY KEY,
    creator_id INT NOT NULL,
    level_id INT NOT NULL,
    type_id INT NOT NULL,

    longitude DECIMAL(7,5),
    latitude DECIMAL(7,5),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(360),
    max_participants INT,

    CONSTRAINT FK_creator FOREIGN KEY(creator_id)
    REFERENCES login(id),
   CONSTRAINT FK_level_activity FOREIGN KEY(level_id)
    REFERENCES level(id),
    CONSTRAINT FK_activity_type FOREIGN KEY(type_id)
    REFERENCES activity_type(id)
);

CREATE TABLE participant(
    user_id INT NOT NULL,
    activity_id INT NOT NULL,
    queue_nr INT,

    CONSTRAINT PK_user_activity PRIMARY KEY(user_id,activity_id),

    CONSTRAINT FK_m2m_user FOREIGN KEY(user_id)
    REFERENCES login(id),
   CONSTRAINT FK_m2m_activity FOREIGN KEY(activity_id)
    REFERENCES activity(id)
);

CREATE TABLE equipment(
    id INT AUTO_INCREMENT PRIMARY KEY,
    activity_id INT NOT NULL,
    description VARCHAR(64) NOT NULL,

    CONSTRAINT FK_activity FOREIGN KEY(activity_id)
    REFERENCES activity(id)
);