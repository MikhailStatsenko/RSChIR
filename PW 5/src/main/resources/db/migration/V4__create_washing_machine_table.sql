CREATE TABLE washing_machines (
      id SERIAL PRIMARY KEY,
      manufacturer VARCHAR(255),
      tank_volume INT,
      seller_number VARCHAR(255),
      product_type VARCHAR(255),
      price DOUBLE PRECISION,
      title VARCHAR(255)
);

INSERT INTO washing_machines (manufacturer, tank_volume, seller_number, product_type, price, title)
VALUES
    ('Samsung', 7, '12345', 'Electronics', 599.99, 'Model A'),
    ('LG', 8, '54321', 'Electronics', 699.99, 'Model B'),
    ('Whirlpool', 6, '98765', 'Electronics', 499.99, 'Model C');
