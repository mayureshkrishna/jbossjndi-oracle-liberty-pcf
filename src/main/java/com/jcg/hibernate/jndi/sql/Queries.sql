/* CREATE DATABASE */
CREATE DATABASE tutorialdb;

/* USE DATABASE */
USE tutorialdb;

/* CREATE TABLE */
CREATE TABLE employee (
	id int(11) NOT NULL AUTO_INCREMENT, 
	name varchar(20) DEFAULT NULL, 
	role varchar(20) DEFAULT NULL, 
	insert_time datetime DEFAULT NULL,
PRIMARY KEY (id));

/* INSERT VALUES IN THE TABLE */
INSERT INTO employee (id, name, role, insert_time) VALUES (1, 'JavaCodeGeek', 'CEO', now());
INSERT INTO employee (id, name, role, insert_time) VALUES (2, 'Harry Potter', 'Editor', now());
INSERT INTO employee (id, name, role, insert_time) VALUES (3, 'Lucifer', 'Editor', now());

/* SHOW TABLE RECORDS */
SELECT * FROM employee;

/* DESCRIBE TABLE */
DESC employee;