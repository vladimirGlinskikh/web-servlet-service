DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS purchases;

CREATE TABLE users
(
    id   SERIAl PRIMARY KEY,
    name VARCHAR(50)

);

CREATE TABLE pets
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(50),
    user_id INTEGER REFERENCES users (id)
);

CREATE TABLE purchases
(
    id      SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    pet_id  INTEGER REFERENCES pets(id),
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
