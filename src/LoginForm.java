import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog {
    private JPanel loginPanel;
    private JButton BtnOk;
    private JButton BtnCancel;


    private JTextField loginEmail;
    private JPasswordField loginPassword;
    private JButton registerButton;
    ;
    private boolean isAdmin = false;


    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450,475));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        BtnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = loginEmail.getText();
                String password = String.valueOf(loginPassword.getPassword());

                User user = getAuthenticatedUser(email, password);

                if (user != null) {
                    if(isAdmin){
                        dispose();
                        AdminPanel adminForm = new AdminPanel(null, user);
                        adminForm.setVisible(true);


                    }else{
                        isAdmin = false;
                        JOptionPane.showMessageDialog(LoginForm.this,
                                "Welcome " + user.getNickname() + "!",
                                "Welcome", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        HelloPanel helloForm = new HelloPanel(null, null,user);
                        helloForm.setVisible(true);
                    }

                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid",
                            "Try again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RegisterPanel registerForm = new RegisterPanel(null);
                registerForm.setVisible(true);
            }
        });
        BtnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });
        setVisible(true);
    }

    private User getAuthenticatedUser(String email, String password) {
        User user = null;

        try {
            Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setNickname(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setPassword(resultSet.getString("password"));
            }
            if (user != null && user.getEmail().equals("FilipKazmierczak@wp.pl")) {
                isAdmin = true;
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public static void main(String[] args) {
         new LoginForm(null);
    }



}



