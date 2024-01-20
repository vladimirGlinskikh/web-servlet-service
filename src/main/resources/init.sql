DROP TABLE IF EXISTS purchases CASCADE;
DROP TABLE IF EXISTS pets CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE pets
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE purchases
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    pet_id  INTEGER REFERENCES pets (id) ON DELETE CASCADE
);


INSERT INTO users (name)
VALUES ('Vladimir'),
       ('Leonid'),
       ('Alexander'),
       ('Pavel'),
       ('Anastasia');

INSERT INTO pets (name)
VALUES ('Fluffy'),
       ('Buddy'),
       ('Max'),
       ('Luna'),
       ('Charlie');

INSERT INTO purchases (user_id, pet_id)
VALUES (1, 2),
       (1, 1),
       (2, 5),
       (4, 3),
       (5, 3),
       (5, 4);


select *
from purchases;
