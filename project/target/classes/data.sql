INSERT INTO ACCOUNT_USER (first_name, last_name, username, mail_address, password, date_of_birth, profile_picture, description, account_role) VALUES
('Marko', 'Markovic', 'markomarkovic', 'marko@markovic.com', 'sifra123', '24.3.1990.', '3', 'opis', 0),
('Nikola', 'Nikolic', 'nikola123', 'nikola@nikolic.com', 'jedandvatri', '18.3.2000.', '1', 'opis', 0);

INSERT INTO ACCOUNT_AUTHOR (id, account_activated) VALUES (2, true);

INSERT INTO BOOK (title, cover_photo, release_date, description, num_of_pages, rating, isbn) VALUES
('Knjiga', 'knjiga_01.png', '12.8.1986.', 'Ovo je definitivno knjiga', 156, 6, 9783161484100);

INSERT INTO GENRE (id, name) VALUES
(6, 'Horor');

INSERT INTO BOOKS_GENRES (genre_id, book_id) VALUES
(6, 1);

INSERT INTO AUTHORS_BOOKS (account_author_id, book_id) VALUES
(2, 1);

INSERT INTO REVIEW (id, rating, text, review_date, account_user_id) VALUES
(35, 8, 'tekst', '12.12.2012.', 1);

INSERT INTO SHELF_ITEM (id, book_id) VALUES
(10, 1);

INSERT INTO SHELF_ITEM_REVIEW (shelf_item_id, review_id) VALUES
(10, 35);

INSERT INTO SHELF (name, is_primary) VALUES ('Polica01', true);

INSERT INTO SHELF_AND_ITEMS (shelf_id, shelf_item_id) VALUES (1, 10);

INSERT INTO ACCOUNT_ACTIVATION_REQUEST (mail_address, phone_number, message, request_date, status) VALUES
('mail@mail.com', '124', 'poruka', '1.1.1900.', 1);