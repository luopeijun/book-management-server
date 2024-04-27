CREATE TABLE books
(
    id     INT AUTO_INCREMENT
        PRIMARY KEY,
    title  VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    year   INT,
    isbn   VARCHAR(64)
);

INSERT INTO books (title, author, year, isbn)
VALUES ('book1', 'author', 2024, '1234-1234-1234-1234');

INSERT INTO books (title, author, year, isbn)
VALUES ('book2', 'author', 2024, '1234-1234-1234-1234');
