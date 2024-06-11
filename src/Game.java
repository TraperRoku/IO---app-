public class Game {
    private Long idGry;
    private String nazwaGry;
    private Double cena;
    private String img;

    User user = new User();

    public Game(String nazwaGry, Double cena, String img) {
        this.nazwaGry = nazwaGry;
        this.cena = cena;
        this.img = img;
    }
    public Long getIdGry() {
        return idGry;
    }

    public void setIdGry(Long idGry) {
        this.idGry = idGry;
    }

    public String getNazwaGry() {
        return nazwaGry;
    }

    public void setNazwaGry(String nazwaGry) {
        this.nazwaGry = nazwaGry;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
