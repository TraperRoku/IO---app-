import java.sql.*;

public class User {
    private int id;
    private String nickname;
    private String password;
    private String email;
    private String phone;

    private Portfel portfel;

    public User(){
        this.portfel = new Portfel();

    }

    public void setPortfelFromDatabase() {
        try (Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD)) {
            // Query to select the user ID based on nickname
            String idQuery = "SELECT id FROM user WHERE name = ?";
            try (PreparedStatement idStatement = connection.prepareStatement(idQuery)) {
                idStatement.setString(1, this.nickname);
                ResultSet idResult = idStatement.executeQuery();

                if (idResult.next()) {
                    this.id = idResult.getInt("id");
                }
            }

            String portfelQuery = "SELECT * FROM Wallet WHERE player_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(portfelQuery)) {
                statement.setInt(1, this.id);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    double balance = resultSet.getDouble("balance");
                    this.portfel.setStanKonta(balance);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public User(String nickname, String password, String email, String phone) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        setPortfelFromDatabase();
    }



    public Portfel getPortfel() {
        return portfel;
    }

    public void setPortfel(Portfel portfel) {
        this.portfel = portfel;
    }
    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    protected static final String DB_URL = "jdbc:mysql://localhost:3309/users?serverTimezone=UTC";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "Filip123";
}
