INSERT INTO SHELF (name, is_primary) VALUES
('CustomShelf01', false);

INSERT INTO ACCOUNT (first_name, last_name, username, mail_address, password, date_of_birth, profile_picture, description, account_role) VALUES
('Marko', 'Markovic', 'markomarkovic', 'marko@markovic.com', 'sifra123', DATE '1990-03-23', '3', 'opis', 0),
('Nikola', 'Nikolic', 'nikola123', 'nikola@nikolic.com', 'jedandvatri', DATE '2000-05-02', '1', 'opis', 0);

INSERT INTO ACCOUNTS_SHELVES (account_id, shelf_id) VALUES
(1, 1);

INSERT INTO ACCOUNT_AUTHOR (id, account_activated) VALUES (2, true);

INSERT INTO BOOK (title, cover_photo, release_date, description, num_of_pages, rating, isbn) VALUES
('Knjiga', 'knjiga_01.png', DATE '1986-10-02', 'Ovo je definitivno knjiga', 156, 6, '9783161484100');

INSERT INTO BOOK_GENRE (id, name) VALUES
(6, 'Horor');

INSERT INTO BOOKS_GENRES (book_genre_id, book_id) VALUES
(6, 1);

INSERT INTO AUTHORS_BOOKS (account_author_id, book_id) VALUES
(2, 1);

INSERT INTO BOOK_REVIEW (id, rating, text, review_date) VALUES
(35, 8, 'tekst', DATE '2012-12-12');

INSERT INTO SHELF_ITEM (id, book_id) VALUES
(10, 1);

INSERT INTO SHELF_ITEM_REVIEW (shelf_item_id, book_review_id) VALUES
(10, 35);

INSERT INTO SHELF_AND_ITEMS (shelf_id, shelf_item_id) VALUES (1, 10);

INSERT INTO ACCOUNT_ACTIVATION_REQUEST (mail_address, phone_number, message, request_date, status) VALUES
('mail@mail.com', '124', 'poruka', DATE '1900-01-18', 1);