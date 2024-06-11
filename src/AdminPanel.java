import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminPanel extends JDialog {
    private JPanel adminPanel;
    private JButton addGameButton;
    private JButton outButton;
    private JTextField nazwaGry;
    private JTextField cena;
    private JTextField img;
    private JLabel icon;

    public AdminPanel(JFrame parent, User user) {
        super(parent);
        setTitle("Admin");
        setContentPane(adminPanel);
        setMinimumSize(new Dimension(600, 775));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        icon.setIcon(new ImageIcon("src/icons/admin.png"));
        outButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm(null);
                loginForm.setVisible(true);
            }
        });

        addGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameName = nazwaGry.getText();
                String gamePrice = cena.getText();
                String gameImg = img.getText();

                if (gameName.isEmpty() || gamePrice.isEmpty() || gameImg.isEmpty()) {
                    JOptionPane.showMessageDialog(adminPanel,
                            "All fields are required",
                            "Try again", JOptionPane.ERROR_MESSAGE);
                } else{
                    try {

                        Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);

                        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO gra(nazwa_gry, cena, img) VALUES(?,?,?)");
                        preparedStatement.setString(1, gameName);
                        preparedStatement.setDouble(2, Double.parseDouble(gamePrice));
                        preparedStatement.setString(3, gameImg);
                        preparedStatement.executeUpdate();

                        JOptionPane.showMessageDialog(adminPanel,
                                "Game added successfully",
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                        nazwaGry.setText(nazwaGry.getText());
                        cena.setText(cena.getText());
                        img.setText(img.getText());

                        connection.close();

                    }catch (SQLException throwables) {
                        throwables.printStackTrace();
                        JOptionPane.showMessageDialog(adminPanel,
                                "Error adding game to database",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(adminPanel,
                            "Invalid price format",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                }


                dispose();
            }
        });

        setVisible(true);
    }
}
