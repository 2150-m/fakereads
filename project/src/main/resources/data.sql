INSERT INTO SHELF (name, is_primary) VALUES
('CustomShelf01', false);

INSERT INTO ACCOUNT (first_name, last_name, username, mail_address, password, date_of_birth, profile_picture, description, account_role) VALUES
('Marko',  'Markovic',   'markomarkovic', 'marko@gmail.com',  'sifra123',    DATE '1990-03-23', 'pic1.jpg', 'opis1', 0),
('Nikola', 'Nikolic',    'nikola123',     'nikola@gmail.com', 'jedandvatri', DATE '2000-05-02', 'pic2.jpg', 'opis2', 0),
('Pera',   'Peric',      'pera123',       'pera@gmail.com',   '123456890',   DATE '1980-03-02', 'pic3.jpg', 'opis3', 0),
('Dusan',  'Dusic',      'dusan666',      'dusan@gmail.com',  '666666',      DATE '2006-06-06', 'pic4.jpg', 'opis4', 0),
('Stefan', 'Stefanovic', 'stefaaa',       'stefan@gmail.com', 'killme123',   DATE '2001-04-04', 'pic5.jpg', 'opis5', 0);


INSERT INTO ACCOUNTS_SHELVES (account_id, shelf_id) VALUES
(1, 1);

INSERT INTO ACCOUNT_AUTHOR (id, account_activated) VALUES (2, true);

INSERT INTO BOOK (title, cover_photo, release_date, description, num_of_pages, rating, isbn) VALUES
('Harry Potter', 'book_harrypoter.png', DATE '1986-10-02', 'harry potterz',               230, 10, 'ISBN-1234'),
('Knjiga 2',     'knjiga_02.png',       DATE '1986-10-02', 'Ovo je definitivno knjiga 2', 100,  8, 'ISBN-3334'),
('Knjiga 3',     'knjiga_03.png',       DATE '1986-10-02', 'Ovo je definitivno knjiga 3', 300,  7, 'ISBN-4444'),
('Knjiga 4',     'knjiga_04.png',       DATE '1986-10-02', 'Ovo je definitivno knjiga 4', 500,  6, 'ISBN-5423');

INSERT INTO BOOK_GENRE (id, name) VALUES
(6, 'Horor'),
(1, 'Action'),
(2, 'Fiction'),
(3, 'Tail'),
(4, 'Drama');

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