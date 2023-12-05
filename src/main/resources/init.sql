DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id   SERIAl PRIMARY KEY,
    name VARCHAR(50)

);

-- DELETE FROM users;


INSERT INTO users (name)
VALUES ('Vladimir'),
       ('Leonid'),
       ('Alexander'),
       ('Pavel'),
       ('Anastasia');

INSERT INTO users(name)
VALUES ('Nikolay');