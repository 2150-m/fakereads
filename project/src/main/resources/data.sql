INSERT INTO KORISNIK (ime, prezime, korisnicko_ime, mejl_adresa, lozinka, datum_rodjenja, profilna_slika, opis, uloga) VALUES
('Marko', 'Markovic', 'markomarkovic', 'marko@markovic.com', 'sifra123', '24.3.1990.', '3', 'opis', 0),
('Nikola', 'Nikolic', 'nikola123', 'nikola@nikolic.com', 'jedandvatri', '18.3.2000.', '1', 'opis', 0);

INSERT INTO AUTOR (id, nalog_aktivan) VALUES (2, true);

INSERT INTO KNJIGA (naslov, naslovna_fotografija, datum_objavljivanja, opis, broj_strana, ocena, isbn) VALUES
('Knjiga', 'knjiga_01.png', '12.8.1986.', 'Ovo je definitivno knjiga', 156, 6, 9783161484100);

INSERT INTO ZANR (naziv) VALUES
('Horor');

INSERT INTO KNJIGA_ZANROVI (zanr_id, knjiga_id) VALUES
(1, 1);