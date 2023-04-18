package wpproject.project.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Korisnik implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String ime;

    @Column
    protected String prezime;

    @Column(unique = true)
    protected  String korisnickoIme;

    @Column(unique = true)
    protected String mejlAdresa;

    @Column
    protected String lozinka;

    @Column
    protected String datumRodjenja;

    @Column
    protected String profilnaSlika;

    @Column
    protected String opis;

    enum Uloga { CITALAC, AUTOR, ADMINISTRATOR }

    @Column
    protected Uloga uloga;

    public Long getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public String getMejlAdresa() {
        return mejlAdresa;
    }

    public String getLozinka() {
        return lozinka;
    }

    public String getDatumRodjenja() {
        return datumRodjenja;
    }

    public String getProfilnaSlika() {
        return profilnaSlika;
    }

    public String getOpis() {
        return opis;
    }

    public Uloga getUloga() {
        return uloga;
    }

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", korisnickoIme='" + korisnickoIme + '\'' +
                ", mejlAdresa='" + mejlAdresa + '\'' +
                ", lozinka='" + lozinka + '\'' +
                ", datumRodjenja='" + datumRodjenja + '\'' +
                ", profilnaSlika='" + profilnaSlika + '\'' +
                ", opis='" + opis + '\'' +
                ", uloga=" + uloga +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public void setMejlAdresa(String mejlAdresa) {
        this.mejlAdresa = mejlAdresa;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public void setDatumRodjenja(String datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public void setProfilnaSlika(String profilnaSlika) {
        this.profilnaSlika = profilnaSlika;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setUloga(Uloga uloga) {
        this.uloga = uloga;
    }
}
