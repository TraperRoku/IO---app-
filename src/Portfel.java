public class Portfel {
    private int id;
    private double stanKonta;

    public Portfel() {}

    public Portfel(double stanKonta) {
        this.stanKonta = stanKonta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStanKonta() {
        return stanKonta;
    }

    public void setStanKonta(double stanKonta) {
        this.stanKonta = stanKonta;
    }
}
