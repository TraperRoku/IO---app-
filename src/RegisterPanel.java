import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterPanel extends JDialog {
    private JPanel registerPanel;
    private JTextField phone;
    private JTextField email;
    private JPasswordField password;
    private JTextField nickname;
    private JButton signInButton;
    private JButton registerButton;
    private JButton cancelButton;


    public RegisterPanel(JFrame parent){
        super(parent);
        setTitle("Register");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(550,675));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm(null);
                loginForm.setVisible(true);

            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name1 = nickname.getText();
                String email1 = email.getText();
                String phone1 = phone.getText();
                String password1 = String.valueOf(password.getPassword());

                User user = new User();
                user.setNickname(name1);
                user.setEmail(email1);
                user.setPhone(phone1);
                user.setPassword(password1);

                if (user.getNickname().isEmpty() || user.getEmail().isEmpty() || user.getPhone().isEmpty() ||
                    user.getPassword().isEmpty()) {
                    JOptionPane.showMessageDialog(registerPanel,
                            "All fields are required",
                            "Try again", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);

                        if (isUnique(user.getEmail(), user.getPhone(), connection)) {

                            String query = "INSERT INTO user (name, email, phone, password) VALUES (?, ?, ?, ?)";
                            PreparedStatement statement = connection.prepareStatement(query);
                            statement.setString(1, name1);
                            statement.setString(2, email1);
                            statement.setString(3, phone1);
                            statement.setString(4, password1);
                            statement.executeUpdate();

                            JOptionPane.showMessageDialog(registerPanel,
                                    "User registered successfully",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            LoginForm loginForm = new LoginForm(null);
                            loginForm.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(registerPanel,
                                    "Email or Phone already exists",
                                    "Try again", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }


        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    private boolean isUnique(String email, String phone, Connection connection) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ? OR phone = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        statement.setString(2, phone);
        ResultSet resultSet = statement.executeQuery();

        boolean unique = !resultSet.next();

        resultSet.close();
        statement.close();

        return unique;
    }

}
