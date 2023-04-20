INSERT INTO KORISNIK (ime, prezime, korisnicko_ime, mejl_adresa, lozinka, datum_rodjenja, profilna_slika, opis, uloga) VALUES
('Marko', 'Markovic', 'markomarkovic', 'marko@markovic.com', 'sifra123', '24.3.1990.', '3', 'opis', 0),
('Nikola', 'Nikolic', 'nikola123', 'nikola@nikolic.com', 'jedandvatri', '18.3.2000.', '1', 'opis', 0);

INSERT INTO AUTOR (id, nalog_aktivan) VALUES (2, true);

INSERT INTO KNJIGA (naslov, naslovna_fotografija, datum_objavljivanja, opis, broj_strana, ocena, isbn) VALUES
('Knjiga', 'knjiga_01.png', '12.8.1986.', 'Ovo je definitivno knjiga', 156, 6, 9783161484100);

INSERT INTO ZANR (id, naziv) VALUES
(6, 'Horor');

INSERT INTO KNJIGE_ZANROVI (zanr_id, knjiga_id) VALUES
(6, 1);

INSERT INTO AUTORI_KNJIGE (autor_id, knjiga_id) VALUES
(2, 1);

INSERT INTO RECENZIJA (id, ocena, tekst, datum_recenzije, korisnik_id) VALUES
(35, 8, 'tekst', '12.12.2012.', 1);

INSERT INTO STAVKA_POLICE (id, knjiga_id) VALUES
(10, 1);

INSERT INTO STAVKA_POLICE_RECENZIJA (stavka_police_id, recenzija_id) VALUES
(10, 35);

INSERT INTO POLICA (naziv, primarna) VALUES ('Polica01', true);

INSERT INTO POLICA_STAVKE_POVEZIVANJE (polica_id, stavka_police_id) VALUES (1, 10);

INSERT INTO ZAHTEV_ZA_AKTIVACIJU_NALOGA_AUTORA (email, telefon, poruka, datum, status) VALUES
('mail@mail.com', '124', 'poruka', '1.1.1900.', 1);