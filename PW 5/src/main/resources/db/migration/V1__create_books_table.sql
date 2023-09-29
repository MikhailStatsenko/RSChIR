CREATE TABLE IF NOT EXISTS books (
     id SERIAL PRIMARY KEY,
     author VARCHAR(255),
     seller_number VARCHAR(255),
     product_type VARCHAR(255),
     price DOUBLE PRECISION,
     title VARCHAR(255)
);

INSERT INTO books (author, seller_number, product_type, price, title) VALUES
      ('Джером Д. Сэлинджер', '1225', 'Книги', 249.99, ' Над пропастью во ржи'),
      ('Джоан Роулинг', '1225', 'Книги', 499.99, 'Гарри Поттер и философский камень'),
      ('Дж. Р. Р. Толкин', '1378', 'Книги', 853.23, 'Властелин колец');