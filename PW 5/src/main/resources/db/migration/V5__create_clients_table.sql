CREATE TABLE clients (
     id SERIAL PRIMARY KEY,
     name VARCHAR(255),
     email VARCHAR(255),
     login VARCHAR(255),
     password VARCHAR(255)
);


INSERT INTO clients (name, email, login, password)
VALUES
    ('Иванов иван', 'ivanov@mail.ru', 'ivan', '123456'),
    ('Александров александр', 'alex_alex@gmail.com', 'alex', 'sunshine'),
    ('Петров петр', 'petrov.p@yandex.ru', 'peter_p', 'qwerty');
