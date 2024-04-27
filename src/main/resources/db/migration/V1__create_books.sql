CREATE TABLE books
(
    id     INT AUTO_INCREMENT
        PRIMARY KEY,
    title  VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    year   INT,
    isbn   VARCHAR(64)
);
