DROP TABLE IF EXISTS phones;

CREATE TABLE phones (
    id SERIAL PRIMARY KEY,
    manufacturer VARCHAR(255),
    battery_capacity INT,
    seller_number VARCHAR(255),
    product_type VARCHAR(255),
    price DOUBLE PRECISION,
    title VARCHAR(255)
);

INSERT INTO phones (manufacturer, battery_capacity, seller_number, product_type, price, title)
VALUES
    ('Apple', 3000, '12345', 'Электроника', 999.99, 'iPhone 15'),
    ('Samsung', 3500, '54321', 'Электроника', 899.99, 'Galaxy S23'),
    ('Google', 4000, '98765', 'Электроника', 799.99, 'Pixel 7');
