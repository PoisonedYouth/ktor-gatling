CREATE TABLE book
(
    id     UUID PRIMARY KEY NOT NULL,
    title  VARCHAR            NOT NULL,
    author VARCHAR            NOT NULL
);
CREATE TABLE `user`
(
    id         UUID PRIMARY KEY NOT NULL,
    first_name VARCHAR            NOT NULL,
    last_name  VARCHAR            NOT NULL,
    birth_date DATE               NOT NULL
);
CREATE TABLE user_book
(
    id      UUID PRIMARY KEY NOT NULL,
    user_id UUID             NOT NULL,
    book_id UUID             NOT NULL,
    FOREIGN KEY (user_id) REFERENCES `user` (id),
    FOREIGN KEY (book_id) REFERENCES book (id)
)