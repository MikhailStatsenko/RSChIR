CREATE TABLE product (
     id SERIAL PRIMARY KEY,
     quantity INT,
     seller_number VARCHAR(255),
     price DECIMAL(10, 2),
     title VARCHAR(255),
     product_type VARCHAR(255),
     manufacturer VARCHAR(255),
     battery_capacity INT,
     tank_volume INT,
     author VARCHAR(255)
);

INSERT INTO product (quantity, seller_number, price, title, product_type, manufacturer, battery_capacity)
VALUES
    (10, '123456', 499.99, 'Смартфон Samsung Galaxy S21', 'Phone', 'Samsung', 4000),
    (8, '123456', 399.99, 'Смартфон Apple iPhone 13', 'Phone', 'Apple', 3500),
    (12, '345678', 299.99, 'Смартфон Google Pixel 6', 'Phone', 'Google', 4200),
    (15, '345678', 449.99, 'Смартфон OnePlus 9', 'Phone', 'OnePlus', 4100);

INSERT INTO product (quantity, seller_number, price, title, product_type, manufacturer, tank_volume)
VALUES
    (20, '1212', 599.99, 'Стиральная машина Bosch Serie 6', 'WashingMachine', 'Bosch', 8),
    (18, '1212', 499.99, 'Стиральная машина Samsung WW80T', 'WashingMachine', 'Samsung', 7),
    (22, '1212', 649.99, 'Стиральная машина LG F2WV5S9S0E', 'WashingMachine', 'LG', 9),
    (25, '1111', 579.99, 'Стиральная машина Whirlpool FreshCare', 'WashingMachine', 'Whirlpool', 7.5);

INSERT INTO product (quantity, seller_number, price, title, product_type, author)
VALUES
    (30, '901234', 19.99, 'Роман "Война и мир"', 'Book', 'Лев Толстой'),
    (28, '901234', 14.99, 'Роман "Преступление и наказание"', 'Book', 'Федор Достоевский'),
    (35, '123456', 24.99, 'Роман "Гарри Поттер и философский камень"', 'Book', 'Дж. К. Роулинг'),
    (32, '123456', 17.99, 'Роман "1984"', 'Book', 'Джордж Оруэлл');

CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    login VARCHAR(255),
    password VARCHAR(255)
);

INSERT INTO client (name, email, login, password)
VALUES
    ('Иван Петров', 'ivan@gmail.com', 'ivan123', 'securePassword123'),
    ('Мария Сидорова', 'maria@yandex.ru', 'maria456', 'strongPassword456');
