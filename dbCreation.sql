CREATE DATABASE if not exists rollerball;
USE rollerball;

drop table if exists user;
CREATE TABLE user ( 
	user_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(20) UNIQUE NOT NULL,
	email VARCHAR(30) UNIQUE NOT NULL,
	password VARBINARY(160),
	salt VARBINARY(64),
	is_active BOOLEAN DEFAULT 1,  /* alias of tinyInt(1); 0 for false, nonzero for true */
	PRIMARY KEY (user_id)
);

drop table if exists game;
CREATE TABLE game ( 
	game_id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
	host SMALLINT UNSIGNED,
    invitee SMALLINT UNSIGNED DEFAULT 0,
    start_time TIMESTAMP,
    end_time TIMESTAMP  DEFAULT NULL,
    result VARCHAR(20) DEFAULT 'UNFINISHED',/*WINNER NAME*/
    abandon BOOLEAN DEFAULT FALSE,
    next_turn SMALLINT UNSIGNED,
	PRIMARY KEY (game_id),
	FOREIGN KEY (host) REFERENCES user(user_id),
	FOREIGN KEY (invitee) REFERENCES user(user_id)
);

 INSERT INTO user (name, email, password, salt ) VALUES
( 'chris', 'chris@yahoo.com', null, null),
( 'zaira', 'zaira@google.com', null, null),
('john', 'john@bing.com', null, null),
('jane', 'jane@aol.com', null, null);


INSERT INTO game (host, invitee, start_time, next_turn) VALUES
(1,2,current_timestamp,1),
(2,1,current_timestamp,2),
(5,1,current_timestamp,5);




