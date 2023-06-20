INSERT INTO SHELF (name, is_primary) VALUES
('CustomShelf01', false);

INSERT INTO ACCOUNT (first_name, last_name, username, mail_address, password, date_of_birth, profile_picture, description, account_role) VALUES
--('Marko',  'Markovic',   'marko',   'marko@gmail.com',    'sifra123',    DATE '1990-03-23', 'pic1.jpg',  'opis1', 0),
--('Nikola', 'Nikolic',    'nikola',  'nikola@gmail.com',   'jedandvatri', DATE '2000-05-02', 'pic2.jpg',  'opis2', 1),
--('Pera',   'Peric',      'pera',    'pera@gmail.com',     '123456890',   DATE '1980-03-02', 'pic3.jpg',  'opis3', 0),
--('Dusan',  'Dusic',      'dusan',   'dusan@gmail.com',    '666666',      DATE '2006-06-06', 'pic4.jpg',  'opis4', 0),
--('Stefan', 'Stefanovic', 'stefan',  'stefan@gmail.com',   '333111333',   DATE '2001-04-04', 'pic5.jpg',  'opis5', 0),
('Admin',  'Adminovic',  'bigboss', 'bigboss@gmail.com',  'bigboss123',  DATE '1700-03-23', 'admin.jpg', 'admin', 2);


--INSERT INTO ACCOUNTS_SHELVES (account_id, shelf_id) VALUES
--(1, 1);

--INSERT INTO ACCOUNT_AUTHOR (id, account_activated) VALUES (2, false);

INSERT INTO BOOK (title, cover_photo, release_date, description, num_of_pages, rating, isbn) VALUES
('Harry Potter', 'covers/harry.png',      DATE '1986-10-02', 'harry potterz',               230, 0, 'ISBN-1234'),
('LOTR',         'covers/lotr.png',       DATE '1986-10-02', 'Ovo je definitivno knjiga 2', 100, 0, 'ISBN-3334'),
('Knjiga 3',     'covers/404.png',        DATE '1986-10-02', 'Ovo je definitivno knjiga 3', 300, 0, 'ISBN-4444'),
('Knjiga 4',     'covers/404.png',        DATE '1986-10-02', 'Ovo je definitivno knjiga 4', 500, 0, 'ISBN-5423');


INSERT INTO BOOK_GENRE (name) VALUES
('Horor'),
('Action'),
('Fiction'),
('Tail'),
('Drama');

INSERT INTO BOOKS_GENRES (book_genre_id, book_id) VALUES
(3, 1);

--INSERT INTO AUTHORS_BOOKS (account_author_id, book_id) VALUES
--(2, 1);

--INSERT INTO BOOK_REVIEW (id, rating, text, review_date) VALUES
--(35, 8, 'tekst', DATE '2012-12-12');

INSERT INTO SHELF_ITEM (book_id) VALUES
(1),
(2),
(3),
(4);

--INSERT INTO SHELF_ITEM_REVIEW (shelf_item_id, book_review_id) VALUES
--(10, 35);

--INSERT INTO SHELF_AND_ITEMS (shelf_id, shelf_item_id) VALUES (1, 10);

--INSERT INTO ACCOUNT_ACTIVATION_REQUEST (mail_address, phone_number, message, request_date, status) VALUES
--('mail@mail.com', '124', 'poruka', DATE '1900-01-18', 1);