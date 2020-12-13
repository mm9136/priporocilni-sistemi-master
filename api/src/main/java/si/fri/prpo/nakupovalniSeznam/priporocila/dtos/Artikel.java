package si.fri.prpo.nakupovalniSeznam.priporocila.dtos;

public class Artikel {

    private Integer id;
    private String naziv;
    private String opis;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) { this.id = id; }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return "Artikel{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", opis='" + opis + '\'' +
                '}';
    }
}

