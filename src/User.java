public class User {
    private int id;
    private String nickname;
    private String password;
    private String email;
    private String phone;
    public User(){}

    public User(String nickname, String password, String email, String phone) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.phone = phone;
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
